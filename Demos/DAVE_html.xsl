<?xml version ="1.0" encoding="UTF-8"?>
<xsl:stylesheet  version="1.0"
        xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xlink="http://www.w3.org/1999/xlink"
        xmlns:mathml="http://www.w3.org/1998/Math/MathML"
>
<xsl:output method="html" indent="yes"/>

<!-- 020427 Bruce Jackson (e.b.jackson@nasa.gov) $Revision: 1.13 $ -->
<!-- Use: "java -cp ${XALAN} org.apache.xalan.sxlt.Process -in filename.xml -xsl DAVE_html.xsl -out filename.html" -->

<!-- Top-level template; parse various elements. Note logic to put
     plural on header lines when necessary. -->

<xsl:template match="/">
  <html>
  <head>
<title>DAVE-ML model document listing</title>
  </head>
  <body>

  <!-- Deal with fileHeader stuff -->

    <xsl:apply-templates select="DAVEfunc/fileHeader"/>

  <!-- Put proper plural on header line, if required -->
    <xsl:if test="count(DAVEfunc/variableDef) &gt; 1">
      <h1>Signal Definitions</h1>
    </xsl:if>
    <xsl:if test="count(DAVEfunc/variableDef) = 1">
      <h1>Signal Definition</h1>
    </xsl:if>

  <!-- If any variable definitions are found, put them in a table
       which we set up here -->

    <xsl:if test="count(DAVEfunc/variableDef) &gt; 0">
      <table cellpadding="2" border="0">
        <tr bgcolor="lightblue">
          <th>Symbol</th>
          <th>Name</th>
          <th>Units</th>
          <th>Sign</th>
          <th>Initial value</th>
          <th>Depends on</th>
        </tr>
        <xsl:apply-templates select="DAVEfunc/variableDef">
          <xsl:sort select="@name"/>
        </xsl:apply-templates>
      </table>
    </xsl:if>

  <!-- Put proper plural on header line, if required -->

    <xsl:if test="count(DAVEfunc/function) &gt; 1">
      <h1>Function Descriptions</h1>
    </xsl:if>
    <xsl:if test="count(DAVEfunc/function) = 1">
      <h1>Function Description</h1>
    </xsl:if>

  <!-- I copied the variable definition logic here, in case we want to
       put the function definitions into a similar table. Could do
       without this if test since the template is used only if a
       function is found. -->

    <xsl:if test="count(DAVEfunc/function) &gt; 0">
      <xsl:apply-templates select="DAVEfunc/function">
         <xsl:sort select="@name"/>
      </xsl:apply-templates>
    </xsl:if>

  <!-- end the page with a footer -->

    <xsl:element name="hr"/>
    <xsl:element name="em">
      <xsl:text>Converted using DAVE_html.xsl, $Revision: 1.13 $</xsl:text>
    </xsl:element>
    <xsl:text>.</xsl:text>
    <xsl:element name="br"/>
    <xsl:text>More information about DAVE is available </xsl:text>
    <xsl:element name="a">
      <xsl:attribute name="href">
        <xsl:text>http://dcb.larc.nasa.gov/utils/fltsim/DAVE</xsl:text>
      </xsl:attribute>
    <xsl:text>here</xsl:text>
    </xsl:element>
    <xsl:text>.</xsl:text>

  </body>
  </html>
</xsl:template>

<!-- Deal with the file header, including an intelligent title line -->

<xsl:template match="fileHeader">
  <xsl:call-template name="modelTitle"/>
  <xsl:apply-templates select="description"/>
  <xsl:apply-templates select="author"/>
  <xsl:apply-templates select="fileCreationDate"/>
  <xsl:apply-templates select="reference"/>
</xsl:template>

<xsl:template name="modelTitle">
  <xsl:choose>
    <xsl:when test="count(@name) &gt; 0">
      <h1><xsl:value-of select="@name"/></h1>
    </xsl:when>
    <xsl:otherwise>
      <h1>DAVE-ML Model Description</h1>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="description">
  <h3>
   <font color="#444444">
   <xsl:value-of select="text()"/>
    </font>
  </h3>
</xsl:template>

<xsl:template match="author">
  <p><b>Author: </b> <xsl:value-of select="@name"/></p>
</xsl:template>

<xsl:template match="fileCreationDate">
  <p><b>Created: </b> <xsl:value-of select="@date"/></p>
</xsl:template>

<xsl:template match="reference">
  <p>
    <b>
      <xsl:text>Reference </xsl:text>
      <xsl:value-of select="position()"/>
      <xsl:text>. </xsl:text>
    </b>

    <xsl:value-of select="@author"/>
    <xsl:text>: </xsl:text>

    <!-- Wrap the title in a hypertext link, if available -->
    <xsl:choose>
      <xsl:when test="count(@xlink:href) = 1">
        <xsl:element name="a">
          <xsl:attribute name="href">
            <xsl:value-of select="@xlink:href"/>
          </xsl:attribute>
          <b><xsl:value-of select="@title"/></b>
        </xsl:element>
      </xsl:when>

      <!-- Otherwise, just spit out the title -->
      <xsl:otherwise>
        <b><xsl:value-of select="@title"/></b>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:text>. </xsl:text>

    <xsl:if test="count(@accession) = 1">
      <xsl:value-of select="@accession"/>
      <xsl:text>, </xsl:text>
    </xsl:if>

    <xsl:value-of select="@date"/>

    <xsl:text>.</xsl:text>
  </p>
</xsl:template>

<!-- Variable definitions turn into a double row in the table -->

<xsl:template match="variableDef">
    <tr>
      <xsl:call-template name="colorRow"/>
      <td align="center" rowspan="2">  <!-- column 1; name (double height w/anchor) -->
	<xsl:element name="a">
          <xsl:attribute name="name">
            <xsl:value-of select="@varID"/>
          </xsl:attribute>
          <b><xsl:value-of select="@name"/></b>
        </xsl:element>
      </td>

      <td align="center">  <!-- column 2: symbol -->
	<em><xsl:value-of select="@symbol"/></em>
      </td>

      <td align="center">  <!-- column 3: units -->
	<xsl:value-of select="@units"/>
      </td>

      <td align="center">  <!-- column 4: sign convention -->
	<xsl:if test="@sign">
	  +<xsl:value-of select="@sign"/> 
	</xsl:if>
      </td>

      <td align="right">  <!-- column 5: initial value -->
	<code>
	  <xsl:value-of select="@initialValue"/>
	</code>
      </td>

      <td rowspan="2" align="center">  <!-- column 6: antecedents -->
        <xsl:apply-templates select="calculation"/>
      </td>

    </tr>

    <tr>
      <xsl:call-template name="colorRow"/>
      <td colspan="4">  <!-- second line -->
        <xsl:value-of select="description/text()"/>
      </td>
    </tr>
</xsl:template>

<xsl:template name="colorRow">
  <xsl:attribute name="bgcolor">
    <xsl:choose>
      <xsl:when test="position() mod 2">
	<xsl:text>white</xsl:text>
      </xsl:when>
      <xsl:otherwise>
	<xsl:text>lightgreen</xsl:text>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:attribute>
</xsl:template>

<xsl:key name="ciKey" match="mathml:ci" use="."/>
<xsl:template match="calculation">
  <font size="-1">
    <xsl:for-each select=".//mathml:ci">
      <xsl:sort select="."/>
        <xsl:value-of select="."/><br/>
    </xsl:for-each>
  </font>
</xsl:template>

<!-- Functions are described as individual tables with values -->

<xsl:template match="function">
  <xsl:variable name="IVConditionCount">
    <xsl:value-of select="count(./independentVarRef)"/>
  </xsl:variable>
  <table cellpadding="2" border="0" width="100%">
    <tr>
      <xsl:attribute name="bgcolor">
        <xsl:text>lightblue</xsl:text>
      </xsl:attribute>
      <td width="25%">
        <xsl:attribute name="rowspan">
          <xsl:value-of select="$IVConditionCount + 1"/>
        </xsl:attribute>
        <b>
          <xsl:value-of select="@name"/>
        </b>
      </td>
      <td colspan="2">
        <font size="-1">
	  <b>
	    <xsl:variable name="depVarID" select="dependentVarRef/@varID"/>
	    <xsl:value-of select="/DAVEfunc/variableDef[@varID=$depVarID]/@name"/>
	  </b>
	  <em>
	    <xsl:text> = f(</xsl:text>
	  </em>
	  <xsl:for-each select="independentVarRef">
            <xsl:call-template name="translateVarID"/>
            <xsl:if test="not(position()=last())">
              <em><xsl:text>, </xsl:text></em>
            </xsl:if>
	  </xsl:for-each>
	  <em>   
	    <xsl:text>)</xsl:text>
	  </em>
        </font>
      </td>
    </tr>
    <xsl:if test="(independentVarRef/@min | independentVarRef/@max | independentVarRef/@extrapolate)">
      <xsl:call-template name="IVconditions">
        <xsl:with-param name="IVcondCount">
          <xsl:value-of select="$IVConditionCount"/>
        </xsl:with-param>
      </xsl:call-template>
    </xsl:if>
    <tr>
      <td colspan="3"><xsl:value-of select="description/text()"/></td>
    </tr>
  </table>
</xsl:template>

<xsl:template name="IVconditions">
  <xsl:param name="IVcondCount"/>
  <xsl:for-each select="independentVarRef">
    <tr bgcolor="lightblue">
      <xsl:if test="position() = 1">
        <td>
          <xsl:attribute name="rowspan">
            <xsl:value-of select="$IVcondCount"/>
          </xsl:attribute>
          <font size="-1"><em><xsl:text>where: </xsl:text></em></font>
        </td>
      </xsl:if>
      <td><font size="-1">
        <xsl:if test="./@min">
          <xsl:value-of select="./@min"/>
          <xsl:text> &lt;= </xsl:text>
        </xsl:if>
        <xsl:if test="(./@min | ./@max)">
          <xsl:call-template name="translateVarID"/>
	</xsl:if>
	<xsl:if test="./@max">
	  <xsl:text> &lt;= </xsl:text>
	  <xsl:value-of select="./@max"/>
	</xsl:if>
        <xsl:if test="./@extrapolate">
          <em>
            <xsl:text>; extrapolate </xsl:text>
            <b>
              <xsl:value-of select="@extrapolate"/>
            </b>
          </em>
        </xsl:if>
      </font></td>
    </tr>
  </xsl:for-each>
</xsl:template>


<xsl:template name="translateVarID">
  <xsl:variable name="indepVarID" select="./@varID"/>
  <b>
    <xsl:choose>
      <xsl:when test="/DAVEfunc/variableDef[@varID=$indepVarID]/@symbol">
	<xsl:value-of select="/DAVEfunc/variableDef[@varID=$indepVarID]/@symbol"/>
      </xsl:when>
      <xsl:otherwise>
	<xsl:value-of select="/DAVEfunc/variableDef[@varID=$indepVarID]/@name"/>
      </xsl:otherwise>
    </xsl:choose>
  </b>
</xsl:template>


</xsl:stylesheet>