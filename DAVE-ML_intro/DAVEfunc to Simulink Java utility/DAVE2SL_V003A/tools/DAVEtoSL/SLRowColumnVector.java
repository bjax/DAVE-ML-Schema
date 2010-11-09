//
// SLRowColumnVector.java
//
//	An object containing a vector of objects and a cableway
//
//	020620 E. B. Jackson <e.b.jackson@larc.nasa.gov>
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

import java.util.ArrayList;
import java.util.Vector;


/**
 *
 * <p> an SLRowColumnVector contains a Vector for the blocks in the
 * row or column and an ArrayList with signals that are carried below
 * the row or to the left of the column in a virtual cable tray.</p>
 *
 **/

public class SLRowColumnVector
{
  Vector cells;
  SLCableTray cableTray;
  boolean isRow;	// if true, we're a row

    /**
     *
     * <p> SLRowColumnVector constructor with number of columns specified </p>
     *
     * @param numCells Number of cells to allocate (can be grown)
     * @param asRow <code>Boolean</code> signifying if we are to be row or col
     *
     **/

    public SLRowColumnVector( int numCells, boolean asRow )
    {
	int numTrays = 5;
	this.cells = new Vector( numCells );
	this.cells.setSize( numCells );	// fill will null objects
	this.cableTray = new SLCableTray( numTrays ); 
	this.isRow = asRow;
    }



    /**
     *
     * <p> Registers cell at specified offset </p>
     *
     * @param offset offset amount (index)
     *
     **/

    public void set( int offset, SLCell cell )
    {
	if(offset > this.cells.size())
	    this.cells.setSize( offset );	// may have one extra with this logic
	this.cells.set( offset, cell );
	if( this.isRow )
	  cell.setRow(this);
	else
	  cell.setCol(this);
    }

    
    /**
     *
     * <p> Gets cell at specified offset </p>
     *
     * @param offset 0-based offset value
     *
     **/

    public SLCell get( int offset )
    {
	if(offset > this.cells.size())
	    return null;
	return (SLCell) this.cells.get( offset );
    }


    /**
     *
     * <p> Returns index (offset) of specified cell </p>
     *
     * @param theCell <code>SLCell</code> to find index of 
     *
     **/

    public int getOffset( SLCell theCell )
    {
	return cells.indexOf( theCell );
    }


    /**
     *
     * <p> Returns width of column or height of row, including cable tray </p>
     *
     **/

    public int getSize()
    {
      int theSize = this.getSizeNoTray() + this.cableTray.getSize();
//    if( isRow )
//  	System.out.print("  height of row returned as ");
//    else
//  	System.out.print("  width of col returned as ");
//      System.out.println(theSize);
      return theSize;
    }


    /**
     *
     * <p> Returns width of column or height of row, not including cable tray </p>
     *
     **/

    public int getSizeNoTray()
    {
      int mySize = 0;
      int cellSize = 0;
      for( int i = 0; i < cells.size(); i++)
	{
	// loop through Vector of cells; return max cell width (if col) or height (if row)
	  SLCell theCell = (SLCell) cells.get(i);
	  if (theCell != null)
	    {
	      if (this.isRow)	// we're a Row of cells - find tallest cell
		cellSize = ((SLCell) cells.get(i)).getMinHeight();
	      else			// We're a column - remember widest cell
		cellSize = ((SLCell) cells.get(i)).getMinHeight();
	      if( cellSize > mySize ) mySize = cellSize;	// remember largest dimension
//System.out.println("     cell " + i + " size is " + cellSize)
	    }
	}
      return mySize;
    }


    public SLCableTray getTray() { return this.cableTray; }
    
    public Integer addToTray( SLSignal theSignal )
    {
	this.cableTray.add( theSignal );
	int theOffset = this.cableTray.indexOf( theSignal );
	return new Integer( theOffset );
    }

}
