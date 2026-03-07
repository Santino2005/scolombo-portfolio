package edu.austral.dissis.chess.engine.Chess.Rules;

import edu.austral.dissis.chess.engine.Chess.Piece.ChessPiece;
import edu.austral.dissis.chess.engine.Chess.Piece.Valuate;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.engine.Commons.Rules.Rules;

import java.util.*;

public class CheckRules implements Rules {

    @Override
    public boolean check(MoveTo moveTo, PieceMaker piece, TurnsManager turnsManager, Board board) {
        Map<Position, PieceMaker> simulatedBoard = new HashMap<>(board.getCells());
        simulatedBoard.remove(moveTo.getFrom());
        simulatedBoard.put(moveTo.getTo(), board.getPieceByPosition(moveTo.getFrom()).get());
        Board simulated = new Board(board.getRowMax(), board.getColMax(), simulatedBoard);
        List<Position> newPosList = new ArrayList<>();
        Position kingpos = null;
        Color currentColor = simulated.getPieceByPosition(moveTo.getTo()).get().getColor();
        Color opponentColor = (currentColor == Color.WHITE) ? Color.BLACK : Color.WHITE;
        for(Map.Entry<Position, PieceMaker> pieceEntry : simulated.getCells().entrySet()){
            if(pieceEntry.getValue().getPieceId().equals('K') && pieceEntry.getValue().getColor() != opponentColor){
                kingpos = pieceEntry.getKey();
                break;
            }
        }
        for(Map.Entry<Position, PieceMaker> pieceEntry : simulated.getCells().entrySet()){
            if(pieceEntry.getValue().getColor() == opponentColor) {
                ChessPiece chessPiece = (ChessPiece) pieceEntry.getValue();
                for (Valuate possibleMove : chessPiece.getPieceMoves()) {
                    newPosList.addAll(possibleMove.possiblePosition(new MoveTo(pieceEntry.getKey(),moveTo.getTo()), simulated));
                }
            }
        }
        return !newPosList.contains(kingpos);
    }
}