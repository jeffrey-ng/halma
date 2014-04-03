package s260452449.MyTools;

import boardgame.Board;
import halma.CCMove;

/**
 * Created by jeffrey on 4/2/2014.
 */
public class Pair {
    private CCMove move;
    private Board board;
    public Pair(CCMove move, Board board) {
        this.move = move;
        this.board = board;
    }

    public CCMove getMove(){ return move;}
    public Board getBoard(){ return board;}
    public void setMove(CCMove move) {this.move = move;}
    public void setBoard(Board board) {this.board = board;}
}
