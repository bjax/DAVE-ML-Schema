// BlockMathProduct
//
//	Multiplication math function block
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
 * <p> The MathProduct block represents a scalar multiply/divide block </p>
 *
 **/

public class BlockMathProduct extends BlockMath
{

    String blockType;	// should be either "product" or "quotient"
    // quotient blocks have only two inputs and calculate #1/#2.
    // product block inputs are multiplied together.
    
    /**
     *
     * <p> Constructor for Product Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathProduct( Element applyElement, Model m )
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
	if(blockType.equals("times"))
	  {
	      this.genInputsFromApply(ikid, 1);
	  }
	else if (blockType.equals("quotient"))
	    {
		if(kids.size() != 3)
		    System.err.println("Error - <apply><quotient/> only handles 2 arguments, not " + (kids.size()-1));
		else
		    this.genInputsFromApply(ikid, 1);
	    }
	else
	  {
	    System.err.println("Error - BlockMathProduct constructor called with" +
			       " type element:" + blockType);
	  }

//System.out.println("    BlockMathProduct constructor: " + this.getName() + " created.");
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
	writer.write(" and is a Product math block.");
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
	writer.writeln("      BlockType		      Product");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	writer.writeln("      Ports                   [" + this.numInputs() + ", 1]");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      ShowName                off");
	if(this.blockType.equals("times"))
	    writer.writeln("      Inputs		      \"" + this.numInputs() + "\"");
	else
	    writer.writeln("      Inputs		      \"*/\"");

	writer.writeln("      Multiplication	      \"Element-wise(.*)\"");
	writer.writeln("      SaturateOnIntegerOverflow	on");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
