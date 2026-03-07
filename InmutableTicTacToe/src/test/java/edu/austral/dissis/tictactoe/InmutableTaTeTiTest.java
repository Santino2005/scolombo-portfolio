package edu.austral.dissis.tictactoe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class InmutableTaTeTiTest {

  @Test
  public void setSymbolTest() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 0, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(2, 1, Symbol.O);
    InmutableTaTeTi newgame3 = newgame2.setSymbol(1, 2, Symbol.O);

    Assertions.assertEquals(Symbol.Empty, game.getSymbol(0, 0));
    Assertions.assertEquals(Symbol.X, newgame.getSymbol(0, 0));
    Assertions.assertEquals(Symbol.O, newgame2.getSymbol(2, 1));
    Assertions.assertEquals(Symbol.O, newgame3.getSymbol(1, 2));
  }

  @Test
  public void setSymbolOutOfColumnTest() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 3, Symbol.X);
    for (int checknomodification = 0; checknomodification < 3; checknomodification++) {
      Assertions.assertEquals(Symbol.Empty, newgame.getSymbol(checknomodification, 0));
      Assertions.assertEquals(Symbol.Empty, newgame.getSymbol(checknomodification, 1));
      Assertions.assertEquals(Symbol.Empty, newgame.getSymbol(checknomodification, 2));
    }
  }

  @Test
  public void setSymbolAgain() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 0, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(0, 0, Symbol.O);

    Assertions.assertEquals(Symbol.X, newgame2.getSymbol(0, 0));
  }

  @Test
  public void getSymbolTest() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    game.setSymbol(1, 1, Symbol.X);
    InmutableTaTeTi newgame = game.setSymbol(1, 1, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(2, 2, Symbol.O);
    InmutableTaTeTi newgame3 = newgame2.setSymbol(0, 1, Symbol.X);

    Assertions.assertEquals(Symbol.Empty, game.getSymbol(1, 1));
    Assertions.assertEquals(Symbol.X, newgame.getSymbol(1, 1));
    Assertions.assertEquals(Symbol.O, newgame2.getSymbol(2, 2));
    Assertions.assertEquals(Symbol.X, newgame3.getSymbol(0, 1));
    Assertions.assertNull(newgame3.getSymbol(0, 3));
  }

  @Test
  void findWinnerRowTest() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 0, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(0, 1, Symbol.X);
    InmutableTaTeTi newgame3 = newgame2.setSymbol(0, 2, Symbol.X);

    Assertions.assertEquals(Symbol.X, newgame3.findWinner());
  }

  @Test
  void findWinnerDiagonalTest() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 0, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(1, 1, Symbol.X);
    InmutableTaTeTi newgame3 = newgame2.setSymbol(2, 2, Symbol.X);

    Assertions.assertEquals(Symbol.X, newgame3.findWinner());
  }

  @Test
  void findWinnerDiagonal2Test() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 2, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(1, 1, Symbol.X);
    InmutableTaTeTi newgame3 = newgame2.setSymbol(2, 0, Symbol.X);

    Assertions.assertEquals(Symbol.X, newgame3.findWinner());
  }

  @Test
  void findWinnerColumnTest() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(0, 0, Symbol.X);
    InmutableTaTeTi newgame2 = newgame.setSymbol(1, 0, Symbol.X);
    InmutableTaTeTi newgame3 = newgame2.setSymbol(2, 0, Symbol.X);

    Assertions.assertEquals(Symbol.X, newgame3.findWinner());
  }

  @Test
  void boardBloquedIfSomeoneAlreadyWon() {
    InmutableTaTeTi game = new InmutableTaTeTi();

    InmutableTaTeTi newgame = game.setSymbol(1, 1, Symbol.X);
    InmutableTaTeTi newgameO = newgame.setSymbol(2, 1, Symbol.O);
    InmutableTaTeTi newnewgame = newgameO.setSymbol(0, 1, Symbol.X);
    InmutableTaTeTi newnewnewgame = newnewgame.setSymbol(0, 2, Symbol.X);
    InmutableTaTeTi newnewnewgame3 = newnewnewgame.setSymbol(0, 0, Symbol.X);
    InmutableTaTeTi newnewnewgameE = newnewnewgame3.setSymbol(2, 2, Symbol.O);

    Assertions.assertEquals(Symbol.X, newnewnewgame3.findWinner());
    Assertions.assertEquals(Symbol.Empty, newnewnewgameE.getSymbol(2, 2));
  }

  @Test
  public void userSetSymbol() {
    Player gamer1 = new Player(Symbol.X);
    Player gamer2 = new Player(Symbol.O);

    InmutableTaTeTi board = new InmutableTaTeTi();

    InmutableTaTeTi board1 = gamer1.setSymbol(1, 1, board);
    InmutableTaTeTi board2 = gamer2.setSymbol(1, 2, board1);
    InmutableTaTeTi board3 = gamer1.setSymbol(0, 0, board2);
    InmutableTaTeTi board4 = gamer2.setSymbol(2, 0, board3);
    InmutableTaTeTi board5 = gamer1.setSymbol(2, 2, board4);
    InmutableTaTeTi endgame = gamer2.setSymbol(1, 0, board5);

    Assertions.assertEquals(Symbol.Empty, board5.getSymbol(1, 0));
    Assertions.assertEquals(Symbol.Empty, endgame.getSymbol(1, 0));
  }

  @Test
  public void userGetSymbol() {
    Player gamer1 = new Player(Symbol.X);
    Player gamer2 = new Player(Symbol.O);

    InmutableTaTeTi board = new InmutableTaTeTi();

    InmutableTaTeTi board1 = gamer1.setSymbol(1, 1, board);
    InmutableTaTeTi board2 = gamer2.setSymbol(1, 2, board1);

    Assertions.assertEquals(Symbol.X, board1.getSymbol(1, 1));
    Assertions.assertEquals(Symbol.O, board2.getSymbol(1, 2));
  }

  @Test
  public void userWinner() {
    Player gamer1 = new Player(Symbol.X);
    Player gamer2 = new Player(Symbol.O);

    InmutableTaTeTi board = new InmutableTaTeTi();

    InmutableTaTeTi board1 = gamer1.setSymbol(1, 1, board);
    InmutableTaTeTi board2 = gamer2.setSymbol(1, 2, board1);
    InmutableTaTeTi board3 = gamer1.setSymbol(0, 0, board2);
    InmutableTaTeTi board4 = gamer2.setSymbol(1, 0, board3);
    InmutableTaTeTi board5 = gamer1.setSymbol(2, 2, board4);

    Assertions.assertEquals(Symbol.X, gamer1.winner(board5));
  }
}
