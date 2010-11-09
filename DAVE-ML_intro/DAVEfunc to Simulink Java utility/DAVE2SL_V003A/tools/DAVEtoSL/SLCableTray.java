//
// SLCableTray.java
//
//	020621 E. B. Jackson <e.b.jackson@larc.nasa.gov>
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;

    /**
     *
     * <p> Extends ArrayList for new add capability, where
     * duplicates are not added </p>
     *
     **/

public class SLCableTray extends ArrayList
{

    static int offset = 5;	// line offset for each item in tray

    public SLCableTray()
    {
	super();
    }

    public SLCableTray( int count )
    {
	super( count );
    }

    public boolean add(Object theObject)
    {
	if( !this.contains( theObject ) )
	    return super.add( theObject );
	return false;
    }

    public int getSize()
    {
	if( this.size() > 0)
	    return (this.size()-1)*offset;
	else
	    return 0;
    }

    public int getStandoff( SLSignal theSignal )
    {
	return this.indexOf( theSignal )*offset;
    }
}
