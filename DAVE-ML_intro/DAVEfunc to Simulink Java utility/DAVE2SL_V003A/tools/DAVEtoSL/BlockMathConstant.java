// BlockMathConstant
//
//	Constant value source block
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
 * <p>  The constant block represents a constant value source </p>
 *
 **/

public class BlockMathConstant extends BlockMath
{
    String myValue;

    /**
     *
     * <p> Constructor for constant value Block <p>
     *
     * @param constantValue <code>String</code> containing value of constant
     *
     **/

    public BlockMathConstant( String constantValue, Model m )
    {
	// Initialize superblock elements
	super(m);

	myValue = constantValue;

	this.setName("const_" + constantValue + "_");

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
	writer.write(" and is a Constant Value math block.");
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
	writer.writeln("      BlockType		      Constant");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      ShowName                off");
	writer.writeln("      Value                   \"" + myValue + "\"");
	writer.writeln("      VectorParams1D          on");
	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
