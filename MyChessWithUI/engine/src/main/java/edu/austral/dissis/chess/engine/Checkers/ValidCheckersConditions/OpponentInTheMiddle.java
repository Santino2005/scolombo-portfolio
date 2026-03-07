package edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.Optional;

public class OpponentInTheMiddle implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        Position from = moveTo.getFrom();
        Position to = moveTo.getTo();
        int middleRow = (from.row() + to.row()) / 2;
        int middleCol = (from.column() + to.column()) / 2;
        Position middle = new Position(middleRow, middleCol);

        Optional<PieceMaker> middlePiece = board.getPieceByPosition(middle);
        Optional<PieceMaker> fromPiece = board.getPieceByPosition(from);

        return middlePiece.isPresent()
                && fromPiece.isPresent()
                && !middlePiece.get().getColor().equals(fromPiece.get().getColor());
    }

}
