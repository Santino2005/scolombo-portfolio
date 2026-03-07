package edu.austral.dissis.chess.engine.Commons.Rules;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;

public class BasicRulesValidator implements Rules {

    @Override
    public boolean check(MoveTo moveTo, PieceMaker piece, TurnsManager turnsManager, Board board) {
        if (!board.validPosition(moveTo.getTo())) {
            return false;
        }
        boolean validMove = false;
        for (Valid possibleMove : piece.getValidPiecesMoves()) {
            if (possibleMove.valid(moveTo, board)) {
                validMove = true;
                break;
            }
        }
        return validMove;
    }
}
