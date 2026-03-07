package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KingMoveRestriction implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        int positionRow = moveTo.getTo().row();
        int positionCol = moveTo.getTo().column();
        Map<Position, PieceMaker> simulatedBoard = new HashMap<>(board.getCells());
        simulatedBoard.remove(moveTo.getFrom());
        simulatedBoard.put(moveTo.getTo(), board.getPieceByPosition(moveTo.getFrom()).get());
        List<Position> list =  List.of(new Position(positionRow, positionCol+1), new Position(positionRow, positionCol-1)
                , new Position(positionRow+1,positionCol), new Position(positionRow+1, positionCol+1), new Position(positionRow, positionCol-1), new Position(positionRow-1,positionCol)
                , new Position(positionRow-1,positionCol+1), new Position(positionRow-1,positionCol-1));
        for(Position pos : list){
            if(simulatedBoard.get(pos) != null && simulatedBoard.get(pos).getPieceId().equals('K')
                    && simulatedBoard.get(pos).getColor() != board.getCells().get(moveTo.getFrom()).getColor()){
                return false;
            }
        }
        return true;
    }

}
