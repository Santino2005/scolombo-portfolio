package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.FreePath;

import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.MoveConditions.IsPieceOnRoad;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

public class FreeRowPath implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int columnWay = moveTo.getFrom().column();
        int step = moveTo.getTo().column() > columnWay ? 1 : -1;
        return checkRow(columnWay,step,moveTo,board);
    }
    private boolean checkRow(int columnWay, int step, MoveTo moveTo, Board board){
        if (columnWay == moveTo.getTo().column() || moveTo.getFrom().row() != moveTo.getTo().row()) {
            return false;
        }
        for (int col = columnWay + step; col != moveTo.getTo().column(); col += step) {
            if (new IsPieceOnRoad().valid(new MoveTo(moveTo.getFrom(), new Position(moveTo.getFrom().row(),col)),board)){
                return false;
            }
        }
        return true;
    }

}
