package s260452449.MyTools;

import boardgame.Board;
import boardgame.Player;
import halma.CCBoard;
import halma.CCMove;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by jeffrey on 3/28/2014.
 */
public class GeneralTools {

    //Builds all possible boards from legal moves at current time. One layer deep.
    public ArrayList<Pair> getNextPossibleBoards(Board theBoard) {
        ArrayList<Board> allPossibleBoards = new ArrayList<Board>();
        ArrayList<Pair> pairs = new ArrayList<Pair>();
        CCBoard board = (CCBoard) theBoard;
        for (CCMove move : board.getLegalMoves()) {
            CCBoard temp = (CCBoard) board.clone();
            temp.move(move);
            pairs.add(new Pair(move,temp));
            allPossibleBoards.add(board);
        }
        return pairs;
    }

    public ArrayList<Pair> getNextPossibleBoardsForPlayer(Board theBoard, int PlayerID) {

        ArrayList<Pair> pairs = new ArrayList<Pair>();
        CCBoard board = (CCBoard) theBoard;

        for (Point p: board.getPieces(PlayerID)) {
            for (CCMove move : board.getLegalMoveForPiece(p,PlayerID)) {
                //if (getDistance(move))
                CCBoard temp = (CCBoard) board.clone();
                temp.move(move);
                pairs.add(new Pair(move,temp));
            }
        }
        return pairs;
    }

    public boolean isBackwardsMove(Board theBoard,CCMove move) {
        CCBoard board = (CCBoard) theBoard;
        Point target = getOppositeTarget(move.getPlayerID());
        double oldDistance = getDistance(new CCMove(move.getPlayer_id(),move.getFrom(),target));
        double newDistance = getDistance(new CCMove(move.getPlayer_id(),move.getFrom(),target));

        if (newDistance > oldDistance) {
            return false;
        }
        return true;
    }

    //Get rating of board for specified player.
    public int getDistanceToDestination(Board theBoard, Integer playerID, int moveCount) {
        //Rating is the sum of every piece to the target.
        CCBoard board = (CCBoard) theBoard;


        //ArrayList<Point> remainingGoal = remainingGoalPoints(theBoard, playerID);

        double rating = 0;

        CCMove move;
        Point from;
        Point to;
        for (Point p: board.getPieces(playerID)) {
            from = p;
           // if (moveCount > 200) {
               // to = closestGoalPoint(theBoard,from,remainingGoal);
            //} else {
             to = getOppositeTarget(playerID);
           // }

            move = new CCMove(playerID,from,to);
            rating += getDistance(move);
        }


        return (int)rating;

    }

    public int getDistanceFromBase(Board theBoard, Integer playerID) {
        //Rating is the sum of every piece to the target.
        CCBoard board = (CCBoard) theBoard;
        ArrayList<CCMove> moves = board.getLegalMoves();

        ArrayList<Point> myBase = new ArrayList<Point>(board.bases[playerID]);
        ArrayList<Point> remainingGoal = remainingGoalPoints(theBoard, playerID);

        double rating = 0;

        CCMove move;
        Point from;
        Point to;
        for (Point p: board.getPieces(playerID)) {
            from = p;
            //to = p;
            to = getMyOwnBaseTarget(playerID);
            // to = farthestGoalPoint(theBoard, p, remainingGoal);
           // to = farthestGoalPointFromBase(theBoard,playerID,remainingGoal);

            move = new CCMove(playerID,from,to);
            rating += getDistance(move);
        }



        return (int)rating;

    }

    public int getNeighbourCount(Board theBoard, Integer playerID){
        CCBoard board = (CCBoard) theBoard;
        ArrayList<Point> myPieces = board.getPieces(playerID);
        int neighbours = 0;
        for (Point p: myPieces) {
            for (Point ap: myPieces) {
                for (int x =(int) p.getX()-1;x< (int)p.getX()+1;x++) {
                    for (int y =(int) p.getY()-1;y< (int)p.getY()+1;y++) {
                        if (((int)ap.getX() == x) && (int)ap.getY() == y) {
                            neighbours++;
                        }
                    }
                }
            }

        }


        return neighbours;

    }

    public int getNumWithNoNeighbours(Board theBoard, Integer playerID){
        CCBoard board = (CCBoard) theBoard;
        ArrayList<Point> myPieces = board.getPieces(playerID);

        int noNeighbourCount=0;
        boolean hasNeighbour = false;
        for (Point p: myPieces) {
            for (int x =(int) p.getX()-1;x< (int)p.getX()+1;x++) {
                for (int y =(int) p.getY()-1;y< (int)p.getY()+1;y++) {
                    if (myPieces.contains(new Point(x,y))) {
                        hasNeighbour = true;
                    }
                }
            }
            if (!hasNeighbour) { noNeighbourCount++;}
            hasNeighbour = false;
        }


        return noNeighbourCount;

    }


    //Get Remaining Available goal points for given player
    public ArrayList<Point> remainingGoalPoints(Board theBoard, int player_id) {
        CCBoard board = (CCBoard) theBoard;
        Set<Point> t = board.bases[player_id^3];
        ArrayList<Point> myTargetBase = new ArrayList<Point>(t);
        ArrayList<Point> remainingPoints = new ArrayList<Point>();
        for (Point p: board.getPieces(player_id)) {
            if (!myTargetBase.contains(p)) {
                remainingPoints.add(p);
            }
        }
        return remainingPoints;
    }

    //Get Closest Goal Point in availablePoints to given piece
    public Point closestGoalPoint(Board theBoard, Point piece,ArrayList<Point> availablePoints) {
        CCBoard board = (CCBoard) theBoard;
        Double shortestDistance = Double.MAX_VALUE;

        //ArrayList<Point> availablePoints = remainingGoalPoints(theBoard,player_id);
        Point closestGoalPoint = availablePoints.get(0);

        for (Point p: availablePoints) {
            double dX = getAbs(piece.getX(), p.getX());
            double dY = getAbs(piece.getY(), p.getY());


            double distance = Math.sqrt(dX * dX + dY * dY);

            if (distance < shortestDistance) {
                shortestDistance = distance;
                closestGoalPoint = p;
            }
        }

        return closestGoalPoint;
    }

    public Point farthestGoalPointFromBase (Board theBoard, int PlayerID, ArrayList<Point> availablePoints) {
        CCBoard board = (CCBoard) theBoard;
        Double longestDistance = Double.MIN_VALUE;
        Point farthestPoint = availablePoints.get(0);


        ArrayList<Point> myBase = new ArrayList<Point>(board.bases[PlayerID]);
        ArrayList<Point> theirBase = new ArrayList<Point>(board.bases[PlayerID^3]);
        Point myFarthestPoint = myBase.get(0);
        Point OppositeFarthestPoint = theirBase.get(0);
        for (Point p: theirBase) {


            double dX = getAbs(myFarthestPoint.getX(), p.getX());
            double dY = getAbs(myFarthestPoint.getY(), p.getY());
            double distance = Math.sqrt(dX * dX + dY * dY);

            if (distance > longestDistance) {
                longestDistance = distance;
                OppositeFarthestPoint = p;
            }
        }
        return OppositeFarthestPoint;
    }

    // Get distance that Move covers.
    public double getDistance(CCMove move) {

        double dX = getAbs(move.getTo().getX(), move.getFrom().getX());
        double dY = getAbs(move.getTo().getY(), move.getFrom().getY());

        double distance = Math.hypot(dX,dY);

        return distance;

    }

//    public boolean inBase(Board theBoard, Point point, int playerID) {
//        CCBoard board = (CCBoard) theBoard;
//        Set<Point> t = board.bases[playerId^3];
//        ArrayList<Point> myTargetBase = new ArrayList<Point>(t);
//        ArrayList<Point> remainingPoints = new ArrayList<Point>();
//        for (Point p: board.getPieces(player_id)) {
//            if (!myTargetBase.contains(p)) {
//                remainingPoints.add(p);
//            }
//        }
//        return remainingPoints;
//    }

    //Get absolute distance
    public double getAbs(double x, double y) {
        double abs = x - y;
        if (abs < 0) abs = y - x;
        return(abs);
    }

    public boolean isRepeatMove(CCMove previous, CCMove current) {

        if(previous.getFrom().equals(current.getTo()) && previous.getTo().equals(current.getFrom())) {
            return true;
        }
        return false;

    }

    public Point getOppositeTarget(int PlayerID) {
        Point target = new Point(0,0);
        switch (PlayerID) {
            case 0: target = new Point(15,15);break;
            case 1: target = new Point(0,15);break;
            case 2: target = new Point(15,0);break;
            case 3:target = new Point(0,0); break;
        }
        return target;

    }

    public Point getMyOwnBaseTarget(int PlayerID) {
        Point target = new Point(0,0);
        switch (PlayerID) {
            case 0:target = new Point(0,0); break;
            case 1: target = new Point(15,0);break;
            case 2: target = new Point(0,15);break;
            case 3: target = new Point(15,15);break;
        }
        return target;

    }

    public int getNumPiecesInBase(Board theBoard, Integer playerID) {
        CCBoard board = (CCBoard) theBoard;
        int count =0;
        ArrayList<Point> myBase = new ArrayList<Point>(board.bases[playerID]);
        for (Point p: board.getPieces(playerID)) {
           if (myBase.contains(p)) {
               count++;
           }
        }
        if (board.getPieces(playerID).contains(getMyOwnBaseTarget(playerID))) { count = count + 100;}
        return count;
    }

    public int getNumPiecesInTargetBase(Board theBoard, Integer playerID) {
        CCBoard board = (CCBoard) theBoard;
        int count =0;
        ArrayList<Point> myTargetBase = new ArrayList<Point>(board.bases[playerID^3]);
        for (Point p: board.getPieces(playerID)) {
            if (myTargetBase.contains(p)) {
                count++;
            }
        }
        return count;
    }

    public int getLargestDistanceBetweenPieces(Board theBoard, Integer playerID) {
        CCBoard board = (CCBoard) theBoard;
        double largestDistance = 0;
        CCMove move;
        Point from;
        Point to;
        for (Point p: board.getPieces(playerID)) {
            for (Point np: board.getPieces(playerID)) {
                from = np;
                to = p;
                move = new CCMove(playerID,from,to);
                double distance = getDistance(move);
                largestDistance = largestDistance + distance;
//                if (distance>largestDistance) {
//                    largestDistance = distance;
//                }

            }
        }

        return (int)largestDistance;

    }


}
