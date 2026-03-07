package edu.austral.dissis.chess.engine.Chess.Piece;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.List;

public interface Valuate extends PossibleMoves, Valid {

    public boolean valid(MoveTo moveTo, Board board);
    List<Position> possiblePosition(MoveTo moveTo, Board board);

}
