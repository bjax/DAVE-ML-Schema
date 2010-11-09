// Signal
//
// 020422 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;

/**
 * <p>	Object representing each "variable" in algorithm </p>
 *
 * <p>	There should be one of these for each signal line going
 *	between blocks in a model. Each signal has a parent block &
 *	port and one or more child blocks & ports - in essence, a
 *	network.  </p>
 *
 **/

public class Signal
{
    Model ourModel;		// our model parent
    String myName;	// name of signal
    String myVarID;	// variable name
    String myUnits;	// units of signal
    Block source;	// source block for signal
    int sourcePort;	// port number on source block (1-based)
    BlockArrayList dests;	// list of downstream blocks
    ArrayList destPorts; // list of port numbers (1-based) on output blocks
    boolean hasIC;	// flag to indicate an initial value has been specified
    String IC;		// initial value of signal (e.g. for constants)


    /**
     * <p> Elementary constructor </p>
     **/

    public Signal()
    {
	ourModel = null;
	myName =  "";
	myVarID = "";
	myUnits = "";
	source = null;
	sourcePort = 0;
	dests = null;
	destPorts = null;
	hasIC = false;
	IC = "";
    }


    /**
     *
     * <p> Simple constructor </p>
     *
     * @param signalName A <code>String</code> containing the name of
     * the signal to construct
     * @param m <code>Model</code> we're part of
     *
     **/

    public Signal(String signalName, Model m)
    {
	this( signalName, signalName, "unkn", 1, m);
    }


    /**
     * 
     * <p> Explicit constructor to create from <code>Signal</code>scratch </p>
     *
     * @param signalName Name of signal - does not have to be unique
     * @param varID ID of signal - must be unique
     * @param units Unit of measure of signal (e.g. deg/sec)
     * @param numConnects How many connections it goes to (guess)
     * @param m <code>Model</code> we're part of
     *
     **/

    public Signal(String signalName, String varID, String units, int numConnects, Model m)
    {
	this();
	ourModel = m;
	this.setName(signalName);
	this.setVarID(varID);
	this.setUnits(units);
	dests = new BlockArrayList(numConnects);
	destPorts = new ArrayList(numConnects);
//System.out.println("  Signal constructor: " + myName + "(" + units + ")");
	m.add(this);
    }

    /**
     *
     * <p> Builds <code>Signal</code> from org.jdom.Element </p>
     *
     * @param signalElement jdom.org.Element description of a signal
     * element; if an &lt;apply&gt; element, generate network of
     * signals and blocks corresponding to the provided algorithm.
     *
     * @param m <code>Model</code> to which new blocks and signals
     * belong. Any new blocks or signals created should be
     * <b>add</b>ed to <code>Model</code> as they are created,
     * including <b>this</b> object.
     *
     **/

    public Signal(Element signalElement, Model m)
    {
	this("", m);
	if(signalElement.getName().equals("apply"))
	    {
		Block b = BlockMath.factory(signalElement, m);
		if(b != null)
		    {
			this.setName(b.getName());
			b.addOutput(this);
		    }
		else
		    System.err.println("Null block returned.");
	    }
	else
	    {
		this.setName(  signalElement.getAttributeValue("name" ) );
		this.setVarID( signalElement.getAttributeValue("varID") );
		this.setUnits( signalElement.getAttributeValue("units") );

		// Record the initial condition, if any

		Attribute theIC = signalElement.getAttribute("initialValue");
		if( theIC != null )
		    {
			this.hasIC = true;
			this.IC = theIC.getValue();
		    }

		// Search for calculation/math element

		Namespace mathml = Namespace.getNamespace("", "http://www.w3.org/1998/Math/MathML");
		Element calc = signalElement.getChild("calculation");
		if (calc != null)
		    {
//System.out.println("Calculation element found in signal " + myName + "...");
			Element math = calc.getChild("math", mathml);

			if (math != null)
			    {
//System.out.println("...it appears to be valid math!");
				Element apply = math.getChild("apply", mathml);
				if( apply != null)
				    {
//System.out.println("...with an apply element");
					Block outBlock = this.handleApply( apply, m );
				    }
				else
				    System.err.println("Invalid math element in signal " + myName + 
						       "... no <apply> element found.");
			    }
			else
			    System.err.println("Invalid calculation element in signal " + myName + 
					       "... no <math> element found.");
		    }
	    }
    }



    /**
     * <p> Recursive function that builds math element networks </p>
     *
     * <p> This method returns the last block of a perhaps
     *     extensive network of blocks constructed in accordance with
     *     a calculation element. It builds and connects blocks and
     *	   signals as required to complete the specified calculation. 
     *     Implicit signals (variables not named in &lt;ci&gt;
     *     elements) are created and attached to necessary
     *     blocks. Explicit inputs (those called out in &lt;ci&gt;
     *     blocks) are simply named as block inputs to be hooked up
     *     later. 
     *
     * @param applyElement JDOM <code>Element</code> containing the
     *	   apply description.
     * @return Block that represents the output of the calculation.
     *
     **/

    private Block handleApply( Element applyElement, Model m )
    {
	Block b = BlockMath.factory(applyElement, m);
	if(b == null)
	    System.err.println("Null block returned.");
	else
	    {
		b.addOutput( this, 1 );
	    }
	return b;
    }


    /**
     * <p> Set the name of the signal </p>
     *
     * @param theName <code>String</code> to use for name
     *
     **/

    private void setName( String theName ) { this.myName = theName; }
//System.out.println("Signal name set to " + this.getName()); }

    
    /**
     *
     * <p> Set the units of the signal </p>
     *
     * @param theUnits <code>String</code> to use for name
     *
     **/

    private void setUnits( String theUnits ) { this.myUnits = theUnits; }
//System.out.println("Signal " + this.getName() + " units set to (" + this.getUnits() + ")");}



    /**
     *
     * <p> Set the unique varID of the signal </p>
     *
     * @param theVarID <code>String</code> to use for varID
     *
     **/

    private void setVarID( String theVarID ) { this.myVarID = theVarID; }



    /**
     *
     * <p> Connect to upstream source block </p>
     *
     * <p> By convention, upstream block is responsible for recording
     *     this connection for itself. </p>
     *
     * @param sourceBlock the source block
     * @param portNum the source block's port number (1-based)
     *
     **/

    public void addSource(Block sourceBlock, int portNum)
    {
//System.out.println("  Signal " + myName + " is adding source block " + sourceBlock.getName());
	source = sourceBlock;
	sourcePort = portNum;
    }

    /**
     *
     * <p> Connect to downstream block </p>
     *
     * <p> By convention, the upstream element (this signal) alerts
     *     downstream block of the new connection. </p>
     *
     * @param sinkBlock the downstream block to add
     * @param portNum the port number on the downstream block (1-based)
     *
     **/

    public void addSink(Block sinkBlock, int portNum)
    {
//System.out.println("  Signal " + myName + " is adding sink block " + sinkBlock.getName());
	dests.add( sinkBlock );
	destPorts.add( new Integer(portNum) );
	sinkBlock.addInput( this, portNum );
    }

    /**
     *
     * <p> Returns output block array list </p>
     *
     **/

    public BlockArrayList getDests()
    {
	return this.dests;
    }

    /**
     *
     * <p> Returns <code>ArrayList</code> with the port numbers of the associated
     *     output blocks </p>
     *
     **/

    public ArrayList getDestPortNumbers()
    {
	return this.destPorts;
    }

    /**
     *
     * <p> Returns signal name as <code>String</code> </p>
     *
     **/

    public String getName() { return myName; }

    /**
     *
     * <p> Returns variable name as <code>String</code> </p>
     *
     **/

    public String getVarID() { return myVarID; }

    /**
     *
     * <p> Returns units as <code>String</code> </p>
     *
     **/

    public String getUnits() { return myUnits; }

    /**
     *
     * <p> Returns source block </p>
     *
     **/
    
    public Block getSource() { return source; }


    /**
     *
     * <p> Returns source (1-based) port number </p>
     *
     **/
    
    public int getSourcePort() { return sourcePort; }


    /**
     *
     * <p> Returns true if we have are connected to an upstream block </p>
     *
     **/

    public boolean hasSource() { return (source != null); }


    /**
     *
     * <p> Returns true if we have are connected to any downstream block </p>
     *
     **/

    public boolean hasDest() { return (!dests.isEmpty()); }


    /**
     *
     * <p> Returns true if IC value specified for this variable </p>
     *
     **/

    public boolean hasIC() { return this.hasIC; }

    
    /**
     *
     * <p> Returns text value of initial condition </p>
     *
     **/

    public String getIC() { return this.IC; }


    /**
     *
     * <p> Generates brief description on output stream </p>
     *
     * @param writer FileWriter to use
     * @throws <code>IOException</code>
     *
     **/
    
    public void describeSelf( FileWriter writer ) throws IOException
    {

	int numDests = dests.size();
	Block outputBlock;
	Integer outputBPort;
	int thePortNum;

	writer.write("Signal \"" + myName + "\" (" + myUnits + ") [" + myVarID + "] connects ");

	if (source == null)
	    writer.write("NO SOURCE BLOCK to ");
	else
	    {
		writer.write("outport " + sourcePort + " of block ");
		writer.write(source.getName() + " to " );
	    }

	switch (numDests)
	    {
	    case 0:
		writer.write("NO SINK BLOCKS.");
		break;
	    case 1:
		thePortNum = ((Integer) destPorts.get(0)).intValue();
		writer.write("inport " + thePortNum + " of block ");
		outputBlock = (Block) dests.get(0);
		writer.write(outputBlock.getName() + ".");
		break;
	    default:
		writer.write("the following " + numDests + " blocks:\n");
		Iterator outBlockIterator = dests.iterator();
		Iterator outBPortIterator = destPorts.iterator();
		while ( outBlockIterator.hasNext() )
		    {
			outputBlock = (Block) outBlockIterator.next();
			outputBPort = (Integer) outBPortIterator.next();
			thePortNum = outputBPort.intValue();
			writer.write("   inport " + thePortNum + " of block " + outputBlock.getName() );
			if (outBlockIterator.hasNext()) writer.write("\n");
		    }
	    }
    }
}
