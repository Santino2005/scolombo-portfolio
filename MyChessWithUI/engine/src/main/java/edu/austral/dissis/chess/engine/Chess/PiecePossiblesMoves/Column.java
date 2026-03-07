package edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Column implements PossibleMoves {

    private final List<Integer> directions = new ArrayList<>();
    public Column(boolean right, boolean left){
        if(right){
            directions.add(1);
        }
        if(left){
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
        int currentRow = initialPos.getFrom().row() + direction;
        int currentCol = initialPos.getFrom().column();
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
            currentRow += direction;
            actualPosition = new Position(currentRow, currentCol);
        }
        return list;
    }

}
