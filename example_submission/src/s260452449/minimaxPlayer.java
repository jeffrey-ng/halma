package s260452449;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;
import halma.CCBoard;
import halma.CCMove;
import s260452449.MyTools.AlphaBeta;
import s260452449.MyTools.GeneralTools;
import s260452449.MyTools.IMoveAlgorithm;
import sXXXXXXXXX.mytools.MyTools;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class minimaxPlayer extends Player{
    private String myName;
    private GeneralTools tools;
    int moveCount;

    private IMoveAlgorithm algorithm;

    public minimaxPlayer() {
        super("260452449");
        myName = "260452449";
        tools = new GeneralTools();
        algorithm = new AlphaBeta(this.playerID);
        moveCount = 0;
    }

    public minimaxPlayer(String s) {
        super(s);
        myName = s;
        tools = new GeneralTools();
        algorithm = new AlphaBeta(this.playerID);
        moveCount = 0;
    }

    public Board createBoard() { return new CCBoard();}

    public Move chooseMove(Board theboard)
    {
        Random rand = new Random();
        // Cast the arguments to the objects we want to work with
        CCBoard board = (CCBoard) theboard;
        Move move;
        // Get the list of legal moves.
        ArrayList<CCMove> moves = board.getLegalMoves();
        try {
            move = algorithm.getOptimalMove(theboard,2,this.playerID,moveCount);
        } catch (Exception e) {
            System.out.println("Problem making move decision.");
            move = moves.get(rand.nextInt(moves.size()));
        }


        moveCount++;
        return (CCMove) move;
    }

}
