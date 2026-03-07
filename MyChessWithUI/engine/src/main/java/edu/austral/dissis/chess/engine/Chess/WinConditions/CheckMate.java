package edu.austral.dissis.chess.engine.Chess.WinConditions;

import edu.austral.dissis.chess.engine.Chess.Piece.ChessPiece;
import edu.austral.dissis.chess.engine.Chess.Piece.Valuate;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.engine.Commons.WinCondition.WinConditions;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CheckMate implements WinConditions {

    @Override
    public boolean winner(Color color, Game game) {
        Board board = game.getCurrentBoard();
        Optional<Position> kingPositionOpt = getKingPosition(board, color);
        if (kingPositionOpt.isEmpty()){
            return false;
        }

        Position kingPosition = kingPositionOpt.get();

        if (!isInCheck(kingPosition, board, color)){
            return false;
        }

        for (Map.Entry<Position, PieceMaker> entry : board.getCells().entrySet()) {
            Position from = entry.getKey();
            PieceMaker piece = entry.getValue();

            if (piece.getColor() != color){
                continue;
            }
            ChessPiece pieceChess = (ChessPiece) piece;
            for (Valuate moveLogic : pieceChess.getPieceMoves()) {
                List<Position> possibleTargets = moveLogic.possiblePosition(new MoveTo(from, from), board);

                for (Position to : possibleTargets) {
                    MoveTo move = new MoveTo(from, to);
                    Game simulatedGame = board.move(move, piece, game.getTurnToPlay(),game.getRules());
                    Board newBoard = simulatedGame.getCurrentBoard();
                    Position newKingPos = piece.getPieceId() == 'K' ? to : kingPosition;

                    if (!isInCheck(newKingPos, newBoard, color)) {
                        System.out.println("Jugada salvadora: " + from + " -> " + to);
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private boolean isInCheck(Position kingPos, Board board, Color kingColor) {
        Color opponentColor = (kingColor == Color.WHITE) ? Color.BLACK : Color.WHITE;

        for (Map.Entry<Position, PieceMaker> entry : board.getCells().entrySet()) {
            PieceMaker piece = entry.getValue();
            if (piece.getColor() != opponentColor){
                continue;
            }
            ChessPiece chessPiece = (ChessPiece) piece;
            for (Valuate moveLogic : chessPiece.getPieceMoves()) {
                List<Position> possibleTargets = moveLogic.possiblePosition(new MoveTo(entry.getKey(), kingPos), board);
                if (possibleTargets.contains(kingPos)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Optional<Position> getKingPosition(Board board, Color color) {
        for (Map.Entry<Position, PieceMaker> entry : board.getCells().entrySet()) {
            PieceMaker piece = entry.getValue();
            if (piece.getColor() == color && piece.getPieceId() == 'K') {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
}
