package edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

public class OpponentOneSquareBehind implements Valid {
    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        Position from = moveTo.getFrom();
        Position to = moveTo.getTo();

        int rowStep = Integer.compare(to.row(), from.row());
        int colStep = Integer.compare(to.column(), from.column());

        int beforeRow = to.row() - rowStep;
        int beforeCol = to.column() - colStep;
        Position beforeTo = new Position(beforeRow, beforeCol);

        if (!board.validPosition(beforeTo)){
            return false;
        }

        return board.getPieceByPosition(beforeTo).isPresent();
    }
}
