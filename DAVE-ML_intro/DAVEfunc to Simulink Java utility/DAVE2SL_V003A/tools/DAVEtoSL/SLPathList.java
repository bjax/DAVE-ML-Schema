//
// SLPathList.java
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * <p> An extension to ArrayList object, intended to carry arrays of
 *	segment lists. </p>
 *
 **/

public class SLPathList extends ArrayList
{

  public SLPathList()
  {
    super();
  }

  public SLPathList( int size )
  {
    super( size );
  }

  public boolean add( Object o )
  {
    if (!(o instanceof ArrayList))
      {
	  System.err.println("Error - adding non-ArrayList to SLPathList object");
	  return false;
      }
    else
	super.add(o);
    return true;
  }

  public void merge( SLPathList otherList )
  {
//System.out.println("Merging path list... ");
//otherList.describe();

//System.out.println("...into path list... ");
//this.describe();

    Iterator it = otherList.iterator();
    while( it.hasNext() )
      {
	this.add( it.next() );
      }

//System.out.println("Merge complete... ");
//this.describe();
//System.out.println();
  }

  public void describe()
  {
    System.out.println("...an SLPathList with " + this.size() + " paths:");
    int i = 1;
    Iterator it = this.iterator();
    while( it.hasNext() )
    {
      System.out.println(" Path " + i + ": ");
      ArrayList a = (ArrayList) it.next();
      Iterator ait = a.iterator();
      while( ait.hasNext() )
	{
	  SLLineSegment ls = (SLLineSegment) ait.next();
	  ls.describe("  ");
	}
      i++;
    }
    System.out.println();
  }
}
