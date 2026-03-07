package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.MoveConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

import java.util.Optional;

public class IsEnemyPieceOnPosition implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        Optional<PieceMaker> fromPiece = board.getPieceByPosition(moveTo.getFrom());
        Optional<PieceMaker> toPiece = board.getPieceByPosition(moveTo.getTo());
        return toPiece.isPresent() && fromPiece.isPresent()
                && fromPiece.get().getColor() != toPiece.get().getColor();
    }

}
