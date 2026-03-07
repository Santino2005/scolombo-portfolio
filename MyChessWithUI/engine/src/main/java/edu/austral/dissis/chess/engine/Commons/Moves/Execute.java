package edu.austral.dissis.chess.engine.Commons.Moves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.Map;

public interface Execute {

    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board);

}
