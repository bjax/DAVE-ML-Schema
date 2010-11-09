//
//  TreeFileWriter.java
//
//    Provides special output functions for writing text file representations
//	of XML-derived DAVE networks.
//
//    020419 E. B. Jackson <e.b.jackson@larc.nasa.gov>
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.io.IOException;
import java.io.FileWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class TreeFileWriter extends FileWriter
{
    /**
     *
     * <p> Constructor for TreeFileWriter; derived from FileWriter
     *  but specialized to write Simulink .mdl files 
     *
     * @param fileName Name of file to open
     * @throws IOException
     *
     */

    public TreeFileWriter(String fileName) throws IOException
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
     * <p> Writes newline </p>
     *
     */

    public void writeln() throws IOException
    {
	super.write( "\n" );
    }

    /**
     *
     * <p> This method writes out contents of DAVE network </p>
     *
     **/

    public void describe( Model m ) throws IOException
    {
	this.writeln("Contents of model:");
	this.writeln();
	this.writeln("Number of signals: " + m.numSignals());
	this.writeln();

	// List signals & info
	int i = 1;
	Iterator signalIterator = m.getSignals().iterator();
	while (signalIterator.hasNext())
	    {
		write(i + " ");
		((Signal) signalIterator.next()).describeSelf( (FileWriter) this );
		this.writeln();
		i++;
	    }

	this.writeln();
	this.writeln("Number of blocks: " + m.numBlocks());
	this.writeln();

	// List blocks & info
	i = 1;
	Iterator blockIterator = m.getBlocks().iterator();
	while (blockIterator.hasNext())
	    {
		write(i + " ");
		((Block) blockIterator.next()).describeSelf( (FileWriter) this );
		this.writeln();
		i++;
	    }
    }
}
