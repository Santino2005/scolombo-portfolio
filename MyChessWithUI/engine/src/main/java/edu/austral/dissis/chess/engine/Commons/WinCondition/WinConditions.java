package edu.austral.dissis.chess.engine.Commons.WinCondition;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;

public interface WinConditions {

    boolean winner(Color color, Game game);

}
