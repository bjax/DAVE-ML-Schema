//
//  MatFileWriter.java
//
//    Provides special output functions for writing Matlab workspace (.mat) files.
//    Temporarily set to write only Matlab script (.m) files instead.
//
//    020611 E. B. Jackson <e.b.jackson@larc.nasa.gov>
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.io.IOException;
import java.io.FileWriter;
import java.util.Date;
import java.text.SimpleDateFormat;

public class MatFileWriter extends FileWriter
{
    /**
     *
     * <p> Constructor for MatFileWriter; derived from FileWriter
     *  but specialized to write Matlab .mat files
     *
     * @param fileName Name of file to open
     * @throws IOException
     *
     */

    public MatFileWriter(String fileName) throws IOException
    {
	super(fileName);
    }

    /**
     *
     * <p> Addeds newline to the end of each write </p>
     *
     * @param cbuf String containing text to write
     *
     */

    public void writeln( String cbuf ) throws IOException
    {
	super.write( cbuf + "\n" );
    }

    /** 
     *
     * <p> Writes the header for a .mat file </p>
     *
     * @param writer Instance of the MatFileWriter class
     * @throws IOException
     *
     */

    public void writeMatHeader(MatFileWriter writer, String modelName )
      throws IOException
    {
//  	SimpleDateFormat timeStamp = new SimpleDateFormat("EE MMM d HH:mm:ss yyyy");

	writer.writeln("%% M file script to provide data for " + modelName + ".mdl model");
//  	writer.writeln("Model {");
//  	writer.write("  Name			  \"");
//  	writer.write(modelName);
//  	writer.writeln("\"");
//  	writer.writeln("  Version		  4.00");
//  	writer.writeln("  SampleTimeColors	  off");
//  	writer.writeln("  LibraryLinkDisplay	  \"none\"");
//  	writer.writeln("  WideLines		  off");
//  	writer.writeln("  ShowLineDimensions	  off");
//  	writer.writeln("  ShowPortDataTypes	  off");
    }


    /** 
     *
     * <p> Writes the footer for Matlab .mat file </p>
     *
     * @param writer Instance of the MatFileWriter class
     * @throws IOException
     *
     */

    public void writeMatFooter(MatFileWriter writer)
      throws IOException
    {
	//	writer.writeln("  }");	// closes System
	//	writer.writeln("}");	// closes Model
    }

}
