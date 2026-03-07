package edu.austral.dissis.chess.engine

import edu.austral.dissis.chess.engine.board.BoardXCoordinates
import edu.austral.dissis.chess.engine.board.BoardYCoordinates
import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.pieces.Pawn
import edu.austral.dissis.chess.engine.util.IntOffset
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue


class PawnTest {

    private val whitePiece : Piece = Pawn(color = Piece.Color.WHITE, position = IntOffset(x = BoardXCoordinates.first(), y = BoardYCoordinates.first()))

    private val blackPiece : Piece = Pawn(color = Piece.Color.BLACK, position = IntOffset(x = BoardXCoordinates.first(), y = BoardYCoordinates.first()))

    @Test
    fun testFirstMoveForward() {
        val pawn: Piece = Pawn(color = Piece.Color.WHITE, position = IntOffset(x = 'A'.code, y = 2))

        val moves = pawn.getAvailableMoves(listOf(pawn))

        assertTrue(moves.isNotEmpty())
        assertEquals(IntOffset(x = 'A'.code, y = 3), moves.first())
    }

    @Test
    fun testSecondMoveForwardTrue() {
        val pawn = Pawn(color = Piece.Color.WHITE, position = IntOffset(x = 'A'.code, y = 2))

        val moves = pawn.getAvailableMoves(listOf(pawn))

        assertEquals(2, moves.size)
        assertTrue(IntOffset(x = 'A'.code, y = 3) in moves)
        assertTrue(IntOffset(x = 'A'.code, y = 4) in moves)
    }

    @Test
    fun testSecondMoveForwardFalse() {
        val pawn = Pawn(color = Piece.Color.WHITE, position = IntOffset(x = 'A'.code, y = 3))

        val moves = pawn.getAvailableMoves(listOf(pawn))

        assertEquals(1, moves.size)
        assertEquals(IntOffset(x = 'A'.code, y = 4), moves.first())
    }

    @Test
    fun testNoPossibleMoves(){
        val pawn = Pawn(color = Piece.Color.WHITE, position = IntOffset(x = 'A'.code, y = 3))

        blackPiece.position = IntOffset(x = 'A'.code, y = 4)

        val pieces = listOf(pawn, blackPiece)

        val moves = pawn.getAvailableMoves(pieces)

        assertTrue(moves.isEmpty())
    }

    @Test
    fun testCaptureMove(){
        val pawn = Pawn(color = Piece.Color.WHITE, position = IntOffset(x = 'A'.code, y = 3))

        blackPiece.position = IntOffset(x = 'B'.code, y = 4)

        val pieces = listOf(pawn, blackPiece)

        val moves = pawn.getAvailableMoves(pieces)

        assertTrue(moves.contains(blackPiece.position))
    }

}