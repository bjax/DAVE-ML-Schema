#!/usr/bin/make
#
# Makefile for PDF; creates better index
# 2010-04-23: Created EBJ based on suggestions in Stayton's 
#             DocBook XLS: The Complete Guide, Fourth Edition
#
#
#  This is necessary because fop's generated index has duplicate page numbers
#  with no ranges (e.g. 12-15).
#
#  Generates 1) a .fo file with marked up index
#            2) an initial .pdf file with XML for index w/page numbers
#            3) extracts the XML index from the initial .pdf file
#            4) cleans up temporary index .xml file
#            5) [not implemented] inserts temporary index into original ref.xml file
#            5) [not implemented] generates a final .pdf with good index

AUTO_DTD_DIR  = Auto_DTD

FOP           = /Users/bjax/downloaded_sw/fop
DOCBOOK       = /Users/bjax/Documents/DocBook/docbook-xsl
MY_XSL        = ./DAVE-ML_db2pdf.xsl
TARGET        = DAVE-ML_ref
AUTO_DTD_REF  = ${AUTO_DTD_DIR}/DAVEfunc_ref_auto.xml

SOURCE_DOCS   = ${TARGET}.xml ${TARGET}.css ${AUTO_DTD_REF} \
                $(addprefix figures/,$(FIGURES))

make: ${TARGET}_temp_index.xml

#${TARGET}_temp_index.xml : ${TARGET}_temp.pdf
#	${DOCBOOK}/fo/pdf2index ${TARGET}_temp.pdf >$@

${TARGET}_temp_index.xml : ${TARGET}_temp.txt
	delete_to_index.sed < $<        | \
	tr "\255" "-"                   | \
	sed -e '1,/====cut here====/ d' | \
	tr "\014" "Q"                   | \
	sed -e '/^Q$$/{;N;N;d;}'        | \
        sed -e '/<\/$$/N' -e 's/<\/\n/<\//' | \
        sed -e '/<\/$$/N' -e 's/<\/\n/<\//' | \
        sed -e '/<\/$$/N' -e 's/<\/\n/<\//' | \
        sed -e '/<\/$$/N' -e 's/<\/\n/<\//' | \
        sed -e '/<\/$$/N' -e 's/<\/\n/<\//' > $@

${TARGET}_temp.txt : ${TARGET}_temp.pdf
	pstotext $< >$@

${TARGET}_temp.pdf : ${TARGET}.fo
	@echo "Making DocBook into .pdf file..."
	${FOP}/fop -fo $< -pdf $@

${TARGET}.fo : ${SOURCE_DOCS} ${MY_XSL} my_fo_titlepage.xsl
	@echo "Making DocBook into .fo file..."
	xsltproc -o ${TARGET}.fo --stringparam make.index.markup 1 ${DOCBOOK}/fo/docbook.xsl ${TARGET}.xml

.PHONY : clean

clean:
	-rm ${TARGET}_temp_index.xml
	-rm ${TARGET}_temp.pdf
	-rm ${TARGET}.fo
	-rm ${TARGET}_temp.txt
