#!/bin/sh
#
# 080228: Created to use lynx -dump to get just text from single index HTML page
# this makes a single HTML file without using chunking

SAXON=/Users/bjax/downloaded_sw/saxon6/saxon.jar
DB=/Users/bjax/Documents/DocBook/docbook-xsl
DB_SS=DAVE-ML_db2html1.xsl
DB_XSL_EXTENSIONS=${DB}/extensions/saxon651.jar

java -cp ${DB_XSL_EXTENSIONS} -jar ${SAXON} -o DAVE-ML_ref.html DAVE-ML_ref.xml ${DB_SS}

cat DAVE-ML_ref.html | lynx -stdin -dump > DAVE-ML_ref.txt
