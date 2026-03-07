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
import java.util.Optional;

public class CheckersEat implements SpecialMoves {

    @Override
    public boolean isSpecialMove(PieceMaker piece, MoveTo moveTo, Board board) {
        Color color = board.getPieceByPosition(moveTo.getFrom()).get().getColor();
        int dir = (Color.WHITE == color) ? 1 : -1;
        Position digR = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column() + 1);
        Position digL = new Position(moveTo.getFrom().row() + dir, moveTo.getFrom().column() - 1);
        Optional<PieceMaker> pieceByPosition = board.getPieceByPosition(digR);
        Optional<PieceMaker> piecePosition = board.getPieceByPosition(digL);
        return (piecePosition.isPresent() && color != piecePosition.get().getColor()
                || ( pieceByPosition.isPresent() && color != pieceByPosition.get().getColor()));
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
        if(to.row() == board.getRowMax() || to.row() == 1){
            newCells.put(to, new FactoryPieces().createCheckerQueen("queen", piece.getColor(), piece.getId(), 'Q'));
        }else {
            newCells.put(to, piece);
        }
        return newCells;
    }

}
