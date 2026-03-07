package edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;

public class EatIfIsPossible implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board){
        List<Position> allMoves = new ArrayList<>();
        if(board.getPieceByPosition(moveTo.getFrom()).isEmpty()){
            return false;
        }
        Color color = board.getPieceByPosition(moveTo.getFrom()).get().getColor();
        int dir = (Color.WHITE == color) ? 1 : -1;
        Position digR = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column() + 1);
        Position digTwoR = new Position(moveTo.getFrom().row() + 2* dir, moveTo.getFrom().column() + 2);
        Position digL = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column() - 1);
        Position digTwoL = new Position(moveTo.getFrom().row() + 2 * dir, moveTo.getFrom().column() - 2);
        if (board.validPosition(digR) && board.validPosition(digTwoR) && board.getPieceByPosition(digTwoR).isEmpty()
                && (board.getPieceByPosition(digR).isPresent() && board.getPieceByPosition(digR).get().getColor() != color)) {
            allMoves.add(digR);
        }
        if (board.validPosition(digL) && board.validPosition(digTwoL) && board.getPieceByPosition(digTwoL).isEmpty()
                && (board.getPieceByPosition(digL).isPresent() && board.getPieceByPosition(digL).get().getColor() != color)) {
            allMoves.add(digL);
        }
        return allMoves.isEmpty();
    }
}
