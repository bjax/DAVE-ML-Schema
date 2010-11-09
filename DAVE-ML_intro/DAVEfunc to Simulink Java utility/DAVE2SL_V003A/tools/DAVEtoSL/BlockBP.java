// BlockBP
//
//	Object representing a breakpoint block
//
// 020424 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import org.jdom.Element;

import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 *
 * <p>  The BP Block represents a breakpoint set. </p>
 *
 **/

public class BlockBP extends Block
{
    ArrayList breakpoints;

    /**
     *
     * <p> Constructor for breakpoint Block <p>
     *
     * @param bpElement JDOM Element pointing to a breakpoint definition
     * @param m Our parent <code>Model</code> to which we belong
     * @throws IOException
     *
     **/

    public BlockBP( Element bpElement, Model m ) throws IOException
    {
	// Initialize superblock elements
	super(bpElement.getAttributeValue("bpID"), 1, 1, m);

	// Set our simulink size
	this.mdlHeight = 52;
	this.mdlWidth  = 50;

	// Get breakpoint values as arraylist
	breakpoints = ParseText.toList( bpElement.getChild("bpVals").getTextTrim() );

    }

    /**
     *
     * <p> Constructor creates necessary BP block for given dangling func table input </p>
     *
     *
     * @param theSignal wire with no source that connects to downstream function block
     * @param bplist <code>List</code> of breakpoint definition <code>Element</code>s
     * @param m <code>Model</code> to join
     * @throws <code>IOException</code>
     *
     **/

    public BlockBP( Signal theSignal, List bpList, Model m ) throws IOException
    {
	super("",1,5,m);	// will fix name later

	Element bpDef = null;		// this will be the bp element that defines us

	// get block downstream of connector signal; confirm it is a function table block
	BlockArrayList bal = theSignal.getDests();	           // returns all downstream blocks
	ArrayList ports = theSignal.getDestPortNumbers();          // and associated port numbers
	Iterator bali = bal.iterator();			           // get iterators - one for block list
	Iterator pni  = ports.iterator();			   // and one for the signal's ports
	Block b = (Block) bali.next();                             // get first block - all have same bpID

	if(!(b instanceof BlockFuncTable))
	    System.err.println("Found non-function table block downstream of breakpoint.\n");
	else
	    {	// found function table block
		// get bpID associated with that input
		BlockFuncTable theFTB = (BlockFuncTable) b;        // cast into right object
		Integer n = (Integer) pni.next();		   // this is port # for downstream block
		String bpID = theFTB.getBPID( n.intValue() );      // ask block for BPID assoc. with port

		// now search for matching BPID in breakpoint list
		Iterator i = bpList.iterator();
		boolean foundBPID = false;
		while(i.hasNext())
		    {
			bpDef = (Element) i.next();
			if (bpID.equals(bpDef.getAttributeValue("bpID")))
			    {
				foundBPID = true;
				break;
			    }
		    }
		if(!foundBPID)
		    System.err.println("Could not match BP ID\n");
		else
		    {
			// Get breakpoint values as arraylist
			breakpoints = ParseText.toList( bpDef.getChild("bpVals").getTextTrim() );
			this.setName(bpID);	// remember our name (like MACH_x_MACH1)

			// hook up to output
			this.addOutput(theSignal, 1 );	// Remember who we're connected to,
					// also contacts signal to tell 'em we're their source

			// find our input signal
			String varID = theFTB.getVarID( n.intValue() );

			theSignal = m.getSignals().findByID( varID );

			// hook up to input
			if(theSignal != null)
			    {
				theSignal.addSink( this, 1 );
			    }
			else
			    System.err.println(" CAN'T FIND SIGNAL " + varID + "!!");

		    }
	    }
    }

    /**
     *
     * <p> Returns length of breakpoint vector </p>
     *
     **/

    public int length()
    {
	return breakpoints.size();
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
	writer.write("and is a breakpoint block with " + this.length() + " breakpoints.");
    }


    /**
     *
     * <p> Writes inport block for Simulink representation
     *
     * @param writer Instance of the SLFileWriter class
     * @param x	Horizontal offset of upper left corner of block
     * @param y Vertical offset of upper left corner of block
     * @throws IOException
     *
     */

    public void createMDL(SLFileWriter writer, int x, int y)
	throws IOException
    {
	writer.writeln("    Block {");
	writer.writeln("      BlockType		      Reference");
	writer.writeln("      Name		      \"" + this.myName + "\"");
	writer.writeln("      Ports		      [1, 1]");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      FontSize		      10");
	writer.writeln("      SourceBlock	      \"simulink3/Functions\n& Tables/PreLook-Up\nIndex Search\"");
	writer.writeln("      SourceType	      \"LookupIdxSearch\"");
	writer.writeln("      bpData		      \"" + breakpoints + "\"");
	writer.writeln("      searchMode	      \"Binary Search\"");
	writer.writeln("      cacheBpFlag	      on");
	writer.writeln("      outputFlag	      off");
	writer.writeln("      extrapMode	      \"Linear Extrapolation\"");
	writer.writeln("      rangeErrorMode	      \"None\"");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");
    }
}
