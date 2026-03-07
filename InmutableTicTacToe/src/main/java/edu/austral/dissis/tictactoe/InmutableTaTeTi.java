package edu.austral.dissis.tictactoe;

import java.util.List;

public class InmutableTaTeTi {

  private final List<List<Symbol>> board;

  public InmutableTaTeTi() {
    List<Symbol> col1 = List.of(Symbol.Empty, Symbol.Empty, Symbol.Empty);
    List<Symbol> col2 = List.of(Symbol.Empty, Symbol.Empty, Symbol.Empty);
    List<Symbol> col3 = List.of(Symbol.Empty, Symbol.Empty, Symbol.Empty);
    this.board = List.of(col1, col2, col3);
  }

  private InmutableTaTeTi(List<Symbol> col1, List<Symbol> col2, List<Symbol> col3) {
    this.board = List.of(col1, col2, col3);
  }

  private List<Symbol> setSymbolCol(int row, int col, Symbol symbol) {
    if (board.get(col).get(row) == Symbol.Empty) {
      if (row == 0) {
        return List.of(symbol, board.get(col).get(1), board.get(col).get(2));
      } else if (row == 1) {
        return List.of(board.get(col).get(0), symbol, board.get(col).get(2));
      } else if (row == 2) {
        return List.of(board.get(col).get(0), board.get(col).get(1), symbol);
      }
    }
    return List.of(board.get(col).get(0), board.get(col).get(1), board.get(col).get(2));
  }

  public InmutableTaTeTi setSymbol(int row, int col, Symbol symbol) {
    List<Symbol> newcol;
    if (findWinner() != Symbol.X && findWinner() != Symbol.O) {
      if (col == 0) {
        newcol = setSymbolCol(row, col, symbol);
        return new InmutableTaTeTi(newcol, board.get(1), board.get(2));
      } else if (col == 1) {
        newcol = setSymbolCol(row, col, symbol);
        return new InmutableTaTeTi(board.get(0), newcol, board.get(2));
      } else if (col == 2) {
        newcol = setSymbolCol(row, col, symbol);
        return new InmutableTaTeTi(board.get(0), board.get(1), newcol);
      } else {
        return new InmutableTaTeTi(board.get(0), board.get(1), board.get(2));
      }
    }
    return new InmutableTaTeTi(board.get(0), board.get(1), board.get(2));
  }

  public Symbol getSymbol(int row, int col) {
    if (col == 0) {
      return board.get(0).get(row);
    } else if (col == 1) {
      return board.get(1).get(row);
    } else if (col == 2) {
      return board.get(2).get(row);
    } else {
      return null;
    }
  }

  public Symbol findWinner() {
    for (int row = 0; row < 3; row++) {
      if (board.get(0).get(row).equals(board.get(1).get(row))
          && board.get(0).get(row).equals(board.get(2).get(row))) {
        return board.get(0).get(row);
      }
    }
    for (int col = 0; col < 3; col++) {
      int finalCol = col;
      if (board.get(finalCol).stream().allMatch(e -> e.equals(board.get(finalCol).get(finalCol)))) {
        return board.get(finalCol).get(0);
      }
    }
    if (board.get(0).get(0).equals(board.get(1).get(1))
        && board.get(0).get(0).equals(board.get(2).get(2))) {
      return board.get(0).get(0);
    }
    if (board.get(2).get(0).equals(board.get(1).get(1))
        && board.get(2).get(0).equals(board.get(0).get(2))) {
      return board.get(2).get(0);
    }
    return null;
  }
}
