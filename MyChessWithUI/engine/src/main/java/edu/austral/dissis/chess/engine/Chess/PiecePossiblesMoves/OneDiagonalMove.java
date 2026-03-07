package edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;

public class OneDiagonalMove implements PossibleMoves {

    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        List<Position> allMoves = new ArrayList<>();
        Color color = board.getPieceByPosition(moveTo.getFrom()).get().getColor();
        int dir = (Color.WHITE == color) ? 1 : -1;
        Position digR = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column() + 1);
        Position digL = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column() - 1);

        if (board.validPosition(digR)
                && (board.getPieceByPosition(digR).isPresent() && board.getPieceByPosition(digR).get().getColor() != color)) {
            allMoves.add(digR);
        }
        if (board.validPosition(digL)
                && (board.getPieceByPosition(digL).isPresent() && board.getPieceByPosition(digL).get().getColor() != color)) {
            allMoves.add(digL);
        }
        return allMoves;
    }
}
