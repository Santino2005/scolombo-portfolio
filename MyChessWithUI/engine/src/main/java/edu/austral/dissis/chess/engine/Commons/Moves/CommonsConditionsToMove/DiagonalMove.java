package edu.austral.dissis.chess.engine.Commons.Moves.CommonsConditionsToMove;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public class DiagonalMove implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        boolean leftBottom = moveTo.getTo().row() - moveTo.getFrom().row() == moveTo.getTo().column() - moveTo.getFrom().column();
        boolean leftTop = -(moveTo.getTo().row() - moveTo.getFrom().row()) == moveTo.getTo().column() - moveTo.getFrom().column();
        boolean rightBottom = moveTo.getTo().row() - moveTo.getFrom().row() == -(moveTo.getTo().column() - moveTo.getFrom().column());
        boolean rightTop = moveTo.getTo().row() - moveTo.getFrom().row() == moveTo.getTo().column() - moveTo.getFrom().column();
        return leftBottom || rightBottom || leftTop || rightTop;
    }
}
