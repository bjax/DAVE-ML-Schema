README.txt file for DAVE/Schemas/trunk/Auto_DTD directory

060908 Bruce Jackson, NASA Langley <e.b.jackson@nasa.gov>

This directory contains the means to build the DTD (Document Type
Definition) that defines the Dynamic Aerospace Vehicle Exchange Markup
Language (DAVE-ML).

More information about DAVE-ML is available at http://daveml.nasa.gov

The DAVE_func_dtd.xml file is the source for both the DTD document and
an appendix to the DAVE-ML reference manual (which is constructed in
the parent directory to this one).

By using the included Makefile, both the revised DTD
(DAVEfunc_auto.dtd, which should be renamed DAVEfunc.dtd when
published) and the updated docbook markup appendix
(DAVEfunc_ref_auto.xml) are generated through XSLT scripts, also
contained herein (xml2dtd.xsl and xml2db.xsl, respectively).

This Makefile is called by the Makefile in the parent directory to
compile and build a new release of the DAVE-ML reference document in
both pdf and html formats. It also can generate a text version of the
reference manual that is not very attractive. It also generates a new
candidate DTD in this directory.

The schema for the DAVE-ML source file is auto_dtd_schema.rnc, a
RelaxNG compact notation schema.

