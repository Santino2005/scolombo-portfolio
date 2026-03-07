package edu.austral.dissis.chess.engine;

import edu.austral.dissis.chess.engine.Chess.Boards.ClassicBoard;
import edu.austral.dissis.chess.engine.Chess.Turn.ChessWithProgressiveTurns;
import edu.austral.dissis.chess.engine.Commons.Rules.BasicRulesValidator;
import edu.austral.dissis.chess.engine.Chess.Rules.CheckRules;
import edu.austral.dissis.chess.engine.Commons.Rules.TurnsRulesValidator;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Chess.WinConditions.CheckMate;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class CheckMateTest {

  @Test
  public void checkMateTest() {
    Board initialBoard = new Board(8, 8, new ClassicBoard().create(new FactoryPieces()));
    //Board initialBoard = new Board(8, 8, new CheckersBoard().create(new FactoryPieces()));
    //TurnsManager turnManager = new TurnsManager(Color.BLACK, Color.WHITE);
    TurnsManager turnManager = new TurnsManager(Color.WHITE, Color.BLACK);
    Game game = new Game(initialBoard,false,turnManager,null,null,
            List.of(new CheckMate()),List.of(new BasicRulesValidator(), new TurnsRulesValidator(), new CheckRules()));
    MoveTo moveTo1 = new MoveTo(new Position(2,7), new Position(4,7));
    MoveTo moveTo2 = new MoveTo(new Position(7,5), new Position(6,5));
    MoveTo moveTo3 = new MoveTo(new Position(2,6), new Position(3,6));
    MoveTo moveTo4 = new MoveTo(new Position(8,4), new Position(4,8));
    MoveTo moveTo5 = new MoveTo(new Position(1,6), new Position(4,3));
    MoveTo moveTo6 = new MoveTo(new Position(8,7), new Position(6,6));
    /*MoveTo moveTo7 = new MoveTo(new Position(5,8), new Position(7,6));
    MoveTo moveTo8 = new MoveTo(new Position(2,1), new Position(3,1));*/

    Game game1 = game.move(moveTo1, game.getCurrentBoard().getPieceByPosition(moveTo1.getFrom()).get(), game.getTurnToPlay());
    Game game2 = game1.move(moveTo2, game1.getCurrentBoard().getPieceByPosition(moveTo2.getFrom()).get(), game1.getTurnToPlay());
    Game game3 = game2.move(moveTo3, game2.getCurrentBoard().getPieceByPosition(moveTo3.getFrom()).get(), game2.getTurnToPlay());
    Game game4 = game3.move(moveTo4, game3.getCurrentBoard().getPieceByPosition(moveTo4.getFrom()).get(), game3.getTurnToPlay());
    Game game5 = game4.move(moveTo5, game4.getCurrentBoard().getPieceByPosition(moveTo5.getFrom()).get(), game4.getTurnToPlay());
    Game game6 = game5.move(moveTo6, game5.getCurrentBoard().getPieceByPosition(moveTo6.getFrom()).get(), game5.getTurnToPlay());

    //Game game7 = game6.move(moveTo7, game6.getCurrentBoard().getPieceByPosition(moveTo7.getTo()).get(), game6.getTurnToPlay());
    Assertions.assertNull(game3.getWinner());
    //Game game8 = game7.move(moveTo8, game7.getCurrentBoard().getPieceByPosition(moveTo8.getTo()).get(), game7.getTurnToPlay());

    Assertions.assertEquals(game4.getCurrentBoard(), game5.getCurrentBoard());
    Assertions.assertEquals(Color.BLACK, game5.getWinner());
  }
}
