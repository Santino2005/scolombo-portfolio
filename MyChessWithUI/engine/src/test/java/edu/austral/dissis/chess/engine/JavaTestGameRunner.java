package edu.austral.dissis.chess.engine;

import edu.austral.dissis.chess.engine.Commons.Rules.BasicRulesValidator;
import edu.austral.dissis.chess.engine.Chess.Rules.CheckRules;
import edu.austral.dissis.chess.engine.Commons.Rules.TurnsRulesValidator;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Chess.WinConditions.CheckMate;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.GameAndAdapters.Game;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.test.*;
import edu.austral.dissis.chess.test.game.*;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class JavaTestGameRunner implements TestGameRunner {

  private final Game game;
  private final Stack<Game> undoStack;
  private final Stack<Game> redoStack;
  private final FactoryPieces factory = new FactoryPieces();

  public JavaTestGameRunner() {
    this(null, new Stack<>(), new Stack<>());
  }

  private JavaTestGameRunner(Game game, Stack<Game> undoStack, Stack<Game> redoStack) {
    this.game = game;
    this.undoStack = undoStack;
    this.redoStack = redoStack;
  }

  @NotNull
  @Override
  public TestMoveResult executeMove(TestPosition from, TestPosition to) {
    Position fromPos = new Position(from.getRow(), from.getCol());
    Position toPos = new Position(to.getRow(), to.getCol());

    Optional<PieceMaker> pieceOpt = game.getCurrentBoard().getPieceByPosition(fromPos);
    if (pieceOpt.isEmpty()){
      return new TestMoveFailure(getBoard());
    }
    PieceMaker piece = pieceOpt.get();
    Game newGame = game.move(new MoveTo(fromPos, toPos), piece, game.getTurnToPlay());

    if (!newGame.getCurrentBoard().equals(game.getCurrentBoard())) {
      Stack<Game> newUndoStack = cloneStack(undoStack);
      newUndoStack.push(game);
      Stack<Game> newRedoStack = new Stack<>();
      if(newGame.isFinished()){
        if (newGame.getWinner() == Color.WHITE) {
           return new WhiteCheckMate(convertToTestBoard(newGame.getCurrentBoard()));
        } else if (newGame.getWinner() == Color.BLACK) {
          return new BlackCheckMate(convertToTestBoard(newGame.getCurrentBoard()));
        }
      }
      System.out.printf("Moved %s from %s to %s%n", piece.getPieceId(), fromPos, toPos);
      System.out.println(convertToTestBoard(newGame.getCurrentBoard()));

      return new TestMoveSuccess(new JavaTestGameRunner(newGame, newUndoStack, newRedoStack));
    }
    return new TestMoveFailure(getBoard());
  }

  @NotNull
  @Override
  public TestBoard getBoard() {
    return convertToTestBoard(game.getCurrentBoard());
  }

  @NotNull
  @Override
  public TestMoveResult redo() {
    if (redoStack.isEmpty()) return new TestMoveFailure(getBoard());

    Stack<Game> newUndoStack = cloneStack(undoStack);
    Stack<Game> newRedoStack = cloneStack(redoStack);
    Game nextGame = newRedoStack.pop();
    newUndoStack.push(game);

    System.out.println("Redo move:");
    System.out.println(convertToTestBoard(nextGame.getCurrentBoard()));

    return new TestMoveSuccess(new JavaTestGameRunner(nextGame, newUndoStack, newRedoStack));
  }

  @NotNull
  @Override
  public TestMoveResult undo() {
    if (undoStack.isEmpty()) return new TestMoveFailure(getBoard());

    Stack<Game> newUndoStack = cloneStack(undoStack);
    Stack<Game> newRedoStack = cloneStack(redoStack);
    Game prevGame = newUndoStack.pop();
    newRedoStack.push(game);

    System.out.println("Undo move:");
    System.out.println(convertToTestBoard(prevGame.getCurrentBoard()));

    return new TestMoveSuccess(new JavaTestGameRunner(prevGame, newUndoStack, newRedoStack));
  }

  @NotNull
  @Override
  public TestGameRunner withBoard(@NotNull TestBoard board) {
    Map<Position, PieceMaker> pieceMap = new HashMap<>();

    for (Map.Entry<TestPosition, TestPiece> entry : board.getPieces().entrySet()) {
      Position position = new Position(entry.getKey().getRow(), entry.getKey().getCol());
      Color color = convertCharToColor(entry.getValue().getPlayerColorSymbol());
      PieceMaker piece = fromTestPiece(entry.getValue().getPieceTypeSymbol(), color);
      pieceMap.put(position, piece);
    }

    Board newBoard = new Board(8, 8, pieceMap);
    Game newGame = new Game(newBoard,false,
            new TurnsManager(Color.WHITE,Color.BLACK),null,null,
            List.of(new CheckMate()),
            List.of(new BasicRulesValidator(), new TurnsRulesValidator(), new CheckRules()));

    System.out.println("Initial board:");
    System.out.println(convertToTestBoard(newGame.getCurrentBoard()));

    return new JavaTestGameRunner(newGame, new Stack<>(), new Stack<>());
  }

  private Stack<Game> cloneStack(Stack<Game> original) {
    return (Stack<Game>) original.clone();
  }

  private PieceMaker fromTestPiece(Character symbol, Color color) {
    final String id = symbol + (color == Color.WHITE ? "W" : "B");
    return switch (symbol) {
      case 'K' -> factory.createKing("King", color, id, 'K');
      case 'Q' -> factory.createQueen("Queen", color, id, 'Q');
      case 'R' -> factory.createRook("Rook", color, id, 'R');
      case 'B' -> factory.createBishop("Bishop", color, id, 'B');
      case 'N' -> factory.createKnight("Knight", color, id, 'N');
      case 'P' -> factory.createPawn("Pawn", color, id, 'P');
      default -> throw new IllegalArgumentException("Unknown piece symbol: " + symbol);
    };
  }

  private TestBoard convertToTestBoard(Board board) {
    Map<TestPosition, TestPiece> testMap = new HashMap<>();
    for (int row = 1; row <= 8; row++) {
      for (int col = 1; col <= 8; col++) {
        Position pos = new Position(col, row);
        Optional<PieceMaker> pieceOpt = board.getPieceByPosition(pos);
        if (pieceOpt.isEmpty()) {
          continue;
        }
        PieceMaker piece = pieceOpt.get();
        testMap.put(new TestPosition(col, row), new TestPiece(piece.getPieceId(), convertColorToChar(piece.getColor())));
      }
    }
    return new TestBoard(new TestSize(8, 8), testMap);
  }

  private char convertColorToChar(Color color) {
    return switch (color) {
      case WHITE -> 'W';
      case BLACK -> 'B';
    };
  }

  private Color convertCharToColor(char symbol) {
    return switch (symbol) {
      case 'W' -> Color.WHITE;
      case 'B' -> Color.BLACK;
      default -> throw new IllegalArgumentException("Invalid color symbol: " + symbol);
    };
  }
}
