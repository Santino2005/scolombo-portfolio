package edu.austral.dissis.chess.engine.Commons.GameAndAdapters;

import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Positions.Position;
import edu.austral.dissis.chess.gui.*;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class JavaAdapter implements GameEngine {
  private Game game;
  private final Stack<Game> undoStack;
  private final Stack<Game> redoStack;

  public JavaAdapter(Game game) {
    this.game = game;
    this.undoStack = new Stack<>();
    this.redoStack = new Stack<>();
  }

  @NotNull
  @Override
  public InitialState init() {
    var dimension = game.getCurrentBoard();
    BoardSize boardSize = new BoardSize(dimension.getColMax(), dimension.getRowMax());
    List<ChessPiece> pieces = convertPieces();
    PlayerColor currentPlayer = convertColor(game.getTurnToPlay().playerToMove());
    return new InitialState(boardSize, pieces, currentPlayer);
  }

  @NotNull
  @Override
  public MoveResult applyMove(@NotNull Move move) {
    Position from = new Position(move.getFrom().getRow(), move.getFrom().getColumn());
    Position to = new Position(move.getTo().getRow(), move.getTo().getColumn());
    Optional<PieceMaker> piece = game.getCurrentBoard().getPieceByPosition(from);

    if (piece.isEmpty()) {
      return new InvalidMove("No piece at source position.");
    }

    undoStack.push(game);
    redoStack.clear();

    Game result = game.move(new MoveTo(from, to), piece.get(), game.getTurnToPlay());
    if (!result.getCurrentBoard().equals(game.getCurrentBoard())) {
      game = result;
      List<ChessPiece> pieces = convertPieces();
      PlayerColor currentPlayer = convertColor(game.getTurnToPlay().playerToMove());
      UndoState undoState = new UndoState(!undoStack.isEmpty(), !redoStack.isEmpty());
      return new NewGameState(pieces, currentPlayer, undoState);
    } else {
      if(game.isFinished()){
        return new InvalidMove("Game is finished: " + game.getWinner() + " Won");
      }
      undoStack.pop();
      return new InvalidMove("Invalid move");
    }
  }

  @NotNull
  @Override
  public NewGameState undo() {
    if (undoStack.isEmpty()) {
      return getCurrentState();
    }
    redoStack.push(game);
    game = undoStack.pop();
    return getCurrentState();
  }

  @NotNull
  @Override
  public NewGameState redo() {
    if (redoStack.isEmpty()) {
      return getCurrentState();
    }
    undoStack.push(game);
    game = redoStack.pop();
    return getCurrentState();
  }

  private NewGameState getCurrentState() {
    List<ChessPiece> pieces = convertPieces();
    PlayerColor currentPlayer = convertColor(game.getTurnToPlay().playerToMove());
    UndoState undoState = new UndoState(!undoStack.isEmpty(), !redoStack.isEmpty());
    return new NewGameState(pieces, currentPlayer, undoState);
  }

  private List<ChessPiece> convertPieces() {
    List<ChessPiece> pieces = new ArrayList<>();
    for (Map.Entry<Position, PieceMaker> entry : game.getCurrentBoard().getCells().entrySet()) {
      pieces.add(convertPiece(entry.getValue(), entry.getKey()));
    }
    return pieces;
  }

  private ChessPiece convertPiece(PieceMaker piece, Position position) {
    edu.austral.dissis.chess.gui.Position guiPos = new edu.austral.dissis.chess.gui.Position(position.row(), position.column());
    return new ChessPiece(piece.getId(), convertColor(piece.getColor()), guiPos, piece.getName());
  }

  private PlayerColor convertColor(Color color) {
    return color.equals(Color.WHITE) ? PlayerColor.WHITE : PlayerColor.BLACK;
  }
}
