/*package edu.austral.dissis.chess.engine;

import edu.austral.dissis.chess.engine.Chess.board.Board;
import edu.austral.dissis.chess.engine.Chess.board.GameFactory;
import edu.austral.dissis.chess.engine.Chess.board.User;
import edu.austral.dissis.chess.engine.Commons.AbstractBoard;
import edu.austral.dissis.chess.engine.Commons.Color;
import edu.austral.dissis.chess.engine.Commons.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClassicRulesTest {

  @Test
  public void cantMoveOtherColorPieces() {

    AbstractBoard board = new GameFactory().createClassicBoard().getBoard();

    User user1 = new User(Color.WHITE, "pepe");
    User user2 = new User(Color.BLACK, "popo");

    Position initial = new Position(2, 5);
    Position finalPos = new Position(3, 5);

    Position initial2 = new Position(7, 5);
    Position finalPos2 = new Position(6, 5);

    Position initial3 = new Position(1, 4);
    Position finalPos3 = new Position(5, 8);

    Position initial4 = new Position(7, 1);
    Position finalPos4 = new Position(6, 1);

    Position initial5 = new Position(5, 8);
    Position finalPos5 = new Position(7, 6);

    AbstractBoard whiteMoves =
        board.move(initial, finalPos, board.getCells().get(initial), user1.getColor());
    AbstractBoard blackMoves =
        whiteMoves.move(initial2, finalPos2, whiteMoves.getCells().get(initial2), user2.getColor());
    AbstractBoard blackMoves2 =
        blackMoves.move(initial3, finalPos3, blackMoves.getCells().get(initial3), user1.getColor());
    AbstractBoard blackMoves3 =
        blackMoves2.move(
            initial4, finalPos4, blackMoves2.getCells().get(initial4), user2.getColor());
    AbstractBoard blackMoves4 =
        blackMoves3.move(
            initial5, finalPos5, blackMoves3.getCells().get(initial5), user1.getColor());

    Assertions.assertEquals(Color.BLACK, whiteMoves.getColorToPlay());
    Assertions.assertEquals(Color.WHITE, blackMoves.getColorToPlay());
    Assertions.assertEquals(Color.BLACK, blackMoves2.getColorToPlay());
    Assertions.assertEquals(Color.WHITE, blackMoves3.getColorToPlay());
    Assertions.assertEquals(Color.BLACK, blackMoves4.getColorToPlay());
  }

  @Test
  public void notIsTheColorToPlay() {

    Board board = new GameFactory().createNoPawnsBoard().getBoard();

    User user1 = new User(Color.WHITE, "pepe");
    User user2 = new User(Color.BLACK, "popo");

    Position initial = new Position(1, 8);
    Position finalPos = new Position(8, 8);

    Position initial2 = new Position(8, 8);
    Position finalPos2 = new Position(8, 3);

    Board whiteMoves = board.move(initial, finalPos, board.getCells().get(initial), user1.getColor());
    Board whiteMovesAgain =
        whiteMoves.move(initial2, finalPos2, whiteMoves.getCells().get(initial2), user2.getColor());

    Assertions.assertEquals(whiteMovesAgain.getCells(), whiteMoves.getCells());
    Assertions.assertEquals(whiteMovesAgain.getColorToPlay(), whiteMoves.getColorToPlay());
  }

  @Test
  public void getRules() {
    Board board = new GameFactory().createNoPawnsBoard().getBoard();

    Assertions.assertEquals(Color.WHITE, new OriginalRules().starterPlayer());
    Assertions.assertEquals(new OriginalRules().starterPlayer(), board.getColorToPlay());
  }

}

 */
