package edu.austral.dissis.chess.engine.Checkers.CheckersMovesValidator;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Moves.MoveValidator;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

import java.util.ArrayList;
import java.util.List;

public class CheckersMovesValidator implements Valid {

    private final List<MoveValidator> moves;

    public CheckersMovesValidator(List<MoveValidator> moves) {
        this.moves = moves;
    }
    @Override
    public boolean valid(MoveTo moveTo, Board board) {
        return checkMoveValidator(moveTo,board);
    }

    private boolean checkMoveValidator(MoveTo moveTo, Board board){
        for(MoveValidator moves : moves){
            if(moves.valid(moveTo,board)){
                return true;
            }
        }
        return false;
    }

}
