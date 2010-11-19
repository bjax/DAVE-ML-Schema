#!/bin/sh
#
# 030905: changed from using XALAN to SAXON to get access to DocBook
#         XSLT extensions like embedded notes. Also added customization
#         "driver" stylesheet.
# 031009: Added backup of existing document; copy embedded images from 
#         stylesheet distribution directory
# 031023: Added cp -R of figures directory to output directory; made OUT
#         directory variable.
# 061119: Removed version number from docbook-xsl; relying on symbolic link
# 061121: Removed version number from saxon6; relying on symbolic link. 
#         Saxonb8 doesn't support chunking.

SAXON=/Users/bjax/downloaded_sw/saxon6/saxon.jar
DB=/Users/bjax/Documents/DocBook/docbook-xsl
DB_SS=DAVE-ML_db2html.xsl
DB_XSL_EXTENSIONS=${DB}/extensions/saxon651.jar
OUT=html

if [ -d bu ]; then
    rm -rf bu
fi
mv ${OUT} bu
mkdir ${OUT}
cp -R ${DB}/images ${OUT}
cp -R figures ${OUT}
cp DAVE-ML_ref.css ${OUT}

java -cp ${DB_XSL_EXTENSIONS} -jar ${SAXON} DAVE-ML_ref.xml ${DB_SS}

cd ${OUT}; ln -s DAVE-ML_ref.html index.html

