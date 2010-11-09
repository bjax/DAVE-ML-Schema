// BlockMath
//
//	Superclass representing an arbitrary math function
//
// 020515 EBJ
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
 * <p>  The Math block represents an arbitrary math function </p>
 *
 **/

public class BlockMath extends Block
{
    
    /**
     *
     * <p> Constructor for math Block <p>
     *
     * @param m <code>Model</code> we're part of
     *
     **/

    public BlockMath(Model m)
    {
	// Initialize superblock elements
	super("", 3, 1, m);

	// Set our simulink size
	this.mdlHeight = 30;
	this.mdlWidth  = 30;
    }

    /**
     *
     * <p> Determines block type and calls appropriate constructor </p>
     *
     * @param applyElement Reference to <code>org.jdom.Element</code>
     * containing "apply" element
     * @param m		The parent <code>Model</code>
     *
     **/

    public static BlockMath factory( Element applyElement, Model m)
    {
//System.out.println("    BlockMath factory called.");
	// Parse parts of the Apply element
	List kids = applyElement.getChildren();
	Iterator ikid = kids.iterator();

	// first element should be our type; also use for name
	Element first = (Element) ikid.next();
	String theType = first.getName();
	
	// take appropriate action based on type
	if(theType.equals("abs")) 
	    return new BlockMathAbs( applyElement, m );
	if( theType.equals("lt" ) ||
	    theType.equals("leq") ||
	    theType.equals("eq" ) ||
	    theType.equals("geq") ||
	    theType.equals("gt" ) ||
	    theType.equals("neq") )
	    return new BlockMathRelation( applyElement, m );
	if(theType.equals("minus"))
	    return new BlockMathGain( applyElement, m );
	if(theType.equals("piecewise")) 
	    return new BlockMathSwitch( applyElement, m );
	if(theType.equals("plus"))
	    return new BlockMathSum( applyElement, m );
	if( theType.equals("times") ||
	    theType.equals("quotient") )
	    return new BlockMathProduct( applyElement, m);
	if( theType.equals("power") )
	    return new BlockMathFunction( applyElement, m);

	System.err.println("... don't recognize \"" + theType + "\" math element found.");	
	return null;
    }

    /**
     *
     * <p> Finds or generates appropriate inputs from math constructs </p>
     * 
     * @param ikid List <code>Iterator</code> for elements of top-level <apply>.
     * @param inputPortNumber <code>Int</code> with 1-based input number
     *
     **/

    public void genInputsFromApply( Iterator ikid, int inputPortNumber )
    {
	int i = inputPortNumber;
	while( ikid.hasNext() )
	    {
		// look at each input
		Element in = (Element) ikid.next();
		String name = in.getName();
//System.out.println("Found input math type " + name + "... adding as input " + i);
		// is it single scalar variable name <ci>?
		if( name.equals("ci") )
		    {
			String varID = in.getTextTrim();	// get variable name
			this.addVarID(i, varID);		// add it to proper input
			this.hookUpInput(i++);			// and hook up to signal, if defined.
		    }
		else if( name.equals("cn") ) // or maybe a constant value?
		    {
			String constantValue = in.getTextTrim();	// get constant value
//			this.addVarID(i, constantValue);		// add it as input - placeholder, not needed
			this.addConstInput(constantValue, i++);		// Create and hook up constant block
		    }
		else if( name.equals("apply") ) // recurse
		    {
//			this.addVarID(i, "");				// placeholder - no longer needed
			Signal s = new Signal(in, ourModel);		// Signal constructor recognizes <apply>...
									// .. and will call our BlockMath.factory() ...
			if( s!= null )					// .. and creates upstream blocks & signals
			    s.addSink(this,i++);			// hook us up as output of new signal path
			else
			    System.err.println("Null signal returned when creating recursive math element.");
		    }
		else
		    {
			System.err.println("Didn't find usable input type, instead found: " + in.getName());
		    }
	    }
    }
}
