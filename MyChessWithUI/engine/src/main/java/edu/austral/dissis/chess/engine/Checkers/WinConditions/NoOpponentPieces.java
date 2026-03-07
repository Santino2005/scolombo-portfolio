package edu.austral.dissis.chess.engine.Checkers.WinConditions;

import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.engine.Commons.WinCondition.WinConditions;

import java.util.Map;

public class NoOpponentPieces implements WinConditions {
    @Override
    public boolean winner(Color color, Game game) {
        Map<Position, PieceMaker> boardPieces = game.getCurrentBoard().getCells();
        for (PieceMaker piece : boardPieces.values()) {
            if (piece.getColor() != color) {
                return false;
            }
        }
        return true;
    }
}
