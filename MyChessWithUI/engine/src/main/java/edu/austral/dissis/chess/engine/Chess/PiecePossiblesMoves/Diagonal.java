package edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;

public class Diagonal implements PossibleMoves {

    private final List<List<Integer>> directions = new ArrayList<>();

    public Diagonal(boolean topLeft, boolean topRight, boolean bottomLeft, boolean bottomRight) {
        if (topLeft) directions.add(List.of(1, -1));
        if (topRight) directions.add(List.of(1,1));
        if (bottomLeft) directions.add(List.of(-1,-1));
        if (bottomRight) directions.add(List.of(-1,1));
    }

    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        List<Position> positions = new ArrayList<>();
        for(List<Integer> direction : directions){
            positions.addAll(checkWays(moveTo,board,direction));
        }
        return positions;
    }

    private List<Position> checkWays(MoveTo initialPos, Board board, List<Integer> directions ) {

        List<Position> list = new ArrayList<>();

        int currentRow = initialPos.getFrom().row() + directions.get(1) ;
        int currentCol = initialPos.getFrom().column() + directions.get(0);
        MoveTo moveTo = new MoveTo(initialPos.getFrom(), new Position(currentRow,currentCol));

        while (board.validPosition(new Position(currentRow,currentCol))) {
            if (board.getPieceByPosition(moveTo.getTo()).isEmpty()){
                list.add(moveTo.getTo());
            }  else if(board.getPieceByPosition(moveTo.getTo()).get().getPieceId().equals('K') &&
                    board.getPieceByPosition(moveTo.getTo()).get().getColor() !=
                            board.getPieceByPosition(initialPos.getFrom()).get().getColor()) {
                list.add(moveTo.getTo());
                break;
            }else {
                break;
            }
            currentRow += directions.get(1);
            currentCol += directions.get(0);
            moveTo = new MoveTo(moveTo.getFrom(),new Position(currentRow, currentCol));
        }
        return list;
    }
}
