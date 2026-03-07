package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class PromotionCondition implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        return (moveTo.getTo().row() == board.getRowMax()
                && board.getRowMax() == moveTo.getFrom().row() + 1
                && board.getPieceByPosition(moveTo.getFrom()).get().getColor().equals(Color.WHITE)
                || moveTo.getTo().row() == 1
                && 1 == moveTo.getFrom().row() - 1
                && board.getPieceByPosition(moveTo.getFrom()).get().getColor().equals(Color.BLACK));
    }
}
