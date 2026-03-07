package edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class DiagonalTwoMoves implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int colDiff = Math.abs(moveTo.getFrom().column() - moveTo.getTo().column());
        int rowDiff = Math.abs(moveTo.getFrom().row() - moveTo.getTo().row());
        return colDiff == 2 && rowDiff == 2;
    }
}
