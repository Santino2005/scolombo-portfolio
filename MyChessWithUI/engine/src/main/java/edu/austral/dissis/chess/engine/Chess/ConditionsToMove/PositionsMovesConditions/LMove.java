package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class LMove implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int rowWay = Math.abs(moveTo.getTo().row() - moveTo.getFrom().row());
        int colWay = Math.abs(moveTo.getTo().column() - moveTo.getFrom().column());
        return (rowWay == 2 && colWay == 1) || (rowWay == 1 && colWay == 2);
    }
}
