package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.MoveConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class IsPieceOnRoad implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        return board.getPieceByPosition(moveTo.getTo()).isPresent();
    }
}
