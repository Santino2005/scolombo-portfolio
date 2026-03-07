package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class OneSquareMove implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        return Math.abs(moveTo.getFrom().row() - moveTo.getTo().row()) <= 1
                && Math.abs(moveTo.getFrom().column() - moveTo.getTo().column()) <= 1;
    }
}
