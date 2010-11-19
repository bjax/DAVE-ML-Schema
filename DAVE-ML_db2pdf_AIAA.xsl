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

<!-- Helvetica 10 for body -->
<xsl:param name="body.font.family">Helvetica</xsl:param>
<xsl:param name="body.font.master">10</xsl:param>

<!-- AIAA appendix customizations -->

<!-- Figure labels prepended with B- -->
<xsl:param name="local.l10n.xml" select="document('')"/>
<l:i18n xmlns:l="http://docbook.sourceforge.net/xmlns/l10n/1.0">
  <l:l10n language="en">
    <l:context name="title">
      <l:template name="example" text="Example&#160;B-%n.&#160;%t"/>
      <l:template name="figure" text="Figure&#160;B-%n.&#160;%t"/>
    </l:context>
    <l:context name="xref-number">
      <l:template name="example" text="example&#160;B-%n"/>
      <l:template name="figure" text="figure&#160;B-%n"/>
      <l:template name="section" text="section&#160;B-%n"/>
    </l:context>
    <l:context name="xref-number-and-title">
      <l:template name="example" text="example&#160;B-%n, &#8220;%t&#8221;"/>
      <l:template name="figure" text="figure&#160;B-%n, &#8220;%t&#8221;"/>
    </l:context>
  </l:l10n>
</l:i18n>

<!-- Prepend 'B' in front of section names, for AIAA appendix -->
<!-- original template is in common/labels.xsl -->

<xsl:template match="sect1" mode="label.markup">
  <!-- if the parent is a component, maybe label that too -->
  <xsl:variable name="parent.is.component">
    <xsl:call-template name="is.component">
      <xsl:with-param name="node" select=".."/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="component.label">
    <xsl:if test="$section.label.includes.component.label != 0
                  and $parent.is.component != 0">
      <xsl:variable name="parent.label">
        <xsl:apply-templates select=".." mode="label.markup"/>
      </xsl:variable>
      <xsl:if test="$parent.label != ''">
        <xsl:apply-templates select=".." mode="label.markup"/>
        <xsl:apply-templates select=".." mode="intralabel.punctuation"/>
      </xsl:if>
    </xsl:if>
  </xsl:variable>


  <xsl:variable name="is.numbered">
    <xsl:call-template name="label.this.section"/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="@label">
      <xsl:value-of select="@label"/>
    </xsl:when>
    <xsl:when test="$is.numbered != 0">
      <xsl:variable name="format">
        <xsl:call-template name="autolabel.format">
          <xsl:with-param name="format" select="$section.autolabel"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:copy-of select="$component.label"/>
      <xsl:text>B-</xsl:text>
      <xsl:number format="{$format}" count="sect1"/>
    </xsl:when>
  </xsl:choose>
</xsl:template>

<xsl:template match="sect2|sect3|sect4|sect5" mode="label.markup">
  <!-- label the parent -->
  <xsl:variable name="parent.section.label">
    <xsl:call-template name="label.this.section">
      <xsl:with-param name="section" select=".."/>
    </xsl:call-template>
  </xsl:variable>
  <xsl:if test="$parent.section.label != '0'">
    <xsl:apply-templates select=".." mode="label.markup"/>
    <xsl:apply-templates select=".." mode="intralabel.punctuation"/>
  </xsl:if>

  <xsl:variable name="is.numbered">
    <xsl:call-template name="label.this.section"/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="@label">
      <xsl:value-of select="@label"/>
    </xsl:when>
    <xsl:when test="$is.numbered != 0">
      <xsl:variable name="format">
        <xsl:call-template name="autolabel.format">
          <xsl:with-param name="format" select="concat('B-',$section.autolabel)"/>
        </xsl:call-template>
      </xsl:variable>
      <xsl:choose>
        <xsl:when test="local-name(.) = 'sect2'">
          <xsl:number format="{$format}" count="sect2"/>
        </xsl:when>
        <xsl:when test="local-name(.) = 'sect3'">
          <xsl:number format="{$format}" count="sect3"/>
        </xsl:when>
        <xsl:when test="local-name(.) = 'sect4'">
          <xsl:number format="{$format}" count="sect4"/>
        </xsl:when>
        <xsl:when test="local-name(.) = 'sect5'">
          <xsl:number format="{$format}" count="sect5"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:message>label.markup: this can't happen!</xsl:message>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
  </xsl:choose>
</xsl:template>

<!-- custom footer -->
<xsl:template name="footer.content">
  <xsl:param name="pageclass" select="''"/>
  <xsl:param name="sequence" select="''"/>
  <xsl:param name="position" select="''"/>
  <xsl:param name="gentext-key" select="''"/>

<!--
  <fo:block>
    <xsl:value-of select="$pageclass"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$sequence"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$position"/>
    <xsl:text>, </xsl:text>
    <xsl:value-of select="$gentext-key"/>
  </fo:block>
-->

  <fo:block>
    <!-- pageclass can be front, body, back -->
    <!-- sequence can be odd, even, first, blank -->
    <!-- position can be left, center, right -->
    <xsl:choose>
      <xsl:when test="$pageclass = 'titlepage'">
        <!-- nop; no footer on title pages -->
      </xsl:when>

      <xsl:when test="$double.sided != 0 and $sequence = 'even'
                      and $position='left'">
        <fo:page-number/>
      </xsl:when>

      <xsl:when test="$double.sided != 0 and ($sequence = 'odd' or $sequence = 'first')
                      and $position='right'">
        <fo:page-number/>
      </xsl:when>

      <xsl:when test="$double.sided = 0 and $position='right'">
        <fo:page-number/>
      </xsl:when>

      <xsl:when test="$double.sided = 0 and $position='left'">
	<xsl:text>Annex B</xsl:text>
      </xsl:when>

      <xsl:when test="$sequence='blank'">
        <xsl:choose>
          <xsl:when test="$double.sided != 0 and $position = 'left'">
            <fo:page-number/>
          </xsl:when>
          <xsl:when test="$double.sided = 0 and $position = 'center'">
            <fo:page-number/>
          </xsl:when>
          <xsl:otherwise>
            <!-- nop -->
          </xsl:otherwise>
        </xsl:choose>
      </xsl:when>


      <xsl:otherwise>
        <!-- nop -->
      </xsl:otherwise>
    </xsl:choose>
  </fo:block>
</xsl:template>



<!-- end of AIAA customizations -->

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

<!-- Turn section autolabeling on -->
<xsl:param name="section.autolabel">1</xsl:param>

<!-- make two columns in index -->
<xsl:param name="column.count.indext" select="2"/>

</xsl:stylesheet>
