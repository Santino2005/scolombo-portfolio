package edu.austral.dissis.chess.engine.Checkers.TurnsManager;

import edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions.EatIfIsPossible;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsRules;

public class CheckersAfterEatTurnRule implements TurnsRules {

    @Override
    public boolean checkTurnRule(MoveTo lastMove, Board board) {
        if (!wasCapture(lastMove)) {
            return true;
        }
        return !canPieceKeepEating(lastMove, board);
    }

    @Override
    public TurnsManager PlayerMoved(TurnsManager currentManager) {
        return currentManager.PlayerMoved();
    }

    private boolean wasCapture(MoveTo move) {
        int rowDiff = Math.abs(move.getFrom().row() - move.getTo().row());
        int colDiff = Math.abs(move.getFrom().column() - move.getTo().column());
        return rowDiff > 1 && colDiff > 1;
    }

    private boolean canPieceKeepEating(MoveTo lastMove, Board board) {
        return !new EatIfIsPossible().valid(new MoveTo(lastMove.getTo(), lastMove.getTo()), board);
    }
}
