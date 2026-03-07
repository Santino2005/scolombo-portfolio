package edu.austral.dissis.chess.engine.Checkers.Rules;

import edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions.Capture;
import edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions.EatIfIsPossible;
import edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions.OpponentInTheMiddle;
import edu.austral.dissis.chess.engine.Commons.Moves.CommonsConditionsToMove.IsPieceNotOnRoad;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.engine.Commons.Rules.Rules;

import java.util.Map;

public class CheckEveryPieceIfItCanEat implements Rules {


    @Override
    public boolean check(MoveTo moveTo, PieceMaker piece, TurnsManager turnsManager, Board board) {
        if(new IsPieceNotOnRoad().valid(moveTo, board)
                && new Capture().valid(moveTo,board)
                && new OpponentInTheMiddle().valid(moveTo, board)){
            return true;
        }
        for (Map.Entry<Position, PieceMaker> entry : board.getCells().entrySet()) {
            Position pos = entry.getKey();
            PieceMaker otherPiece = entry.getValue();
            if (otherPiece.getColor().equals(piece.getColor())) {
                if (!((new EatIfIsPossible().valid(new MoveTo(pos, pos), board)))) {
                    return false;
                }
            }
        }
        return true;
    }

}
