#!/usr/bin/make
#
# Makefile for Auto_DTD directory
#
# Creates both new .DTD and new DocBook reference section from .xml source file
#
# 030711 Bruce Jackson <mailto:e.b.jackson@nasa.gov>
# 2011-01-06 Removed install target (and WEBSITE def)
#


SCHEMA_DIR =..
TRANG_DIR  = ~/downloaded_sw/trang
JING_DIR   = ~/downloaded_sw/jing
VAL_LAB    = ../validation_lab
XALAN      = ~/downloaded_sw/xalan
NXML-MODE  = ~/downloaded_sw/nxml-mode/schema
CLASSPATH  = $(TRANG_DIR):$(JING_DIR)/bin


make: DAVEfunc_auto.dtd DAVEfunc_ref_auto.xml $(SCHEMA_DIR)/DAVEfunc.dtd DAVEfunc.rnc

DAVEfunc_auto.dtd: DAVEfunc_dtd.xml xml2dtd.xsl
	@echo "Making .dtd file..."
	@java -jar ${XALAN}/xalan.jar -in DAVEfunc_dtd.xml -xsl xml2dtd.xsl -out DAVEfunc_auto.dtd

# some silliness here: trang wants to use same default namespace in both DAVEfunc.rnc and mathml2.rnc
# which Emacs (for one) doesn't understand
DAVEfunc.rnc: DAVEfunc_auto.dtd
	@echo  "Making .rnc schema version..."
	@java -jar $(TRANG_DIR)/trang.jar DAVEfunc_auto.dtd DAVEfunc.rnc
	@sed "s|daveml.org/2010/DAVEML|www.w3.org/1998/Math/MathML|" mathml2.rnc > mathml2a.rnc
	@mv mathml2a.rnc mathml2.rnc

$(SCHEMA_DIR)/DAVEfunc.dtd: DAVEfunc_auto.dtd
	@echo  "Copying $@ to schema directory..."
	@cp $< $@

DAVEfunc_ref_auto.xml: DAVEfunc_dtd.xml xml2db.xsl
	@echo "Making DocBook appendix..."
	@java -jar ${XALAN}/xalan.jar -in DAVEfunc_dtd.xml -xsl xml2db.xsl -out DAVEfunc_ref_auto.xml

clean:
	-rm DAVEfunc_auto.dtd
	-rm DAVEfunc_ref_auto.xml
	-rm DAVEfunc.rnc mathml2.rnc mathml2-qname-1.mod.rnc
