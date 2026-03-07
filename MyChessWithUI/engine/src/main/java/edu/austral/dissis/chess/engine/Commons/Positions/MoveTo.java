package edu.austral.dissis.chess.engine.Commons.Positions;

import edu.austral.dissis.chess.engine.Commons.Positions.Position;

public class MoveTo {

    private final Position from;
    private final Position to;

    public MoveTo(Position from, Position to) {
        this.from = from;
        this.to = to;
    }
    public Position getFrom(){
        return from;
    }
    public Position getTo(){
        return to;
    }
}
