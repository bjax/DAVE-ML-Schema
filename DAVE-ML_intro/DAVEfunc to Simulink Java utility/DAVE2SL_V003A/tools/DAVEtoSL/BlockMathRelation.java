// BlockMathRelation
//
//	Relational operator math function block
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
 * <p>  The MathRelation block represents an Relational operator (scalar input) function</p>
 *
 **/

public class BlockMathRelation extends BlockMath
{
    String relationOp;
    
    /**
     *
     * <p> Constructor for relation Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathRelation( Element applyElement, Model m )
    {
	// Initialize superblock elements
	super(m);

	// Parse parts of the Apply element
	List kids = applyElement.getChildren();
	Iterator ikid = kids.iterator();

	// first element should be our type; also use for name
	Element first = (Element) ikid.next();
	this.relationOp = first.getName();
	this.setName( this.relationOp );
	
	// take appropriate action based on type
	if(!isRelation())
	    {
		System.err.println("Error - BlockMathRelation constructor called with" +
				   " unknown relation operator " + this.relationOp);
	    }
	else
	    {
		// look for two inputs
		if(kids.size() != 3)
		    System.err.println("Error - <apply><[relation]/> only handles 2 arguments, not " + (kids.size()-1));
		else
		    this.genInputsFromApply(ikid, 1);
	    }

//System.out.println("    BlockMathRelation constructor: " + this.getName() + " created.");
    }


    /**
     *
     * <p> Determines if string is acceptable MathML relation </p>
     *
     * @return <code>boolean</code> true if is a relation
     *
     **/

    public boolean isRelation( )
    {
	if (this.relationOp.equals("lt")) return true;
	if (this.relationOp.equals("leq")) return true;
	if (this.relationOp.equals("eq")) return true;
	if (this.relationOp.equals("geg")) return true;
	if (this.relationOp.equals("gt")) return true;
	if (this.relationOp.equals("neq")) return true;

	return false;
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
	writer.write(" and is a Relational Operator math block.");
    }

    /**
     *
     * <p> Writes relation block for Simulink representation
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
	writer.writeln("      BlockType		      RelationalOperator");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      ShowName                off");

	writer.write("	      Operator");
	if(relationOp.equals("lt"))
	    writer.writeln("		      \"<\"");
	if(relationOp.equals("leq"))
	    writer.writeln("		      \"<=\"");
	if(relationOp.equals("eq"))
	    writer.writeln("		      \"==\"");
	if(relationOp.equals("geq"))
	    writer.writeln("		      \">=\"");
	if(relationOp.equals("gt"))
	    writer.writeln("		      \">\"");
	if(relationOp.equals("neq"))
	    writer.writeln("		      \"~=\"");


	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
