package edu.austral.dissis.chess.engine.board

val BoardXCoordinates = List(8) { 'A'.code + it }  // [65..72] → A..H

// Filas 1 a 8
val BoardYCoordinates = (1..8).toList()