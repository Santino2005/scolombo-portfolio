package edu.austral.dissis.chess.engine.Chess.PieceMovesValidator;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PossibleMoves;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;

import java.util.ArrayList;
import java.util.List;

public class AllPossibleMoves implements PossibleMoves {
    private final List<PossibleMoves> possibleMoves;

    public AllPossibleMoves(List<PossibleMoves> possibleMoves) {
        this.possibleMoves = possibleMoves;
    }

    @Override
    public List<Position> possiblePosition(MoveTo moveTo, Board board) {
        List<Position> addPositions = new ArrayList<>();
        for(PossibleMoves moves : possibleMoves){
            addPositions.addAll(moves.possiblePosition(moveTo,board));
        }
        return addPositions;
    }
}
