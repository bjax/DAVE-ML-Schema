// BlockFuncTable
//
//	Object representing a Function Table block
//
// 020424 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import org.jdom.Element;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.IOException;

/**
 *
 * <p>  The Function Table Block represents a nonlinear, multidimensional function. </p>
 *
 **/

public class BlockFuncTable extends Block
{
    ArrayList bpIDs;		// breakpoint IDs associated with each input/dimension
    ArrayList functionValues;	// stored as Doubles
    int[] myDimensions;		// dimensions of table
    String description;		// description of function
    String functionDefName;	// name of function definition
    String tableName;		// name of table

    /**
     *
     * <p> Constructor for BlockFuncTable </p>
     *
     * @param function  Top-level Element function definition
     * @param m <code>Model</code> to which we attach
     * @param bkpts	List of breakpoint Elements
     * @throws <code>IOException</code>
     *
     **/

    public BlockFuncTable( Element function, Model m, List bkpts) throws IOException
    { 
	// Save our name, generate default scalar input and output ports.
	super(function.getAttributeValue("name"), 1, 5, m);

	// Make ourselves a tad bigger than default
	this.mdlWidth = 50;
	this.mdlHeight = 50;

	// Parse description, if any
	this.description = function.getChild("description").getTextTrim();

	// Get name of function definition, if any
	this.functionDefName = function.getChild("functionDefn").getAttributeValue("name");
	this.setName(this.functionDefName);	// substitute function name

	// Get name of table, if any
	this.tableName = function.getChild("functionDefn").getChild("griddedTable").getAttributeValue("name");

	// Set up bpID array
	this.bpIDs = new ArrayList(5);

	// Parse down to and load table values
	this.functionValues = parseFuncVals(function); 

	// Parse and record breakpoint and variable IDs; set # of dimensions
	parseIDsFromFunctionElement(function);

	// Hook up to output signal, if it exists; otherwise, create it
	String depVarName = hookUpOutput(function, m);

	// Hook up to breakpoint output signal or create them if necessary; also set each dimension
	hookUpInputs(m);

//System.out.println("    BlockFuncTable constructor finished:");
//System.out.println("      function " + myName + " has " + myDimensions.length + " dimensions,");
//System.out.println("      which outputs signal " + depVarName + ", has been created.");
//System.out.println();
    }


    /**
     *
     * <p> Finds and returns array of function table values </p>
     *
     * @param function JDOM "function" element
     *
     **/

    protected ArrayList parseFuncVals( Element function ) throws IOException
    {
	Element functionDef = function.getChild("functionDefn");
	Element gTable      = functionDef.getChild("griddedTable");
	Element table       = gTable.getChild("dataTable");
	return ParseText.toList(table.getTextTrim());
    }


    /**
     *
     * <p> Finds and saves a list of breakpoint references found in an
     *     XML function declaration. </p>
     *
     * @param function JDOM "function" element
     *
     **/

    protected void parseIDsFromFunctionElement( Element function )
    {
	List     iVarRefs        = function.getChildren("independentVarRef");
	Iterator iVarRefIterator = iVarRefs.iterator();

	Element  functionDef     = function.getChild("functionDefn");
	Element  gTable          = functionDef.getChild("griddedTable");
	List     bpRefs          = gTable.getChild("breakpointRefs").getChildren("bpRef");
	Iterator bpRefIterator   = bpRefs.iterator();

//System.out.println("    For existing table " + myName + "...");
//System.out.println("      Creating dimension vector of size " + bpRefs.size());

	this.myDimensions        = new int[ bpRefs.size() ];

	int i = 0;
	while (bpRefIterator.hasNext())
	    {
		// get name of breakpoint identifier
		Element bpRefElement = (Element) bpRefIterator.next();
		this.addBPID(i, bpRefElement.getAttributeValue("bpID"));

		// and associated independent variable identifier
		Element iVarRefElement = (Element) iVarRefIterator.next();
		this.addVarID(i+1, iVarRefElement.getAttributeValue("varID"));

		i++;
	    }
    }

    /**
     * 
     * <p> Add breakpoint ID to list </p>
     *
     * @param portNum port to associate with breakpoint ID (0-based)
     * @param bpID <code>String</code> containing breakpoint ID
     *
     **/

    public void addBPID( int portNum, String bpID )
    {
	// increase length of array if necessary
	while(bpIDs.size() <= portNum)
	    bpIDs.add("");
	bpIDs.set(portNum, bpID);
    }


    /**
     *
     * <p> Returns breakpoint ID (<code>bpID</code>) associated with
     *     particular port number. </p>
     *
     * @param portNum integer offset (1-based) port number
     *
     **/

    public String getBPID( int portNum )
    {
	int portIndex = portNum-1;
	return (String) bpIDs.get( portIndex );
    }

    /**
     *
     * <p> Set or reconfirm dimensions of upstream BP blocks </p>
     *
     **/
    public void setDimensionsFromBPs()
    {
	Iterator it = this.getInputIterator();
	int portNumber = 0;
	while(it.hasNext())
	    {
		Signal s = (Signal) it.next();
		Block b = s.getSource();
		if(b==null) System.err.println("No source block found error.");
		else
		    {
			if(!(b instanceof BlockBP)) System.err.println("Source block not BP.");
			else
			    {
				myDimensions[portNumber] = ((BlockBP) b).length();
				portNumber++;
			    }
		    }
	    }
	//System.out.print("    Dimensions for table " + myName + " set: [");
	//for(int i=0; i< myDimensions.length; i++)
	//    if (i < (myDimensions.length-1))
	//	System.out.print(myDimensions[i] + " x ");
	//    else
	//	System.out.println(myDimensions[i] + "]");
	//System.out.println("      table values = "); 
	//try {
	//printTable( functionValues, myDimensions );
	//} catch (Exception e) {;}

    }


    /**
     *
     * <p> Using specified output (dependent) variable name, look for
     * such a declared variable. If not found, create appropriate
     * one. </p>
     *
     * @param function JDOM function Element
     * @param m our parent <code>Model</code>
     *
     **/

    protected String hookUpOutput( Element function, Model m )
    {

	// Parse and discover dependent variable ID

	Element depVar      = function.getChild("dependentVarRef");
	String depVarName   = depVar.getAttributeValue("varID");

	Iterator sigIterator = m.getSignals().iterator();
	boolean depVarSignalFound = false;
	Signal dVsig = null;

	// Look for existing variable definition (signal)

	while (sigIterator.hasNext())
	    {	// look for matching explicit signal
	        dVsig = (Signal) sigIterator.next();
		if (depVarName.equals(dVsig.getVarID()))
		    {
			depVarSignalFound = true;
			break;
		    }
	    }

	// if not found, make our own
	if (!depVarSignalFound)
	    {
		// create and connect to new signal
		dVsig = new Signal( depVarName, depVarName, "unkn", 10, m );
	    }

	this.addOutput(dVsig, 1);	// connect to new or existing signal

	return depVarName;
    }

    /**
     *
     * <p> Hook up to specified breakpoint blocks. Note that a
     *  breakpoint vector can be used by more than function block, for
     *  example, left and right aileron deflections may use same
     *  breakpoint values but be normalized by different values when
     *  running. Therefore, we create a unique block name for the
     *  breakpoint block that combines the breakpoint set name with
     *  the independent value name. this assures we are free to reuse
     *  an offset-and-index (normalized breakpoint) when they have the
     *  same combined name. </p>
     *
     * @param m Our parent <code>Model</code>
     *
     **/

    protected void hookUpInputs(Model m)
    {
	int portCount = 0;

	// Parse and discover independent variable IDs

	Iterator iVarIDIterator = this.getVarIDIterator();
	Iterator bpIDIterator   = this.bpIDs.iterator();
	String signalVarID = null;

	while (bpIDIterator.hasNext())
	    {
		// get name of breakpoint identifier
		String bpID = (String) bpIDIterator.next();

		if( !iVarIDIterator.hasNext() )
		    {
			System.err.println("***unexpected end of VarID Array***");
			signalVarID = "*EMPTY*";
		    }
		else
		// get corresponding independent variable ID
		    {
			signalVarID = (String) iVarIDIterator.next();
		    }

		// combine independent variable ID with breakpoint ID
		String bpSignalID = signalVarID + "_x_" + bpID;

		// look for existing signal from previously built breakpoint block
		Signal theSignal = m.getSignals().findByID( bpSignalID );
		if (theSignal != null)
		    {
			theSignal.addSink( this, portCount+1 );	// does double link
			portCount++;	// count # of dimensions
		    }
		else
		    {
			// Signal not found, create it...

			// Look at predeclared signals to match breakpoint input varID
			// to get name & units
			
			Signal theBPInputSignal = m.getSignals().findByID( signalVarID );

			if( theBPInputSignal != null )
			    {
				// create and connect to new intermediate signal
				String connectorName	= theBPInputSignal.getName() + "_by_" + bpID;
				String units		= theBPInputSignal.getUnits();
				Signal iVsig = new Signal( connectorName, bpSignalID, units, 10, m );
				iVsig.addSink( this, portCount+1 );	// hook up to new signal
				portCount++;				// increment port count
			    }
			else
			    {
				// else block - error
				System.err.println("Error - thought I had found existing "
						   + bpSignalID + " but then it got away...");
			    }
		    }
	    }
    }

    /** 
     *
     * <p> Recursively prints table values </p>
     *
     * @throws <code>IOException</code>
     **/

    public static int printTable( Writer writer, ArrayList table, int[] dims, int startIndex)
	throws IOException
    {

	int offset;
	int i;

// System.out.println("printTable called recursively, index = " + startIndex + 
//		   "; dims.length = " + dims.length + "; dims[0] = " + dims[0]);

	switch (dims.length)
	    {
	    case 0:	// shouldn't happen
		return 0;
	    case 1:
		for ( i = 0; i < dims[0]; i++)
		    {
			Double theValue = (Double) table.get(i+startIndex);
			writer.write( theValue.toString() );
			if( i < dims[0]-1) writer.write(", ");
		    }
		return i;
	    case 2:
		for ( i = 0; i < dims[0]; i++)
		    {
			int[] newDims = new int[1];
			newDims[0] = dims[1];
			offset = printTable( writer, table, newDims, startIndex );
			if( i < dims[0]-1) writer.write(", ");
			writer.write("\n");
			startIndex = startIndex + offset;
		    }
		return startIndex;
	    default:
		for ( i = 0; i < dims[0]; i++ )
		    {
			int[] newDims = new int[1];
			newDims[0] = dims[1];
//System.out.println(" For dimension " + dims.length + " layer " + i);
//System.out.println();
			offset = printTable( writer, table, newDims, startIndex );
			if( i < dims[0]-1) writer.write(", ");
			writer.write("\n");
			startIndex = startIndex + offset;
		    }
		return startIndex;
	    }
    }

    /** 
     *
     * <p> This method calls recursive table value printer method </p>
     *
     * @throws <code>IOException</code>
     *
     **/

    public static void printTable( ArrayList table, int[] dims)
	throws IOException
    {
	// point to System.out
	OutputStreamWriter osw = new OutputStreamWriter(System.out);
	printTable(osw, table, dims, 0);
    }

    /** 
     *
     * <p> This method directs output to designated Writer </p>
     *
     * @param writer <code>Writer</code> to receive values
     * @param table <code>ArrayList</code> of values to print
     * @param dims array of integers representing dimensions of table
     *
     * @throws <code>IOException</code>
     *
     **/

    public static void printTable( Writer writer, ArrayList table, int[] dims)
	throws IOException
    {
	printTable(writer, table, dims, 0);
    }


    /**
     *
     * <p> Generates written description of current instance on output stream </p>
     *
     * @param writer FileWriter instance to write to
     * @throws <code>IOException</code>
     *
     **/

    public void describeSelf(FileWriter writer) throws IOException
    {
	super.describeSelf(writer);
	writer.write("and is a function table block with " + functionValues.size() + " table points.");
    }



    /**
     *
     * <p> Writes function table block for Simulink representation
     *
     * @param writer Instance of the SLFileWriter class
     * @throws IOException
     *
     */

    public void createMDL(SLFileWriter writer, int x, int y)
	throws IOException
    {
//System.out.println("Created MDL for block " + this.myName);
	writer.writeln("    Block {");
	writer.writeln("      BlockType		      Reference");
	writer.writeln("      Name		      \"" + myName + "\"");
	writer.writeln("      Ports		      [" +  myDimensions.length + ", 1]");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      FontSize		      10");
	writer.writeln("      SourceBlock	      \"simulink3/Functions\n& Tables/Interpolation (n-D)\nusing PreLook-Up\"");
	writer.writeln("      SourceType	      \"LookupNDInterpIdx\"");
	writer.writeln("      numDimsPopupSelect      \"" + myDimensions.length + "\"");
	writer.writeln("      explicitNumDims	      \"" + myDimensions.length + "\"");
	writer.write(  "      table		      \"");
	if (this.tableName == null)
	    {
		writer.write("[");
		try {
		    printTable(writer, this.functionValues, this.myDimensions, 0);
		} catch (Exception e) { ; }
		writer.write("]");
	    }
	else
	    writer.write( this.tableName );
	writer.writeln("\"");
	
	writer.writeln("      interpMethod	      \"Linear\"");
	writer.writeln("      extrapMethod	      \"Linear\"");
	writer.writeln("      rangeErrorMode	      \"None\"");
	this.createMDLPortInfo( writer, 1 );	// write out port structure with name
	writer.writeln("    }");
    }

    /**
     *
     * <p> Writes data into workspace </p>
     *
     * @param writer <code>MatFileWriter</code> to receive data
     * @throws IOException
     **/

    public void writeMat(MatFileWriter writer)
	throws IOException
    {
      writer.writeln("%% Block \"" + this.getName() + "\" wishes to save the following data:");
      writer.writeln(this.tableName + " = [");
      BlockFuncTable.printTable( writer, this.functionValues, this.myDimensions );
      writer.writeln("];");
    }

}
