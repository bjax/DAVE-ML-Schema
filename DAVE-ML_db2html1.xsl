<?xml version="1.0"?>

<!-- Makes single HTML page version of ref manual -->

<!-- Customization layer for DAVE-ML reference -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- load default stylesheet -->

<xsl:import href="/Users/bjax/Documents/DocBook/docbook-xsl/xhtml/docbook.xsl"/>

<!-- My customizations -->

<xsl:param name="base.dir">html/</xsl:param>
<xsl:param name="html.extra.head.links">1</xsl:param>
<xsl:param name="root.filename">DAVE-ML_ref</xsl:param>
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

</xsl:stylesheet>
