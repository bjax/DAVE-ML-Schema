#!/usr/bin/make
#
# Makefile for Reference directory
#
# Creates new DocBook reference file from .xml source file
#
# 030711 Bruce Jackson <mailto:e.b.jackson@nasa.gov>
#

export WEBSITE = ../Website/DTDs/new

AUTO_DTD_DIR  = Auto_DTD
HTML_DIR      = html
PDF_DIR       = .
WORDML_DIR    = .
FIGURES       = gb_3Dungridded.png prg_2Dungridded.png DAVE-ML_excerpt.jpg \
	        interp.png interp_discrete.png interp.jpg interp_discrete.jpg \
                unraveled_table.png

SOURCE_DOCS   = DAVE-ML_ref.xml DAVE-ML_ref.css ${AUTO_DTD_REF} \
                $(addprefix figures/,$(FIGURES))

AUTO_DTD      = ${AUTO_DTD_DIR}/DAVEfunc_auto.dtd
AUTO_DTD_REF  = ${AUTO_DTD_DIR}/DAVEfunc_ref_auto.xml
AUTO_DTD_DOCS = ${AUTO_DTD_DIR}/DAVEfunc_dtd.xml ${AUTO_DTD_DIR}/xml2db.xsl 
HTML_DOC      = ${HTML_DIR}/index.html
PDF_DOC       = ${PDF_DIR}/DAVE-ML_ref.pdf 
WORDML_DOC    = ${WORDML_DIR}/DAVE-ML_ref.doc

make: ${HTML_DOC} ${AUTO_DTD}

.PHONY: pdf text html all doc
pdf: ${PDF_DOC}

text: DAVE-ML_ref.txt

html: ${HTML_DOC}

doc:  ${WORDML_DOC}

#all: make pdf text doc

all: make pdf

${HTML_DOC}: ${SOURCE_DOCS} toXhtml.sh DAVE-ML_db2html.xsl
	@echo "Making DocBook into .html files..."
	@./toXhtml.sh

${PDF_DOC}: ${SOURCE_DOCS} toPDF.sh DAVE-ML_db2pdf.xsl  my_fo_titlepage.xsl
	@echo "Making DocBook into .pdf file..."
	@./toPDF.sh
	@open ${PDF_DOC}

my_fo_titlepage.xsl: my_fo_titlepage.xml
	xsltproc --output $@ /Users/bjax/Documents/DocBook/docbook-xsl/template/titlepage.xsl my_fo_titlepage.xml

DAVE-ML_ref.txt: ${SOURCE_DOCS} toText.sh DAVE-ML_db2html1.xsl
	@echo "Making DocBook into .txt file..."
	@./toText.sh

${WORDML_DOC}: ${SOURCE_DOCS} toWordML.sh DAVE-ML_db2wordml.xsl
	@echo "Making DocBook into .doc file..."
	@./toWordML.sh

${AUTO_DTD_REF} ${AUTO_DTD_DIR}/DAVEfunc_auto.dtd : ${AUTO_DTD_DOCS}
	@cd ${AUTO_DTD_DIR} && $(MAKE)

install: all
	-@mkdir ${WEBSITE}
	@cd ${AUTO_DTD_DIR} && $(MAKE) install
	@echo "Copying reference docs to Web directory..."
	-@mkdir ${WEBSITE}/Ref
	@cp -R html/* ${WEBSITE}/Ref
	@cp ${PDF_DOC} ${WEBSITE}/DAVE-ML_ref.pdf
	@cp DAVEfunc.dtd ${WEBSITE}

clean:
	@cd ${AUTO_DTD_DIR} && $(MAKE) clean
	-rm -rf text
	-rm -rf html
	-rm ${PDF_DOC}
