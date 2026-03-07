package edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;

public class MoveToFront implements PossibleMoves {

    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        List<Position> allMoves = new ArrayList<>();

        int dir = (Color.WHITE == board.getPieceByPosition(moveTo.getFrom()).get().getColor()) ? 1 : -1;

        Position oneStepForward = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column());

        if (board.getPieceByPosition(oneStepForward).isEmpty()) {
            allMoves.add(oneStepForward);
        }
        return allMoves;
    }

}
