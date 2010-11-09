#!/bin/sh
#
# 080228: Created to try to create Word ML document

SAXON=/Users/bjax/downloaded_sw/saxon6/saxon.jar
DB=/Users/bjax/Documents/DocBook/docbook-xsl
DB_SS=DAVE-ML_db2wordml.xsl
DB_XSL_EXTENSIONS=${DB}/extensions/saxon651.jar

java -cp ${DB_XSL_EXTENSIONS} -jar ${SAXON} -o DAVE-ML_ref.docx DAVE-ML_ref.xml ${DB_SS}



