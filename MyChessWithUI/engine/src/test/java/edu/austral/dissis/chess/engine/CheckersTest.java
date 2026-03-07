package edu.austral.dissis.chess.engine;

import edu.austral.dissis.chess.engine.Checkers.Boards.CheckersBoard;
import edu.austral.dissis.chess.engine.Checkers.Rules.CheckEveryPieceIfItCanEat;
import edu.austral.dissis.chess.engine.Checkers.WinConditions.NoOpponentPieces;
import edu.austral.dissis.chess.engine.Checkers.TurnsManager.CheckersAfterEatTurnRule;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.engine.Commons.Rules.BasicRulesValidator;
import edu.austral.dissis.chess.engine.Commons.Rules.TurnsRulesValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CheckersTest {

    @Test
    public void whiteMustCaptureIfPossible() {

        Board initialBoard = new Board(8, 8, new CheckersBoard().create(new FactoryPieces()));
        TurnsManager turnManager = new TurnsManager(Color.BLACK, Color.WHITE);
        Game game = new Game(initialBoard,false,turnManager,null,new CheckersAfterEatTurnRule(),List.of(new NoOpponentPieces()),List.of(new BasicRulesValidator(), new TurnsRulesValidator(), new CheckEveryPieceIfItCanEat()));

        MoveTo moveTo1 = new MoveTo(new Position(6,2), new Position(5,3));
        MoveTo moveTo2 = new MoveTo(new Position(3,1), new Position(4,2));
        MoveTo moveTo3 = new MoveTo(new Position(5,3), new Position(3,1));

        Game game1 = game.move(moveTo1, game.getCurrentBoard().getPieceByPosition(moveTo1.getFrom()).get(), game.getTurnToPlay());
        Game game2 = game1.move(moveTo2, game1.getCurrentBoard().getPieceByPosition(moveTo2.getFrom()).get(), game1.getTurnToPlay());
        Game game3 = game2.move(moveTo3, game2.getCurrentBoard().getPieceByPosition(moveTo3.getFrom()).get(), game2.getTurnToPlay());

        Assertions.assertEquals(Color.BLACK, game3.getCurrentBoard().getPieceByPosition(moveTo3.getTo()).get().getColor());
    }
}