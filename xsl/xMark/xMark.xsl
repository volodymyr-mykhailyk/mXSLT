<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mFunc="http://www.intelliarts.com/xsl/multithreading"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs mFunc"
    version="2.0">

    <xsl:output indent="no" doctype-public="Test-Doctype" doctype-system="test.dtd"/>

    <xsl:function name="mFunc:convertName">
        <xsl:param name="name"/>

        <xsl:variable name="convertabale" select="matches($name, '.*[a-z].*.*[a-z].*.*[a-z].*')"/>
        <xsl:sequence select="if ($convertabale)
                                then concat('converted-', $name, '-element')
                                else $name"/>
    </xsl:function>

    <xsl:function name="mFunc:convertValue">
        <xsl:param name="value"/>

        <xsl:variable name="convertabale" select="matches($value, '[a-z]{2}')"/>
        <xsl:sequence select="if ($convertabale)
                                then concat(substring($value, string-length($value)-1, 1), mFunc:convertValue(substring($value, 1, string-length($value)-1)))
                                else $value"/>
    </xsl:function>

    <xsl:template match="*">
        <xsl:element name="{mFunc:convertName(name())}">
            <xsl:for-each select="@*">
                <xsl:attribute name="{mFunc:convertName(name())}" select="mFunc:convertName(.)"/>
            </xsl:for-each>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="text()">
        <xsl:variable name="tokens" select="tokenize(., '\s')"/>
        <xsl:for-each select="$tokens">
            <xsl:sort/>
            <xsl:copy-of select="mFunc:convertValue(.)"/>
            <xsl:text> </xsl:text>
        </xsl:for-each>
    </xsl:template>

    <xsl:key name="person" match="*[@id]" use="@id"/>

    <xsl:template match="*[@person][not(ancestor::open_auction[1])]">
        <xsl:element name="{mFunc:convertName(name())}">
            <xsl:apply-templates select="key('person', @person)"/>
            <!--<xsl:apply-templates select="/descendant::person[@id = current()/@person][1]"/>-->
        </xsl:element>
    </xsl:template>

    <xsl:template match="itemref">
        <xsl:element name="{mFunc:convertName(name())}">
            <!--<xsl:for-each select="/descendant::*[@id = current()/@item][1]">-->
            <xsl:for-each select="key('person', @item)">
                <xsl:apply-templates select="location"/>
                <xsl:apply-templates select="quantity"/>
                <xsl:apply-templates select="name"/>
                <xsl:apply-templates select="payment"/>
                <xsl:apply-templates select="shipping"/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="incategory">
        <xsl:element name="{mFunc:convertName(name())}">
            <!--<xsl:for-each select="/descendant::*[@id = current()/@item][1]">-->
            <xsl:for-each select="key('person', @category)">
                <xsl:apply-templates select="name"/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

    <xsl:template match="watch">
        <xsl:element name="{mFunc:convertName(name())}">
            <!--<xsl:for-each select="/descendant::*[@id = current()/@open_auction][1]">-->
            <xsl:for-each select="key('person', @open_auction)">
                <xsl:apply-templates select="initial"/>
                <xsl:apply-templates select="current"/>
                <xsl:apply-templates select="itemref"/>
                <xsl:apply-templates select="quantity"/>
                <xsl:apply-templates select="type"/>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>