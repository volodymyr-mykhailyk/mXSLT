<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:mFunc="http://www.intelliarts.com/xsl/multithreading"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    exclude-result-prefixes="xs mFunc"
    version="2.0">
    
    <xsl:import href="mXSLT.xsl"/>

    <xsl:template match="site">
        <xsl:for-each select="*" xmlns:saxon="http://saxon.sf.net/" saxon:threads="4">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

    <xsl:template match="region">
        <xsl:for-each select="*" xmlns:saxon="http://saxon.sf.net/" saxon:threads="4">
            <xsl:apply-templates select="."/>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>