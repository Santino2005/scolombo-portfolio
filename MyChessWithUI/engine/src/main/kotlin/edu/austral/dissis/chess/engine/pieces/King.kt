package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.pieces.movement.getPieceMoves
import edu.austral.dissis.chess.engine.util.IntOffset

class King(
    override val color: Piece.Color,
    override var position: IntOffset,

): Piece {
    override fun getAvailableMoves(pieces: List<Piece>): Set<IntOffset> =
        getPieceMoves(pieces) {
            straightMoves(maxMovements = 1)
            diagonalMoves(maxMovements = 1)
        }
}