package edu.austral.dissis.chess.engine.Commons.Moves.CommonsConditionsToMove;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class IsPieceNotOnRoad implements Valid {
    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        return board.getPieceByPosition(moveTo.getTo()).isEmpty();
    }
}
