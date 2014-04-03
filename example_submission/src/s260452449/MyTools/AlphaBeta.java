package s260452449.MyTools;

import boardgame.Board;
import boardgame.Move;
import halma.CCBoard;
import halma.CCMove;
import s260452449.MyTools.Tree.Leaf;
import s260452449.MyTools.Tree.Node;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class AlphaBeta implements IMoveAlgorithm{

    Heuristics heur;
    Integer searchDepth;
    GeneralTools tools;
    Leaf tree;
    CCMove lastMove;
    int myPlayerID;
    int myTeamMemberID;

    public AlphaBeta(){
        heur = new Heuristics();
        tools = new GeneralTools();
        heur.setWeights(0,weights(50,30,100,10000));
    };

    public Move getOptimalMove(Board theBoard, Integer searchDepth, int MyPlayerID) throws Exception{
        this.searchDepth = searchDepth;
        tree = new Leaf(null,theBoard,0);
        this.myPlayerID = MyPlayerID;
//        if (myPlayerID == 0) { this.myTeamMemberID =3;}
//        if (myPlayerID == 3) { this.myTeamMemberID =0;}
//        if (myPlayerID == 1) { this.myTeamMemberID =2;}
//        if (myPlayerID == 2) { this.myTeamMemberID =1;}

        return alphaBetaSearch(theBoard);
    }


    public Move alphaBetaSearch(Board theBoard) throws Exception{
        Integer utility = playerMaxUtility(tree, Integer.MIN_VALUE, Integer.MAX_VALUE);
//        Integer utility = utilityManager(tree,Integer.MIN_VALUE,Integer.MAX_VALUE,myPlayerID);
        //CCBoard bestBoard;

        for (Node currentNode : tree.getChildren())
        {
            if (currentNode.value.intValue() == utility.intValue()) {

                //bestBoard = (CCBoard) ((Leaf) currentNode).getBoard();
                return currentNode.move;
                //return bestBoard.getLastMoved();
            }
        }

        for (Node currentNode : tree.getChildren()) {
//            try
//            {
//                String filename= "Moves.txt";
//                FileWriter fw = new FileWriter(filename,true); //the true will append the new data
//                fw.write("Move with value "+ currentNode.value + "\n");//appends the string to the file
//                fw.close();
//            }
//            catch(IOException ioe)
//            {
//                System.err.println("IOException: " + ioe.getMessage());
//            }
        }
        throw new Exception("Error selecting next move in alpha beta. Not found from child nodes.");

    }


    private Integer evalCurrentGame(Board theBoard)
    {
        CCBoard board = (CCBoard) theBoard;
        if (board.getWinner() == board.NOBODY) {
            return heur.evaluateIncomplete(theBoard);
        } else {
            return heur.evaluateComplete(theBoard);
        }
    }


    private Integer playerMaxUtility(Leaf node, Integer currentMin, Integer currentMax) {


        if (searchEnd(node)) {
            return evalCurrentGame(node.getBoard());
        }

        Integer val = Integer.MIN_VALUE;

        for (Pair p: tools.getNextPossibleBoards(node.getBoard())) {
            //Leaf nextNode = new Leaf(b, node.depth + 1);
            Leaf nextNode = new Leaf(p.getMove(),p.getBoard(),node.depth+1);
            val = Math.max(val, playerMinUtility(nextNode,currentMin,currentMax));
            node.value = val;
            node.addChild(nextNode);


            if (val >= currentMax) { return val; }

            currentMin = Math.max(currentMin, val);
        }

        return val;
    }

    private Integer playerMinUtility(Leaf node, Integer currentMin, Integer currentMax) {
        if (searchEnd(node)) {
            return evalCurrentGame(node.getBoard());
        }

        Integer val = Integer.MAX_VALUE;


        for (Pair p: tools.getNextPossibleBoards(node.getBoard())) {
            Leaf nextNode = new Leaf(p.getMove(),p.getBoard(),node.depth+1);
            //Leaf nextNode = new Leaf(b, node.depth + 1);
            val = Math.min(val, playerMaxUtility(nextNode,currentMin,currentMax));
            node.value = val;
            node.addChild(nextNode);

            if (val <= currentMin) {return val; }

            currentMax = Math.min(currentMax,val);
        }

        return val;
    }

//    private Integer utilityManager(Leaf node, Integer currentMin, Integer currentMax, int PlayerID) {
//
//        if(PlayerID == myPlayerID || PlayerID == myTeamMemberID) {
//            return playerMaxUtilityForPlayer(node, currentMin,currentMax,PlayerID);
//        } else {
//            return playerMinUtilityForPlayer(node, currentMin, currentMax, PlayerID);
//        }
//    }
//
//    private Integer playerMaxUtilityForPlayer(Leaf node, Integer currentMin, Integer currentMax, int PlayerID) {
//
//
//        if (searchEnd(node)) {
//            return evalCurrentGame(node.getBoard());
//        }
//        nextTurn(PlayerID);
//        Integer val = Integer.MIN_VALUE;
//
//        for (Pair p: tools.getNextPossibleBoardsForPlayer(node.getBoard(),PlayerID)) {
//            //Leaf nextNode = new Leaf(b, node.depth + 1);
//            Leaf nextNode = new Leaf(p.getMove(),p.getBoard(),node.depth+1);
//            val = Math.max(val, utilityManager(nextNode,currentMin,currentMax,PlayerID));
//            node.value = val;
//            node.addChild(nextNode);
//
//
//            if (val >= currentMax) { return val; }
//
//            currentMin = Math.max(currentMin, val);
//        }
//
//        return val;
//    }
//
//    private Integer playerMinUtilityForPlayer(Leaf node, Integer currentMin, Integer currentMax, int PlayerID) {
//        if (searchEnd(node)) {
//            return evalCurrentGame(node.getBoard());
//        }
//        nextTurn(PlayerID);
//        Integer val = Integer.MAX_VALUE;
//
//
//        for (Pair p: tools.getNextPossibleBoardsForPlayer(node.getBoard(),PlayerID)) {
//            Leaf nextNode = new Leaf(p.getMove(),p.getBoard(),node.depth+1);
//            //Leaf nextNode = new Leaf(b, node.depth + 1);
//            val = Math.min(val, utilityManager(nextNode,currentMin,currentMax,PlayerID));
//            node.value = val;
//            node.addChild(nextNode);
//
//            if (val <= currentMin) {return val; }
//
//            currentMax = Math.min(currentMax,val);
//        }
//
//        return val;
//    }



    private Boolean searchEnd(Leaf node) {
        return node.depth > searchDepth || node.getBoard().getWinner() != Board.NOBODY;
    }

    private Hashtable<Heuristics.WeightNames, Integer> weights(int pos, int nei, int ib, int pib)
    {
        Hashtable<Heuristics.WeightNames, Integer> tmp = new Hashtable<Heuristics.WeightNames, Integer>();
        tmp.put(Heuristics.WeightNames.distanceToDest, pos);
        tmp.put(Heuristics.WeightNames.neighbours, nei);
        tmp.put(Heuristics.WeightNames.distanceFromBase, ib);
        tmp.put(Heuristics.WeightNames.piecesInBase,pib);
        return tmp;
    }

//    private int nextTurn(int PlayerID) {
//        if (PlayerID == 3) { return 0;}
//        return PlayerID++;
//    }

}