package s260452449.MyTools;

import boardgame.Board;
import halma.CCBoard;

import java.util.Hashtable;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class Heuristics {

    private GeneralTools tools;
    public enum WeightNames {
        neighbours, distanceToDest, distanceFromBase, piecesInBase

    }

    private Hashtable<Integer,Hashtable<WeightNames,Integer>> weights;

    private Board board;

    public Heuristics() {
        weights = new Hashtable<Integer, Hashtable<WeightNames, Integer>>();
        tools = new GeneralTools();
    }

    public void setWeights(Integer playerID, Hashtable<WeightNames,Integer> weights) {
        if (weights == null) {
            Hashtable<WeightNames,Integer> tempWeights = new Hashtable<WeightNames, Integer>();
            //Init new weights here.
            this.weights.put(playerID,tempWeights);
        } else {
            this.weights.put(playerID,weights);
        }
    }

    public Integer evaluateComplete(Board theBoard) {
        CCBoard board = (CCBoard) theBoard;
//        int win = Integer.MAX_VALUE;
//        int lose = Integer.MIN_VALUE;
        int win = 1000;
        int lose = -1000;
        if (board.getWinner() == 0) {
            return win;
        } else if (board.getWinner() == 1) {
            return lose;
        }

        return 0;
    }


    public Integer evaluateIncomplete(Board theBoard) {
        CCBoard board = (CCBoard) theBoard;
        Integer utility = 0;
        for (int pid = 0; pid < board.getNumberOfPlayers();pid ++) {

        }

        utility -= tools.getDistanceToDestination(theBoard,0) * weights.get(0).get(WeightNames.distanceToDest);
//        utility -= tools.getDistanceToDestination(theBoard, 0);
        //utility += tools.getDistanceFromBase(theBoard,0)* weights.get(0).get(WeightNames.distanceFromBase);;
        utility += tools.getDistanceFromBase(theBoard,0)* 1000;
        utility -= tools.getNumPiecesInBase(theBoard,0)* weights.get(0).get(WeightNames.piecesInBase);
        utility += tools.getNeighbourCount(theBoard,0) * weights.get(0).get(WeightNames.neighbours);


        return utility;

    }


}
