package edu.austral.dissis.chess.engine.Chess.PieceMovesValidator;

import edu.austral.dissis.chess.engine.Chess.Piece.Valuate;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Moves.MoveValidator;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import java.util.ArrayList;
import java.util.List;

public class PieceMovesValidator implements Valuate {

    private final List<MoveValidator> moves;
    private final AllPossibleMoves allPossibleMoves;

    public PieceMovesValidator(List<MoveValidator> moves, AllPossibleMoves allPossibleMoves) {
        this.moves = moves;
        this.allPossibleMoves = allPossibleMoves;
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
    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        return new ArrayList<>(allPossibleMoves.possiblePosition(moveTo, board));
    }
}
