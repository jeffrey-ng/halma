package s260452449.MyTools;

import boardgame.Move;
import boardgame.Board;

/**
 * Created by jeffrey on 3/28/2014.
 */
public interface IMoveAlgorithm {


    /**
     *
     * @param theboard
     * @param seachDepth
     * @return
     */
    Move getOptimalMove(Board theboard, Integer seachDepth) throws Exception;
}
