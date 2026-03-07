package edu.austral.dissis.chess.engine.pieces.movement

import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.util.IntOffset


enum class StraightMovement {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

fun Piece.getStraightMoves(
    pieces: List<Piece>,
    movement: StraightMovement,
    maxMovements: Int = 7,
    canCapture: Boolean = true,
    captureOnly: Boolean = false
): Set<IntOffset> {

    return getMoves(
        pieces = pieces,
        getPosition = {
            when (movement) {
                StraightMovement.UP -> IntOffset(position.x, position.y + it)
                StraightMovement.DOWN -> IntOffset(position.x, position.y - it)
                StraightMovement.LEFT -> IntOffset(position.x - it, position.y)
                StraightMovement.RIGHT -> IntOffset(position.x + it, position.y)
            }
        },
        maxMovements = maxMovements,
        canCapture = canCapture,
        captureOnly = captureOnly
    )
}

