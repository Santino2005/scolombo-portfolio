package edu.austral.dissis.chess.engine.Commons.Rules;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;

public class TurnsRulesValidator implements Rules {
    @Override
    public boolean check(MoveTo moveTo, PieceMaker piece, TurnsManager turnsManager, Board board) {
        return turnsManager.playerToMove() == piece.getColor();
    }
}
