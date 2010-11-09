// NameSpace
//
//	Creates and manages unique namespace, such as guaranteeing
//	unique block names for Simulink MDL file.
//
// 020514 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * <p> NameSpace represents a list of strings in use. It provides
 * methods to add, delete, and create unique names based on an initial
 * namespace. </p>
 *
 **/

public class NameSpace extends ArrayList
{
    public NameSpace()  {super();}
    public NameSpace(int initialCapacity) { super(initialCapacity); }

    // ensure uniqueness, so override ArrayList methods that don't
    public NameSpace(Collection c)       { throw new UnsupportedOperationException(); }
    public boolean add(Object o)         { throw new UnsupportedOperationException(); }
    public void    add(int index, Object o)  { throw new UnsupportedOperationException(); }
    public boolean addAll(Collection o)  { throw new UnsupportedOperationException(); }
    public boolean addAll(int index, Collection o)  { throw new UnsupportedOperationException(); }
    public void    addFirst(Object o)    { throw new UnsupportedOperationException(); }
    public void    addLast( Object o)    { throw new UnsupportedOperationException(); }

    /**
     *
     * <p> Changes name to meet namespace requirements, but does not ensure uniqueness </p>
     *
     * @param s <code>String</code> with proposed name
     * @return String
     *
     **/

    public String  fixName( String s) { return s; }	// returns acceptable filtered version of name


    /**
     *
     * <p> Returns <code>boolean</code> which indicates if supplied name is unique to namespace </p>
     *
     * @param s <code>String</code> with candidate name
     * @return boolean indicating if <b>s</b> is unique or not
     *
     **/

    public boolean isUnique(String s) { return (this.indexOf(s) == -1); }


    /**
     *
     * <p> Changes name to meet namespace requirements, and appends
     * integer until unique name is created. Returns acceptable,
     * unique name</p>
     *
     * @param s <code>String</code> with candidate name
     * @return String containing unique-ified <b>s</b> name
     *
     **/

    public String addUnique(String s)
    {
	String name = this.fixName(s);	// perform any unique filtering
	if (this.isUnique(s)) 
	    super.add(s);
	else
	    {
//System.out.print("-->Non-unique name " + s + " found; changed to ");
		int suffix = 1;
		while( !this.isUnique(s + suffix) ) suffix++;
		name = s + suffix;
		super.add(name);
//System.out.println(name);
	    }
	return name;
    }
}
