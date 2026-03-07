package edu.austral.dissis.chess.engine.Commons.Pieces;

import edu.austral.dissis.chess.engine.Checkers.CheckersMovesValidator.CheckersMovesValidator;
import edu.austral.dissis.chess.engine.Checkers.Moves.CheckersBasicMove;
import edu.austral.dissis.chess.engine.Checkers.Moves.CheckersEat;
import edu.austral.dissis.chess.engine.Checkers.Moves.CheckersPromotion;
import edu.austral.dissis.chess.engine.Checkers.Moves.QueenCheckersEat;
import edu.austral.dissis.chess.engine.Checkers.Piece.CheckersPiece;
import edu.austral.dissis.chess.engine.Checkers.ValidCheckersConditions.*;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.MoveConditions.IsEnemyPieceOnPosition;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.MoveConditions.IsNotKing;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions.IsSecondPieceFirstMove;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.PositionsMovesConditions.*;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.FreePath.FreeColumnPath;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.FreePath.FreeDiagonalPath;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.FreePath.FreeRowPath;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions.CastlingCondition;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions.KingMoveRestriction;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions.PromotionCondition;
import edu.austral.dissis.chess.engine.Chess.ConditionsToMove.SpecialConditions.TwoMoveToFront;
import edu.austral.dissis.chess.engine.Chess.Moves.CastlingMove;
import edu.austral.dissis.chess.engine.Chess.Moves.ChessBasicMove;
import edu.austral.dissis.chess.engine.Chess.Moves.PromotionMove;
import edu.austral.dissis.chess.engine.Chess.Piece.ChessPiece;
import edu.austral.dissis.chess.engine.Chess.PieceMovesValidator.AllPossibleMoves;
import edu.austral.dissis.chess.engine.Chess.PieceMovesValidator.PieceMovesValidator;
import edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves.*;
import edu.austral.dissis.chess.engine.Chess.PiecePossiblesMoves.LAllMove;
import edu.austral.dissis.chess.engine.Commons.Colors.Color;
import edu.austral.dissis.chess.engine.Commons.Moves.CommonsConditionsToMove.DiagonalMove;
import edu.austral.dissis.chess.engine.Commons.Moves.CommonsConditionsToMove.IsPieceNotOnRoad;
import edu.austral.dissis.chess.engine.Commons.Moves.MoveValidator;

import java.util.List;

public class FactoryPieces {

        public PieceMaker createBishop(String name, Color color, String id, Character character) {

                MoveValidator validator = new MoveValidator(List.of(
                        new FreeDiagonalPath(),
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(),
                        new DiagonalMove()));
                MoveValidator valid = new MoveValidator(List.of(
                        new FreeDiagonalPath(),
                        new IsNotKing(),
                        new IsPieceNotOnRoad(), new DiagonalMove())
                );
                AllPossibleMoves possibleMoves = new AllPossibleMoves(
                        List.of(new Diagonal(true,true,true,true)));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(
                        List.of(validator,valid), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove()), pieceMovesValidator);
        }

        public PieceMaker createKnight(String name, Color color, String id, Character character) {
                MoveValidator validator = new MoveValidator(List.of(
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(), new LMove())
                );
                MoveValidator valid = new MoveValidator(List.of(
                        new IsNotKing(),
                        new IsPieceNotOnRoad(), new LMove())
                );
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new LAllMove()));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(List.of(validator,valid), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove()), pieceMovesValidator);
        }

        public PieceMaker createQueen(String name, Color color, String id, Character character) {
                MoveValidator diagonalValidator = new MoveValidator(List.of(
                        new FreeDiagonalPath(),
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(),
                        new DiagonalMove())
                );
                MoveValidator valid = new MoveValidator(List.of(
                        new FreeDiagonalPath(),
                        new IsNotKing(),
                        new IsPieceNotOnRoad(),
                        new DiagonalMove())
                );

                MoveValidator colValidator = new MoveValidator(List.of(
                        new FreeColumnPath(),
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(),
                        new RowMove())
                );
                MoveValidator colValid = new MoveValidator(List.of(
                        new FreeColumnPath(),
                        new IsNotKing(),
                        new IsPieceNotOnRoad(),
                        new RowMove())
                );

                MoveValidator rowValidator = new MoveValidator(List.of(
                        new FreeRowPath(),
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(),
                        new ColumnMove())
                );
                MoveValidator rowValid = new MoveValidator(List.of(
                        new FreeRowPath(),
                        new IsNotKing(),
                        new IsPieceNotOnRoad(),
                        new ColumnMove())
                );
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new Diagonal(true,true,true,true), new Row(true,true), new Column(true,true)));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(
                        List.of(diagonalValidator,colValidator,rowValidator,colValid,rowValid,valid), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove()), pieceMovesValidator);
        }
        public PieceMaker createArchbishop(String name, Color color, String id, Character character) {
                MoveValidator diagValidator = new MoveValidator(List.of(
                        new FreeDiagonalPath(),
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(), new DiagonalMove())
                );

                MoveValidator knightValidator = new MoveValidator(List.of(
                        new IsNotKing(),
                        new IsPieceNotOnRoad(), new LMove())
                );
                MoveValidator diagValid = new MoveValidator(List.of(
                        new FreeDiagonalPath(),
                        new IsNotKing(),
                        new IsPieceNotOnRoad(), new DiagonalMove())
                );

                MoveValidator knightValid = new MoveValidator(List.of(
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(), new LMove())
                );
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new Diagonal(true,true,true,true), new LAllMove()));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(List.of(diagValidator,diagValid,knightValid,knightValidator), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove()), pieceMovesValidator);
        }

        public PieceMaker createChancellor(String name, Color color, String id, Character character, boolean moved) {

                MoveValidator col = new MoveValidator(
                        List.of(new FreeColumnPath(), new IsEnemyPieceOnPosition(), new IsNotKing(), new RowMove()));
                MoveValidator colValid = new MoveValidator(
                        List.of(new FreeColumnPath(), new IsPieceNotOnRoad(), new IsNotKing(), new RowMove()));
                MoveValidator row = new MoveValidator(
                        List.of(new FreeRowPath(), new IsEnemyPieceOnPosition(), new IsNotKing(), new ColumnMove()));
                MoveValidator rowValid = new MoveValidator(
                        List.of(new FreeRowPath(), new IsPieceNotOnRoad(), new IsNotKing(), new ColumnMove()));
                MoveValidator knightValidator = new MoveValidator(List.of(
                        new IsNotKing(),
                        new IsPieceNotOnRoad(), new LMove())
                );
                MoveValidator knightValid = new MoveValidator(List.of(
                        new IsNotKing(),
                        new IsEnemyPieceOnPosition(), new LMove())
                );
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new LAllMove(),new Column(true,true), new Row(true,true)));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(List.of(colValid,col,rowValid,row,knightValid, knightValidator), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove()), pieceMovesValidator);
        }

        public PieceMaker createKing(String name, Color color, String id, Character character) {
                MoveValidator valid = new MoveValidator(List.of(new OneSquareMove(), new IsPieceNotOnRoad(), new KingMoveRestriction(), new OneSquareMove()));
                MoveValidator validator = new MoveValidator(List.of(new OneSquareMove(), new IsEnemyPieceOnPosition(), new KingMoveRestriction(), new OneSquareMove()));
                MoveValidator CastlingValidation = new MoveValidator(List.of(new CastlingCondition()));
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new KingMove()));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(List.of(validator,valid,CastlingValidation), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove(),
                        new CastlingMove(List.of(new IsFirstMove(), new IsSecondPieceFirstMove(), new FreeRowPath()))),pieceMovesValidator);
        }

        public PieceMaker createRook(String name, Color color, String id, Character character) {
                MoveValidator col = new MoveValidator(List.of(new FreeColumnPath(), new IsNotKing(), new IsEnemyPieceOnPosition(), new RowMove()));
                MoveValidator colValid = new MoveValidator(List.of(new FreeColumnPath(), new IsNotKing(), new IsPieceNotOnRoad(), new RowMove()));
                MoveValidator row = new MoveValidator(List.of(new FreeRowPath(), new IsNotKing(), new IsPieceNotOnRoad(), new ColumnMove()));
                MoveValidator rowValid = new MoveValidator(List.of(new FreeRowPath(), new IsNotKing(), new IsEnemyPieceOnPosition(), new ColumnMove()));
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new Row(true,true), new Column(true,true)));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(List.of(colValid,col,rowValid,row), possibleMoves);
                return new ChessPiece(id, color, name, character, List.of(new ChessBasicMove()),pieceMovesValidator);
        }

        public PieceMaker createPawn(String name, Color color, String id, Character character) {
                MoveValidator moveValidator = new MoveValidator(List.of(new IsPieceNotOnRoad(), new OneMoveToFront()));
                MoveValidator dobleMove = new MoveValidator(List.of(new IsPieceNotOnRoad(), new TwoMoveToFront()));
                MoveValidator diagMove = new MoveValidator(List.of(new IsEnemyPieceOnPosition(), new IsNotKing(), new DiagonalEat()));
                MoveValidator PromotionCondition = new MoveValidator(List.of(new PromotionCondition()));
                AllPossibleMoves possibleMoves = new AllPossibleMoves(List.of(new OneDiagonalMove(), new MoveToFront(), new DoubleMoveToFront()));
                PieceMovesValidator pieceMovesValidator = new PieceMovesValidator(List.of(moveValidator,diagMove,dobleMove,PromotionCondition), possibleMoves);
                return new ChessPiece(id, color, name, character,List.of(new ChessBasicMove(), new PromotionMove()),pieceMovesValidator);
        }
        public PieceMaker createChecker(String name, Color color, String id, Character character) {
                return new CheckersPiece(id, color, name, character,List.of(new CheckersBasicMove(), new CheckersEat(), new CheckersPromotion()),
                        new CheckersMovesValidator(List.of(
                                new MoveValidator(List.of(new IsPieceNotOnRoad(),new OpponentInTheMiddle(), new DiagonalTwoMoves(), new Capture())),
                                new MoveValidator(List.of(new IsPieceNotOnRoad(), new EatIfIsPossible(), new DiagonalCheckersMove())),
                                new MoveValidator(List.of(new CheckerPromotionCondition())))));
        }
        public PieceMaker createCheckerQueen(String name, Color color, String id, Character character) {
                return new CheckersPiece(id, color, name, character,List.of(new CheckersBasicMove(), new QueenCheckersEat()),
                        new CheckersMovesValidator(List.of(
                        new MoveValidator(List.of(new IsPieceNotOnRoad(), new OpponentOneSquareBehind(), new DiagonalMove())),
                        new MoveValidator(List.of(new FreeDiagonalPath(), new IsPieceNotOnRoad(), new DiagonalMove())))));
        }
}

