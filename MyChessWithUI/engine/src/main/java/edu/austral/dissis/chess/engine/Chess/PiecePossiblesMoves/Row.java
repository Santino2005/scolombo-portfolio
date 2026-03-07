package edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Row implements PossibleMoves {

    private final List<Integer> directions = new ArrayList<>();
    public Row(boolean top, boolean bottom){
        if(top){
            directions.add(1);
        }
        if(bottom){
            directions.add(-1);
        }
    }

    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        List<Position> positions = new ArrayList<>();
        for (int direction : directions){
            positions.addAll(checkWays(moveTo, board, direction));
        }
        return positions;
    }
    private List<Position> checkWays(MoveTo initialPos, Board board, int direction) {
        List<Position> list = new ArrayList<>();
        Optional<PieceMaker> minitialPiece = board.getPieceByPosition(initialPos.getFrom());
        int currentRow = initialPos.getFrom().row();
        int currentCol = initialPos.getFrom().column() + direction;
        Position actualPosition = new Position(currentRow, currentCol);
        while (board.validPosition(actualPosition)) {
            Optional<PieceMaker> maybePiece = board.getPieceByPosition(actualPosition);

            if (maybePiece.isEmpty()) {
                list.add(actualPosition);
            } else if (maybePiece.get().getPieceId().equals('K') &&
                    maybePiece.get().getColor() !=
                           minitialPiece.get().getColor()) {
                list.add(actualPosition);
                break;
            } else {
                break;
            }
            currentCol += direction;
            actualPosition = new Position(currentRow, currentCol);
        }
        return list;
    }
}
