#!/bin/sh
#
# 031014: Added this header; added DB alias for easy swapping
#
export FOP=/Users/bjax/downloaded_sw/fop
export MY_XSL=./DAVE-ML_db2pdf_AIAA.xsl
#export JAVACMD="/usr/bin/java -Xmx1024M"  # now having to do this in fop/fop script
${FOP}/fop -xml DAVE-ML_ref.xml -xsl ${MY_XSL} -pdf DAVE-ML_ref_aiaa.pdf
