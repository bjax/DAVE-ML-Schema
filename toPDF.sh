#!/bin/sh
#
# 031014: Added this header; added DB alias for easy swapping
# 2010-04-21: Added commands to generate just FO for debugging; commented out
#
FOP=/Users/bjax/downloaded_sw/fop
#DB=/Users/bjax/Documents/DocBook/docbook-xsl
MY_XSL=./DAVE-ML_db2pdf.xsl
TARGET=DAVE-ML_ref
#xsltproc -o ${TARGET}.fo --stringparam make.index.markup 1 ${DB}/fo/docbook.xsl ${TARGET}.xml
#export JAVACMD="/usr/bin/java -Xmx1024M"  # now having to do this in fop/fop script
${FOP}/fop -xml ${TARGET}.xml -xsl ${MY_XSL} -pdf DAVE-ML_ref.pdf
