// BlockMathSum
//
//	Summing math function block
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
 * <p> The MathSum block represents a scalar summer </p>
 *
 **/

public class BlockMathSum extends BlockMath
{
    
    /**
     *
     * <p> Constructor for Sum Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathSum( Element applyElement, Model m )
    {
	// Initialize superblock elements
	super(m);

	// Parse parts of the Apply element
	List kids = applyElement.getChildren();
	Iterator ikid = kids.iterator();

	// first element should be our type; also use for name
	Element first = (Element) ikid.next();
	String blockType = first.getName();
	this.setName( blockType );
	
	// take appropriate action based on type
	if(!blockType.equals("plus"))
	  {
	    System.err.println("Error - BlockMathSum constructor called with" +
			       " type element:" + blockType);
	  }
	else
	  {
//System.out.println("   BlockMathSum constructor called with " + kids.size() + "elements.");
	      this.genInputsFromApply(ikid, 1);
	  }

//System.out.println("    BlockMathSum constructor: " + this.getName() + " created.");
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
	writer.write(" and is a Sum math block.");
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
	writer.writeln("      BlockType		      Sum");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	writer.writeln("      Ports                   [" + this.numInputs() + ", 1]");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      ShowName                off");
	writer.writeln("      IconShape		      \"rectangular\"");
	writer.write("      Inputs		      \"");
	for( int i = 0; i< this.numInputs(); i++ ) writer.write("+");
	writer.writeln("\"");
	writer.writeln("      SaturateOnIntegerOverflow	on");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
