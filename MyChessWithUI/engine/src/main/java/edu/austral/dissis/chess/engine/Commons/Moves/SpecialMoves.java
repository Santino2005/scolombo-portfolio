package edu.austral.dissis.chess.engine.Commons.Moves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.Map;

public interface SpecialMoves extends IsSpecialMove, Execute {

    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board);
    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board);

}
