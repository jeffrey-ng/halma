package s260452449.MyTools;

import boardgame.Board;
import halma.CCBoard;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class Heuristics {

    private GeneralTools tools;
    public enum WeightNames {
        neighbours, distanceToDest, distanceFromBase, piecesInMyBase, noNeighbours, piecesInTargetBase

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


    public Integer evaluateIncomplete(Board theBoard, int PlayerID, int moveCount) {
        CCBoard board = (CCBoard) theBoard;
        Integer utility = 0;
        for (int pid = 0; pid < board.getNumberOfPlayers();pid ++) {

        }
        //PlayerID = 0;
       utility -= tools.getDistanceToDestination(theBoard,PlayerID, moveCount) * (weights.get(PlayerID).get(WeightNames.distanceToDest) + 2*(moveCount));
//        utility -= tools.getDistanceToDestination(theBoard, 0);
        utility += tools.getDistanceFromBase(theBoard,0)* weights.get(0).get(WeightNames.distanceFromBase);;
        utility += tools.getDistanceFromBase(theBoard,PlayerID)* weights.get(PlayerID).get(WeightNames.distanceFromBase);
        utility -= tools.getNumPiecesInBase(theBoard,PlayerID)* (weights.get(PlayerID).get(WeightNames.piecesInMyBase) + moveCount*moveCount);
        utility += tools.getNumPiecesInTargetBase(theBoard, PlayerID)* (weights.get(PlayerID).get(WeightNames.piecesInTargetBase) +2 *(moveCount));
//        int nCount = tools.getNeighbourCount(theBoard,PlayerID);
//        if (nCount > 36) {
//            utility -= nCount * weights.get(PlayerID).get(WeightNames.neighbours);
//        } else {
//            utility += nCount * weights.get(PlayerID).get(WeightNames.neighbours);
//        }
        //utility -= tools.getLargestDistanceBetweenPieces(theBoard,0) * 5;
        //utility -= tools.getNumWithNoNeighbours(theBoard,PlayerID) * weights.get(PlayerID).get(WeightNames.noNeighbours);
        return utility;

    }

    public int[] generalHeuristicEvaluation(Board theBoard, int moveCount) {
        CCBoard board = (CCBoard) theBoard;
        Integer utility;
        int[] groupUtility = new int[4];
        for (int pid = 0; pid < board.getNumberOfPlayers(); pid++) {
            utility = 0;
            utility -= tools.getDistanceToDestination(theBoard,pid, moveCount) * (weights.get(pid).get(WeightNames.distanceToDest) + (moveCount));
            utility += tools.getNumPiecesInTargetBase(theBoard, pid)* (weights.get(pid).get(WeightNames.piecesInTargetBase) + 2*(moveCount));
            utility -= tools.getNumPiecesInBase(theBoard,pid)* (weights.get(pid).get(WeightNames.piecesInMyBase) + moveCount*moveCount);

            groupUtility[pid] = utility;
        }

        return groupUtility;

    }
    //Longest Chain Heuristic

    //At a certain point, switch to looking to fill spots in endzone.

    // Weight different spots with different weights. Closest, highest. Slowly draw out.
}
