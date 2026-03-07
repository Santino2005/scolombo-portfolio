package edu.austral.dissis.chess.engine.Chess.Moves;

import edu.austral.dissis.chess.engine.Chess.Piece.ChessPiece;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.Map;

public class PromotionMove implements SpecialMoves {
    @Override
    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board) {
        return (moveTo.getTo().row() == board.getRowMax()
                && board.getRowMax() == moveTo.getFrom().row() + 1
                && board.getPieceByPosition(moveTo.getFrom()).get().getColor().equals(Color.WHITE)
                || moveTo.getTo().row() == 1
                && 1 == moveTo.getFrom().row() - 1
                && board.getPieceByPosition(moveTo.getFrom()).get().getColor().equals(Color.BLACK));
    }

    @Override
    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board) {
        Map<Position, PieceMaker> newCells = new HashMap<>(board.getCells());
        newCells.remove(moveTo.getFrom());
        ChessPiece queen = (ChessPiece) new FactoryPieces().createQueen("queen", piece.getColor(), piece.getId(), 'Q');
        newCells.put(moveTo.getTo(), queen.move());
        return newCells;
    }
}
