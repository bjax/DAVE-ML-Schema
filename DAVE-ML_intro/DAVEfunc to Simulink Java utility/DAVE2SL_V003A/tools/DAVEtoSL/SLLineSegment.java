//
// SLLineSegment.java
//
//	020621 E. B. Jackson <e.b.jackson@larc.nasa.gov>


package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;

/**
 *
 * <p> Contains span information for line segments. </p>
 *
 * <p> If horizontal segment, entries are horizontal tray number,
 * starting column, ending column </p>
 *
 * <p> If a vertical segment, entries are vertical tray number,
 * starting row, ending row, and source/dest port numbers. Port 0 indicates uninitialized. </p>
 *
 **/

public class SLLineSegment
{
  SLSignal parentSignal;
  boolean isHorizSeg;
  int[] coord;
  int trayOffset;
  SLCell destCell;	// defined if only one destination
  SLLineSegment nextSeg;// defined if only one path
  ArrayList branches;	// defined if branches required; elements are SLLineSegments leading to destinations 

  public SLLineSegment() 
  { 
    parentSignal = null; 
    isHorizSeg = false; 
    coord = new int[5]; 
    trayOffset = 0;
    destCell  = null;
    nextSeg = null;
    branches = new ArrayList(5);
  }

  public SLLineSegment(SLSignal signal, int hTray, int start, int end)
  {
    // horizontal segment along row 'hTray' beginning at column 'start' to column 'end' (0-based)
    this();
    isHorizSeg = true;
    parentSignal = signal;
    coord[0] = hTray;
    coord[1] = start;
    coord[2] = end;
    coord[3] = 0;	// not used for rows
    coord[4] = 0;	// not used for rows
  }

  public SLLineSegment(SLSignal signal, int vTray, int start, int end, 
		       int startPortNumber, int destPortNumber )
  {
    // vertical segment along column 'vTray' beginning at row 'start' to row 'end' (0-based)
    this();
    parentSignal = signal;
    coord[0] = vTray;
    coord[1] = start;
    coord[2] = end;
    coord[3] = startPortNumber;
    coord[4] = destPortNumber;
  }

  public boolean isHoriz() { return isHorizSeg; }
  public boolean isVert() { return !isHorizSeg; }

  public void setDestPort( int thePort ) { coord[4] = thePort; }
  public void setDestCell( SLCell theDest ) { this.destCell = theDest; }
  public void setNextSeg( SLLineSegment next ) { this.nextSeg = next; }

  public void setTrayOffset()
  {
    // ask for offset from appropriate row or column
    // by passing it our parent as unique identifier
  }

  private void setStart(int start) { coord[1] = start; }
  private void setSourcePort(int port) { coord[3] = port; }

  public int getTray() { return coord[0]; }
  public int getTrayOffset() { return trayOffset; }
  public int getStart() { return coord[1]; }
  public int getEnd() { return coord[2]; }
  public int getSourcePort() { return coord[3]; }
  public int getDestPort() { return coord[4]; }
  public ArrayList getBranches() { return branches; }

  public int numBranches() { return branches.size(); }
  public void addBranch( SLLineSegment newBranch )
  {
    this.branches.add( newBranch );
  }

  public SLCell getDestCell() { return this.destCell; }

  /**
   * <p> Shorten our segment so that it starts from the end of the supplied segment </p>
   *
   * @param predecessor  SLLineSegment that will lead to us in a branch
   *
   **/

  public void subtract( SLLineSegment predecessor )
  {
//System.out.println("Shortening a segment...");

    // make sure we start at the same location, otherwise we'll have errro
    if ( (predecessor.isHoriz()       != this.isHoriz()       ) ||
	 (predecessor.getStart()      != this.getStart()      ) ||
	 (predecessor.getSourcePort() != this.getSourcePort() ) ||
	 (predecessor.getTray()       != this.getTray()       ) ||
	 (predecessor.getTrayOffset() != this.getTrayOffset() ) )
      System.err.println(" Line segment subtraction attempted for discontinuous segments!");
    else
      {
	this.describe("");
	// if dest port is defined in predecessor, means input to next column - adjust
	this.setStart( predecessor.getEnd() );
	this.setSourcePort( predecessor.getDestPort() );
	this.describe("");
//System.out.println("   ... line shortened.");
      }
    return;
  }


  /**
   *
   * <p> Returns the vertical or horizontal distance of a given line
   *    segment.  If the vertical segment runs downward, returned
   *    length is positive. Leftward running horizontal paths are not
   *    supported. </p>
   *
   **/

  public int calcLength()
  {

    SLSignal sig = this.parentSignal;
    SLDiagram d = sig.getDiagram();

    int start   = this.getStart();
    int end     = this.getEnd();
    int p	= this.getSourcePort();
    int q	= this.getDestPort();
    
    int length  = 0;


    if (this.isVert())
      {
	//
	//	COLUMN
	//
//insert test here to see if starting and ending pts
//identical, if so return 0 (placeholder for + and - branches)

//System.out.println("   calculating length of vertical segment beside column " + this.getTray());
	// figure direction
	int dir = 1;
	if (end < start)	// these are row numbers; + down, 0-based
	  dir = -1;
	if (end == start)
	  {
	    if (( p == 0 ) && ( q > 0 )) dir = -1; // going from tray up to port
	    if (( q == 0 ) && ( p > 0 )) dir =  1; // going from port down to tray
	    if (( p > 0) && (q > 0 ))	// both ports - shouldn't happen...
	      if ( q > p ) dir = -1; // going up from start port to finish port
	      else dir = 1;
	  }
					 
//System.out.println(" with direction " + dir + " from row " + start + " to row " + end);
	// add up width of intervening rows, not including start or finish row
	for( int i = start+dir; i*dir <= dir*(end-dir); i=i+dir )
	  {
//System.out.print("   Row " + i + "  ");
	    length = length + ((SLRowColumnVector) d.getRow(i)).getSize();
	  }

	boolean adjacent = ( (end == start) && (p != 0) && (q != 0) );
	if( !adjacent  )	// skip if input and output blocks are adjacent - length 0.
	  {
	    // since is a vertical segment, must account for port number (if specified)
	    if ( p != 0 ) 	// if starting point has non-zero port number, figure distance
	      {
		  // look first to the left; if null, 
		SLCell theCell = d.getCell(start, this.getTray());
		if( theCell == null )	// look to the right
		    theCell = d.getCell(start, this.getTray()+1);

		// for now, use half the cell height
		int offset = theCell.getHeight()/2;
		length = length + offset;
//System.out.println("   with starting port offset " + offset );
	      }
	    else	// add height of start row plus vertical tray offset
	      if( end != start )
		length = length + ((SLRowColumnVector) d.getRow(start)).getSize();

	    if ( q != 0 )	// if ending point has non-zero port number, figure distance plus tray offset
	      {
		// Using [end, tray+1] since dest is input port in next column
		// for now, use half the cell height 
		SLCell theCell = d.getCell(end, this.getTray()+1);
		if( theCell == null ) 
		    theCell = d.getCell(end, this.getTray());

		int offset = theCell.getHeight()/2;
		length = length + offset;
//System.out.println("   and ending port offset " + offset );
	      }
	    else
	      {}	// adjust for vertical tray offset
	  }
	// set direction
	length = dir*length;	// note: -1 dir means upward, since row coord is +down
      }
    else
      {
	//
	//	ROW
	//
	//	System.out.println("   calculating length of horizontal segment on row " + this.getTray());
	// add up width of intervening columns, not including start or finish column
	for( int i = start+1; i <= (end-2); i++ )
	  length = length + ((SLRowColumnVector) d.getCol(i)).getSize();

	// don't count tray width in last column
	length = length + ((SLRowColumnVector) d.getCol(end-1)).getSizeNoTray();

	// subtract standoff in first column

	SLRowColumnVector startCol = (SLRowColumnVector) d.getCol(start);
	SLCableTray startTray = startCol.getTray();
	int standoff = startTray.getStandoff( sig );

	length = length - standoff;

	// add standoff distance to finish column
	
	SLRowColumnVector endCol = (SLRowColumnVector) d.getCol(end-1);
	SLCableTray endTray = endCol.getTray();
	standoff = endTray.getStandoff( sig );

	length = length + standoff;

      }

    return length;

  }


  /**
   *
   * <p> Generate a description of ourself on the out stream (non-recursive) </p>
   *
   **/

  public void describe()
  {
    this.describe("");
  }


  /**
   *
   * <p> Generate a description of ourself on the out stream (recursive) </p>
   *
   * @param indent String containing the offset to use for indentation
   **/

  public void describe( String indent )
  {
    System.out.print(indent);

    if (this.isHoriz()) 
      System.out.print("H: [" + this.getTray() + "+" + this.getTrayOffset() 
		       + "," + this.getStart() + "] to [" + this.getTray() + "," + this.getEnd() + "]");
    else
      System.out.print("V: [" + this.getStart() + "," + this.getTray() + "+" + 
		       this.getTrayOffset() + "," + this.getSourcePort() + "] to ["
		       + this.getEnd()   + "," + this.getTray() + "+" + 
		       this.getTrayOffset() + "," + this.getDestPort() +"]");

    // write code for Cell, Seg, or Branches
    System.out.print(" (");

    if( this.nextSeg != null )
      System.out.print("S");
    else
      System.out.print(" ");

    if( this.branches.size() > 0)
      System.out.print("B");
    else
      System.out.print(" ");

    if( this.destCell != null )
      System.out.print("D)");
    else
      System.out.print(" )");

    // add more info to end of line

    if( this.destCell == null )
      if( this.nextSeg == null )
	if( this.branches.size() == 0 )	// no dest cell, no next segment, and no branches
	  System.out.println("");
	else	
	  {				// no dest cell, no next segment, but branches
	    System.out.println(", then " + this.branches.size() + " branches:");
	    Iterator it = this.branches.iterator();
	    while (it.hasNext())
	      {
		SLLineSegment ls = (SLLineSegment) it.next();
		ls.describe( indent + "  ");
	      }
	  }
      else
	{				// no dest cell, but next segment - ignore branches
	  System.out.println(", then ");
	  this.nextSeg.describe( indent + "  ");
	}
    else				// with dest cell
      {
	System.out.print(" to '" + destCell.getBlock().getName() + "'");
	if( this.nextSeg == null )
	  if( this.branches.size() == 0 ) 
	    {				// and no follow-on segment
	      System.out.println(".");
	    }
	  else
	    {				// with a dest cell, no next segment, but more branches
	      System.out.println(", then " + this.branches.size() + " branches:");
	      Iterator it = this.branches.iterator();
	      while (it.hasNext())
		{
		  SLLineSegment ls = (SLLineSegment) it.next();
		  ls.describe( indent + "  ");
		}
	    }
	else
	  {				// with a dest cell, and another segment - ignore branches
	    System.out.println(", then ");
	    this.nextSeg.describe( indent + "  ");
	  }
      }
  }



  /**
   *
   * <p> Generate a Simulink Model description of our branches and paths (recursive) </p>
   *
   * @param writer SLFileWriter to send description
   * @param indent String containing the offset to use for indentation
   * @param ptsEntered boolean indicating if semicolon needed to start Points parameter
   * @throws IOException
   *
   **/

  public void createMDL( SLFileWriter writer, String indent, boolean ptsEntered ) 
    throws IOException
  {
    boolean hasBranched = false;

    int length = this.calcLength();

    if( length != 0 )
      {
	if( ptsEntered ) writer.write( "; " );
	// finish pixel routing (points)  description
	if (this.isHoriz()) 
	  writer.write( this.calcLength() + ", 0");
	else
	  writer.write( "0, " + this.calcLength());

	if( this.nextSeg != null )	// if follow-on segment, assume no dest or branch
	  {
	    this.nextSeg.createMDL(writer, indent + "  ", true);
	    return;
	  }
      }

    // terminate routing description
    writer.writeln("]");

    // here on null nextSeg - now handle branching

    if( this.branches.size() != 0 )
      {
	hasBranched = true;
        Iterator ib = this.branches.iterator();
	while( ib.hasNext() )
	  {
	    SLLineSegment ls = (SLLineSegment) ib.next();
	    writer.writeln(indent + "Branch {");
	    writer.write( indent + "  Points [");
	    ls.createMDL( writer, indent + "  ", false );
	    writer.writeln(indent + "}");
	  } 

      }


    if( this.destCell == null )
      if( !hasBranched )
	{
	  // no branches or next seg - better have destination
	  System.err.println("Error - signal '" + this.parentSignal.getName() 
			     + "' has no destination cell!");
	  return;
	}
      else
	{
	  // here with null destCell but having branched - done
	  return;
	}
    else
      {
	// non-null destCell - we have a place to go
	// if this node has branched, need to surround destination with branch delimiters

	if ( hasBranched ) writer.writeln(indent + "Branch {");

	Block destBlock = destCell.getBlock();
	if( destBlock == null )
	  {
	    System.err.println("Error - signal '" + this.parentSignal.getName() 
			       + "' returns null destination block!");
	    return;
	  }

	int destPort = this.getDestPort();
	if( destPort == 0 )
	  System.err.println("Error - signal '" + this.parentSignal.getName()
			     + "' has no destination port defined.");

	// write destination info
	writer.write( indent );

	if ( hasBranched ) writer.write("  ");

        writer.writeln("DstBlock		\"" + 
		       destBlock.getName() + "\"");
	writer.write( indent );

	if ( hasBranched ) writer.write("  ");

        writer.writeln("DstPort			" + destPort );

	if ( hasBranched ) writer.writeln(indent + "}");
	
      }
  }
}
