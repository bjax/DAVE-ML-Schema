// SignalArrayList
//
//	Extends ArrayList for Signal objects
//
// 020501 EBJ

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import org.jdom.Element;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

public class SignalArrayList extends ArrayList
{

    /**
     *
     * <p> Constructor takes no arguments </p>
     *
     **/

    public SignalArrayList() { super(); }

    /**
     *
     * <p> Constructor takes initial capacity estimate </p>
     *
     * @param initialCapacity integer estimate of required slots
     *
     **/

    public SignalArrayList( int initialCapacity ) { super(initialCapacity); }


    /**
     *
     * <p> Constructor converts existing <code>Collection</code> </p>
     *
     * @param c existing <code>Collection</code>
     *
     **/

    public SignalArrayList( Collection c ) { super(c); }

    /**
     *
     * <p> This overrides <code>ArrayList</code> next() method to return typed
     * <code>Signal</code> object </p>
     *
     **/

    public Signal next()
    {
	return (Signal) this.next();
    }

    /**
     *
     * <p> This method locates a <code>Signal</code> from a varID </p>
     *
     **/
    
    public Signal findByID( String ID )
    {
	Iterator signalIterator = this.iterator();
	while( signalIterator.hasNext() )
	    {
		Signal theSignal = (Signal) signalIterator.next();
		if (ID.equals(theSignal.getVarID())) return theSignal;
	    }
	return null;
    }

    /**
     *
     * <p> This method returns new <code>SignalArrayList</code>
     * containing just the <code>Signal</code>s that are inputs to
     * function blocks. </p>
     *
     **/

    public SignalArrayList findFuncInputs()
    {
	SignalArrayList funcInputs = new SignalArrayList( this.size() );
	BlockArrayList bal;
	Iterator i;
	
	Iterator signalIterator = this.iterator();
	while( signalIterator.hasNext() )
	    {
		Signal s = (Signal) signalIterator.next();
		bal = s.getDests();
		i = bal.iterator();
		while( i.hasNext() )
		    {
			Block b = (Block) i.next();
			if( b instanceof BlockFuncTable )
			    {
				funcInputs.add( s );
				break;
			    }
		    }
	    }
	return funcInputs;
    }
 }
