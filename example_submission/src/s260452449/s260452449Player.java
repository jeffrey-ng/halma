package s260452449;

import halma.CCBoard;
import halma.CCMove;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;
import sXXXXXXXXX.mytools.MyTools;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by jeffrey on 3/27/2014.
 */
public class s260452449Player extends Player {
    public s260452449Player() { super("260452449"); }
    public s260452449Player(String s) { super(s); }

    public Board createBoard() { return new CCBoard();}

    public Move chooseMove(Board theboard)
    {
       Random rand = new Random();
        // Cast the arguments to the objects we want to work with
        CCBoard board = (CCBoard) theboard;

        // Get the list of legal moves.
        ArrayList<CCMove> moves = board.getLegalMoves();

        // Use my tool for nothing
        MyTools.getSomething();

        // Return a randomly selected move.
        return (CCMove) moves.get(rand.nextInt(moves.size()));
    }
}
