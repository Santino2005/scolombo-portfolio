package edu.austral.dissis.chess.engine.Checkers.Moves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.Map;

public class CheckersPromotion implements SpecialMoves {

    @Override
    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board) {
        int finalRow = moveTo.getTo().row();
        Color color = piece.getColor();

        return (color == Color.WHITE && finalRow == board.getRowMax())
                || (color == Color.BLACK && finalRow == 1);
    }

    @Override
    public Map<Position, PieceMaker> execute(MoveTo moveTo, PieceMaker piece, Board board) {
        Map<Position, PieceMaker> newCells = new HashMap<>(board.getCells());
        Position from = moveTo.getFrom();
        Position to = moveTo.getTo();
        int middleRow = (from.row() + to.row()) / 2;
        int middleCol = (from.column() + to.column()) / 2;
        Position captured = new Position(middleRow, middleCol);
        newCells.remove(from);
        newCells.remove(captured);
        newCells.put(moveTo.getTo(), new FactoryPieces().createCheckerQueen("queen", piece.getColor(), piece.getId(), 'Q'));
        return newCells;
    }

}
