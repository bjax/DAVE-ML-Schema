//
// SLSignal.java
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.ListIterator;
import java.io.IOException;


/**
 *
 * <p> An extension to the DAVE Signal object that includes Simulink diagram routing information </p>
 *
 **/

public class SLSignal extends Signal
{
  SLDiagram parentDiagram;	// our parent diagram
  SLCell sourceCell;		// starting cell
  SLCell destCell;		// defined if only one destination that is adjacent
  ArrayList branches;		// temporary set of ArrayLists of SLLineSegments leading to each destination
  SLLineSegment path;		// branching tree of SLLineSegments, filled in with call to singlePath() method

  /**
   *
   * <p> Elementary constructor </p>
   *
   **/

  public SLSignal()
  {
    super();
    //System.out.println("SLSignal basic constructor called.");
    parentDiagram = null;
    sourceCell = null;
    branches = new ArrayList(10);
    path = null;
  }


  /**
   *
   * <p> Expanding copy constructor </p>
   *
   * @param oldSignal Signal to convert to SLSignal
   * @param theParentDiagram The Diagram we belong to
   *
   **/

  public SLSignal( Signal oldSignal, SLDiagram theParentDiagram )
  {
    this();
    //System.out.println("SLSignal copy constructor called with old signal " + oldSignal.getName());    
    this.parentDiagram = theParentDiagram;

    // copy over most fields
    this.ourModel = oldSignal.ourModel;
    this.myName = oldSignal.getName();
    this.myVarID = oldSignal.getVarID();
    this.myUnits = oldSignal.getUnits();
    this.source = oldSignal.getSource();
    this.sourcePort = oldSignal.getSourcePort();
    if(this.source != null)
      this.sourceCell = this.parentDiagram.getCell( this.source );
    else
      System.out.println("null source for signal " + this.getName());

    if(oldSignal.dests != null )
      {
	//System.out.println("Copying dests from old signal " + oldSignal.getName() + " to new SLSignal");
	this.dests = oldSignal.getDests();
	this.destPorts = oldSignal.getDestPortNumbers();
	this.hasIC = oldSignal.hasIC;
	this.IC = oldSignal.IC;
      }
    else
      System.err.println("Null dests found in signal " + getName() );
  }


  /**
   *
   * <p> Returns parent diagram of signal </p>
   *
   **/

  public SLDiagram getDiagram() { return this.parentDiagram; }

    
  /**
   *
   * <p> Creates routing information from diagram &amp; dest
   * info. This method creates separate paths from source block to
   * destination block with overlapping lines.</p>
   *
   **/

  public void initialRoute( )
  {
    //System.out.println("routing signal " + getName() + "...");
    //if(parentSignal != null) System.out.println("this is a branch");
    // set source row & column
    int sourceRow = this.sourceCell.getRowIndex();
    int sourceCol = this.sourceCell.getColIndex();
	
    // if we have only a single destination, don't use branches 
    if( this.dests != null )
      {
	if ( this.dests.size() == 1 )
	  { 	// only one sink - record our destination
	    Block destBlock = (Block) this.dests.get(0);
	    Integer destPort = (Integer) this.destPorts.get(0);
	    SLCell destination = parentDiagram.getCell( destBlock );
	    int destRow = destination.getRowIndex();
	    int destCol = destination.getColIndex();
	    if ( ( destRow != sourceRow) || ( destCol != ( sourceCol + 1 ) ) )
	      {	// add routing if not adjacent - otherwise, no routing needed.
		//		System.out.println(" Signal " + this.getName() + " joins two non-adjacent blocks.");
		this.pathTo(destination, destPort);	// adds single branch element
	      }
	    else
	      this.destCell = destination; 		// destination cell is immediately adjacent to source cell
	  }
	else	// more than one destination block - create branches
	  {
	    // figure out temporary individual wiring, destination by destination
	    Iterator id = this.dests.iterator();
	    Iterator ip = this.destPorts.iterator();
	    while (id.hasNext())
	      {
		Integer destPort = (Integer) ip.next();
		Block destBlock = (Block) id.next();
		SLCell destination = parentDiagram.getCell( destBlock );
		//System.out.println("setting path for signal " + getName() + " from block " 
		//   + this.sourceCell.getBlock().getName() + " to block " + destBlock.getName() 
		//   + " with destPort " + destPort);
		this.pathTo(destination, destPort);	// adds elements to branch list
	      }
	  }
      }
    else
      System.err.println("Null destination found in signal " + getName() );
  }


  /**
   *
   * <p> Generates end-to-end path to single destination; adds this
   * path to SLSignal's branch ArrayList. </p>
   *
   **/

  public void pathTo( SLCell destination, Integer destinationPort )
  {
    int sourceRow = this.sourceCell.getRowIndex();
    int sourceCol = this.sourceCell.getColIndex();
    int destRow   = destination.getRowIndex();
    int destCol   = destination.getColIndex();

    int destPort  = destinationPort.intValue();

    ArrayList newBranch = new ArrayList(10);
    this.branches.add(newBranch);

    System.out.println("singlePath called to route signal " + this.getName() + 
		       " from [" + sourceRow + "," + sourceCol + "] to [" +
		       destRow + "," + destCol + "]. ");

    // if adjacent cols, no need to store row & second col
    if ((destination.getColIndex() - this.sourceCell.getColIndex()) > 1 ) 
      {
	// create initial vertical line
	newBranch.add(  new SLLineSegment( this, sourceCol, sourceRow, destRow, 1, 0 ));

	// not adjacent columns; create connecting horizontal ...
	newBranch.add( new SLLineSegment( this, destRow, sourceCol, destCol ));

	// ...and final vertical segment to destination cell
	//System.out.println("setting line segment from signal " + getName() + " with destPort = " + destPort);
	SLLineSegment ls = new SLLineSegment( this, destCol, destRow, destRow, 0, destPort );
	ls.setDestCell( destination );	// add destination
	newBranch.add( ls );
      }
    else
      {
	// source and destination columns are adjacent; create single vertical connector
	SLLineSegment ls = new SLLineSegment( this, sourceCol, sourceRow, destRow, 1, destPort );
	ls.setDestCell( destination );
	newBranch.add( ls );
      }
  }



  /**
   *
   * <p> Replaces (and removes) the individual paths found in the
   * "branches" field and generates an equivalent branching tree
   * structure referenced by the "path" field.  This tree structure is
   * composed of <code>SLLineSegment</code>s containing their own
   * "branches" to other <code>SLLineSegment</code> leading to
   * destination; the trunk of this tree is the
   * <code>SLLineSegment</code> found in "path". </p>
   *
   **/

  public void makeBranchingRoute()
  {
    // if non-adjacent and has an output signal, must route
    if( this.branches.size() > 0 )	// equiv to destCell == null
      {
	// In following, 'ib' is an iterator through the branches
	// ArrayList. Each element of 'branches' is an ArrayList of
	// SLLineSegments; each one contains a set of SLLineSegments
	// leading from the source cell to a destination cell.

	// 'numPaths' is the total number of destination cells

	// 'pathItList' is SLPathList, one for each path to a
	// destination. As branches are encountered, this list is
	// split into separate lists (during findBranchingPath method
	// recursion). As destinations are reached, the 'pathItList'
	// is trimmed.

	SLPathList pathList = new SLPathList( this.branches.size() );
	Iterator ib = this.branches.iterator();

	// build array (pathItList) of path iterators

	while( ib.hasNext() )
	  {
	    // for each destination, get the ArrayList object and
	    // store in 'pathItList'.
	    ArrayList al = (ArrayList) ib.next();
	    pathList.add( al );
	  }

	// At this point, 'pathItList' contains an array of array
	// lists of SLLineSegments; each list describes path from
	// present common position to separate destinations. Now need
	// to build single tree of SLLineSegments with branches to
	// reduce the common paths to trunks and branches.

	System.out.println();
	System.out.println("initial route is defined by...");
	pathList.describe();

	// construct path to point of separation

	this.path = this.findBranchingPath( pathList );
	
      }
    System.out.println("\nRemoving old individual branches...");
    this.branches = null;
    System.out.println("\nNew branching path:\n");
    this.describeBranches();
  }


  /**
   *
   * <p> This method returns branching tree structure of
   * SLLineSegments that combines any common paths of the indicated
   * separate paths which are assumed to begin from the same
   * location. </p>
   *
   * @param pathList <code>SLPathList</code> contains an
   *                   <code>ArrayList</code> of
   *                   <code>ArrayList</code>s containing
   *                   <code>SLLineSegment</code>s that lead from a
   *                   common point to separate destinations
   *
   * @return <code>SLLineSegment</code> that is root of equivalent branching tree
   *
   **/

  public SLLineSegment findBranchingPath( SLPathList pathList )
  { 

    // This recursive method returns root of a branching tree that
    // follows each path defined by the pathList array

    System.out.println("\nfindBranchingPath called with ... ");

    pathList.describe();

    int numPaths = pathList.size();

    if( numPaths == 0 )
      System.err.println("Error - findBranchingPoint called with no paths");

    // get first path's first SLLineSegment
    Iterator ipl = (Iterator) pathList.iterator();
    if (ipl == null)          System.err.println("Error - findBranchingPoint found null iterator");
    if (!ipl.hasNext())       System.err.println("Error - findBranchingPoint found empty path list");
    ArrayList currentPath = (ArrayList) ipl.next();
    if (currentPath == null)  System.err.println("Error - FindBranchingPoint found empty array");
    ListIterator lisl = currentPath.listIterator();
    if (!lisl.hasNext())      System.err.println("Error - findBranchingPoint found empty segment list");
    SLLineSegment ls = (SLLineSegment) lisl.next();
    if (ls == null)           System.err.println("Error - findBranchingPoint found empty line segment");

    if( numPaths == 1 )
      {
	System.out.println(" handling single path:");
	ls.describe();
	int destPort = ls.getDestPort();
	if (destPort == 0)
	  {
	    lisl.remove();	// remove first segment from path list
	    System.out.println("  which does not have a destination block; adding to path & recursing");
	    ls.setNextSeg( findBranchingPath( pathList ) );
	  }
	else
	  {
	    System.out.println("  which has a destination block; adding to path without recursion");
	  }
	return ls;
      }

    // This path taken only if more signal has more than one destination

    System.out.println(" handling " + pathList.size() + " paths");	
    boolean mustBranch = false;

    // Bug - should look for case where lines go both directions, and branch at 0 offset
    // for now, use abs value for nearest

    SLLineSegment nearest = null;	// will record segment with smallest length
    ArrayList nearestPath = null;	// will record path with shortest segment
    Hashtable table = null;		// table to store different segment lengths

    // record it as current shortest path
    nearest = ls;	// by default, this is the one to compare to others
    nearestPath = currentPath;	// path with shortest first segment

    // record parameters to compare with others
    boolean horiz = ls.isHoriz();
    int tray      = ls.getTray();
    int offset    = ls.getTrayOffset();
    int start     = ls.getStart();
    int sourcePort = ls.getSourcePort();
    int length    = ls.calcLength( );	// returns vertical distance for leg

    System.out.println("  Initial segment length is " + length );

    while( ipl.hasNext() )	// loop through each remaining viable path
      {
	currentPath = (ArrayList) ipl.next();
	lisl = currentPath.listIterator();
	if ( lisl.hasNext() )
	  {
	    ls = (SLLineSegment) lisl.next();	// next candidate line segment

	    // check correctness of info - starting point should match original segment
	    if (ls.isHoriz()       != horiz     ) System.err.println("Mismatched orientation!");
	    if (ls.getStart()      != start     ) System.err.println("Source row or cols don't match!");
	    if (ls.getSourcePort() != sourcePort) System.err.println("Source ports don't match!");
	    if (ls.getTray()       != tray      ) System.err.println("Vertical column tray numbers don't match!");
	    if (ls.getTrayOffset() != offset    ) System.err.println("Column offsets don't match!");

	    // decide on what path to write
	    int newLength = ls.calcLength( );
	    System.out.print("    next segment has length " + newLength + "; ");
	    if( newLength != length ) 
	      {
		mustBranch = true;
		System.out.println("must branch!");

		// if not existing, create hashtable and add both elements

		if( table == null )
		  {
		    table = new Hashtable( 20 );
		    SLPathList a = new SLPathList( 5 );
		    a.add( nearestPath );
		    table.put( new Integer( length ), a );
		  }

		SLPathList a = null;

		// see if we already have this length represented
		Integer key = new Integer( newLength );
		if( table.containsKey( key ) )
		  // if so, get its array and add the new segment to the list
		  a = (SLPathList) table.get( key );
		else
		  // create new object to add to table since this length is not found
		  a = new SLPathList( 5 );

		// add this path's iterator to list of segments with this length
		a.add( currentPath );
		table.put( key, a );

		// figure out which is nearest path - is new row
		// higher (smaller number) than current choice?

		// BUG - should handle case of two directions by
		// branching from 0 offset!

//need to check for both signs in results, and insert a zero-length
//segment as placeholder and signal to createMDL later to start a
//branch immediately behind outport.

		if (Math.abs(newLength) < Math.abs(length))
		  {
		    nearest = ls;
		    nearestPath = currentPath;
		    length = newLength;
		  }
	      }
	    else
	      System.out.println("no branch needed.");
	  }
      }

    System.out.print("\nNearest path is ");
    nearest.describe(" ");

    // if no branch is necessary, daisy chain next segment

    if(!mustBranch)
      {
	// nearest points to first segment of shared path; remove it
	// and all duplicates from pathList
	ipl = pathList.iterator();	// rewind iterator
	while( ipl.hasNext() )
	  {
	    currentPath = (ArrayList) ipl.next();
	    lisl = currentPath.listIterator();
	    lisl.next();	// point to first segment...
	    lisl.remove();	// ...and remove it from path
	    if(!(lisl.hasNext()))	// if no more segments exist...
	      ipl.remove();	// ... remove whole path from list
	  }
	System.out.println("  No branch found, recursing...");
	nearest.setNextSeg( findBranchingPath( pathList ) );
      }
    else	// must branch
      {
	System.out.println("  must branch - Map has " + table.size() + " keys.");

	// Group paths into two SLPathLists: the ones that branch or
	// terminate at the shortest path, and the ones that go
	// farther...

	SLPathList newPathList = new SLPathList( 10 );
	Collection values = table.values();	// get set of ArrayLists of branches
	Iterator iv = values.iterator();	// ...with same length segment
	while (iv.hasNext())
	  {
	    SLPathList nextPathList = (SLPathList) iv.next();	// retrieve paths with same initial length segment
	    Iterator it = nextPathList.iterator();
	    ArrayList nextPath = (ArrayList) it.next();

	    if (nextPath == nearestPath)
	      {				// found bunch that share shortest first segment
		// remove (common) first paths from each path in this set
		Iterator it1 = nextPath.iterator();
		SLLineSegment deadLS = (SLLineSegment) it1.next();
		System.out.println("Removing:");
		deadLS.describe();
		it1.remove();	// yank first element
		if(!( it1.hasNext() ))
		  {	// must remove this path completely
		    System.out.println("[removing empty path]");
		    it.remove();
		  }
		while( it.hasNext() )
		  {
		    ArrayList parallelPath = (ArrayList) it.next();
		    it1 = parallelPath.iterator();
		    deadLS = (SLLineSegment) it1.next();
		    System.out.println("Also removing:");
		    deadLS.describe();
		    it1.remove();	// yank first identical elements
		    if(!( it1.hasNext() ))
		      {	// must remove this path completely
			System.out.println("[removing empty path]");
			it.remove();
		      }
		  }
		System.out.print("Found shortest path; ");
		if( nextPathList.size() > 0 )
		  {
		    System.out.println("recursing...");
		    nearest.addBranch( findBranchingPath( nextPathList ) );
		  }
		else
		  {
		    System.out.println("no paths remaining with shortest length.\n");
		  }
	      }
	    else
	      {				// add to non-shortest-first-segment paths
		System.out.println("Found path that is NOT shortest; adding to next bunch...\n");
		newPathList.merge( nextPathList );
	      }
	  }

	// Remaining group, newPathList, needs to have each first
	// segment shortened so that they start at the branch point

	Iterator it = newPathList.iterator();		// iterator into path segments
	while( it.hasNext() )
	  {
	    ArrayList path = (ArrayList) it.next();
	    lisl = (ListIterator) path.listIterator();
	    ls = (SLLineSegment) lisl.next();		// get first segment
	    ls.subtract( nearest );			// shorten segment so it starts at branch
	  }

	nearest.addBranch( findBranchingPath( newPathList ));		// create new branching path for shortened paths

      }	// end of branching-required path
    return nearest;
  }



  /**
   *
   * <p> Obtains tray assignments for lines </p>
   *
   **/

  public void assignTrays()
  {
  }



  /**
   * 
   * <p> Creates text description of all branches </p>
   *
   * <p> An <code>SLSignal</code> has two possible descriptions of
   * routing information: on the first pass (by calling the
   * initialRoute method) the "branches" <code>ArrayList</code> field
   * will be filled out with individual, non-branching paths from the
   * output port to each destination. Following the second pass (by
   * calling the makeBranchingRoute method) the branches are merged
   * into a singla branching tree structure starting with the
   * <code>SLLineSegment</code> found in the "path" field. </p>
   *
   **/

  public void describeBranches()
  {
    // write header information

    System.out.println();

    if (this.path == null)	// use old branching information
      {
	if (this.branches == null)
	  {
	    System.err.println("No branching information found in signal '" + this.getName() + "'!");
	    return;
	  }
	else
	  {
	    System.out.print("  Signal '" + getName() );
	    if (this.branches.size() <= 1)
	      {
		System.out.println("' has one path: from block '" + this.sourceCell.getBlock().getName() +
				   "' at location [" + this.sourceCell.getRowIndex() + "," +
				   this.sourceCell.getColIndex() + "] ");
		if (this.destCell != null)
		  System.out.println("   to adjacent block '" + this.destCell.getBlock().getName() +
				     "' at location [" + this.destCell.getRowIndex() + "," +
				     this.destCell.getColIndex() + "]");
	      }
	    else
	      {
		System.out.println("' has " + this.branches.size() + " branches: from block '" + this.sourceCell.getBlock().getName() +
				   "' at location [" + this.sourceCell.getRowIndex() + "," +
				   this.sourceCell.getColIndex() + "] ");
	      }

	    // write routing information

	    Iterator ib = this.branches.iterator();
	    while (ib.hasNext())
	      {
		SLLineSegment ls = null;
		ArrayList list = (ArrayList) ib.next();
		Iterator iline = list.iterator();

		while (iline.hasNext())			// scan ahead...
		  ls = (SLLineSegment) iline.next();	// to find destination

		SLCell destination = ls.getDestCell();
		System.out.println("   to block '"   + destination.getBlock().getName() +
				   "' at location [" + destination.getRowIndex() + "," +
				   destination.getColIndex() + "] via");

		iline = list.iterator();	// start over at beginning of path
		boolean first = true;
		while (iline.hasNext())
		  {
		    if(first)
		      {
			first = false;
			System.out.print("     ");
		      }
		    else
		      System.out.print("     then ");

		    ls = (SLLineSegment) iline.next();

		    ls.describe();
		  }
	      }
	  }
      }	// end of null "path" field
    else
      {	// use branching structure found in "path" field
	this.path.describe();
      }
  }


  /**
   * 
   * <p> Creates text description of path (branching tree) </p>
   *
   **/

  public void describePath()
  {
    System.out.println();
    System.out.print("  Signal '" + getName() + "' runs from block '" + this.sourceCell.getBlock().getName() +
		     "' at location [" + this.sourceCell.getRowIndex() + "," +
		     this.sourceCell.getColIndex() + "] ");
    if(path == null)
      {
	System.out.println(" to adjacent block '" + this.destCell.getBlock().getName() + 
			   "' at location [" + this.destCell.getRowIndex() + "," +
			   this.destCell.getColIndex() + "]. ");
      }
    else
      {
	System.out.println(" to " + this.dests.size() + " different destination(s) as follows: ");
	this.path.describe("    ");
      }
  }


  /**
   *
   * <p> Writes signal wiring for Simulink representation
   *
   * @param writer Instance of the SLFileWriter class
   * @throws IOException
   *
   */

  public void createMDL(SLFileWriter writer) throws IOException
  {
    // Write header

    writer.writeln("    Line {");
    writer.writeln("      Name                    \"" + this.getName()  + "\"");
    writer.writeln("      Labels                  [0, 0]");
    writer.writeln("      SrcBlock		      \"" + source.getName() + "\"");
    writer.writeln("      SrcPort		      " + sourcePort);

    // Describe path. If adjacent, skip wiring and branching

    if(this.path == null)
      {
	// since no path defined, must have only one destination
	Block destBlock = (Block) this.dests.get(0);
	if (destBlock == null) 
	  {
	    System.err.println("Error - in SLSignal.createMDL, found not path and no destination block for signal '" + this.getName() + "'!");
	    return;
	  }
	Integer destPort = (Integer) this.destPorts.get(0);
        writer.writeln("      DstBlock		\"" + 
		       destBlock.getName() + "\"");
        writer.writeln("      DstPort			" + destPort.intValue() );
      }
    
    // Not adjacent - create branches and points

    else
      {
	// start points description - finished by next segment
	writer.write("      Points			[" + sourceCell.distToEdge() + ", 0");
	this.path.createMDL(writer, "	", true);	// send tab as first indent
      }

    // Write footer

    writer.writeln("    }");
  }	// end of createMDL

}
