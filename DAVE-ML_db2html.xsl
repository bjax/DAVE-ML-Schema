<?xml version="1.0"?>
<!-- Customization layer for DAVE-ML reference -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- load default stylesheet -->

<xsl:import href="/Users/bjax/Documents/DocBook/docbook-xsl/xhtml/chunk.xsl"/>

<!-- My customizations -->

<xsl:param name="index.prefer.titleabbrev">1</xsl:param>
<xsl:param name="section.autolabel">1</xsl:param>
<xsl:param name="base.dir">html/</xsl:param>
<xsl:param name="html.extra.head.links">0</xsl:param>
<xsl:param name="navig.showtitles">0</xsl:param>
<xsl:param name="root.filename">DAVE-ML_ref</xsl:param>
<xsl:param name="chunk.section.depth">3</xsl:param>
<xsl:param name="chuck.fast">1</xsl:param>
<xsl:param name="chunk.tocs.and.lots">1</xsl:param>
<xsl:param name="chunk.first.sections">1</xsl:param>
<xsl:param name="html.stylesheet">DAVE-ML_ref.css</xsl:param>
<xsl:param name="toc.section.depth">3</xsl:param>
<xsl:param name="use.id.as.filename">1</xsl:param>
<xsl:param name="generate.toc">
  article  toc,title
  appendix toc,title
</xsl:param>

<!-- Don't generate 'Name' section in refentries  
     but instead use element name as title   -->
<xsl:param name="refentry.generate.name">0</xsl:param>
<xsl:param name="refentry.generate.title">1</xsl:param>

<!-- put version number in footers -->
<xsl:template name="user.footer.content">
  <P class="CVSinfo">
    <xsl:value-of select="//releaseinfo[1]"/>
  </P>
</xsl:template>

<!-- add changebars (copied from changebars.xsl in DocBook distro) -->
<xsl:param name="show.revisionflag" select="'1'"/>

<xsl:template name="system.head.content">
<xsl:param name="node" select="."/>

<style type="text/css">
<xsl:text>
div.added    { background-color: #ffff99; }
div.deleted  { text-decoration: line-through;
               background-color: #FF7F7F; }
div.changed  { background-color: #99ff99; }
div.off      {  }

span.added   { background-color: #ffff99; }
span.deleted { text-decoration: line-through;
               background-color: #FF7F7F; }
span.changed { background-color: #99ff99; }
span.off     {  }
</xsl:text>
</style>
</xsl:template>

<xsl:template match="*[@revisionflag]">
  <xsl:choose>
    <xsl:when test="local-name(.) = 'para'
                    or local-name(.) = 'simpara'
                    or local-name(.) = 'formalpara'
                    or local-name(.) = 'section'
                    or local-name(.) = 'sect1'
                    or local-name(.) = 'sect2'
                    or local-name(.) = 'sect3'
                    or local-name(.) = 'sect4'
                    or local-name(.) = 'sect5'
                    or local-name(.) = 'chapter'
                    or local-name(.) = 'preface'
                    or local-name(.) = 'itemizedlist'
                    or local-name(.) = 'varlistentry'
                    or local-name(.) = 'glossary'
                    or local-name(.) = 'bibliography'
                    or local-name(.) = 'index'
                    or local-name(.) = 'appendix'">
      <div class='{@revisionflag}'>
	<xsl:apply-imports/>
      </div>
    </xsl:when>
    <xsl:when test="local-name(.) = 'phrase'
                    or local-name(.) = 'ulink'
                    or local-name(.) = 'link'
                    or local-name(.) = 'filename'
                    or local-name(.) = 'literal'
                    or local-name(.) = 'member'
                    or local-name(.) = 'glossterm'
                    or local-name(.) = 'sgmltag'
                    or local-name(.) = 'quote'
                    or local-name(.) = 'emphasis'
                    or local-name(.) = 'command'
                    or local-name(.) = 'xref'">
      <span class='{@revisionflag}'>
	<xsl:apply-imports/>
      </span>
    </xsl:when>
    <xsl:when test="local-name(.) = 'listitem'
                    or local-name(.) = 'entry'
                    or local-name(.) = 'title'">
      <!-- nop; these are handled directly in the stylesheet -->
      <xsl:apply-imports/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:message>
	<xsl:text>Revisionflag on unexpected element: </xsl:text>
	<xsl:value-of select="local-name(.)"/>
	<xsl:text> (Assuming block)</xsl:text>
      </xsl:message>
      <div class='{@revisionflag}'>
	<xsl:apply-imports/>
      </div>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- template to apply breadcrumbs to header -->

<xsl:template name="breadcrumbs"> 
  <xsl:param name="this.node" select="."/> 
  <div class="breadcrumbs"> 
    <xsl:for-each select="$this.node/ancestor::*"> 
      <span class="breadcrumb-link"> 
	<a> 
	  <xsl:attribute name="href"> 
	    <xsl:call-template name="href.target"> 
	      <xsl:with-param name="object" select="."/> 
	      <xsl:with-param name="context" select="$this.node"/> 
	    </xsl:call-template> 
	  </xsl:attribute> 
	  <xsl:apply-templates select="." mode="titleabbrev.markup"/> 
	</a> 
      </span> 
      <xsl:text> &gt; </xsl:text> 
    </xsl:for-each> 

    <!-- And display the current node, but not as a link --> 

    <span class="breadcrumb-node"> 
      <xsl:apply-templates select="$this.node" mode="titleabbrev.markup"/> 
    </span> 

  </div> 
</xsl:template> 

<xsl:template name="user.header.content"> 
  <xsl:call-template name="breadcrumbs"/> 
</xsl:template> 

</xsl:stylesheet>
