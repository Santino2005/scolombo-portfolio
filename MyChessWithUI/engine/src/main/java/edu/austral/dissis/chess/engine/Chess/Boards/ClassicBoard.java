package edu.austral.dissis.chess.engine.Chess.Boards;

import edu.austral.dissis.chess.engine.Commons.Board.BoardCreation;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.Map;

public class ClassicBoard implements BoardCreation {
    @Override
    public Map<Position, PieceMaker> create(FactoryPieces factoryPieces) {
        Map<Position, PieceMaker> initialCells = new HashMap<>();
        addBishop(initialCells, factoryPieces);
        addRook(initialCells, factoryPieces);
        addKnight(initialCells, factoryPieces);
        addQueen(initialCells, factoryPieces);
        addKing(initialCells, factoryPieces);
        addPawn(initialCells, factoryPieces);
        return initialCells;
    }
    private Map<Position, PieceMaker> addPawn(Map<Position, PieceMaker> mapToAdd, FactoryPieces factoryPieces) {
        for (int i = 1; i < 9; i++) {
            mapToAdd.put(new Position(7, i), factoryPieces.createPawn("pawn", Color.BLACK, "7"+i, 'P'));
        }
        for (int i = 1; i < 9; i++) {
            mapToAdd.put(new Position(2, i), factoryPieces.createPawn("pawn", Color.WHITE, "2"+i, 'P'));
        }
        return mapToAdd;
    }
    private Map<Position, PieceMaker> addBishop(
            Map<Position, PieceMaker> mapToAdd, FactoryPieces factoryPieces) {
        mapToAdd.put(new Position(1, 3), factoryPieces.createBishop("bishop", Color.WHITE, "13", 'B'));
        mapToAdd.put(new Position(1, 6), factoryPieces.createBishop("bishop", Color.WHITE, "16", 'B'));
        mapToAdd.put(new Position(8, 3), factoryPieces.createBishop("bishop", Color.BLACK, "83", 'B'));
        mapToAdd.put(new Position(8, 6), factoryPieces.createBishop("bishop", Color.BLACK, "86", 'B'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addRook(Map<Position, PieceMaker> mapToAdd, FactoryPieces factoryPieces) {
        mapToAdd.put(new Position(1, 1), factoryPieces.createRook("rook", Color.WHITE, "11", 'R'));
        mapToAdd.put(new Position(1, 8), factoryPieces.createRook("rook", Color.WHITE, "18", 'R'));
        mapToAdd.put(new Position(8, 1), factoryPieces.createRook("rook", Color.BLACK, "81", 'R'));
        mapToAdd.put(new Position(8, 8), factoryPieces.createRook("rook", Color.BLACK, "88", 'R'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addQueen(Map<Position, PieceMaker> mapToAdd, FactoryPieces factoryPieces) {
        mapToAdd.put(new Position(1, 4), factoryPieces.createQueen("queen", Color.WHITE, "14", 'Q'));

        mapToAdd.put(new Position(8, 4), factoryPieces.createQueen("queen", Color.BLACK, "84", 'Q'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addKing(Map<Position, PieceMaker> mapToAdd, FactoryPieces factoryPieces) {
        mapToAdd.put(new Position(1, 5), factoryPieces.createKing("king", Color.WHITE, "15", 'K'));
        mapToAdd.put(new Position(8, 5), factoryPieces.createKing("king", Color.BLACK, "85", 'K'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addKnight(
            Map<Position, PieceMaker> mapToAdd, FactoryPieces factoryPieces) {
        mapToAdd.put(new Position(1, 2), factoryPieces.createKnight("knight", Color.WHITE, "12", 'N'));
        mapToAdd.put(new Position(1, 7), factoryPieces.createKnight("knight", Color.WHITE, "17", 'N'));
        mapToAdd.put(new Position(8, 2), factoryPieces.createKnight("knight", Color.BLACK, "82", 'N'));
        mapToAdd.put(new Position(8, 7), factoryPieces.createKnight("knight", Color.BLACK, "87", 'N'));
        return mapToAdd;
    }
}
