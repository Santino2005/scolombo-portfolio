/*package edu.austral.dissis.chess.engine;

import edu.austral.dissis.chess.engine.Chess.board.GameFactory;
import edu.austral.dissis.chess.engine.Chess.board.User;
import edu.austral.dissis.chess.engine.Chess.rules.CheckRule;
import edu.austral.dissis.chess.engine.Commons.AbstractBoard;
import edu.austral.dissis.chess.engine.Commons.Color;
import edu.austral.dissis.chess.engine.Commons.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BoardTest {
  @Test
  public void getColorToPlay() {
    AbstractBoard board = new GameFactory().createClassicBoard().getBoard();

    User user1 = new User(Color.WHITE, "pepe");
    User user2 = new User(Color.BLACK, "popo");

    Position initial = new Position(2, 5);
    Position finalPos = new Position(3, 5);

    Position initial2 = new Position(7, 6);
    Position finalPos2 = new Position(5, 6);

    Position initial3 = new Position(1, 4);
    Position finalPos3 = new Position(5, 8);

    Position initial4 = new Position(7, 7);
    Position finalPos4 = new Position(6, 7);
    Position initial5 = new Position(5, 1);
    Position finalPos5 = new Position(6, 2);

    Position initial6 = new Position(8, 1);
    Position finalPos6 = new Position(7, 1);

    Position initial7 = new Position(6, 2);
    Position finalPos7 = new Position(7, 1);

    Position initial8 = new Position(7, 8);
    Position finalPos8 = new Position(6, 8);

    Position initial9 = new Position(7, 1);
    Position finalPos9 = new Position(8, 2);
    AbstractBoard whiteMoves =
        board.move(initial, finalPos, board.getCells().get(initial), user1.getColor());
    AbstractBoard blackMoves =
        whiteMoves.move(initial2, finalPos2, whiteMoves.getCells().get(initial2), user2.getColor());
    AbstractBoard whiteMoves2 =
        blackMoves.move(initial3, finalPos3, blackMoves.getCells().get(initial3), user1.getColor());
    AbstractBoard blackMoves2 =
        whiteMoves2.move(
            initial4, finalPos4, whiteMoves2.getCells().get(initial4), user2.getColor());
    boolean gameEnded = new CheckRule(whiteMoves2).checkMate(whiteMoves2.getColorToPlay());
    /*Board whiteMoves3 =
        blackMoves2.move(
            initial5, finalPos5, blackMoves2.getCells().get(initial5), user1.getColor());
    Board blackMoves3 =
        whiteMoves3.move(
            initial6, finalPos6, whiteMoves3.getCells().get(initial6), user2.getColor());
    Board whiteMoves4 =
        blackMoves3.move(
            initial7, finalPos7, blackMoves3.getCells().get(initial7), user1.getColor());
    Board blackMoves4 =
        whiteMoves4.move(
            initial8, finalPos8, whiteMoves4.getCells().get(initial8), user2.getColor());
    Board whiteMoves5 =
        blackMoves4.move(
            initial9, finalPos9, blackMoves4.getCells().get(initial9), user1.getColor());

    Assertions.assertEquals(Color.WHITE, blackMoves2.getColorToPlay());
  }
  @Test
  public void CastlingTest() {
    Board board = new GameFactory().createCastlingBoard().getBoard();

    User user1 = new User(Color.WHITE, "pepe");
    User user2 = new User(Color.BLACK, "popo");

    Position initial = new Position(1, 5);
    Position finalPos = new Position(2, 5);

    Position initial2 = new Position(8, 5);
    Position finalPos2 = new Position(7, 5);

    Position initial3 = new Position(2, 5);
    Position finalPos3 = new Position(3, 5);

    Position initial4 = new Position(7, 5);
    Position finalPos4 = new Position(6, 5);

    Position initial5 = new Position(3, 5);
    Position finalPos5 = new Position(4, 5);

    Position initial6 = new Position(6, 5);
    Position finalPos6 = new Position(5, 5);

    Board whiteMoves = board.move(initial, finalPos, board.getCells().get(initial), user1.getColor());
    Board blackMoves =
            whiteMoves.move(initial2, finalPos2, whiteMoves.getCells().get(initial2), user2.getColor());
    Board whiteMoves2 = blackMoves.move(initial3, finalPos3, blackMoves.getCells().get(initial3), user1.getColor());
    Board blackMoves2 =
            whiteMoves2.move(initial4, finalPos4, whiteMoves2.getCells().get(initial4), user2.getColor());
    Board whiteMoves3 = blackMoves2.move(initial5, finalPos5, blackMoves2.getCells().get(initial5), user1.getColor());
    Board blackMoves3 =
            whiteMoves3.move(initial6, finalPos6, whiteMoves3.getCells().get(initial6), user2.getColor());

    Assertions.assertEquals(Color.BLACK, whiteMoves3.getColorToPlay());
    Assertions.assertEquals(Color.BLACK, blackMoves3.getColorToPlay());
  }
}
 */
