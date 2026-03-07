package edu.austral.dissis.chess.engine.board

import edu.austral.dissis.chess.engine.pieces.Piece
import edu.austral.dissis.chess.engine.util.IntOffset
import kotlin.properties.Delegates

class Board {
    private val boardPieces = mutableListOf<Piece>()
    val pieces get() = boardPieces.toList()

    var selectedPiece: Piece? = null
        private set

    var selectedPieceMoves: Set<IntOffset> = emptySet()
        private set

    var playerTurn = Piece.Color.WHITE
        private set

    fun selectPiece(piece: Piece) {
        if (piece.color != playerTurn)
            return

        if (piece == selectedPiece) {
            clearSelection()
        } else {
            selectedPiece = piece
            selectedPieceMoves = piece.getAvailableMoves(pieces = pieces).toSet()
        }
    }

    fun moveSelectedPiece(x: Int, y: Int) {
        val piece = selectedPiece ?: return

        if (!isAvailableMove(x = x, y = y))
            return

        if (piece.color != playerTurn)
            return

        movePieceTo(piece = piece, position = IntOffset(x, y))
        clearSelection()

        switchTurn()
    }

    fun isAvailableMove(x: Int, y: Int): Boolean =
        selectedPieceMoves.any { it.x == x && it.y == y }

    private fun movePieceTo(piece: Piece, position: IntOffset) {
        val targetPiece = pieces.find { it.position == position }

        if (targetPiece != null) {
            removePiece(targetPiece)
        }

        piece.position = position
    }

    private fun removePiece(piece: Piece) {
        boardPieces.remove(piece)
    }

    private fun clearSelection() {
        selectedPiece = null
        selectedPieceMoves = emptySet()
    }

    private fun switchTurn() {
        playerTurn = if (playerTurn == Piece.Color.WHITE)
            Piece.Color.BLACK
        else
            Piece.Color.WHITE
    }
}