package edu.austral.dissis.chess.engine.Commons.GameAndAdapters;

import edu.austral.dissis.chess.engine.Commons.Board.Board;
import edu.austral.dissis.chess.engine.Commons.Pieces.PieceMaker;
import edu.austral.dissis.chess.engine.Commons.Positions.MoveTo;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Rules.Rules;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsManager;
import edu.austral.dissis.chess.engine.Commons.Turns.TurnsRules;
import edu.austral.dissis.chess.engine.Commons.WinCondition.WinConditions;

import java.util.List;

public class Game {

    private final Board currentBoard;
    private final boolean finished;
    private final Color winner;
    private final List<WinConditions> winConditions;
    private final TurnsManager turnToPlay;
    private final List<Rules> rules;
    private final TurnsRules turnsRules;

    public Game(Board board, boolean finished, TurnsManager turnsManager, Color winner, TurnsRules turnsRules, List<WinConditions> winConditions, List<Rules> rules) {
        this.currentBoard = board;
        this.finished = finished;
        this.winner = winner;
        this.turnToPlay = turnsManager;
        this.winConditions = winConditions;
        this.rules = rules;
        this.turnsRules = turnsRules;
    }

    public Game(Board board, TurnsManager turn, List<Rules> rules) {
        this.currentBoard = board;
        this.winner = null;
        this.finished = false;
        this.turnToPlay = turn;
        this.winConditions = null;
        this.turnsRules = null;
        this.rules = rules;
    }

    public List<WinConditions> getWinConditions() {
        return winConditions;
    }

    public List<Rules> getRules() {
        return rules;
    }

    public TurnsManager getTurnManager() {
        return turnToPlay;
    }

    public Color getPlayerToMove() {
        return turnToPlay.playerToMove();
    }

    public Board getCurrentBoard() {
        return currentBoard;
    }

    public TurnsManager getTurnToPlay() {
        return turnToPlay;
    }

    public Color getWinner() {
        return winner;
    }

    public boolean isFinished() {
        return finished;
    }

    public Game move(MoveTo moveTo, PieceMaker piece, TurnsManager color) {
        if (finished || rules == null || winConditions == null) {
            return this;
        }
        for(Rules rule : rules){
            if(!rule.check(moveTo, piece, turnToPlay, currentBoard)){
                return new Game(currentBoard, turnToPlay.PlayerKeepsPlaying(), rules);
            }
        }
        Game newGame = currentBoard.move(moveTo, piece, color, rules);
        TurnsManager updatedTurnsManager;
        if (turnsRules != null) {
            TurnsRules updatedTurnRule = turnsRules;
            if (updatedTurnRule.checkTurnRule(moveTo, newGame.getCurrentBoard())) {
                updatedTurnsManager = updatedTurnRule.PlayerMoved(turnToPlay);
            } else {
                updatedTurnsManager = turnToPlay.PlayerKeepsPlaying();
            }
        } else {
            updatedTurnsManager = turnToPlay.PlayerMoved();
        }
        Color newWinner = null;
        for (WinConditions condition : winConditions) {
            if (condition.winner(color.getOpponent(), newGame)) {
                newWinner = color.playerToMove();
                break;
            }
        }
        if (newWinner != null) {
            return new Game(newGame.getCurrentBoard(), true, updatedTurnsManager, newWinner, turnsRules, winConditions, rules);}
        if (newGame.getCurrentBoard().equals(currentBoard)) {
            return this;
        }
        return new Game(newGame.getCurrentBoard(), false, updatedTurnsManager, null, turnsRules, winConditions, rules);
    }
}

