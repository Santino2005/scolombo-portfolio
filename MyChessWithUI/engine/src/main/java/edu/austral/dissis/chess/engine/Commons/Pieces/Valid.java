package edu.austral.dissis.chess.engine.Commons.Pieces;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public interface Valid {

    public boolean valid(MoveTo moveTo, Board board);
}
