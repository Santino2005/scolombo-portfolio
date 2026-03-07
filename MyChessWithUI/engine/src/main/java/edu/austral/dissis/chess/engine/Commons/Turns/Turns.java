package edu.austral.dissis.chess.engine.Commons.Turns;

import edu.austral.dissis.chess.engine.Commons.Colors.Color;

public interface Turns {

    TurnsManager PlayerMoved();

    TurnsManager PlayerKeepsPlaying();

    Color playerToMove();

    Color getOpponent();
}
