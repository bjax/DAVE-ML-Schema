README for DAVE/References
030709 Bruce Jackson

This directory contains makings of two projects: a DocBook
reference to the DAVEfunc.dtd, and an XML-based representation of the
DTD.

The thought dawned on me, while working on the reference, that it sure
would be nice if the DTD itself were well-formed XML; this would allow
automatic generation of documentation like I was working on.

I had already downloaded "livedtd.pl" from sagehill.com, which
converts a DTD into HTML, but wasn't really satisfied with this
solution since (1) it only generated HTML, and I need printed docs,
and (2) the anchor names in the generated HTML do not reflect the
element names, so it's tough to hook this output in some automated way
to a larger reference doc.

So, in one day (mostly) I drafted an internal DTD and converted the
"DAVEfunc.dtd" (version 1.5b) into "DAVEfunc_dtd.xml", and wrote (in
the same day) an XSL transformation to recreate the .dtd version
("xml2dtd.xsl"). An associated shell script, makeDTD, runs the XALAN
xslt processor against "DAVEfunc_dtd.xml" and creates
"DAVEfunc_auto.dtd".  These are found in subdirectory "Auto_DTD".

The other files in this directory are my start on the DocBook
reference, "DAVE-ML_ref.xml". By running XALAN against this source and
using the "xhtml/docbook.xsl" stylesheet, out pops
"DAVE-ML_ref.html". The shell script "toXhtml.sh" performs this task.

The file "elements.txt" contains a list of the 35 elements described
in DAVEfunc.dtd version 1.5b.

TODO: Next step is to write an xsl stylesheet to create DocBook ref pages
from a dtd.xml file. [done 030713]

TODO: I should also investigate using XSD (XML Schema Document) and/or RELAX
NG schema, instead of a home-grown one.

