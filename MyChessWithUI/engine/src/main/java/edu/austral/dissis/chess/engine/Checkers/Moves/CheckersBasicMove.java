package edu.austral.dissis.chess.engine.Checkers.Moves;

import edu.austral.dissis.chess.engine.Checkers.Piece.CheckersPiece;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.Map;

public class CheckersBasicMove implements SpecialMoves {
    @Override
    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board) {
        for(SpecialMoves specialMoves : piece.getPieceSpecialMoves()){
            if(!(specialMoves instanceof CheckersBasicMove) && specialMoves.isSpecialMove(piece, moveTo, board)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board) {
        Map<Position, PieceMaker> newCells = new HashMap<>(board.getCells());
        CheckersPiece checkersPiece = (CheckersPiece) piece;
        newCells.remove(moveTo.getFrom());
        newCells.remove(moveTo.getTo());
        newCells.put(moveTo.getTo(), checkersPiece);
        return newCells;
    }
}
