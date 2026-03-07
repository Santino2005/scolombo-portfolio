package edu.austral.dissis.chess.engine.Commons.Board;

import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;
import edu.austral.dissis.chess.engine.Commons.Moves.SpecialMoves;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.engine.Commons.Rules.Rules;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;

import java.util.*;

public class Board {

    private final int rowMax;
    private final int colMax;
    private final Map<Position, PieceMaker> cells;

    public Board(int rowMax, int colMax, Map<Position, PieceMaker> cells) {
        this.rowMax = rowMax;
        this.colMax = colMax;
        this.cells = Collections.unmodifiableMap(cells);
    }
    public int getRowMax(){
        return rowMax;
    }
    public int getColMax(){
        return colMax;
    }
    public Map<Position, PieceMaker> getCells(){
        return cells;
    }

    public Game move(MoveTo moveTo, PieceMaker piece, TurnsManager turnsManager, List<Rules> rules){

        Map<Position, PieceMaker> newCells = new HashMap<>(cells);
        Board simulatedBoard = new Board(rowMax,colMax,move(moveTo, piece, newCells));
        return new Game(simulatedBoard, turnsManager.PlayerMoved(), rules);
    }

    public Optional<PieceMaker> getPieceByPosition(Position position){
        return Optional.ofNullable(cells.get(position));
    }

    public Optional<Position> getPositionByPiece(PieceMaker piece, Color color){
        Position pos = null;
        for(Map.Entry<Position,PieceMaker>  search : cells.entrySet()){
            if(search.getValue().getId().equals(piece.getId()) && color.equals(search.getValue().getColor())){
                pos = search.getKey();
            }
        }
        return Optional.ofNullable(pos);
    }

    private Map<Position, PieceMaker> move(MoveTo moveTo, PieceMaker piece, Map<Position, PieceMaker> originalCells){
        Map<Position, PieceMaker> newCells = new HashMap<>(originalCells);
        SpecialMoves moveToDo = getSpecialMove(piece, moveTo, this);
        if(moveToDo == null){return newCells;}
        return moveToDo.execute(moveTo, piece, this);
    }
    private SpecialMoves getSpecialMove(PieceMaker piece, MoveTo moveTo, Board board){
        SpecialMoves moveToDo = null;
        for (SpecialMoves specialMoves : piece.getPieceSpecialMoves()) {
            if (specialMoves.isSpecialMove(piece, moveTo, board)) {
                moveToDo = specialMoves;
            }
        }
        return moveToDo;
    }

    public boolean validPosition(Position moveTo){
        int finalPosRow = moveTo.row();
        int finalPosCol = moveTo.column();
        return finalPosRow <= this.rowMax && finalPosCol <= this.colMax && finalPosRow > 0 && finalPosCol > 0;
    }
}
