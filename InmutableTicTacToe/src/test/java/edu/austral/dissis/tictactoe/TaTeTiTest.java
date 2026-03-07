package edu.austral.dissis.tictactoe;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TaTeTiTest {

  @Test
  public void setSymbolTest() {

    TaTeTi tateti = new TaTeTi();

    tateti.setSymbol(0, 0, Symbol.X);
    tateti.setSymbol(1, 1, Symbol.O);
    tateti.setSymbol(0, 1, Symbol.X);
    tateti.setSymbol(2, 2, Symbol.O);

    Assertions.assertEquals(Symbol.X, tateti.setSymbol(0, 2, Symbol.X));
  }

  @Test
  public void getSymbolTest() {

    TaTeTi taTeTi = new TaTeTi();

    taTeTi.setSymbol(1, 1, Symbol.X);
    taTeTi.setSymbol(2, 2, Symbol.O);
    taTeTi.setSymbol(0, 0, Symbol.O);

    Assertions.assertNull(taTeTi.getSymbol(0, 3));
    Assertions.assertNull(taTeTi.getSymbol(3, 0));
    Assertions.assertEquals(Symbol.X, taTeTi.getSymbol(1, 1));
    Assertions.assertEquals(Symbol.O, taTeTi.getSymbol(0, 0));
    Assertions.assertEquals(Symbol.O, taTeTi.getSymbol(2, 2));
  }

  @Test
  public void findWinnerDiagonalTest() {
    TaTeTi taTeTi = new TaTeTi();

    taTeTi.setSymbol(0, 0, Symbol.X);
    taTeTi.setSymbol(1, 1, Symbol.X);
    taTeTi.setSymbol(2, 2, Symbol.X);

    Assertions.assertEquals(Symbol.X, taTeTi.findWinner());
  }

  @Test
  public void findWinnerDiagonal2Test() {
    TaTeTi taTeTi = new TaTeTi();

    taTeTi.setSymbol(0, 2, Symbol.X);
    taTeTi.setSymbol(1, 1, Symbol.X);
    taTeTi.setSymbol(2, 0, Symbol.X);

    Assertions.assertEquals(Symbol.X, taTeTi.findWinner());
  }

  @Test
  public void findWinnerRowTest() {
    TaTeTi taTeTi = new TaTeTi();
    Symbol symbol = Symbol.X;

    taTeTi.setSymbol(0, 0, symbol);
    taTeTi.setSymbol(0, 1, symbol);
    taTeTi.setSymbol(0, 2, symbol);

    Assertions.assertEquals(Symbol.X, taTeTi.findWinner());
  }

  @Test
  public void findWinnerColumnTest() {
    TaTeTi taTeTi = new TaTeTi();
    Symbol symbol = Symbol.X;

    taTeTi.setSymbol(0, 0, symbol);
    taTeTi.setSymbol(1, 0, symbol);
    taTeTi.setSymbol(2, 0, symbol);

    Assertions.assertEquals(Symbol.X, taTeTi.findWinner());
  }
}
