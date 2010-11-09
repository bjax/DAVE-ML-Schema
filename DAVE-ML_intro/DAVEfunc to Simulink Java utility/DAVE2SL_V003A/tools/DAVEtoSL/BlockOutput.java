// BlockOutput
//
//	Object representing an output block
//
// 020506 EBJ
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
 * <p>  The Output Block represents an output to the system </p>
 *
 **/

public class BlockOutput extends Block
{
    int seqNumber;	// our output sequence number (1-based) 
    String units;	// units of measure of downstream block
    
    /**
     *
     * <p> Constructor for output Block <p>
     *
     * @param signal Upstream <code>Signal</code> with which to connect
     * @param number Our model output number (1-based)
     * @param m <code>Model</code> we're part of
     *
     **/

    public BlockOutput( Signal theSignal, int number, Model m )
    {
	// Initialize superblock elements
	super(theSignal.getName(), 1, 1, m);

	// Set our simulink size
	this.mdlHeight = 14;
	this.mdlWidth  = 30;

	// Add vertical padding so centerline matches row
	this.yPad = 8;

	// record our number
	this.seqNumber = number;

	// record our U of M
	this.units = theSignal.getUnits();

	// hook up to upstream signal
//System.out.println("    BlockOutput constructor: " + myName + " is hooking up to signal " + theSignal.getName());
        theSignal.addSink( this, 1 );
//System.out.println("    BlockOutput constructor: " + myName + " as output " + seqNumber);
    }

    /**
     *
     * <p> Generates description of self </p>
     *
     * @throws <code>IOException</code>
     **/

    public void describeSelf(FileWriter writer) throws IOException
    {
	super.describeSelf(writer);
	writer.write("(" + units + ") and is model output #" + seqNumber);
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
//System.out.println("Created MDL for block " + this.myName);
	writer.writeln("    Block {");
	writer.writeln("      BlockType		      Outport");
	writer.writeln("      Name		      \"" + this.myName + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      Port		      \"" + this.seqNumber + "\"");
	writer.writeln("      OutputWhenDisabled      \"held\"");
	writer.writeln("      InitialOutput	      \"[]\"");
	writer.writeln("    }");
    }
}
