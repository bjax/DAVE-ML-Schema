// BlockMathAbs
//
//	Absolute value math function block
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
 * <p>  The Math block represents an absolute value (scalar input) function</p>
 *
 **/

public class BlockMathAbs extends BlockMath
{
    
    /**
     *
     * <p> Constructor for abs Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathAbs( Element applyElement, Model m )
    {
	// Initialize superblock elements
	super(m);

	// Parse parts of the Apply element
	List kids = applyElement.getChildren();
	Iterator ikid = kids.iterator();

	// first element should be our type; also use for name
	Element first = (Element) ikid.next();
	this.setName( first.getName() );
	
	// take appropriate action based on type
	if(!first.getName().equals("abs"))
	    {
		System.err.println("Error - BlockMathAbs constructor called with" +
				   " wrong type element.");
	    }
	else
	    {
		// look for single input
		if(kids.size() > 2)
		    System.err.println("Error - <apply><abs/> only handles single arguments, not " + (kids.size()-1));
		else
		    this.genInputsFromApply(ikid, 1);
	    }

//System.out.println("    BlockMathAbs constructor: " + myName + " created.");
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
	writer.write(" and is a Absolute Value math block.");
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
	writer.writeln("      BlockType		      Abs");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      ShowName                off");
	writer.writeln("      SaturateOnIntegerOverflow	on");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
