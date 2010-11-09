// ParseText
//
//	Object to convert string containing list of comma, tab, or
//	space-separated values into an array.
//
// 020424 E. B. Jackson

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * <p>	Class to convert a string containing comma, tab, or
 *	space-separated values into an array. </p>
 *
 **/

public class ParseText
{
    protected StreamTokenizer st;
    protected boolean goodNumber;

    public ParseText(String inputData)
    {
	StringReader sr = new StringReader(inputData);
	             st = new StreamTokenizer(sr);
		     st.resetSyntax();			// all chars ignored
		     st.whitespaceChars(0x09, 0x2C);	// delimiters include comma and + sign
		     st.wordChars(0x2D,0x2e);		// designate '-', '.' as word components
		     st.wordChars(0x30,0x39);		// designate '0' through '9' as word chars
		     st.wordChars(0x45,0x45);		// 'E' is now part of word
		     st.wordChars(0x65,0x65);		// as is 'e'

		     //		     st.parseNumbers();
		     //		     st.eolIsSignificant(false);
//	System.out.println(inputData);
    }

    public double next()
    throws IOException
    {
	goodNumber = false;
	double value = Double.NaN;

	do{
	    st.nextToken();
	//	return (double) st.nval;
	    switch(st.ttype) {
	    case StreamTokenizer.TT_NUMBER:	// in present incarnation, should never select
//		System.out.println("Input number is '" + st.nval + "'; conversion is ");
		value = st.nval;
		goodNumber = true; 
		break;
	    case StreamTokenizer.TT_WORD:
//		System.out.println("Input word is '" + st.sval + "'; conversion is ");
		try {
		    value = Double.parseDouble(st.sval);
		    goodNumber = true;
		} catch (NumberFormatException e) {goodNumber = false;}
		break;
	    case StreamTokenizer.TT_EOL:
//		System.out.println("End of line");
		break;
	    case StreamTokenizer.TT_EOF:
//		System.out.println("End of file");
		break;
	    default:
		System.out.println("Ordinary character, value is " + st.ttype);
	    }
	}
	while ((!goodNumber) && (st.ttype != StreamTokenizer.TT_EOF));
	return value;
     }

    public boolean eof()
    {
	return (st.ttype == StreamTokenizer.TT_EOF);
    }

    public boolean validNumber()
    {
	return ( goodNumber && 
		 (st.ttype != StreamTokenizer.TT_EOF));

    }
    
    /**
     *
     * <p> Converts string of comma-, space- or tab-separatead
     * variables to ArrayList. </p>
     *
     **/

    public ArrayList toList() throws IOException
    {
	ArrayList al = new ArrayList(10);

	while (!this.eof() )
	    {
		Double dbl = new Double(this.next());
		if (this.validNumber())
		    al.add( dbl );
	    }

	return al;
    }

    /**
     *
     * <p> Static version of routine toList </p>
     *
     * @param values String containing comma- or whitespace-separated values.
     *
     **/

    static public ArrayList toList(String values) throws IOException
    {
	ParseText pt = new ParseText(values);
	return pt.toList();
    }

    /** 
     *
     * <p> This class defines a main() method to test the ParseText logic. To
     *	use, run : java ParseText\$Test
     *
     **/

    public static class Test {
	/** Entry point for testing purposes **/
	public static void main(String[] args)
	    throws IOException
	{
	    ParseText pt;

	    if (args.length < 1)
		pt = new ParseText("  ,  1.0, +2.9,3.2,+4	-5.,Z,9.99E99  ");
	    else
		pt = new ParseText(args[0]);

//  	    while (!pt.eof() )
//  		{
//  		    System.out.println(pt.next());
//  		}
	    
	    ArrayList theList = pt.toList();

	    System.out.println();
	    System.out.println("Returned array:");
	    System.out.println();
	    System.out.print("  [");

	    Iterator listIterator = theList.iterator();

	    while(listIterator.hasNext())
		{
		    System.out.print(listIterator.next());
		    if(listIterator.hasNext()) System.out.print(", ");
		}

	    System.out.println("]");
	}
    }
}
