package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.pieces.movement.getPieceMoves
import edu.austral.dissis.chess.engine.util.IntOffset

class Bishop(
    override val color: Piece.Color,
    override var position: IntOffset,

): Piece {
    override fun getAvailableMoves(pieces: List<Piece>): Set<IntOffset> =
        getPieceMoves(pieces) {
            diagonalMoves()

        }
}