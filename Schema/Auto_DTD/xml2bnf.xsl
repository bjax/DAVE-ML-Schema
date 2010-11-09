<?xml version ="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mathml="http://www.w3.org/TR/Math/MathML2">
  <xsl:output method="text" indent="yes"/>

  <!-- 

       Converts my home-grown DTD .xml specification into a file
       containing Extended Backus-Naur Form (EBNF) notation for each element.

       2009-06-05 Bruce Jackson (e.b.jackson@nasa.gov)
       Use: "java -cp ${XALAN} org.apache.xalan.xslt.Process -in filename.xml -xsl xml2bnf.xsl -out filename.html"
       
   -->

  <xsl:template match="/dtd">
    <xsl:for-each select="el">
      <xsl:sort select="name"/>
      <xsl:call-template name="element"/>
    </xsl:for-each>
  </xsl:template>


 
  <!--      ++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!--      +      Generate the element BNF markup       + -->
  <!--      ++++++++++++++++++++++++++++++++++++++++++++++ -->

  <xsl:template name="element">

    <!--      ============================================== -->
    <!--      |     Template for element reference info      -->
    <!--      ============================================== -->

    <xsl:variable name="newline">
      <xsl:text>
      </xsl:text>
    </xsl:variable>

    <xsl:value-of select="name"/>
    <xsl:if test="count(./attr) > 0">
      <xsl:text> : </xsl:text>
      <xsl:call-template name="attributes"/>
    </xsl:if>
    
    <xsl:if test="count(./data | ./elRef | ./sequence | ./choice)  > 0">
      <xsl:value-of select="$newline"/>
      <xsl:text> ::= </xsl:text>
      <xsl:apply-templates select="./data | ./elRef | ./sequence | ./choice"/>
    </xsl:if>
    
    <xsl:value-of select="$newline"/>
  </xsl:template>
  
  
  <!--      ============================================== -->
  <!--      |        List the element's attributes         -->
  <!--      ============================================== -->
  
  <xsl:template name="attributes">
    <xsl:value-of select="./attr[attribute(default)='required']/@name" separator=", "></xsl:value-of>
    <xsl:if test="count(./attr[attribute(default)='implied']) > 0">
      <xsl:if test="count(./attr[attribute(default)='required']) > 0">
        <xsl:text>,</xsl:text>
      </xsl:if>
      <xsl:text> [</xsl:text>
      <xsl:value-of select="./attr[attribute(default)='implied']/@name" separator=", "></xsl:value-of>
      <xsl:text>] </xsl:text>
    </xsl:if>
  </xsl:template>
 
  <!--      ============================================== -->
  <!--      |                  Misc templates              -->
  <!--      ============================================== -->
  
  <xsl:template match="elRef" mode="#default">
    <xsl:value-of select="./@elID" separator=", "/>
    <xsl:call-template name="element_suffix">
      <xsl:with-param name="num" select="../@num"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="elRef" mode="sequence">
    <xsl:value-of select="./@elID" separator=", "/>
    <xsl:call-template name="element_suffix" >
      <xsl:with-param name="num" select="../@num"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="elRef" mode="choice">
    <xsl:value-of select="./@elID" separator=" | "/>
    <xsl:call-template name="element_suffix" >
      <xsl:with-param name="num" select="../@num"/>
    </xsl:call-template>
  </xsl:template>
  
  <xsl:template match="data">
    <xsl:text>#PCDATA</xsl:text>
  </xsl:template>
  
  <xsl:template match="choice">
    <xsl:text>(</xsl:text><xsl:apply-templates mode="choice"/><xsl:text>)</xsl:text>
  </xsl:template>
  
  <xsl:template match="sequence">
    <xsl:apply-templates mode="sequence"/>
  </xsl:template>
  
  <xsl:template name="element_suffix">
    <xsl:param name="num"/>
    <xsl:choose>
      <xsl:when test="num=oneplus"><xsl:text>+</xsl:text></xsl:when>
      <xsl:when test="num=binary"><xsl:text>?</xsl:text></xsl:when>
      <xsl:when test="num=zeroplus"><xsl:text>*</xsl:text></xsl:when>
    </xsl:choose>
  </xsl:template>
  
  
  
</xsl:stylesheet>
