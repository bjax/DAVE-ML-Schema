// BlockInput
//
//	Object representing an input block
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
 * <p>  The Input Block represents an input to the system </p>
 *
 **/

public class BlockInput extends Block
{
    int seqNumber;	// our input sequence number (1-based) 
    String units;	// units of measure for downstream signal
    
    /**
     *
     * <p> Constructor for input Block <p>
     *
     * @param signal Downstream <code>Signal</code> with which to connect
     * @param number which model input we are (1-based)
     * @param m <code>Model</code> we're part of
     *
     **/

    public BlockInput( Signal theSignal, int number, Model m )
    {
	// Initialize superblock elements
	super(theSignal.getName(), 1, 1, m);

	// Set our simulink size
	this.mdlHeight = 14;
	this.mdlWidth  = 30;

	// Add padding so our centerline is in middle of row
	this.yPad = 8;

	// record our number
	this.seqNumber = number;

	// record our units
	this.units = theSignal.getUnits();

	// hook up to downstream signal
	this.addOutput( theSignal, 1 );
//System.out.println("    BlockInput constructor: " + myName + " as input " + seqNumber);
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
	writer.write("(" + units + ") and is model input #" + seqNumber);
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
	writer.writeln("      BlockType		      Inport");
	writer.writeln("      Name		      \"" + this.myName + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      Port		      \"" + this.seqNumber + "\"");
	writer.writeln("      LatchInput	      off");
	writer.writeln("      Interpolate	      on");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");
    }

}



