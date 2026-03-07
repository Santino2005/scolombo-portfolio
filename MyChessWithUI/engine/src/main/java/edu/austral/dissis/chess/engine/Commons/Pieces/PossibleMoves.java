package edu.austral.dissis.chess.engine.Commons.Pieces;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.List;

public interface PossibleMoves {

    List<Position> possiblePosition(MoveTo moveTo, Board board);

}
