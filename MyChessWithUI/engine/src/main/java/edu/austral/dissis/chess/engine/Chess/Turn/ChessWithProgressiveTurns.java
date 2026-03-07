package edu.austral.dissis.chess.engine.Chess.Turn;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsRules;

public class ChessWithProgressiveTurns implements TurnsRules {
    private int progressive;
    private int movesThisTurn = 0;
    private final TurnsManager turnsManager;

    public ChessWithProgressiveTurns(int start, TurnsManager turnsManager) {
        this.progressive = start;
        this.turnsManager = turnsManager;
    }

    @Override
    public boolean checkTurnRule(MoveTo lastMove, Board board) {
        if(board.getPieceByPosition(lastMove.getFrom()).isEmpty()){
            return false;
        }
        Color color = board.getPieceByPosition(lastMove.getFrom()).get().getColor();
        return color == turnsManager.playerToMove();
    }
    @Override
    public TurnsManager PlayerMoved(TurnsManager turnsManager) {
        movesThisTurn++;
        if (movesThisTurn >= progressive) {
            progressive++;
            movesThisTurn = 0;
            return turnsManager.PlayerMoved();
        }
        return turnsManager.PlayerKeepsPlaying();
    }
}
