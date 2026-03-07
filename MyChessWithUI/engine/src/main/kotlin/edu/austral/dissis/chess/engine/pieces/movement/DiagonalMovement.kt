package edu.austral.dissis.chess.engine.pieces.movement

import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.util.IntOffset

enum class DiagonalMovement {
    UP_LEFT,
    UP_RIGHT,
    DOWN_LEFT,
    DOWN_RIGHT
}

fun Piece.getDiagonalMoves(
    pieces: List<Piece>,
    movement: DiagonalMovement,
    maxMovements: Int = 7,
    canCapture: Boolean = true,
    captureOnly: Boolean = false
): Set<IntOffset> {

    return getMoves(
        pieces = pieces,
        getPosition = {
            when (movement) {
                DiagonalMovement.UP_LEFT -> IntOffset(position.x - it, position.y + it)
                DiagonalMovement.UP_RIGHT -> IntOffset(position.x + it, position.y + it)
                DiagonalMovement.DOWN_LEFT -> IntOffset(position.x - it, position.y - it)
                DiagonalMovement.DOWN_RIGHT -> IntOffset(position.x + it, position.y - it)
            }
        },
        maxMovements = maxMovements,
        canCapture = canCapture,
        captureOnly = captureOnly,
    )
}