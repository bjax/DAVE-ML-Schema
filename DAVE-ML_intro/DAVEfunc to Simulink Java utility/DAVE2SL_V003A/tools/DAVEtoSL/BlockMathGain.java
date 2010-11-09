// BlockMathGain
//
//	Gain value math function block
//
// 020517 EBJ
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
 * <p> The BlockMathGain block represents a gain block </p>
 *
 **/

public class BlockMathGain extends BlockMath
{
    double gain;
    
    /**
     *
     * <p> Constructor for Gain Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathGain( Element applyElement, Model m )
    {
	// Initialize superblock elements
	super(m);

	// Initialize our elements
	this.gain = 1;

	// Parse parts of the Apply element
	List kids = applyElement.getChildren();
	Iterator ikid = kids.iterator();

	// first element should be our type; also use for name
	Element first = (Element) ikid.next();
	this.setName( first.getName() );
	
	// take appropriate action based on type
	if(!first.getName().equals("minus"))
	  {
	    System.err.println("Error - BlockMathGain constructor called with" +
			       " wrong type element.");
	  }
	else
	  {
	      this.gain = -1.0;
	      genInputsFromApply( ikid, 1 );
	  }

//System.out.println("    BlockMathGain constructor: " + myName + " created with a gain of " + this.gain);
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
	writer.write(" and is a Gain block.");
    }

    /**
     *
     * <p> Writes abs block for Simulink representation
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
	writer.writeln("      BlockType		      Gain");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      Gain		      \"" + this.gain + "\"");
	writer.writeln("      Multiplication	      \"Element-wise(K.*u)\"");
	writer.writeln("      SaturateOnIntegerOverflow	on");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
