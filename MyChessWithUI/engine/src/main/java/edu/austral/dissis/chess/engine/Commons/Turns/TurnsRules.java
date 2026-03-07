package edu.austral.dissis.chess.engine.Commons.Turns;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

public interface TurnsRules {

    public boolean checkTurnRule(MoveTo lastMove, Board board);
    public TurnsManager PlayerMoved(TurnsManager turnsManager);

}
