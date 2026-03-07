package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

public class TwoMoveToFront implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int dir = (board.getPieceByPosition(moveTo.getFrom()).get().getColor().equals(Color.WHITE)) ? 1 : -1;
        if((Math.abs(moveTo.getTo().row() - moveTo.getFrom().row()) != 2
                || moveTo.getFrom().column() != moveTo.getTo().column()) && board.validPosition(moveTo.getTo())){
            return false;
        }
        Position intermediate = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column());
        return board.getPieceByPosition(intermediate).isEmpty() && board.getPieceByPosition(moveTo.getTo()).isEmpty();
    }
}
