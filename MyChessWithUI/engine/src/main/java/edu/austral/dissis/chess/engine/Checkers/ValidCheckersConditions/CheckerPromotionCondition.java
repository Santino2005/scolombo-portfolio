package edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class CheckerPromotionCondition implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int finalRow = moveTo.getTo().row();
        Color color = board.getPieceByPosition(moveTo.getFrom()).get().getColor();

        return (color == Color.WHITE && finalRow == board.getRowMax())
                || (color == Color.BLACK && finalRow == 1);
    }

}
