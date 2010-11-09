// Model
//
//	Object representing overall model network
//
// 020516 EBJ
//

package gov.nasa.larc.bjax.DAVE.tools.DAVEtoSL;

/**
 *
 * <p>  The Model object represents lines and blocks in a DAVE file </p>
 *
 **/

public class Model
{

    SignalArrayList signals;
    BlockArrayList blocks;
    
    /**
     *
     * <p> Constructor for Model </p>
     *
     * @param numBlocks  Estimated number of blocks expected
     * @param numSignals Estimated number of signals expected
     *
     **/

    public Model(int numBlocks, int numSignals)
    {
	blocks  = new  BlockArrayList(numBlocks );
	signals = new SignalArrayList(numSignals);
    }

    /**
     *
     * <p> Constructor for Model with default number of signals and blocks </p>
     *
     **/

    public Model()
    {
	this(20, 20);
    }



    /**
     *
     * <p> Access signals list </p>
     *
     **/

    public SignalArrayList getSignals() { return signals; }


    /**
     *
     * <p> Access blocks list </p>
     *
     **/

    public BlockArrayList getBlocks() { return blocks; }


    /**
     *
     * <p> Add block </p>
     *
     * @param newBlock <code>Block</code> to be added to blocks list
     *
     **/

    public void add( Block newBlock ) { blocks.add(newBlock); }
//System.out.println("Adding block " + newBlock.getName() + " to model."); }



    /**
     *
     * <p> Add signal </p>
     *
     * @param newSignal <code>Signal</code> to be added to signals list
     *
     **/

    public void add( Signal newSignal ) { signals.add(newSignal); }
//System.out.println("Adding signal " + newSignal.getName() + " to model."); }


    /**
     *
     * <p> Return number of blocks </p>
     *
     **/

    public int numBlocks() { return blocks.size(); }


    /**
     *
     * <p> Return number of signals </p>
     *
     **/

    public int numSignals() { return signals.size(); }



}
