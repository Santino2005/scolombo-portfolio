package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions;

import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.FreePath.FreeRowPath;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions.IsFirstMove;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.List;

public class CastlingCondition implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int rowDiff = Math.abs(moveTo.getTo().row() - moveTo.getFrom().row());
        int colDiff = Math.abs(moveTo.getTo().column() - moveTo.getFrom().column());
        if (rowDiff != 0 || colDiff != 2){
            return false;
        }
        int rookColumn = moveTo.getTo().column() > moveTo.getFrom().column() ? board.getColMax() : 1;
        Position rookPos = new Position(moveTo.getFrom().row(), rookColumn);
        MoveTo rookMove = new MoveTo(moveTo.getFrom(), rookPos);
        List<Valid> newList = List.of(new IsFirstMove(), new IsSecondPieceFirstMove(), new FreeRowPath());
        for(Valid valid : newList){
            if(!valid.valid(rookMove, board)){
                return false;
            }
        }
        return true;
    }

}
