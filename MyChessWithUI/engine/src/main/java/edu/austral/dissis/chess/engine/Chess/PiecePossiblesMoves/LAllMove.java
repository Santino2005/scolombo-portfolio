package edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LAllMove implements PossibleMoves {

    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        List<Position> allMoves = new ArrayList<>();
        List<Position> positions = getPositions(moveTo);
        for (Position pos : positions) {
            if(board.validPosition(pos)){
                if(board.getPieceByPosition(pos).isEmpty()){
                    allMoves.add(pos);
                }else if(board.getPieceByPosition(pos).get().getPieceId().equals('K') &&
                        board.getPieceByPosition(pos).get().getColor() !=
                                board.getPieceByPosition(moveTo.getFrom()).get().getColor()) {
                    allMoves.add(pos);
                    break;
                }
            }
        }
        return allMoves;
    }

    @NotNull
    private List<Position> getPositions(MoveTo moveTo) {
        int row = moveTo.getFrom().row();
        int col = moveTo.getFrom().column();
        return List.of(
                new Position(row + 2, col + 1),
                new Position(row + 2, col - 1),
                new Position(row - 2, col + 1),
                new Position(row - 2, col - 1),
                new Position(row + 1, col + 2),
                new Position(row + 1, col - 2),
                new Position(row - 1, col + 2),
                new Position(row - 1, col - 2));
    }

}
