package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class DiagonalEat implements Valid {


    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int dir = (board.getPieceByPosition(moveTo.getFrom()).get().getColor() == Color.WHITE) ? 1 : -1;
        return Math.abs(moveTo.getTo().column() - moveTo.getFrom().column()) == 1 &&
                moveTo.getTo().row() == moveTo.getFrom().row() + dir;
        }
}
