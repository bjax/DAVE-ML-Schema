#!/usr/bin/make
#
# Makefile for Auto_DTD directory
#
# Creates both new .DTD and new DocBook reference section from .xml source file
#
# 030711 Bruce Jackson <mailto:e.b.jackson@nasa.gov>
#

ifndef WEBSITE
  WEBSITE = /Users/bjax/Documents/WebSites/DAVE_Web_Site/DTDs/incoming
endif

SCHEMA_DIR =..
TRANG_DIR  = ~/downloaded_sw/trang
JING_DIR   = ~/downloaded_sw/jing
VAL_LAB    = ../validation_lab
XALAN      = ~/downloaded_sw/xalan
NXML-MODE  = ~/downloaded_sw/nxml-mode/schema
CLASSPATH  = $(TRANG_DIR):$(JING_DIR)/bin


make: DAVEfunc_auto.dtd DAVEfunc_ref_auto.xml $(SCHEMA_DIR)/DAVEfunc.dtd \
      $(VAL_LAB)/DAVEfunc.dtd $(VAL_LAB)/DAVEfunc.rnc 

DAVEfunc_auto.dtd: DAVEfunc_dtd.xml xml2dtd.xsl
	@echo "Making .dtd file..."
	@java -jar ${XALAN}/xalan.jar -in DAVEfunc_dtd.xml -xsl xml2dtd.xsl -out DAVEfunc_auto.dtd

DAVEfunc.rnc: DAVEfunc_auto.dtd
	@echo  "Making .rnc version..."
	@java -cp $(CLASSPATH) -jar $(TRANG_DIR)/trang.jar DAVEfunc_auto.dtd DAVEfunc.rnc

$(VAL_LAB)/DAVEfunc.rnc: DAVEfunc.rnc
	@echo  "Copying $@ to validation directory..."
	@mkdir -p $(VAL_LAB)
	@cp DAVEfunc.rnc mathml2.rnc mathml2-qname-1.mod.rnc $(VAL_LAB)

$(VAL_LAB)/DAVEfunc.dtd: DAVEfunc_auto.dtd
	@echo  "Copying $@ to validation directory..."
	@mkdir -p $(VAL_LAB)
	@cp $< $@

$(SCHEMA_DIR)/DAVEfunc.dtd: DAVEfunc_auto.dtd
	@echo  "Copying $@ to schema directory..."
	@cp $< $@

DAVEfunc_ref_auto.xml: DAVEfunc_dtd.xml xml2db.xsl
	@echo "Making DocBook appendix..."
	@java -jar ${XALAN}/xalan.jar -in DAVEfunc_dtd.xml -xsl xml2db.xsl -out DAVEfunc_ref_auto.xml

install: make
	@echo "Copying new DTD to Web directory..."
	@mkdir -p ${WEBSITE}
	@cp DAVEfunc_auto.dtd ${WEBSITE}/DAVEfunc.dtd
	@echo "Copying new Relax NG to nxml-mode directory..."
	@cp DAVEfunc.rnc ${NXML-MODE}/DAVEfunc.rnc

clean:
	-rm DAVEfunc_auto.dtd
	-rm DAVEfunc_ref_auto.xml
	-rm DAVEfunc.rnc mathml2.rnc mathml2-qname-1.mod.rnc
