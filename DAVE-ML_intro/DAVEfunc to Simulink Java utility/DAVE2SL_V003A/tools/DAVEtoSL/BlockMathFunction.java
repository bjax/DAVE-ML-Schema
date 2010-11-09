// BlockMathFunction
//
//	Multipurpose function block
//
// 020614 EBJ
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
 * <p> The MathFunction block represents a scalar exponentiation block </p>
 *
 **/

public class BlockMathFunction extends BlockMath
{

    String blockType;	// can be several types: "pow" is current only one

    /**
     *
     * <p> Constructor for Function Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathFunction( Element applyElement, Model m )
    {
	// Initialize superblock elements
	super(m);

	// Parse parts of the Apply element
	List kids = applyElement.getChildren();
	Iterator ikid = kids.iterator();

	// first element should be our type; also use for name
	Element first = (Element) ikid.next();
	this.blockType = first.getName();
	this.setName( blockType );
	
	// take appropriate action based on type
	if(blockType.equals("power"))
	  {
	      this.genInputsFromApply(ikid, 1);
	  }
	else
	  {
	    System.err.println("Error - BlockMathFunction constructor called with" +
			       " type element:" + blockType);
	  }

//System.out.println("    BlockMathFunction constructor: " + this.getName() + " created.");
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
	writer.write(" and is a Function math block.");
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
	writer.writeln("      BlockType		      Math");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	writer.writeln("      Ports                   [" + this.numInputs() + ", 1]");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      ShowName                off");
	writer.write("      Operator		      \"");
	if(this.blockType.equals("power")) writer.write("pow");
	writer.writeln("\"");
	writer.writeln("      OutputSignalType	      \"auto\"");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
