package edu.austral.dissis.chess.engine.pieces

import edu.austral.dissis.chess.engine.util.IntOffset


interface Piece {

    val color: Color

    enum class Color {
        WHITE,
        BLACK;

        val isWhite: Boolean
            get() = this == WHITE

        val isBlack: Boolean
            get() = this == BLACK
    }
    var position: IntOffset

    fun getAvailableMoves(pieces: List<Piece>): Set<IntOffset>
}