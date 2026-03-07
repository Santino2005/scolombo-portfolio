package edu.austral.dissis.tictactoe;

import java.util.ArrayList;
import java.util.List;

public class TaTeTi {

  List<Symbol> col1 = new ArrayList<>();
  List<Symbol> col2 = new ArrayList<>();
  List<Symbol> col3 = new ArrayList<>();
  List<List<Symbol>> board;

  public TaTeTi() {
    int i = 0;
    while (i < 3) {
      col1.add(Symbol.Empty);
      col2.add(Symbol.Empty);
      col3.add(Symbol.Empty);
      i++;
    }
    board = List.of(col1, col2, col3);
  }

  public Symbol setSymbol(int row, int col, Symbol symbol) {
    if (col == 0) {
      if (col1.get(row) == Symbol.Empty) {
        col1.set(row, symbol);
      }
    } else if (col == 1) {
      if (col2.get(row) == Symbol.Empty) {
        col2.set(row, symbol);
      }
    } else if (col == 2) {
      if (col3.get(row) == Symbol.Empty) {
        col3.set(row, symbol);
      }
    }
    return findWinner();
  }

  public Symbol getSymbol(int row, int col) {
    if (row <= 2 && row >= 0) {
      if (col == 0) {
        return col1.get(row);
      } else if (col == 1) {
        return col2.get(row);
      } else if (col == 2) {
        return col3.get(row);
      }
    }
    return null;
  }

  public Symbol findWinner() {
    for (int row = 0; row < 3; row++) {
      if (col1.get(row) == col2.get(row) && col1.get(row) == col3.get(row)) {
        return col1.get(row);
      }
    }
    for (int col = 0; col < 3; col++) {
      int finalCol = col;
      if (board.get(finalCol).get(0) != null) {
        if (board.get(finalCol).stream().allMatch(e -> e == board.get(finalCol).get(finalCol))) {
          return board.get(finalCol).get(0);
        }
      }
    }
    if (col1.get(0) == col2.get(1) && col1.get(0) == (col3.get(2))) {
      return col1.get(0);
    }
    if (col3.get(0) == col2.get(1) && col3.get(0) == col1.get(2)) {
      return col3.get(0);
    }
    return null;
  }
}
