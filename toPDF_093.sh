#!/bin/sh
#
# 070615: Created to use verison 0.93 FOP
#
FOP=/Users/bjax/downloaded_sw/fop-0.93
MY_XSL=./DAVE-ML_db2pdf.xsl
${FOP}/fop -xml DAVE-ML_ref.xml -xsl ${MY_XSL} -pdf DAVE-ML_ref.pdf
