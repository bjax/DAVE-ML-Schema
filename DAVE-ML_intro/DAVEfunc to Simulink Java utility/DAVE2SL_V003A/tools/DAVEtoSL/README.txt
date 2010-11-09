021028 Successfully revived this older version (V0_3) by following steps:

  1. Had to checkout latest version of SLCell.java and tag it with
  V0_3, then check out as "cvs co -r V0_3". This creates a new
  directory, DAVE, with subdirectory tools/DAVE2SL containing source
  code. 

  2. Running "make" works to create .class files (I'm running Mac OS X
     10.2).
  
  3. I updated the alias at
     ~/Documents/Dev/java/gov/nasa/larc/bjax/DAVE to point to the new
     DAVE directory; alternatively, I could have changed my classpath
     in the d2s alias defined in ~/.cshrc.

  4. Had to create aliases in the directory containing the .xml file:

     DAVEfunc.dtd -> /Users/bjax/Documents/Dev/DAVE/DAVEfunc.dtd
     mathml2 -> /Users/bjax/Documents/Dev/DTDs/mathml2

  5. The command 'd2s' is defined in my .cshrc as follows:

     alias d2s 'java -cp ${JDOM}:${MYJAVA}:${XERCES} \
	   gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL.DAVEtoSL \!*'

     where

	setenv DEV    ${HOME}/Documents/Dev
	setenv MYJAVA ${DEV}/java
	setenv XERCES ${MYJAVA}/xerces/xerces-2_0_1/build/classes
	setenv JDOM   ${MYJAVA}/jdom-b8/build/jdom.jar
	setenv DAVE   ${DEV}/DAVE


020421 Writes valid header/footer

This command works:
   
   java -cp .:jdom-b8/build/jdom.jar:xerces.jar DAVEtoSL ../../twoD_table.xml b

020423 Created alias 'd2s' in .cshrc file, but can't run make from emacs now...
