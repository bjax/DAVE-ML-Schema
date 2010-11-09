// BlockMathSwitch
//
//	Switch element math function block
//
// 020517 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import org.jdom.Element;
import org.jdom.Namespace;

import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 *
 * <p>  This Math block represents a switch function </p>
 *
 **/

public class BlockMathSwitch extends BlockMath
{
    
    /**
     *
     * <p> Constructor for switch Block <p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public BlockMathSwitch( Element applyElement, Model m )
    {
	// Initialize superblock elements
	super(m);

	// Parse parts of the Apply element
	Namespace mathml = Namespace.getNamespace("", "http://www.w3.org/1998/Math/MathML");
	Element piecewise = (Element) applyElement.getChild("piecewise", mathml);

	// take appropriate action based on type
	if(piecewise == null)
	  {
	      List elist = applyElement.getChildren();
	      Iterator elisti = elist.iterator();
	      Element el = (Element) elisti.next();
	      System.err.println("Error - BlockMathSwitch constructor called with" +
				 " wrong type element. Expected <piecewise>, found <" +
				 el.getQualifiedName() + ">.");
	  }
	else
	  {
	      // set name of ourself - will be "uniquified" later
	      this.setName( "switch" );
	
	      // look for two children: <piece> and <otherwise>
	      List kids = piecewise.getChildren();
	      if(kids.size() != 2)
		  System.err.println("Error - expected single <piece> and <otherwise> elements, found " + kids.size() 
				     + " elements total.");
	      else
		  {
		      // Get the two children that describe switch
		      Element piece     = (Element) piecewise.getChild("piece", mathml);
		      Element otherwise = (Element) piecewise.getChild("otherwise", mathml);

		      // check they are not null
		      if (piece == null) {
			  System.err.println("Error - <piecewise> element contained no <piece> element");
			  return; }
		      if (otherwise==null) {
			  System.err.println("Error - <piecewise> element contained no <otherwise> element");
			  return; }

		      List pieces = piece.getChildren();
		      if (pieces.size() != 2) {
			  System.err.println("Error - <piece> element can only interpret two children - found "
					     + pieces.size() + " instead.");
			  return; }
		      List other = otherwise.getChildren();
		      if (other.size() != 1) {
			  System.err.println("Error - <otherwise> element needs one child - found "
					     + other.size() + " instead.");
			  return; }
		      // process like: 		  this.genInputsFromApply(ikid);
		      // piece has two children: output 1 and condition it is used, and
		      // otherwise has default condition.
		      // Input 1 is passed if condition is true
		      this.genInputsFromApply( pieces.iterator(), 1 );	// input 1 & condition (input 2)
		      this.genInputsFromApply( other.iterator(), 3 );	// input 3 (otherwise)
		  }

	  }

//System.out.println("    BlockMathSwitch constructor: " + myName + " created.");
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
	writer.write(" and is a Switch math block.");
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
	writer.writeln("      BlockType		      Switch");
	writer.writeln("      Name		      \"" + this.getName() + "\"");
	this.createMDLPosition( writer, x, y );
	writer.writeln("      AttributesFormatString  \"Threshold = %<Threshold>\"");
	writer.writeln("      Threshold		      \"0.5\"");

	this.createMDLPortInfo( writer, 1 );	// try to create port with name

	writer.writeln("    }");

//System.out.println("Created MDL for block " + this.myName);
    }

}
