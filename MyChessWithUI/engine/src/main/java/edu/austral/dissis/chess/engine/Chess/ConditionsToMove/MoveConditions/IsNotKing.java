package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.MoveConditions;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

import java.util.Optional;

public class IsNotKing implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        Optional<PieceMaker> piece = board.getPieceByPosition(moveTo.getTo());
        return piece.isEmpty() || piece.get().getPieceId() != 'K';
    }


}
