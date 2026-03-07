package edu.austral.dissis.chess.engine.Commons.Pieces;

import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;

import java.util.List;

public interface PieceMaker {
    public String getId();
    public String getName();
    public Color getColor();
    public Character getPieceId();
    public List<SpecialMoves> getPieceSpecialMoves();
    public List<Valid> getValidPiecesMoves();
}
