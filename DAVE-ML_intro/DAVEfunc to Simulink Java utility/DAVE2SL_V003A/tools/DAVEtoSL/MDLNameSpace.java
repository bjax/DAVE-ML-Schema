// MDLNameSpace
//
//	Creates and manages unique namespace with valid MDL identifiers.
//
// 020514 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * <p> MDLNameSpace represents a list of strings in use. It provides
 * methods to add, delete, and create unique names based on an initial
 * namespace. It guarantees MDL-compatible names, changing the
 * original if necessary. </p>
 *
 **/

public class MDLNameSpace extends NameSpace
{
    /**
     *
     * <p> Normal constructor </p>
     *
     **/

    public MDLNameSpace()  {super();}


    /**
     *
     * <p> Constructor that set initial array capacity </p>
     *
     * @param initialCapacity Estimate of initial capacity
     *
     **/

    public MDLNameSpace(int initialCapacity) { super(initialCapacity); }


    /**
     *
     * <p> Static method that converts supplied <code>String</code> to
     *     acceptable Simulink block name. </p>
     *
     * <p> Note that the manual implies all UNICODE is acceptable
     *     but I've found it best to remove punctuation, parens and
     *	   brackets, and convert whitespace into underscores. Returns
     *     acceptable name as <code>String</code> but does not guarantee
     *     uniqueness or enter the name into namespace. </p>
     *
     * @param s <code>String</code> containing the original name
     * @return String
     *
     **/

    public static String convertToMDLString(String s)
    {	
//System.out.println("convertToMDLString called for " + s);
	// replace any whitespace with underbars
	// remove any parentheses or brackets
	// remove any punctation or math operators

	// not yet implemented
	return s;
    }


    /**
     *
     * <p> Converts supplied name <code>String</code> into a
     *     Simulink-acceptable name, as described in class method
     *     <code>convertToMDLString</code>
     *
     * @param s <code>String</code> containing candidate name
     * @return String
     *
     **/

    public String fixName(String s)  { return convertToMDLString(s); }

}
