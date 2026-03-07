package edu.austral.dissis.chess.engine.Commons.Moves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public interface IsSpecialMove {

    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board);

}
