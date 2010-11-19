#!/bin/sh
#
# 031014: Added this header; added DB alias for easy swapping
#
DB=/Users/bjax/Documents/DocBook/docbook-xsl
FOP=/Users/bjax/downloaded_sw/fop-0.92beta
MY_XSL=./DAVE-ML_db2pdf.xsl
${FOP}/fop -xml DAVE-ML_ref.xml -xsl ${MY_XSL} -pdf DAVE-ML_ref.pdf
