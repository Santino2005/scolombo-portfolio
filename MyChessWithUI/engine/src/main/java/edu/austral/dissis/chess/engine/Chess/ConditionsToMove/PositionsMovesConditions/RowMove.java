package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class RowMove implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int dir = moveTo.getFrom().row();
        return moveTo.getFrom().column() == moveTo.getTo().column() && (dir > moveTo.getTo().row() || dir < moveTo.getTo().row());
    }
}
