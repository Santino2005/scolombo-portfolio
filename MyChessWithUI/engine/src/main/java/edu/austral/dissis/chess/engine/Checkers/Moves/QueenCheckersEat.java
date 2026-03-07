package edu.austral.dissis.chess.engine.Checkers.Moves;

import edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions.OpponentOneSquareBehind;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.Map;

public class QueenCheckersEat implements SpecialMoves {
    @Override
    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board) {
        return new OpponentOneSquareBehind().valid(moveTo, board)
                && board.getPieceByPosition(moveTo.getFrom()).get().getColor() != piece.getColor();
    }

    @Override
    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board) {
        // A revisar pq no come en ninguna direccion
        Map<Position, PieceMaker> newCells = new HashMap<>(board.getCells());
        Position from = moveTo.getFrom();
        Position to = moveTo.getTo();
        int rowStep = Integer.compare(to.row(), from.row());
        int colStep = Integer.compare(to.column(), from.column());
        int beforeRow = to.row() - rowStep;
        int beforeCol = to.column() - colStep;
        Position captured = new Position(beforeRow, beforeCol);
        newCells.remove(from);
        newCells.remove(captured);
        newCells.put(to,piece);
        return newCells;
    }
}
