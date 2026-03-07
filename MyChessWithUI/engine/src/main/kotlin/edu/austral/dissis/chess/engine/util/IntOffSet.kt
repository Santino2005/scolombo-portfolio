package edu.austral.dissis.chess.engine.util

data class IntOffset(val x: Int, val y: Int) {
    operator fun plus(other: IntOffset): IntOffset = IntOffset(x + other.x, y + other.y)

    fun isValid(boardSize: Int = 8): Boolean = x in 0 until boardSize && y in 0 until boardSize
}