package com.moritzsus.chessflow.model;

import androidx.annotation.NonNull;

public class ChessPiece {
    // piece graphics from: https://www.vecteezy.com/vector-art/3444481-chess-icon-vector-chess-icon-vector-illustration
    public enum PieceType {NONE, PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING}
    public enum PieceColor {NONE, WHITE, BLACK}
    private PieceType pieceType;
    private PieceColor pieceColor;

    public ChessPiece(PieceType pieceType, PieceColor pieceColor) {
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }
    public PieceColor getPieceColor() {
        return pieceColor;
    }
}
