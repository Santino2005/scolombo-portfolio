package edu.austral.dissis.chess.engine.Checkers.Boards;

import edu.austral.dissis.chess.engine.Commons.Board.BoardCreation;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import java.util.HashMap;
import java.util.Map;

//Comment just to Commit

public class CheckersBoard implements BoardCreation {
    @Override
    public Map<Position, PieceMaker> create(FactoryPieces factoryPieces) {
        Map<Position, PieceMaker> initialCells = new HashMap<>();

        for (int row = 1; row <= 3; row++) {
            for (int col = 1; col <= 8; col++) {
                if ((row + col) % 2 == 0) {
                    initialCells.put(new Position(row, col),
                            factoryPieces.createChecker("pawn", Color.WHITE, row + "" + col, 'D'));
                }
            }
        }

        for (int row = 6; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                if ((row + col) % 2 == 0) {
                    initialCells.put(new Position(row, col),
                            factoryPieces.createChecker("pawn", Color.BLACK, row + "" + col, 'D'));
                }
            }
        }

        return initialCells;
    }
}
