<?xml version ="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:mathml="http://www.w3.org/TR/Math/MathML2">
  <xsl:output method="xml" indent="yes"/>

  <!-- 

       Converts my home-grown DTD .xml specification into a file
       containing DocBook reference pages for each element.

       030711 Bruce Jackson (e.b.jackson@nasa.gov)
       Use: "java -cp ${XALAN} org.apache.xalan.xslt.Process -in filename.xml -xsl xml2db.xsl -out filename.html"
       
       2009-04-15 EBJ Rewrote to use xsl:element instead of stupid text equivalents

       2010-03-15 EBJ Rewrote indention and newline logic for Content
       Model. Now, each template is called with the input set at the
       proper place for writing, as before; but sequences are now all
       on separate lines and must generate a LF and indentation for
       subsequent items UNLESS it is the last item in a choice.
       
       Similiarly, CHOICEs must treat embedded sequences in a special
       way to get the OR clause in the right vertical position.

   -->

  <xsl:template match="/dtd">
    <xsl:element name="sect1">
      <xsl:attribute name="id">
        <xsl:text>elementRefs</xsl:text>
      </xsl:attribute>
      <xsl:element name="title">
        <xsl:text>Element references and descriptions</xsl:text>
      </xsl:element>
      <xsl:element name="para">
	<xsl:text>
	  This section lists the XML tags, or elements, that make up
	  the DAVE-ML grammar. They are listed alphabetically in two
	  sub-sections; the first is a short description of each
	  element; the second sub-section gives details on the
	  element, attributes, and sub-elements.
	</xsl:text>
      </xsl:element>
      <xsl:element name="sect2">
	<xsl:attribute name="id">
	  <xsl:text>elementAlphaList</xsl:text>
	</xsl:attribute>
	<xsl:element name="title">
	  <xsl:text>Alphabetical list of elements</xsl:text>
	</xsl:element>
	<xsl:element name="para">
	  <xsl:call-template name="genList"/>
	</xsl:element>
      </xsl:element> <!-- sect 2 elementAlphaList -->
      <xsl:element name="sect2">
	<xsl:attribute name="id">
	  <xsl:text>elementDescriptions</xsl:text>
	</xsl:attribute>
        <xsl:element name="title">
          <xsl:text>Element descriptions</xsl:text>
        </xsl:element>
	<xsl:element name="para">
	  <xsl:text>
	    This section lists each element in detail, giving the
	    name, content model, attributes, possible parent elements,
	    allowable children elements, and any future plans for the
	    element (such as deprecation).
	  </xsl:text>
	</xsl:element>
        <xsl:for-each select="el">
          <xsl:sort select="name"/>
          <xsl:call-template name="element"/>
        </xsl:for-each>
      </xsl:element>
    </xsl:element>
  </xsl:template>


  <!--      ++++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!--      + Generate an alphabetized varlist of elements + -->
  <!--      ++++++++++++++++++++++++++++++++++++++++++++++++ -->

  <xsl:template name="genList">
    <xsl:element name="variablelist">
      <xsl:for-each select="el">
	<xsl:sort select="name"/>
	<xsl:element name="varlistentry">
	  <xsl:element name="term">
	    <xsl:element name="link">
	      <xsl:attribute name="linkend">
		<xsl:value-of select="name"/>
	      </xsl:attribute>
	      <xsl:element name="varname">
		<xsl:value-of select="name"/>
	      </xsl:element> <!-- varname -->
	    </xsl:element> <!-- link -->
	  </xsl:element> <!-- term -->
	  <xsl:element name="listitem">
	    <xsl:element name="para">
	      <xsl:value-of select="purpose"/>
	    </xsl:element> <!-- para -->
	  </xsl:element> <!-- listitem -->
	</xsl:element> <!-- varlistentry -->
      </xsl:for-each> <!-- select el -->
    </xsl:element> <!-- variableList -->
  </xsl:template>

  <!--      ++++++++++++++++++++++++++++++++++++++++++++++ -->
  <!--      +     Generate the element documentation     + -->
  <!--      ++++++++++++++++++++++++++++++++++++++++++++++ -->

  <xsl:template name="element">

    <!--      ============================================== -->
    <!--      |     Template for element reference info      -->
    <!--      ============================================== -->


    <!-- initial header stuff -->

    <xsl:element name="refentry">
      <xsl:attribute name="id">
        <xsl:value-of select="name"/>
      </xsl:attribute>

      <xsl:element name="beginpage"/>
      
      <!-- generate index entry -->

      <xsl:element name="indexterm">
	<xsl:element name="primary">
	  <xsl:attribute name="sortas">
	    <xsl:value-of select="name"/>
	  </xsl:attribute>
	  <xsl:text>'</xsl:text>
	    <xsl:value-of select="name"/>
	  <xsl:text>'</xsl:text>
	  <xsl:text> element</xsl:text>
	  <xsl:if test="deprecated">
	    <xsl:text> 
	      (deprecated)
	    </xsl:text>
	  </xsl:if>
	</xsl:element>
	<xsl:element name="secondary">
	  <xsl:text>definition</xsl:text>
	</xsl:element>
      </xsl:element>


      <xsl:element name="refmeta">
        <xsl:element name="refentrytitle">
          <xsl:value-of select="name"/>
        </xsl:element>
        <!-- /refentrytitle -->
      </xsl:element>
      <!-- /refmeta -->

      <!-- REFNAMEDIV -->

      <xsl:call-template name="refnamediv"/>

      <!-- CONTENT MODEL -->

      <xsl:call-template name="content_model"/>

      <!-- ATTRIBUTES -->

      <xsl:call-template name="attributes"/>

      <!-- DESCRIPTION -->

      <xsl:apply-templates select="descrip"/>

      <!-- USED IN -->

      <xsl:call-template name="parents"/>

      <!-- CHILDREN -->

      <xsl:call-template name="children"/>

      <!-- FUTURE -->

      <xsl:apply-templates select="future"/>

      <!-- clean up and spacing -->

    </xsl:element>
    <!-- /refentry -->

  </xsl:template>


  <!--      ============================================== -->
  <!--      |          Generate REFNAMEDIV section         -->
  <!--      ============================================== -->

  <xsl:template name="refnamediv">
    <xsl:element name="refnamediv">
      <xsl:element name="refname">
        <xsl:value-of select="name"/>
      </xsl:element>
      <xsl:element name="refpurpose">
        <xsl:value-of select="./purpose"/>
      </xsl:element>
    </xsl:element>
  </xsl:template>


  <!--      ============================================== -->
  <!--      |            Gen CONTENT MODEL section         -->
  <!--      ============================================== -->

  <xsl:template name="content_model">
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:element name="refsect1">
      <xsl:element name="title">
        <xsl:text>Content model</xsl:text>
      </xsl:element>

      <!-- write synopsis -->

      <xsl:element name="synopsis">
        <xsl:value-of select="name"/>
        <xsl:text> : </xsl:text>

        <!-- write attributes; indicate if [optional] -->

        <xsl:for-each select="./attr">
          <xsl:text> </xsl:text>
          <xsl:if test="@default='implied'">
            <xsl:text>[</xsl:text>
          </xsl:if>
          <xsl:value-of select="@name"/>
          <xsl:if test="@default='implied'">
            <xsl:text>]</xsl:text>
          </xsl:if>
          <xsl:if test="position() != last()">
            <xsl:text>, </xsl:text>
          </xsl:if>
        </xsl:for-each>

        <!-- insert elements with proper grammar -->

        <xsl:variable name="indent5">
          <xsl:text>     </xsl:text>
        </xsl:variable>

	<xsl:value-of select="$newline"/>
	<xsl:value-of select="$indent5"/>

        <xsl:apply-templates select="sequence|choice|elRef">
          <xsl:with-param name="indent" select="$indent5"/>
        </xsl:apply-templates>

        <xsl:if test="empty">
          <xsl:text>EMPTY</xsl:text>
        </xsl:if>

        <xsl:if test="data">
          <xsl:text>(#PCDATA)</xsl:text>
        </xsl:if>

      </xsl:element>
      <!-- /synopsis -->
    </xsl:element>
    <!-- /refsect1 -->

  </xsl:template>


  <!--      ============================================== -->
  <!--      |              Gen ATTRIBUTE section           -->
  <!--      ============================================== -->

  <xsl:template name="attributes">

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:element name="refsect1">
      <xsl:element name="title">
        <xsl:text>Attributes</xsl:text>
      </xsl:element>
      <xsl:element name="variablelist">
        <xsl:choose>
          <xsl:when test="count(./attr) &gt; 0">

            <xsl:for-each select="./attr">
              <xsl:element name="varlistentry">
                <xsl:element name="term">
                  <xsl:element name="varname">
                    <xsl:value-of select="@name"/>
                  </xsl:element>  <!-- /varname -->
                </xsl:element>  <!-- /term -->
		<xsl:element name="listitem"> <!-- listitem (outer) -->

		  <xsl:element name="para">  <!-- para (outer) -->
		    <xsl:if test="@default='implied'">
		      <xsl:text>(optional) </xsl:text>
		    </xsl:if>
		    <xsl:if test="deprecated">
		      <xsl:text>
			(deprecated)
		      </xsl:text>
		    </xsl:if>
		    <xsl:if test="attdef">
		      <xsl:value-of select="attdef"/>
		    </xsl:if>
		    <xsl:if test="@type='enum'">
		      <xsl:text> (enumerated).</xsl:text>
		      <xsl:value-of select="$newline"/>
		      <xsl:element name="itemizedlist">
			<xsl:for-each select="./enumVal">
			  <xsl:element name="listitem">  <!-- listitem (inner) -->
			    <xsl:element name="para">
			      <xsl:element name="varname">
				<xsl:value-of select="."/>
			      </xsl:element>  <!-- /varname -->
			    </xsl:element>  <!-- /para (inner) -->
			  </xsl:element>  <!-- listitem (inner) -->
			</xsl:for-each>  <!-- select="./enumVal" -->
		      </xsl:element>  <!-- /itemizedlist -->
		    </xsl:if>

		    <!-- generate index entry -->
		    <xsl:element name="indexterm">
		      <xsl:element name="primary">
			<xsl:attribute name="sortas">
			  <xsl:value-of select="@name"/>
			</xsl:attribute>
			<xsl:text>'</xsl:text>
			<xsl:value-of select="@name"/>
			<xsl:text>' attribute</xsl:text>
			<xsl:if test="deprecated">
			  <xsl:text>
			    (deprecated)
			  </xsl:text>
			</xsl:if>
		      </xsl:element> <!-- primary -->

		      <xsl:element name="secondary">
			<xsl:text> of '</xsl:text>
			<xsl:value-of select="../name"/>
			<xsl:text>' element</xsl:text>
		      </xsl:element> <!-- secondary element -->
		      
		    </xsl:element> <!-- indexterm element -->

		  </xsl:element> <!-- /para (outer) -->
		</xsl:element> <!-- /listitem (outer) -->
              </xsl:element>  <!-- /varlistentry -->
            </xsl:for-each>
            <!-- select="./attr" -->
          </xsl:when>
          <!-- test="count(./attr) &gt; 0" -->
          <xsl:otherwise>
            <xsl:element name="varlistentry">
              <xsl:element name="term">
                <xsl:text>NONE</xsl:text>
              </xsl:element>
              <xsl:element name="listitem">
                <xsl:element name="para"> </xsl:element>
              </xsl:element>
              <!-- /listitem -->
            </xsl:element>
            <!-- /varlistentry -->
          </xsl:otherwise>
        </xsl:choose>

      </xsl:element>
      <!-- /variablelist -->
    </xsl:element>
    <!-- /refsect1 -->

  </xsl:template>


  <!--      ============================================== -->
  <!--      |             Gen DESCRIPTION section          -->
  <!--      ============================================== -->

  <xsl:template match="descrip">

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:text disable-output-escaping="yes">
      &lt;refsect1&gt;&lt;title&gt;Description&lt;/title&gt;
    </xsl:text>
    <xsl:value-of select="$newline"/>

    <xsl:text disable-output-escaping="yes">&lt;para&gt;</xsl:text>
    <xsl:value-of select="$newline"/>

    <xsl:value-of select="."/>

    <xsl:text disable-output-escaping="yes">&lt;/para&gt;</xsl:text>
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$newline"/>

    <xsl:text disable-output-escaping="yes">&lt;/refsect1&gt;</xsl:text>
    <xsl:value-of select="$newline"/>

  </xsl:template>


  <!--      ============================================== -->
  <!--      |            Generate PARENTS section          -->
  <!--      ============================================== -->

  <xsl:template name="parents">

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:element name="refsect1">
      <xsl:element name="title">
	Possible parents
      </xsl:element>
      <xsl:value-of select="$newline"/>

      <xsl:element name="simplelist">
	<xsl:value-of select="$newline"/>

	<xsl:variable name="myname">
	  <xsl:value-of select="name"/>
	</xsl:variable>

	<xsl:if test="not(//el/descendant::elRef/@elID = $myname)">
	  <xsl:element name="member">
	    NONE - ROOT ELEMENT
	  </xsl:element>
	</xsl:if>

	<xsl:for-each select="//el">
	  <xsl:if test="descendant::elRef/@elID = $myname">
	    <xsl:element name="member">
	      <xsl:element name="link">
		<xsl:attribute name="linkend">
		  <xsl:value-of select="name"/>
		</xsl:attribute>
		<xsl:element name="sgmltag">
		  <xsl:value-of select="name"/>
		</xsl:element> <!-- sgmltag -->
	      </xsl:element> <!-- link -->
	    </xsl:element> <!-- member -->
	    <xsl:value-of select="$newline"/>
	  </xsl:if>
	</xsl:for-each>

      </xsl:element> <!-- simplelist -->

      <xsl:value-of select="$newline"/>

    </xsl:element> <!-- refsect1 -->
    <xsl:value-of select="$newline"/>

  </xsl:template>


  <!--      ============================================== -->
  <!--      |           Generate CHILDREN section          -->
  <!--      ============================================== -->

  <xsl:template name="children">
    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:element name="refsect1">
      <xsl:element name="title">
	Allowable children
      </xsl:element> <!-- title -->

      <xsl:value-of select="$newline"/>

      <xsl:element name="simplelist">
	<xsl:value-of select="$newline"/>

	<xsl:choose>
	  <xsl:when test="count(descendant::elRef) &gt; 0">

	    <xsl:for-each select="descendant::elRef">

	      <xsl:element name="member">

	      <!-- generate index entry -->	      
	      <xsl:element name="indexterm">
		<xsl:element name="primary">
		  <xsl:attribute name="sortas">
		    <xsl:value-of select="@elID"/>
		  </xsl:attribute>
		  <xsl:text>'</xsl:text>
		  <xsl:value-of select="@elID"/>
		  <xsl:text>' element</xsl:text>
		  <xsl:if test="deprecated">
		    <xsl:text>
		      (deprecated)
		    </xsl:text>
		  </xsl:if>
		</xsl:element> <!-- primary -->
		<xsl:element name="secondary">
		  <xsl:text> as child of '</xsl:text>
		  <xsl:value-of select="ancestor::el[position() = last()]/name"/>
		  <xsl:text>' element</xsl:text>
		</xsl:element> <!-- secondary -->
	      </xsl:element> <!-- indexterm -->
	      
	      <xsl:value-of select="$newline"/>

		<xsl:element name="link">
		  <xsl:attribute name="linkend">
		    <xsl:value-of select="@elID"/>
		  </xsl:attribute>
		  <xsl:element name="sgmltag">
		    <xsl:value-of select="@elID"/>
		  </xsl:element> <!-- sgmltag -->
		</xsl:element> <!-- link -->
	      </xsl:element> <!-- member -->
	      <xsl:value-of select="$newline"/>

	    </xsl:for-each>
	  </xsl:when>
	  <xsl:otherwise>
	    <xsl:text disable-output-escaping="yes">&lt;member&gt;NONE&lt;/member&gt;</xsl:text>
	  </xsl:otherwise>
	</xsl:choose>

      </xsl:element> <!-- simplelist -->
      <xsl:value-of select="$newline"/>

    </xsl:element> <!-- refsect1 -->

    <xsl:value-of select="$newline"/>

  </xsl:template>

  <!--      ============================================== -->
  <!--      |           Generate FUTURE section            -->
  <!--      ============================================== -->

  <xsl:template match="future">

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:text disable-output-escaping="yes">
      &lt;refsect1&gt;&lt;title&gt;Future plans for this element&lt;/title&gt;
    </xsl:text>
    <xsl:value-of select="$newline"/>

    <xsl:text disable-output-escaping="yes">&lt;para&gt;</xsl:text>
    <xsl:value-of select="$newline"/>

    <xsl:value-of select="."/>

    <xsl:text disable-output-escaping="yes">&lt;/para&gt;</xsl:text>
    <xsl:value-of select="$newline"/>
    <xsl:value-of select="$newline"/>

    <xsl:text disable-output-escaping="yes">&lt;/refsect1&gt;</xsl:text>
    <xsl:value-of select="$newline"/>

  </xsl:template>

  <!--      ============================================== -->
  <!--      |   Handle SEQUENCES in element definition   | -->
  <!--      ============================================== -->

  <xsl:template match="sequence">
    <xsl:param name="indent"/>

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:variable name="newindent">
      <xsl:value-of select="$indent"/>
      <xsl:text>     </xsl:text>
    </xsl:variable>

    <xsl:for-each select="data|choice|elRef">

      <!-- after first line, must do proper indentation -->
      <xsl:if test="position() > 1">
	<xsl:value-of select="$indent"/>
      </xsl:if>

      <xsl:apply-templates select=".">
        <xsl:with-param name="indent" select="$indent"/>
      </xsl:apply-templates>
      <xsl:value-of select="$newline"/>
    </xsl:for-each>
    <xsl:if test="@num = 'oneplus'">
      <xsl:text>+</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'zeroplus'">
      <xsl:text>*</xsl:text>
    </xsl:if>
    <xsl:if test="@num = 'binary'">
      <xsl:text>?</xsl:text>
    </xsl:if>
  </xsl:template> <!-- end of SEQUENCE template -->


  <!--      ============================================== -->
  <!--      |    Handle CHOICES in element definition    | -->
  <!--      ============================================== -->

  <xsl:template match="choice">
    <xsl:param name="indent"/>

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:variable name="newindent">
      <xsl:value-of select="$indent"/>
      <xsl:text>     </xsl:text>
    </xsl:variable>

    <xsl:text>(</xsl:text>
    <xsl:for-each select="data|sequence|elRef">
      <!-- if we have a sequence embedded in a choice, special formatting -->
      <xsl:choose>
	<xsl:when test="name() = 'sequence'">
	  <xsl:value-of select="$newline"/>
	  <xsl:value-of select="$newindent"/>
	  <xsl:apply-templates select=".">
	    <xsl:with-param name="indent" select="$newindent"/>
	  </xsl:apply-templates>
	  <xsl:if test="position() != last()">
	    <xsl:value-of select="$indent"/>
	    <xsl:text>)</xsl:text>
	    <xsl:value-of select="$newline"/>
	    <xsl:value-of select="$indent"/>
	    <xsl:text>OR</xsl:text>
	    <xsl:value-of select="$newline"/>
	    <xsl:value-of select="$indent"/>
	    <xsl:text>(</xsl:text>
	  </xsl:if>
	</xsl:when>
	<xsl:otherwise>  <!-- otherwise (no sequence), default formatting -->
	  <xsl:apply-templates select=".">
	    <xsl:with-param name="indent" select="$newindent"/>
	  </xsl:apply-templates>
	  <xsl:if test="position() != last()">
	    <xsl:text> | </xsl:text>
	  </xsl:if>
	</xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
    <xsl:if test="sequence">
      <xsl:value-of select="$indent"/>
    </xsl:if>
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
  </xsl:template> <!-- end of CHOICE template -->


  <!--      ======================================== -->
  <!--      | Handle PCDATA in element definition  | -->
  <!--      ======================================== -->

  <xsl:template match="data">
    <xsl:text>#PCDATA</xsl:text>
  </xsl:template>

  <!--      ============================================== -->
  <!--      | Handle ELEMENT REFS in element definition  | -->
  <!--      ============================================== -->

  <xsl:template match="elRef">
    <xsl:param name="indent"/>

    <xsl:variable name="newline">
      <xsl:text>
</xsl:text>
    </xsl:variable>

    <!-- spit out our name embedded in document link -->

    <xsl:text disable-output-escaping="yes">&lt;link linkend='</xsl:text>
    <xsl:value-of select="@elID"/>
    <xsl:text disable-output-escaping="yes">'&gt;</xsl:text>
    <xsl:value-of select="@elID"/>
    <xsl:text disable-output-escaping="yes">&lt;/link&gt;</xsl:text>

    <!-- append proper indicator for how many of me -->

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

</xsl:stylesheet>
