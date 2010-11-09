//
//  DAVEtoSL.java
//
//      Converts DAVE files into Simulink models.
//
// 020419 E. B. Jackson

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.util.*;

/**
 *
 * <p> Converts DAVE files into Simulink models. </p>
 *
 * <p> Written by <a href="mailto:e.b.jackson@larc.nasa.gov">Bruce
 * Jackson</a> of the <a href="http:dcb.larc.nasa.gov">Dynamics and
 * Control Branch</a> of <a href="http://www.nasa.gov">NASA</a>'s <a
 * href="http://larc.nasa.gov">Langley Research Center</a>. </p>
 *
 * <p> 020419: Written EBJ </p>
 *
 */

public class DAVEtoSL
{

    /**
     * <p> This will take the supplied DAVE xml file, and convert that
     * file to a Simulink implementation.</p>
     *
     * @param xmlFileName XML file to read and interpret.
     * @param stubName Filename stub to use for MDL and mat files
     * @throws <code>IOException</code> - when errors occur.  */

    public void convert(String xmlFileName, String stubName)
	throws IOException
    {

	// open input file & parse
	DAVE dave = new DAVE();
	boolean success = dave.parse(xmlFileName);

	if(success) {

	    String mdlFileName = stubName + ".mdl";
	    String matFileName = stubName + "_setup.m"; 
	    File file = new File(stubName);
	    String modelName = file.getName();	// removes path

	    try {
		
	// script file for now
		SLFileWriter  mdlWriter = new  SLFileWriter(mdlFileName);
		MatFileWriter matWriter = new MatFileWriter(matFileName);
		
	// Write headers
		mdlWriter.writeSLHeader(mdlWriter, modelName);
		matWriter.writeMatHeader(matWriter, modelName);

//dave.describeSelf("temp.txt");
	// generate contents
		dave.createMDL( mdlWriter, matWriter );

	// Write footers
		mdlWriter.writeSLFooter( mdlWriter);
		matWriter.writeMatFooter( matWriter);

	// Close the files
		mdlWriter.close();
		matWriter.close();

	    } catch (IOException e )
		{ return; };
	}
    }

    /**
     *
     * <p> Static method to remove filetype from pathname </p>
     *
     * @param inString <code>String</code> containing filename with possible extension
     * @return String containing stub name
     *
     **/

    static String toStubName( String inString )
    {
      StringBuffer buf = new StringBuffer(inString);
	
	int dotIndex = buf.length();	// point to last char
	while( dotIndex > 0 )
	  {
	    dotIndex--;
	    if (buf.charAt(dotIndex) == '.') // look for file type sep
	      break;
	  }

	String stubName = new String(inString);
	if( dotIndex > 0 ) 
	    stubName = buf.substring(0, dotIndex);

	return stubName;
    }


    /** 
     * <p> Provide a static entry point for running. 
     * Do simple argument count and report any error. </p>
     */

    public static void main(String args[]) {
        try {
            if (args.length != 2) {
                System.out.println(
                    "Usage: java DAVEtoSL [XML Document URI] [Simulink output file]");
		System.out.println("Only had " + args.length + " argument(s)");
		for( int i = 0; i<args.length; i++)
		    System.out.println((i+1) + ": " + args[i]);
                System.exit(0);
             }
	    DAVEtoSL daveToSL = new DAVEtoSL();
	    String stubname = DAVEtoSL.toStubName( args[1] );
	    daveToSL.convert(args[0], stubname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

