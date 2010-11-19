<?xml version="1.0"?>
<!-- Customization layer for DAVE-ML reference -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:fo="http://www.w3.org/1999/XSL/Format"
		version="1.0">

<!-- load default stylesheet -->

<xsl:import href="/Users/bjax/Documents/DocBook/docbook-xsl/fo/docbook.xsl"/>

<!-- load my titlepage template -->

<xsl:import href="my_fo_titlepage.xsl"/>

<!-- My customizations -->

<xsl:template match="processing-instruction('hard-pagebreak')">
  <fo:block break-after='page'/>
</xsl:template>

<xsl:param name="generate.toc">
  article toc,title
</xsl:param>

<!-- Change PDF xref page references from [xx] to (p. xx) -->
<xsl:param name="local.l10n.xml" select="document('')"/> 
<l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
  <l:l10n language="en"> 
    <l:context name="xref">
      <l:template name="page.citation" text=" (p. %p)"/>
    </l:context>
  </l:l10n> 
</l:i18n>

<!-- fiddling around with title page elements -->
<xsl:template match="editor" mode="titlepage.mode">
  <fo:block>
    <xsl:call-template name="anchor"/>
    <xsl:call-template name="person.name"/>
    <xsl:if test="affiliation/shortaffil">
      <xsl:text>, </xsl:text>
      <xsl:apply-templates select="affiliation/shortaffil" mode="titlepage.mode"/>
    </xsl:if>
    <xsl:if test="email|affiliation/address/email">
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="(email|affiliation/address/email)[1]"/>
    </xsl:if>
  </fo:block>
</xsl:template>

<xsl:template name="article.titlepage.separator">
  <fo:block break-after='page'/>
</xsl:template>

<xsl:param name="insert.xref.page.number">yes</xsl:param>
<xsl:param name="xref.with.number.and.title">0</xsl:param>

<!-- Times 11 for body -->
<xsl:param name="body.font.family">serif</xsl:param>
<xsl:param name="body.font.master">11</xsl:param>


<!-- for PDF output, need to reduce size of header font;
     I'm mapping the sect1 to be sect3 scaling, etc. as found
     in fo/params.xsl -->

<xsl:attribute-set name="section.title.level1.properties">
  <xsl:attribute name="font-size">
    <xsl:value-of select="$body.font.master * 1.44"/>
    <xsl:text>pt</xsl:text>
  </xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level2.properties">
  <xsl:attribute name="font-size">
    <xsl:value-of select="$body.font.master * 1.2"/>
    <xsl:text>pt</xsl:text>
  </xsl:attribute>
</xsl:attribute-set>
<xsl:attribute-set name="section.title.level3.properties">
  <xsl:attribute name="font-size">
    <xsl:value-of select="$body.font.master"/>
    <xsl:text>pt</xsl:text>
  </xsl:attribute>
</xsl:attribute-set>

<!-- variable listing titles (such as attributes and subelement lists) 
     need to be smaller -->
<xsl:template match="variablelist/title" mode="list.title.mode">
  <fo:block font-size="10pt" font-weight="bold"
	    xsl:use-attribute-sets="normal.para.spacing">
    <xsl:apply-templates/>
  </fo:block>
</xsl:template>

<!-- examples and figures also need to have smaller fonts -->
<xsl:attribute-set name="formal.title.properties">
  <xsl:attribute name="font-size">10pt</xsl:attribute>
</xsl:attribute-set>

<!-- want to have figure names after the figures -->
<xsl:param name="formal.title.placement">figure after</xsl:param>

    <!-- other layout changes -->
<xsl:param name="toc.section.depth">3</xsl:param>
<xsl:param name="page.margin.inner">1.0in</xsl:param>
<xsl:param name="page.margin.outer">1.0in</xsl:param>
<xsl:param name="header.column.widths">1 5 1</xsl:param> <!-- give more room to doc name -->
	<!-- Use unicode characters for callouts -->
<xsl:param name="callout.graphics">0</xsl:param>
<xsl:param name="callout.unicode">1</xsl:param>

<xsl:attribute-set name="monospace.verbatim.properties">
  <xsl:attribute name="wrap-option">wrap</xsl:attribute>
  <xsl:attribute name="hyphenation-character">\</xsl:attribute>
  <xsl:attribute name="font-size">73%</xsl:attribute>
</xsl:attribute-set>


<!-- Don't generate 'Name' section in refentries  
     but instead use element name as title   -->
<xsl:param name="refentry.generate.name">0</xsl:param>
<xsl:param name="refentry.generate.title">1</xsl:param>

<!-- Enlarge the name in refentry; defaults set in fo/param.xsl -->
<xsl:attribute-set name="refentry.title.properties">
  <xsl:attribute name="font-family">Courier</xsl:attribute>
  <xsl:attribute name="font-size">24pt</xsl:attribute>
</xsl:attribute-set>

<!-- Turn section autolabeling on -->
<xsl:param name="section.autolabel">1</xsl:param>

<!-- Abstract title; not working -->
<xsl:attribute-set name="abstract.title.properties">
  <xsl:attribute name="font-weight">normal</xsl:attribute>
  <xsl:attribute name="text-align">right</xsl:attribute>
</xsl:attribute-set>

<!-- change word abstract to overview on title page; also not working -->
<xsl:param name="local.l10n.xml" select="document('')"/>
<l:i18n xmlns:l="http://docobook.sourceforge.net/xmlns/l10n/1.0">
  <l:l10n language="en">
    <l:gentext name="Abstract" text="Overview"/>
    <l:gentext name="abstract" text="Overview"/>
  </l:l10n>
</l:i18n>

<!-- make two columns in index -->
<xsl:param name="column.count.indext" select="2"/>

</xsl:stylesheet>
