package edu.austral.dissis.chess.engine.Chess.Piece;

import edu.austral.dissis.chess.engine.Chess.PieceMovesValidator.PieceMovesValidator;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;

import java.util.*;

public class ChessPiece implements PieceMaker {

    private final String id;
    private final Color color;
    private final String name;
    private final Character pieceId;
    private final PieceMovesValidator pieceMoves;
    private final List<SpecialMoves> specialMoves;
    private final boolean hasMove;

    public ChessPiece(String id, Color color, String name, Character pieceId,
                      List<SpecialMoves> specialMoves, PieceMovesValidator pieceMoves) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.pieceId = pieceId;
        this.hasMove = false;
        this.pieceMoves = pieceMoves;
        this.specialMoves = specialMoves == null ? List.of() : List.copyOf(specialMoves);

    }

    private ChessPiece(String id, Color color, String name, Character pieceId,
                  boolean moved,List<SpecialMoves> specialMoves, PieceMovesValidator pieceMoves){
        this.id = id;
        this.color = color;
        this.name = name;
        this.pieceId = pieceId;
        this.pieceMoves = pieceMoves;
        this.hasMove = moved;
        this.specialMoves = specialMoves == null ? List.of() : List.copyOf(specialMoves);

    }

    @Override
    public String getId(){
        return this.id;
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public Color getColor(){
        return this.color;
    }
    public boolean hasMove(){
        return this.hasMove;
    }
    @Override
    public Character getPieceId(){
        return this.pieceId;
    }

    @Override
    public List<SpecialMoves> getPieceSpecialMoves() {
        return specialMoves;
    }
    @Override
    public List<Valid> getValidPiecesMoves() {
        return List.copyOf(Collections.singleton(pieceMoves));
    }
    public List<Valuate> getPieceMoves(){
        return List.copyOf(Collections.singleton(pieceMoves));
    }
    public ChessPiece move(){
        return new ChessPiece(id,color,name,pieceId,true, specialMoves,pieceMoves);
    }
}
