package edu.austral.dissis.tictactoe;

public class Player {

  Symbol symbol;

  public Player(Symbol symbol) {
    this.symbol = symbol;
  }

  public InmutableTaTeTi setSymbol(int row, int col, InmutableTaTeTi board) {
    return board.setSymbol(row, col, this.symbol);
  }

  public Symbol getSymbol() {
    return this.symbol;
  }

  public Symbol winner(InmutableTaTeTi board) {
    return board.findWinner();
  }
}
