<?xml version ="1.0" encoding="UTF-8"?>
<xsl:stylesheet  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:mathml="http://www.w3.org/TR/Math/MathML2"
  >
  <xsl:output method="xml" indent="no"/>
  
  <!-- 
    Converts my home-grown DTD .xml specification back into a DTD document. Tested with
    DAVEfunc.dtd, version 1.5, and appears to recreate the dtd correctly.
    
    030708 Bruce Jackson (e.b.jackson@nasa.gov) $Revision: 1.4 $ 
    Use: "java -cp ${XALAN} org.apache.xalan.xslt.Process -in filename.xml -xsl xml2dtd.xsl -out filename.html"
  -->
  
  <xsl:template match="/dtd">
    <xsl:apply-templates select="genlcomment | elLevel | el | entityDef | entityRef"/>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++             GENERAL COMMENT                    ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="genlcomment">
    
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:variable name="commentStart">
      <xsl:text> =================================================================</xsl:text>
    </xsl:variable>
    <xsl:variable name="commentEnd">
      <xsl:text>     ================================================================= </xsl:text>
    </xsl:variable>
    
    <xsl:comment>
      <xsl:value-of select="$commentStart"/>
      <xsl:value-of select="$newline"/>
      <xsl:value-of select="."/>
      <xsl:value-of select="$newline"/>
      <xsl:value-of select="$commentEnd"/>
    </xsl:comment>
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$newline"/>
  </xsl:template>
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++                   ELEMENT LEVEL                ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="elLevel">
    
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:value-of select="$newline"/>
    <xsl:comment> +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ </xsl:comment>
    <xsl:value-of select="$newline"/>
    <xsl:comment>                         Level <xsl:value-of select="."/> Elements                         </xsl:comment>
    <xsl:value-of select="$newline"/>
    <xsl:comment> +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ </xsl:comment>
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$newline"/>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++         Generate the ELEMENT declaration       ++ --> 
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="el">

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:variable name="indent">
      <xsl:text>    </xsl:text>
    </xsl:variable>
    
    <xsl:choose>

      <xsl:when test="nodef"></xsl:when>

      <xsl:otherwise>

	<xsl:apply-templates select="./descrip"/>
    
	<xsl:text disable-output-escaping="yes">&lt;!ELEMENT </xsl:text>
	<xsl:value-of select="./name"/>
	<xsl:text> </xsl:text>
    
	<xsl:if test="data">
	  <xsl:text>(#PCDATA)</xsl:text>
	</xsl:if>
    
	<xsl:if test="sequence">
	  <xsl:text>(</xsl:text>
	  <xsl:apply-templates select="sequence">
	    <xsl:with-param name="indent" select="$indent"/>
	  </xsl:apply-templates>
	  <xsl:text>)</xsl:text>
	  <xsl:value-of select="$newline"/>
	</xsl:if>
    
	<xsl:apply-templates select="choice">
	  <xsl:with-param name="indent" select="$indent"/>
	</xsl:apply-templates>
    
	<xsl:if test="elRef">
	  <xsl:text>(</xsl:text>
	  <xsl:apply-templates select='elRef'/>
	  <xsl:text>)</xsl:text>
	</xsl:if>
    
	<xsl:if test="empty">
	  <xsl:text>EMPTY</xsl:text>
	</xsl:if>

	<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
    
	<xsl:value-of select="$newline"/>
    
	<xsl:if test="attr">
	  <xsl:text disable-output-escaping="yes">&lt;!ATTLIST </xsl:text>
	  <xsl:value-of select="name"/>
	  <xsl:value-of select="$newline"/>
	  <xsl:apply-templates select="attr"/>
	  <xsl:text disable-output-escaping="yes">&gt;</xsl:text>
	</xsl:if>
    
	<xsl:value-of select="$newline"/>
	<xsl:value-of select="$newline"/>

      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++           Handle an element SEQUENCE           ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="sequence">
    <xsl:param name="indent"/>
    
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:variable name="newindent">
      <xsl:value-of select="$indent"/>
      <xsl:text>  </xsl:text>
    </xsl:variable>
    
    <xsl:value-of select="$newline"/>
    
    <xsl:for-each select="choice|elRef|data">
      
      <xsl:value-of select="$newindent"/>
      
      <xsl:apply-templates select=".">
        <xsl:with-param name="indent" select="$indent"/>
      </xsl:apply-templates>
      <xsl:if test="position() != last()">
        <xsl:text>, </xsl:text>
        <xsl:value-of select="$newline"/>
      </xsl:if>
    </xsl:for-each>
    
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$indent"/>

    <xsl:if test="@num = 'oneplus'">
      <xsl:text>+</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'zeroplus'">
      <xsl:text>*</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'binary'">
      <xsl:text>?</xsl:text>
    </xsl:if>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++           Handle a CHOICE of elements          ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="choice">
    <xsl:param name="indent"/>
    
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:variable name="newindent">
      <xsl:value-of select="$indent"/>
      <xsl:text>  </xsl:text>
    </xsl:variable>
    
    <xsl:text>(</xsl:text>

    <xsl:for-each select="data|sequence|elRef">

      <xsl:if test="../sequence">
        <xsl:text>(</xsl:text>
      </xsl:if>
      
      
      <xsl:apply-templates select=".">
        <xsl:with-param name="indent" select="$newindent"/>
      </xsl:apply-templates>
      <xsl:if test="../sequence">
        <xsl:text>)</xsl:text>
      </xsl:if>
      <xsl:if test="position() != last()">
        <xsl:text> | </xsl:text>
      </xsl:if> 
      
    </xsl:for-each>
    
    <xsl:text>)</xsl:text>
    <xsl:if test="@num = 'oneplus'">
      <xsl:text>+</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'zeroplus'">
      <xsl:text>*</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'binary'">
      <xsl:text>?</xsl:text>
    </xsl:if>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++       Handle individual elements (ELREF)       ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="elRef">
    <xsl:value-of select="@elID"/>
    <xsl:if test="@num = 'oneplus'">
      <xsl:text>+</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'zeroplus'">
      <xsl:text>*</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'binary'">
      <xsl:text>?</xsl:text>
    </xsl:if>
  </xsl:template>
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++         Handle a data element (PCDATA)         ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="data">
    <xsl:text>#PCDATA</xsl:text>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++        Deal with attribute (ATTR) lists        ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="attr">
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:text>     </xsl:text>
    <xsl:value-of select="@name"/>
    <xsl:text>	</xsl:text>
    
    <xsl:if test="@type = 'simple'">
      <xsl:text>(simple)	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'cdata'">
      <xsl:text>CDATA	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'nmtoken'">
      <xsl:text>NMTOKEN		</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'nmtokens'">
      <xsl:text>NMTOKENS	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'entity'">
      <xsl:text>ENTITY	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'entities'">
      <xsl:text>ENTITIES	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'id'">
      <xsl:text>ID	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'idref'">
      <xsl:text>IDREF	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'notation'">
      <xsl:text>NOTATION	</xsl:text>
    </xsl:if>
    
    <xsl:if test="@type = 'enum'">
      <xsl:text>(</xsl:text>
      <xsl:for-each select="enumVal">
        <xsl:value-of select="."/>
        <xsl:if test="position() != last()">
          <xsl:text> | </xsl:text>
        </xsl:if>
      </xsl:for-each>
      <xsl:text>) </xsl:text>
    </xsl:if>
    
    <xsl:if test="@default = 'implied'">
      <xsl:text>#IMPLIED</xsl:text>
    </xsl:if>
    
    <xsl:if test="@default = 'required'">
      <xsl:text>#REQUIRED</xsl:text>
    </xsl:if>
    
    <xsl:if test="@default = 'fixed'">
      <xsl:text>#FIXED</xsl:text>
      <xsl:if test="@value">
        <xsl:text>	'</xsl:text>
        <xsl:value-of select="@value"/>
        <xsl:text>'</xsl:text>
      </xsl:if>
    </xsl:if>
    
    <xsl:if test="@default = 'literal'">
      <xsl:text>"</xsl:text>
      <xsl:value-of select="@value"/>
      <xsl:text>"</xsl:text>
    </xsl:if>
    
    <xsl:value-of select="$newline"/>
    
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++     Generate an ENTITY DEFINITION statement    ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="entityDef">
    
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    
    <xsl:text disable-output-escaping="yes">&lt;!ENTITY % </xsl:text>
    <xsl:value-of select="@name"/>
    <xsl:text disable-output-escaping="yes"> PUBLIC "</xsl:text>
    <xsl:value-of select="@public"/>
    <xsl:text>"</xsl:text>
    <xsl:value-of select="$newline"/>
    <xsl:text disable-output-escaping="yes">        "</xsl:text>
    <xsl:value-of select="@system"/>
    <xsl:text disable-output-escaping="yes">"&gt;</xsl:text>
    <xsl:value-of select="$newline"/>
    <!-- really should be some selection logic for when entityRef element is hit, but this should work -->
    <xsl:text>%</xsl:text>
    <xsl:value-of select="@name"/>
    <xsl:text>;</xsl:text>
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$newline"/>
  </xsl:template>
  
  
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!-- ++     Print out pretty comments (descrip)        ++ -->
  <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
  
  <xsl:template match="descrip">
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>
    <xsl:variable name="commentStart">
      <xsl:text> =================================================================</xsl:text>
    </xsl:variable>
    <xsl:variable name="commentEnd">
      <xsl:text>     ================================================================= </xsl:text>
    </xsl:variable>
    <xsl:comment>
      <xsl:value-of select="$commentStart"/>
      <xsl:value-of select="$newline"/>
      <xsl:value-of select="."/>
      <xsl:value-of select="$newline"/>
      <xsl:value-of select="$commentEnd"/>
    </xsl:comment>
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$newline"/>
  </xsl:template>
  
  
</xsl:stylesheet>