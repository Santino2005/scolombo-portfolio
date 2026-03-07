package edu.austral.dissis.chess.engine.Checkers.Piece;

import edu.austral.dissis.chess.engine.Checkers.CheckersMovesValidator.CheckersMovesValidator;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;

import java.util.*;

public class CheckersPiece implements PieceMaker {

    private final String id;
    private final Color color;
    private final String name;
    private final Character pieceId;
    private final CheckersMovesValidator validPiecesMoves;
    private final List<SpecialMoves> specialMoves;

    public CheckersPiece(String id, Color color, String name, Character pieceId,
                      List<SpecialMoves> specialMoves, CheckersMovesValidator validPiecesMoves) {
        this.id = id;
        this.color = color;
        this.name = name;
        this.pieceId = pieceId;
        this.validPiecesMoves = validPiecesMoves;
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
    @Override
    public Character getPieceId(){
        return this.pieceId;
    }
    @Override
    public List<SpecialMoves> getPieceSpecialMoves() {
        return specialMoves;
    }
    @Override
    public List<Valid> getValidPiecesMoves(){
        return Collections.singletonList(validPiecesMoves);
    }

}
