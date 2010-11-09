HOME   = /Users/bjax
DEV    = ${HOME}/Documents/Dev
MYJAVA = ${DEV}/java
XERCES = ${MYJAVA}/xerces/xerces-2_0_1/build/xerces.jar
JDOM   = ${MYJAVA}/jdom-b8/build/jdom.jar
CLASSPATH = ${JDOM}:${MYJAVA}:${XERCES}
OBJECTS = \
	Block \
	BlockArrayList \
	BlockBP \
	BlockFuncTable \
	BlockInput \
	BlockMath \
	BlockMathAbs \
	BlockMathConstant \
	BlockMathFunction \
	BlockMathGain \
	BlockMathProduct \
	BlockMathRelation \
	BlockMathSum \
	BlockMathSwitch \
	BlockOutput \
	DAVE \
	DAVEtoSL \
	MDLNameSpace \
	MatFileWriter \
	Model \
	Namespace \
	ParseText \
	SLCableTray \
	SLCell \
	SLDiagram \
	SLFileWriter \
	SLLineSegment \
	SLPathList \
	SLSignal \
	SLRowColumnVector \
	Signal \
	SignalArrayList \
	TreeFileWriter

CLASSES = $(patsubst %, %.class, $(OBJECTS))
SOURCES = $(patsubst %.class,%.java,$(CLASSES))

make : all

all : $(CLASSES)

$(CLASSES): %.class: %.java
	javac -g -classpath $(CLASSPATH) $(SOURCES)

docs : $(SOURCES)
	javadoc -classpath $(CLASSPATH) $?

.PHONY : clean docs all
clean :
	-rm $(CLASSES)
	-rm -rf gov