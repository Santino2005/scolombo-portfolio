package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class OneMoveToFront implements Valid {
    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int dir = (board.getPieceByPosition(moveTo.getFrom()).get().getColor().equals(Color.WHITE)) ? 1 : -1;
        return moveTo.getTo().row() == moveTo.getFrom().row() + dir &&
                moveTo.getTo().column() == moveTo.getFrom().column();
    }
}
