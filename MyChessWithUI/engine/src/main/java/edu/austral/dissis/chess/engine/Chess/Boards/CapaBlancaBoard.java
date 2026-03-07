package edu.austral.dissis.chess.engine.Chess.Boards;

import edu.austral.dissis.chess.engine.Commons.Board.BoardCreation;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.HashMap;
import java.util.Map;

public class CapaBlancaBoard implements BoardCreation {
    @Override
    public Map<Position, PieceMaker> create(FactoryPieces factoryPieces) {
        Map<Position, PieceMaker> initialCells = new HashMap<>();
        addKing(initialCells,factoryPieces);
        addKnight(initialCells,factoryPieces);
        addQueen(initialCells,factoryPieces);
        addPawn(initialCells,factoryPieces);
        addRook(initialCells,factoryPieces);
        addBishop(initialCells,factoryPieces);
        addArchbishop(initialCells,factoryPieces);
        addChancellor(initialCells,factoryPieces);
        return initialCells;
    }

    private Map<Position, PieceMaker> addChancellor(
            Map<Position, PieceMaker> mapToAdd, FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 8), pieces.createChancellor("chancellor", Color.WHITE, "18", 'C', false));
       mapToAdd.put(new Position(8, 8), pieces.createChancellor("chancellor", Color.BLACK, "88", 'C', false));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addArchbishop(
            Map<Position, PieceMaker> mapToAdd, FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 3), pieces.createArchbishop("archbishop", Color.WHITE, "13", 'A'));
        mapToAdd.put(new Position(8, 3), pieces.createArchbishop("archbishop", Color.BLACK, "83", 'A'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addBishop(
            Map<Position, PieceMaker> mapToAdd, FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 4), pieces.createBishop("bishop", Color.WHITE, "14", 'B'));
        mapToAdd.put(new Position(1, 7), pieces.createBishop("bishop", Color.WHITE, "17", 'B'));
        mapToAdd.put(new Position(8, 4), pieces.createBishop("bishop", Color.BLACK, "84", 'B'));
        mapToAdd.put(new Position(8, 7), pieces.createBishop("bishop", Color.BLACK, "87", 'B'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addRook(Map<Position, PieceMaker> mapToAdd, FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 10), pieces.createRook("rook", Color.WHITE, "110", 'R'));
        mapToAdd.put(new Position(1, 1), pieces.createRook("rook", Color.WHITE, "11", 'R'));
        mapToAdd.put(new Position(8, 1), pieces.createRook("rook", Color.BLACK, "81", 'R'));
        mapToAdd.put(new Position(8, 10), pieces.createRook("rook", Color.BLACK, "810", 'R'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addQueen(Map<Position, PieceMaker> mapToAdd, FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 5), pieces.createQueen("queen", Color.WHITE, "15", 'Q'));
        mapToAdd.put(new Position(8, 5), pieces.createQueen("queen", Color.BLACK, "85", 'Q'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addKing(Map<Position, PieceMaker> mapToAdd,FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 6), pieces.createKing("king", Color.WHITE, "16", 'K'));
        mapToAdd.put(new Position(8, 6), pieces.createKing("king", Color.BLACK, "86", 'K'));
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addPawn(Map<Position, PieceMaker> mapToAdd,FactoryPieces pieces) {
        for (int i = 1; i < 11; i++) {
            mapToAdd.put(new Position(7, i), pieces.createPawn("pawn",  Color.BLACK,"7"+i, 'P'));
        }
        for (int i = 1; i < 11; i++) {
            mapToAdd.put(new Position(2, i), pieces.createPawn("pawn",  Color.WHITE, "2"+i,'P'));
        }
        return mapToAdd;
    }

    private Map<Position, PieceMaker> addKnight(
            Map<Position, PieceMaker> mapToAdd,FactoryPieces pieces) {
        mapToAdd.put(new Position(1, 2), pieces.createKnight("knight", Color.WHITE, "12", 'N'));
        mapToAdd.put(new Position(1, 9), pieces.createKnight("knight", Color.WHITE, "19", 'N'));
        mapToAdd.put(new Position(8, 2), pieces.createKnight("knight", Color.BLACK, "82", 'N'));
        mapToAdd.put(new Position(8, 9), pieces.createKnight("knight", Color.BLACK, "89", 'N'));
        return mapToAdd;
    }
}
