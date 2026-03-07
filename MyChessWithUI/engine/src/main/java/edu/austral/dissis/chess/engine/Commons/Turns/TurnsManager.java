package edu.austral.dissis.chess.engine.Commons.Turns;

import edu.austral.dissis.chess.engine.Commons.Colors.Color;

public class TurnsManager implements Turns {
    private final Color firstPlayer;
    private final Color opponent;
    public TurnsManager(Color color1, Color color2){
        this.firstPlayer = color1;
        this.opponent = color2;
    }
    @Override
    public TurnsManager PlayerMoved(){
        return new TurnsManager(opponent, firstPlayer);
    }
    @Override
    public TurnsManager PlayerKeepsPlaying(){
        return new TurnsManager(firstPlayer, opponent);
    }
    @Override
    public Color playerToMove() {
        return firstPlayer;
    }
    @Override
    public Color getOpponent() {
        return opponent;
    }
}
