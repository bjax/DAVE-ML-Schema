//
//  DAVEtoSL.java
//
//      Converts DAVE files into Simulink models.
//
// 020419 E. B. Jackson <e.b.jackson@larc.nasa.gov>

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import org.xml.sax.InputSource;

import java.util.*;

/**
 *
 * <p> Converts DAVE files into networked block &amp; signal objects. </p>
 *
 * <p> Written by <a href="mailto:e.b.jackson@larc.nasa.gov">Bruce
 * Jackson</a> of the <a href="http:dcb.larc.nasa.gov">Dynamics and
 * Control Branch</a> of <a href="http://www.nasa.gov">NASA</a>'s <a
 * href="http://larc.nasa.gov">Langley Research Center</a>. </p>
 *
 * <p> 020419: Written EBJ </p>
 *
 */

public class DAVE
{
    Model m = new Model(20, 20);

    /**
     * <p> This will take the supplied DAVE xml file, and convert that
     * file into lists of <code>Signals</code>s and <code>Block</code>s. </p>
     *
     * @param XMLFileName XML file to read and interpret.
     * @throws <code>IOException</code> - when errors occur.  */

    public boolean parse(String XMLFileName)
	throws IOException
    {
	InputStreamReader reader;
	try {
	// Get DAVE XML file
	FileInputStream input = new FileInputStream(XMLFileName);
	reader = new InputStreamReader(input);
	}
	catch (IOException e) { return false; };

	// Build XML tree
	Document doc = load(reader);
	Element root = doc.getRootElement();

	// Parse into signals and blocks

	// Find wiring info
	List variableList = root.getChildren("variableDef");
	Iterator variableIterator = variableList.iterator();
System.out.println("Found " + variableList.size() + " variable definitions.");
	while (variableIterator.hasNext())
	    new Signal( (Element) variableIterator.next(), m );

	// Find breakpoint definitions
	List bpList = root.getChildren("breakpointDef");
System.out.println("Found " + bpList.size() + " breakpoint definitions.");

	// Find function definitions
	// These automatically wire themselves to any existing BP output signals,
	// generating new breakpoint output signals if require, e.g. MACH_x_MACH1, 
        // and create their own output signal.
	List functionList = root.getChildren("function");
	Iterator functionIterator = functionList.iterator();
System.out.println("Found " + functionList.size() + " function definitions.");
	while (functionIterator.hasNext())
	    new BlockFuncTable( (Element) functionIterator.next(), m, bpList );

  	// Get all unsourced signals that are inputs to function table blocks
System.out.println("Looking for dangling inputs to function tables.");
  	SignalArrayList danglingFuncInputs = m.getSignals().findFuncInputs();

  	// Create appropriate breakpoint blocks, which wire themselves into network
System.out.println("Creating breakpoint blocks.");
  	Iterator bpInIterator = danglingFuncInputs.iterator();
  	while (bpInIterator.hasNext())
  	    new BlockBP( (Signal) bpInIterator.next(), bpList, m );

	// Allow function table blocks to discover dimensions from upstream BP blocks
System.out.println("Looping through function blocks to set dimensions.");
	Iterator blockIterator = m.getBlocks().iterator();
	while( blockIterator.hasNext())
	    {
		Block b = (Block) blockIterator.next();
		if (b instanceof BlockFuncTable) ((BlockFuncTable) b).setDimensionsFromBPs();
	    }

	// Look for dangling inputs & outputs; hook up to in/outports
System.out.println("Looping through signals, hooking up inports & outports.");
	int inports = 1;
	int outports = 1;
	Iterator signalIterator = m.getSignals().iterator();
	while (signalIterator.hasNext())
	{
	    Signal theSignal = (Signal) signalIterator.next();

	    // check for missing source
	    if (!theSignal.hasSource())
		if( theSignal.hasIC())
		    {
			Block b = new BlockMathConstant( theSignal.getIC(), m);
			b.addOutput( theSignal );
		    }
		else
		    new BlockInput( theSignal, inports++, m );

	    // check for missing destination
	    if (!theSignal.hasDest())
		new BlockOutput( theSignal, outports++, m );
	}

	// look for blocks with missing inputs & outputs, hookup if possible or gen error
System.out.println("Looping through blocks, looking for orphan inputs & outputs.");
	boolean missing;
	blockIterator = m.getBlocks().iterator();
	while( blockIterator.hasNext())
	    {
		Block b = (Block) blockIterator.next();
		missing = b.verifyInputs();
		if( missing ) 
		    System.err.println(" Block " + b.getName() + " has missing input(s).");
		missing = b.verifyOutputs();
		if( missing )
		    System.err.println(" Block " + b.getName() + " has missing output(s).");
		
	    }
	
	return true;
    }

    public Document load(InputStreamReader reader)
	throws IOException
    {
	try
	    {
		// Load XML into JDOM Document
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(reader);
		return doc;
	    }
	catch (JDOMException e)
	    {
		throw new IOException(e.getMessage());
	    }
    }

    /**
     *
     * <p> Create MDL output on output stream </p>
     *
     * @param writer Instance of the SLFileWriter class
     * @throws IOException
     *
     **/

    public void createMDL(SLFileWriter writer, MatFileWriter mWriter)
    throws IOException
    {

	// Starting with input and constant blocks, assign to column
	// 1. Follow signal path and set immediate downstream blocks
	// to minimum of column 2, etc. and recurse. When done, column
	// set up with be complete. Also can propagate row so next
	// input block starts below lowest block of previous block.

	// Get all children of input blocks
	int row = 0;	// high water mark
	Block blk = null;
	Signal sig = null;

	MDLNameSpace ns = new MDLNameSpace( m.numBlocks() );	// create namespace for blocks

System.out.println("Looping through " + m.numBlocks() + "-block list to find children, adjust names and set positions.");
	for(Iterator iBlks = m.getBlocks().iterator();
	    iBlks.hasNext();  )
	    {
		blk = (Block) iBlks.next();
		if(blk != null) blk.findChildren(" "); else System.err.println("Null block found.");
		blk.setNameSpace( ns );			// may change to acceptable SL name
		if( (blk instanceof BlockInput) ||
		    (blk instanceof BlockMathConstant) ) {
		    row = blk.setPosition(row+1, 1);
	    }
	}

	// Then need to adjust everything nice and neat.

	SLDiagram d = new SLDiagram(m);
//	d.describeSelf( System.out );

	// write out blocks to MDL file and data to Mat file

	d.createMDL( writer, mWriter );
    }



    /**
     *
     * <p> Describe ourselves </p>
     *
     **/

    public void describeSelf(String textFileName)
    {
	try {
	    TreeFileWriter writer = new TreeFileWriter(textFileName);
	    writer.describe(m);
	    writer.close();
	} catch (IOException e )
	    { return; };
    }


    /**
     * <p> This will take the supplied DAVE xml file, and convert that
     * file to a text tree representation </p>
     *
     * @param XMLFileName XML file to read and interpret.
     * @param textFileName text file to create.
     * @throws <code>IOException</code> - when errors occur.  */

    public void convert(String XMLFileName, String textFileName)
	throws IOException
    {

	// open input file & parse
	boolean success = this.parse(XMLFileName);

	if(success) this.describeSelf( textFileName );
    }

    /** 
     * <p> Provide a static entry point for running DAVE w simple list output.
     * Do simple argument count and report any error. </p>
     **/

    public static void main(String args[]) {
        try {
            if (args.length != 2) {
                System.out.println(
                    "Usage: java DAVE [XML Document URI] [Text output file]");
		System.out.println("Only had " + args.length + " argument(s)");
		for( int i = 0; i<args.length; i++)
		    System.out.println((i+1) + ": " + args[i]);
                System.exit(0);
             }
	    DAVE dave = new DAVE();
	    dave.convert(args[0],args[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
