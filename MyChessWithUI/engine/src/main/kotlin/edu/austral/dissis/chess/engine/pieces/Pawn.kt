package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.pieces.movement.*
import edu.austral.dissis.chess.engine.util.IntOffset

class Pawn(
    override val color: Piece.Color,
    override var position: IntOffset,

): Piece {

    override fun getAvailableMoves(pieces: List<Piece>): Set<IntOffset> {
        val isFirstMove = position.y == 2 && color.isWhite ||
                          position.y == 7 && color.isBlack

        return getPieceMoves(pieces) {
            straightMoves(
                movement = if (color.isWhite) StraightMovement.UP else StraightMovement.DOWN,
                maxMovements = if (isFirstMove) 2 else 1,
                canCapture = false
            )

            diagonalMoves(
                movement = if (color.isWhite) DiagonalMovement.UP_RIGHT else DiagonalMovement.DOWN_RIGHT,
                maxMovements = 1,
                captureOnly = true
            )

            diagonalMoves(
                movement = if (color.isWhite) DiagonalMovement.UP_LEFT else DiagonalMovement.DOWN_LEFT,
                maxMovements = 1,
                captureOnly = true
            )

        }
    }
}

