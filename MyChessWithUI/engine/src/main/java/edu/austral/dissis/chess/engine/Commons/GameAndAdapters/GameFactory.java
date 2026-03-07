package edu.austral.dissis.chess.engine.Commons.GameAndAdapters;

import edu.austral.dissis.chess.engine.Checkers.Boards.CheckersBoard;
import edu.austral.dissis.chess.engine.Checkers.Rules.CheckEveryPieceIfItCanEat;
import edu.austral.dissis.chess.engine.Checkers.TurnsManager.CheckersAfterEatTurnRule;
import edu.austral.dissis.chess.engine.Checkers.WinConditions.NoOpponentPieces;
import edu.austral.dissis.chess.engine.Chess.Boards.CapaBlancaBoard;
import edu.austral.dissis.chess.engine.Chess.Boards.ClassicBoard;
import edu.austral.dissis.chess.engine.Chess.Rules.CheckRules;
import edu.austral.dissis.chess.engine.Chess.WinConditions.CheckMate;
import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Pieces.FactoryPieces;
import edu.austral.dissis.chess.engine.Commons.Rules.BasicRulesValidator;
import edu.austral.dissis.chess.engine.Commons.Rules.TurnsRulesValidator;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;

import java.util.List;

public class GameFactory {

    public Game createClasicGame(){
        return new Game(new Board(8, 8, new ClassicBoard().create(new FactoryPieces())),
                false, new TurnsManager(Color.WHITE, Color.BLACK),
                null,null, List.of(new CheckMate()),List.of(new BasicRulesValidator(),
                new TurnsRulesValidator(), new CheckRules()));
    }

    public Game createCheckersGame(){
        return new Game(new Board(8,8, new CheckersBoard().create(new FactoryPieces())),
                false, new TurnsManager(Color.BLACK, Color.WHITE),
                null,new CheckersAfterEatTurnRule(), List.of(new NoOpponentPieces()),
                List.of(new BasicRulesValidator(), new TurnsRulesValidator(), new CheckEveryPieceIfItCanEat()));
    }
    public Game createCapaBlancaBoard(){
        return new Game(new Board(8,10, new CapaBlancaBoard().create(new FactoryPieces())), false,
                new TurnsManager(Color.WHITE, Color.BLACK), null, null, List.of(new CheckMate()),
                List.of(new BasicRulesValidator(), new TurnsRulesValidator(), new CheckRules()));
    }


}
