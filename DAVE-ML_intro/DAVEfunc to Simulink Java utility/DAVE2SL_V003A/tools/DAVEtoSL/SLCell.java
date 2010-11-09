//
// SLCell.java
//
//	020624 E. B. Jackson <e.b.jackson@larc.nasa.gov>


package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

/**
 *
 * <p> Container to hold simulink item (block) </p>
 *
 * <p> Width and height are actually dictated by our row and
 * column. The SLCell acts as mediator between embedded block (which
 * has a width and height) and the Row/Column parent (who can
 * provide row and height). </p>
 *
 **/

public class SLCell
{
  SLDiagram myParent;
  Block myBlock;
  SLRowColumnVector myRow;
  SLRowColumnVector myCol;

  public SLCell()
  {
    myParent = null;
    myBlock = null;
  }

  public SLCell( Block b, SLDiagram theDiagram )
  {
    this();
    this.myBlock = b;
    this.myParent = theDiagram;
  }
    
  public Block getBlock() { return this.myBlock; }
  public int getRowIndex()  { return myCol.getOffset( this ); }
  public int getColIndex()  { return myRow.getOffset( this ); }
  public SLRowColumnVector getRow() { return this.myRow; }
  public SLRowColumnVector getCol() { return this.myCol; }

  public SLCell previousCellInColumn() 
  {
    return myParent.getCell(this.getRowIndex()-1, this.getColIndex());
  }

  public SLCell previousCellInRow()
  {
    return myParent.getCell(this.getRowIndex(), this.getColIndex()-1);
  }

  public SLCell nextCellInColumn()
  {
    return myParent.getCell(this.getRowIndex()+1, this.getColIndex());
  }

  public SLCell nextCellInRow()
  {
    return myParent.getCell(this.getRowIndex(), this.getColIndex()+1);
  }

  public void setRow( SLRowColumnVector theRow ) { this.myRow = theRow; }
  public void setCol( SLRowColumnVector theCol ) {this.myCol = theCol; }

  /**
   *
   * <p> This method returns desired minimum width, based on specified
   * margin around encapsulated block. </p>
   *
   **/

  public int getMinWidth()
  {
    return this.myBlock.getMdlWidth() + 2*this.myParent.getPadding();
  }

  /**
   *
   * <p> This method returns desired minimum height, based on specified
   * margin around encapsulated block. </p>
   *
   **/

  public int getMinHeight()
  {
    return this.myBlock.getMdlHeight() + 2*this.myParent.getPadding();
  }

  
  public int getWidth()  { return myCol.getSize(); }
  public int getHeight() { return myRow.getSize(); }


  public int distToEdge()
  {
    // returns distance from input or output port to edge of cell,
    // based on size of block and cell itself.

//      System.out.println("for block '" + this.myBlock.getName() + "':");
//      System.out.println("    Col width   " + this.getWidth());
//      System.out.println("  Block width  -" + this.myBlock.getMdlWidth());
//      System.out.println("               ----------");
//      System.out.println("    difference  " + (this.getWidth() - this.myBlock.getMdlWidth()));
//      System.out.println(" half of diff   " + (this.getWidth() - this.myBlock.getMdlWidth())/2);
//      System.out.println();

    return (this.getWidth() - this.myBlock.getMdlWidth())/2;
  }
}
