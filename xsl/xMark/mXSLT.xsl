<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:mFunc="http://www.vmykhailyk.com/xsl/multithreading"
        xmlns:xs="http://www.w3.org/2001/XMLSchema"
        exclude-result-prefixes="xs mFunc"
        version="2.0">

    <xsl:import href="xMark.xsl"/>

    <xsl:template match="regions/* | categories | people | closed_auctions | open_auctions">
        <xsl:param name="currentThread" select="false()" as="xs:boolean"/>
        <xsl:call-template name="spawnThread">
            <xsl:with-param name="currentThread" select="$currentThread"/>
        </xsl:call-template>
    </xsl:template>

    <xsl:template name="spawnThread">
        <xsl:param name="currentThread" select="false()" as="xs:boolean"/>

        <xsl:choose>
            <xsl:when test="$currentThread">
                <xsl:next-match/>
            </xsl:when>
            <xsl:otherwise use-when="function-available('mFunc:spawnNewThread')">
                <xsl:value-of select="mFunc:spawnNewThread(., generate-id(.))"/>
            </xsl:otherwise>
            <xsl:otherwise use-when="not(function-available('mFunc:spawnNewThread'))">
                <xsl:next-match/>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template name="processNodeInCurrentThread">
        <xsl:apply-templates select=".">
            <xsl:with-param name="currentThread" select="true()"/>
        </xsl:apply-templates>
    </xsl:template>

</xsl:stylesheet>
