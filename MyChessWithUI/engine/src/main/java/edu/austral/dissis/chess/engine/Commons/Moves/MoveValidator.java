package edu.austral.dissis.chess.engine.Commons.Moves;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.Valid;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;

import java.util.List;

public class MoveValidator implements Valid {
    private final List<Valid> conditions;

    public MoveValidator(List<Valid> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean valid(MoveTo moveTo, Board board){
        for(Valid conditions : conditions){
            if(!conditions.valid(moveTo,board)){
                return false;
            }
        }
        return true;
    }
}