package edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions;

import edu.austral.dissis.chess.engine.Chess.Piece.ChessPiece;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

import java.util.Optional;

public class IsFirstMove implements Valid {

    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        Optional<PieceMaker> fromPiece = board.getPieceByPosition(moveTo.getFrom());
        if(fromPiece.isEmpty()){
            return false;
        }
        ChessPiece chessPiece = (ChessPiece) fromPiece.get();
        return !chessPiece.hasMove();
    }
}
