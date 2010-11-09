// Block
//
//	Object representing each "block" in algorithm
//
// 020422 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * <p> the Block represents one element in a model's algorithm. These are 
 *	There should be one of these for each element in a model
 *	diagram. These are connected through Signals to other
 *	Blocks. </p>
 *
 * <p>	Convention is to call the upstream element (block or signal)
 *	with the port number and reference to the downstream element
 *	(block or signal). </p>
 *
 **/

public class Block
{
    Model ourModel;		// our model parent
    String myName;		// name of block
    NameSpace nameSpace;	// namespace of block
    private ArrayList inVarIDs;		// variable IDs associated with each input - keep in sync with inputs!
    private SignalArrayList inputs;	// list of input signals
    private SignalArrayList outputs;	// list of output signals
    int mdlWidth;		// for Simulink diagram
    int mdlHeight;		// for Simulink diagram
    int xPad;			// for Simulink diagram - adjustment to block width
    int yPad;			// for Simulink diagram - adjustment to block height
    int myRow;			// for Simulink diagram
    int myCol;			// for Simulink diagram
    private BlockArrayList children;	// our children blocks
    int rowDepthOfChildren;	// lowest descendent's row

    /**
     *
     * <p> Constructor for Block </p>
     *
     * @param blockName Name of block - must be unique
     * @param numInputs number of input ports
     * @param numOutputs number of output ports
     * @param m <code>Model</code> we're part of
     *
     * */

    public Block(String blockName, int numInputs, int numOutputs, Model m)
    { 
	ourModel = m;
	myName = blockName;
	nameSpace = null;
	inVarIDs = new ArrayList(numInputs);
	inputs = new SignalArrayList(numInputs); 
	outputs = new SignalArrayList(numOutputs);
	mdlWidth = 30;	// default values
	mdlHeight = 30;
	xPad = 0;
	yPad = 0;
	myRow = 0;
	myCol = 0;
	children = new BlockArrayList(10);
	rowDepthOfChildren = 0;
//System.out.println("  Block Constructor: " + myName + " built.");
	m.add(this);
    }


    /**
     *
     * <p> Adds input to next port. Returns port number (1-based). </p>
     *
     * @param inSignal signal to add
     *
     */

    public int addInput(Signal inSignal)
    {
	int portIndex;

	inputs.add(inSignal);			// add to end of list
	portIndex = inputs.indexOf(inSignal);	// find it's position
	while (inVarIDs.size() <= portIndex)	// increase length of companion list if necessary
	    inVarIDs.add("");
	String existingID = (String) inVarIDs.get(portIndex);
	if (existingID.equals(""))		// don't override existing ID (for FTB)
	    inVarIDs.set(portIndex, inSignal.getVarID());	// keep in sync with inputs

//System.out.println("Adding signal " + inSignal.getName() + " as input " + (portIndex+1) + " to block " + getName());

	return portIndex+1;
    }


    /**
     *
     * <p> Adds input to specified port </p>
     *
     * @param inSignal signal to add
     * @param portNum port (1-based) to add signal
     *
     */

    public void addInput(Signal inSignal, int portNum)
    {
	int portIndex = portNum-1;
//System.out.println("Adding signal " + inSignal.getName() + " as input " + (portIndex+1) + " to block " + getName());
	while(inputs.size() <= portIndex)		// grow list if necessary to reach offset
	    inputs.add("");
	inputs.set(portIndex, inSignal);		// record input signal

	while(inVarIDs.size() <= portIndex)	// grow ID list with input list
	    inVarIDs.add("");

	String existingID = (String) inVarIDs.get(portIndex);	// see if already set
	if (existingID.equals(""))		// don't override existing ID (for FTB)
	    inVarIDs.set(portIndex, inSignal.getVarID());	// keep in sync with inputs

//System.out.println("  Block " + myName + " has added upstream signal " + inSignal.getName());
    }

    /**
     * 
     * <p> Add variable ID (usually to hook up later) </p>
     *
     * @param portNum port to associate with varID (1-based)
     * @param varID <code>String</code> containing unique variable ID
     *
     **/

    public void addVarID( int portNum, String varID )
    {
	int portIndex = portNum - 1;
	if(portNum < 1) 
	    System.err.println("Block '" + getName() + "' asked to hook up varID '" + varID + 
			       "' to port " + portNum + " -- error!");
	else
	    {
		while(inVarIDs.size() <= portIndex)
		    inVarIDs.add("");
		inVarIDs.set(portIndex, varID);

		while(inputs.size() <= portIndex)	// keep in sync with varID array
		    inputs.add("");
		inputs.set(portIndex, null);
	    }

    }


    /**
     *
     * <p> Adds input to specified port </p>
     *
     * @param inSignal signal to add
     * @param portNum port (1-based) to add signal
     *
     */

    public void addOutput(Signal outSignal, int portNum)
    {
//System.out.println("AddOutput called for block " + getName() + " port number " + portNum);
	int portIndex = portNum-1; 

	while (outputs.size() <= portIndex)	// increase length if necessary
	    outputs.add("");
	outputs.set(portIndex, outSignal);	// add signal to our output list

//System.out.println("  Block " + myName + " has added downstream signal " + outSignal.getName());
	outSignal.addSource( this, portNum );	// tell signal about us
    }


    /**
     *
     * <p> Creates new upstream const block and signal wire </p>
     *
     * @param constantValue <code>String</code> containing value
     * @param inPort <code>int</code> value of our input port to use (1-based)
     *
     **/

    protected void addConstInput( String constantValue, int inPort )
    {
	Block constBlock = new BlockMathConstant( constantValue, ourModel );
	Signal sig = new Signal( "const " + constantValue, ourModel);
	constBlock.addOutput(sig);
	sig.addSink(this, inPort);
    }

    /**
     *
     * <p> Adds output to next port. Returns port number. </p>
     *
     * @param outSignal signal to add
     *
     */

    public int addOutput(Signal outSignal)
    {
	int newPort;

	outputs.add(outSignal);
	newPort = outputs.indexOf(outSignal)+1;	// which port did we get?
	outSignal.addSource( this, newPort );	// tell signal about us
	return newPort;
    }

    /**
     *
     * <p> Return <code>Block</code> name as <code>String</code> </p>
     *
     **/

    public String getName() { return myName; }


    /**
     *
     * <p> Returns variable ID (<code>varID</code>) associated with
     *     particular port number. </p>
     *
     * @param portNum (1-based) port number
     *
     **/

    public String getVarID( int portNum )
    {

	return (String) inVarIDs.get( portNum-1 );
    }


    /**
     *
     * <p> Returns an iterator to loop through input signals </p>
     *
     * @return <code>Iterator</code>
     *
     **/

    public Iterator getInputIterator() { return this.inputs.iterator(); }


    /**
     *
     * <p> Returns an iterator to loop through output signals </p>
     *
     * @return <code>Iterator</code>
     *
     **/

    public Iterator getOutputIterator() { return this.outputs.iterator(); }


    /**
     *
     * <p> Returns an iterator to loop through input variable IDs </p>
     *
     * @return <code>Iterator</code>
     *
     **/

    public Iterator getVarIDIterator() { return this.inVarIDs.iterator(); }


    /**
     *
     * <p> Set namespace and adjust name to match language requirements </p>
     *
     * @param newNameSpace <code>NameSpace</code> name must meet and be unique within
     *
     **/

    public void setNameSpace( NameSpace newNameSpace )
    {
//System.out.println("Setting namespace for block " + this.myName);
	this.nameSpace = newNameSpace;
	this.setName( this.getName() );	// convert old name to be unique and well-formed
    }



    /**
     *
     * <p> Change name of block to one acceptable to namespace </p>
     *
     * @param newName
     *
     **/

    protected void setName( String newName )
    {
	if (this.nameSpace != null)
	    this.myName = this.nameSpace.addUnique( newName );
	else
	    this.myName = newName;
//System.out.println("Setting block name to " + this.myName);
    }

    /**
     *
     * <p> Returns number of inputs </p>
     *
     **/

    public int numInputs() { return this.inputs.size(); }

    /**
     *
     * <p> Returns number of VarIDs </p>
     *
     **/

    public int numVarIDs() { return this.inVarIDs.size(); }


    /**
     *
     * <p> Returns height in Simulink diagram representation  </p>
     *
     **/

    public int getMdlHeight() { return mdlHeight + 2*yPad; }


    /**
     *
     * <p> Returns width in Simulink diagram representation  </p>
     *
     **/

    public int getMdlWidth() { return mdlWidth + 2*xPad; }



    /**
     *
     * <p> Returns my row position (after having it assigned) </p>
     *
     **/

    public int getRow() { return myRow; }



    /**
     *
     * <p> Returns my column position (after having it assigned) </p>
     *
     **/

    public int getCol() { return myCol; }


    /**
     *
     * <p> Hook up a single input signal, if it exists; otherwise, leave
     *     unconnected for now. Returns true if successful. </p>
     *
     * @param port <code>int</code> with 1-based input number
     *
     **/

    protected boolean hookUpInput(int port)
    {
	// get input variable ID
	String signalVarID = (String) inVarIDs.get(port-1);

	// look for existing signal
	Signal theSignal = ourModel.getSignals().findByID( signalVarID );
	if (theSignal != null)
	    {
//System.out.println("     found signal with name " + signalVarID + ", connecting...");
		theSignal.addSink( this, port );	// does double link
		return true;
	    }
	else
	    {
		System.err.println("    No signal named " + signalVarID + 
				   " found; leaving input " + port +
				   " of block " + getName() +
				   " disconnected for now..." );
		return false;
	    }
    }

    /**
     *
     * <p> Hook up to input signals, if they exist; otherwise, leave
     *     unconnected for now </p>
     *
     **/

    protected void hookUpInputs()
    {
	int portCount = 0;

	// loop through input variable IDs

	Iterator iVarIDIterator = this.inVarIDs.iterator();

	while (iVarIDIterator.hasNext())
	    {
		// get independent variable ID
		String signalVarID = (String) iVarIDIterator.next();

		// look for existing signal from previously built breakpoint block
		Signal theSignal = ourModel.getSignals().findByID( signalVarID );
		if (theSignal != null)
		    {
//System.out.println("     found signal with name " + signalVarID + ", connecting...");

			theSignal.addSink( this, portCount+1 );	// does double link
			portCount++;	// count # of dimensions
		    }
		else
		    {
			System.err.println("    No signal named " + signalVarID + " found.");
		    }
	    }
    }


    /**
     *
     * <p> Verify that all inputs are connected. If not, try again to
     * hook them up.  Returns false if all inputs are successfully
     * connected to upstream signals. </p>
     *
     **/

    public boolean verifyInputs()
    {
//System.out.println("Verifying inputs for block " + getName() + " which has " + this.inputs.size() + " inputs.");
	boolean error = false;
	Iterator ii = this.getInputIterator();
	int i = 1;
	while (ii.hasNext())
	    {
//System.out.println("...checking input " + i );
		Signal s = (Signal) ii.next();
		if (s == null) 
		    {
			// Found missing input. Try to connect to existing signal...
			error = error || !this.hookUpInput(i);
		    }
		i++;
	    }
	return error;
    }



    /**
     *
     * <p> Verify that all outputs are connected. If not, try again to
     * hook them up.  Returns false if all outputs are successfully
     * connected to downstream signals. </p>
     *
     **/

    public boolean verifyOutputs()
    {
	boolean error = false;
	Iterator i = this.getOutputIterator();
	while (i.hasNext())
	    {
		Signal s = (Signal) i.next();
		if (s == null) error = true;
	    }
	return error;
    }



    /**
     *
     * <p> Should only run after network complete. Searches downstream
     * signal and gets list of all immediate children. </p>
     *
     **/

    public void findChildren(String prefix)
    {
	Signal sig = null;
	BlockArrayList blks;
	Block kid = null;

	// Only run if haven't before, since children shouldn't change
	if ( children.size() == 0 )
	    {
//System.out.println(prefix + "  Looking for children for block " + myName);

	    // iterate through downstream signals to get next level blocks
	    for ( Iterator iSig = outputs.iterator(); iSig.hasNext(); )
		{
		    sig = (Signal) iSig.next();
		    if (sig == null)
			System.err.println("Block " + myName + " reports " + outputs.size()
					   + " outputs but iterator returns null.");
		    else
			{
			    blks = sig.getDests();
			    if( blks == null )
				System.err.println("Signal " + sig.getName() + " has no outputs.");
			    else
				for ( Iterator iBlk = blks.iterator(); iBlk.hasNext(); )
				    {
					kid = (Block) iBlk.next();
					children.add(kid);
					kid.findChildren(prefix+ " ");
				    }
			}
		}
//System.out.println(prefix + "  found " + children.size() + " children for block " + myName);
	    }
    }



    /**
     *
     * <p> Should only run after children found. If we are assigned a
     * deeper column than we had before, tell all children to go 1
     * deeper; find out their depth in rows and set next child below
     * them. Returns lowest row of any child. </p>
     *
     * @param minimumRow (1-based) value of our min row. 
     * @param minimumCol (1-based) value of our min column. Inports usually row 1.
     * @param prefix <code>String</code> containing offset prefix for writing diagnostics
     *
     **/

    public int setPosition( int minimumRow, int minimumCol, String prefix )
    {
	Block kid = null;

//System.out.print(prefix + "Setting position of " + myName + " and kids. Original row was " + this.myRow);	
	if (this.myRow == 0)  this.myRow = minimumRow;		// don't go lower, but...
	if (minimumCol > this.myCol)  this.myCol = minimumCol;	// ... can slide right

	this.rowDepthOfChildren = myRow;

//System.out.println("; now set to " + this.myRow);	

	int offset = 0;
	for ( Iterator ikid = children.iterator(); ikid.hasNext(); )
	    {
		kid = (Block) ikid.next();
//System.out.println(prefix + "Calling kid named " + kid.getName() + " with offset " + offset);
		int returnedRow =
		    kid.setPosition( this.rowDepthOfChildren + offset, this.myCol + 1, (prefix + " ") );
		if ( this.rowDepthOfChildren < returnedRow ) this.rowDepthOfChildren = returnedRow;

//System.out.println(prefix + "row depth of children set to " + this.rowDepthOfChildren);
		offset = 1;	// after first iteration, step down each time
	    }

//System.out.println(prefix + "Position of block " + myName + ": (" + myRow + "," + myCol + "); returning depth " + this.rowDepthOfChildren);

	if ( myRow > this.rowDepthOfChildren )
	    return myRow;
	else
	    return this.rowDepthOfChildren;
    }


    /**
     *
     * <p> Sets position with a default indentation of single space, if not specified </p>
     *
     * @param minimumRow (1-based) value of our min row. 
     * @param minimumCol (1-based) value of our min column. Inports usually row 1.
     *
     **/

    public int setPosition( int minRow, int minCol ) { return setPosition( minRow, minCol, " "); }


    /**
     *
     * <p> Sets value for row and column, overwriting previous values. </p>
     *
     * @param row (1-based) value of our new row.
     * @param col (1-based) value of our new column.
     * 
     **/

    public void setRowCol( int theRow, int theCol ) 
    { 
//	System.out.println("  >>> Block " + getName() + " position updated from [" +
//			   myRow + "," + myCol + "] to [" +
//			   theRow + "," + theCol + "].");
	myRow = theRow; 
	myCol = theCol; 
	return; 
    }


    /**
     *
     * <p> Recurse through children to find farthest column </p>
     *
     **/

    public int findChildrensFarthestColumn()
    {
	Block kid = null;

	int farthest = this.myCol;
	for (Iterator ikid = children.iterator(); ikid.hasNext(); )
	    {
		kid = (Block) ikid.next();
		int kidsCol = kid.findChildrensFarthestColumn();
		if(kidsCol > farthest) farthest = kidsCol;
	    }
	return farthest;
    }


    /**
     *
     * <p> Recurse through children to find deepest row </p>
     *
     **/

    public int findChildrensDeepestRow()
    {
	Block kid = null;

	int deepest = this.myRow;
	for(Iterator ikid = children.iterator(); ikid.hasNext(); )
	    {
		kid = (Block) ikid.next();
		int kidsRow = kid.findChildrensDeepestRow();
		if(kidsRow > deepest) deepest = kidsRow;
	    }
	return deepest;
    }



    /**
     *
     * <p> Generates brief description on output stream </p>
     *
     * @param writer FileWriter to use
     * @throws <code>IOException</code>
     *
     **/

    public void describeSelf( FileWriter writer ) throws IOException
    {
	int numInputs = inputs.size();
	int numOutputs = outputs.size();

	writer.write("Block \"" + myName);
	writer.write("\" at [" + myRow + "," + myCol + "] has ");
	switch (numInputs)
	    {
	    case 0:
		writer.write("NO INPUTS, ");
		break;
	    case 1:
		writer.write("one input, ");
		break;
	    case 2:
		writer.write("two inputs, ");
		break;
	    case 3:
		writer.write("three inputs, ");
		break;
	    case 4:
		writer.write("four inputs, ");
		break;
	    case 5:
		writer.write("five inputs, ");
		break;
	    case 6:
		writer.write("six inputs, ");
		break;
	    case 7:
		writer.write("seven inputs, ");
		break;
	    case 8:
		writer.write("eight inputs, ");
		break;
	    case 9:
		writer.write("nine inputs, ");
		break;
	    default:
		writer.write(numInputs + " inputs, ");
	    }

	switch (numOutputs)
	    {
	    case 0:
		writer.write("NO OUTPUTS, ");
		break;
	    case 1:
		writer.write("one output, ");
		break;
	    case 2:
		writer.write("two outputs, ");
		break;
	    case 3:
		writer.write("three outputs, ");
		break;
	    case 4:
		writer.write("four outputs, ");
		break;
	    case 5:
		writer.write("five outputs, ");
		break;
	    case 6:
		writer.write("six outputs, ");
		break;
	    case 7:
		writer.write("seven outputs, ");
		break;
	    case 8:
		writer.write("eight outputs, ");
		break;
	    case 9:
		writer.write("nine outputs, ");
		break;
	    default:
		writer.write(numOutputs + ", ");
	    }
    }

    /**
     *
     * <p> Writes comment into Simulink file about lost block </p>
     *
     * @throws <code>IOException</code>
     *
     **/

    public void createMDL(SLFileWriter writer, int x, int y)
	throws IOException
    {
	writer.writeln("%%");
	writer.writeln("%%******** Missing Block Type: block \"" + this.getName() + "\" *******");
	writer.writeln("%%");
    }

    /**
     *
     * <p> Writes placeholder in Mat file unless overwritten </p>
     *
     * @param writer <code>MatFileWriter</code> to receive data
     * @throws IOException
     *
     **/
    public void writeMat(MatFileWriter writer)
	throws IOException
    {
	writer.writeln("%% Block \"" + this.getName() + "\" has no data to save.");
    }


    /**
     *
     * <p> Creates an MDL Port structure on the output stream, using downstream port name </p>
     *
     * @param writer <code>SLFileWriter</code> to send output to
     * @param portNum Port for which to generate structure (1-based)
     * @throws IOException
     *
     **/

    protected boolean createMDLPortInfo(SLFileWriter writer, int portNum) 
	throws IOException
    {
	// Writes port name structure into MDL file, if available

	String downstreamSignalName = ((Signal) outputs.get(portNum-1)).getName();
	boolean success = false;

	if (downstreamSignalName.length() > 0)
	    {
		writer.writeln("      Port {");
		writer.writeln("	PortNumber		\"" + portNum + "\"");
		writer.writeln("	Name			\"" + downstreamSignalName + "\"");
		writer.writeln("	TestPoint		off");
		writer.writeln("	LinearAnalysisOutput	off");
		writer.writeln("	LinearAnalysisInput	off");
		writer.writeln("	RTWStorageClass		\"Auto\"");
		writer.writeln("      }");
		success = true;
	    }

	return success;
    }



    /**
     *
     * <p> Puts Position command on output stream to properly position the block with padding </p>
     *
     * @param writer SLFileWriter to use for writing
     * @param x int containing centerpoint horizontal position relative to upper left corner of diagram
     * @param y int containing centerpoint vertical offset relative to upper left corner of diagram
     * @throws java.io.IOException
     *
     **/

    protected boolean createMDLPosition(SLFileWriter writer, int x, int y )
	throws IOException
    {
	int x0 = x - this.mdlWidth/2 + this.xPad;
	int y0 = y - this.mdlHeight/2 + this.yPad;

	writer.writeln("      Position		      [" + x0      + "," + y0 + "," 
		       + (x0+this.mdlWidth) + "," + (y0+this.mdlHeight) + "]");
	return true;
    }
}
