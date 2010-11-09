// BlockArrayList
//
//	Extends ArrayList for Block objects
//
// 020501 EBJ

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import org.jdom.Element;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;

/**
 *
 * <p> The <code>BlockArrayList</code> extends the
 * <code>ArrayList</code> object to allow searching for specific types
 * of <code>Block</code>s. </p>
 *
 **/

public class BlockArrayList extends ArrayList
{

    /**
     *
     * <p> Constructor takes no arguments </p>
     *
     **/

    public BlockArrayList() { super(); }

    /**
     *
     * <p> Constructor takes initial capacity estimate </p>
     *
     * @param initialCapacity integer estimate of required slots
     *
     **/

    public BlockArrayList( int initialCapacity ) { super(initialCapacity); }


    /**
     *
     * <p> Constructor converts existing Collection </p>
     *
     * @param c existing Collection
     *
     **/

    public BlockArrayList( Collection c ) { super(c); }

}
