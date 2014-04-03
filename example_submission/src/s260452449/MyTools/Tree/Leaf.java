package s260452449.MyTools.Tree;


import boardgame.Board;
import halma.CCMove;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class Leaf extends Node{

    private Board board;
    public Leaf (CCMove move, Board theBoard, Integer depth)
    {
        super(move, depth);
        this.board = theBoard;
    }

    public Board getBoard() {
        return this.board;
    }
    public void setBoard(Board theBoard) {
        this.board = theBoard;
    }
}
