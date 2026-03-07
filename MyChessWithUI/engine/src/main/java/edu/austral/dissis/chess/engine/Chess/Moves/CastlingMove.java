package edu.austral.dissis.chess.engine.Chess.Moves;

import edu.austral.dissis.chess.engine.Chess.Piece.ChessPiece;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CastlingMove implements SpecialMoves {

    private final List<Valid> validList;
    public CastlingMove(List<Valid> validList) {
        this.validList = validList;
    }

    @Override
    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board) {
        int rowDiff = Math.abs(moveTo.getTo().row() - moveTo.getFrom().row());
        int colDiff = Math.abs(moveTo.getTo().column() - moveTo.getFrom().column());
        if (rowDiff != 0 || colDiff != 2){
            return false;
        }
        int rookColumn = moveTo.getTo().column() > moveTo.getFrom().column() ? board.getColMax() : 1;
        Position rookPos = new Position(moveTo.getFrom().row(), rookColumn);
        MoveTo rookMove = new MoveTo(moveTo.getFrom(), rookPos);
        for(Valid valid : validList){
            if(!valid.valid(rookMove, board)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board) {
        Map<Position, PieceMaker> newCells = new HashMap<>(board.getCells());

        int colDiff = moveTo.getTo().column() - moveTo.getFrom().column();
        Optional<PieceMaker> king = board.getPieceByPosition(moveTo.getFrom());
        newCells.remove(moveTo.getFrom());
        int rookCol = colDiff > 0 ? board.getColMax() : 1;
        Position rookPos = new Position(moveTo.getFrom().row(), rookCol);
        Optional<PieceMaker> rook = board.getPieceByPosition(rookPos);
        Position newRookPos = colDiff > 0 ?
                new Position(moveTo.getFrom().row(), moveTo.getTo().column() - 1)
                : new Position(moveTo.getFrom().row(), moveTo.getTo().column() + 1);
        newCells.remove(rookPos);
        ChessPiece chessKing = (ChessPiece) king.get();
        ChessPiece chessRook = (ChessPiece) rook.get();
        newCells.put(moveTo.getTo(), chessKing.move());
        newCells.put(newRookPos, chessRook.move());
        return newCells;
    }
}
