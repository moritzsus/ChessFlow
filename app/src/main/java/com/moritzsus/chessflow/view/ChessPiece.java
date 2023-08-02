package com.moritzsus.chessflow.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.moritzsus.chessflow.R;

// TODO: class needed? or as bimtmaps in chessBoardView?
public class ChessPiece extends AppCompatImageView {
    // piece graphics from: https://www.vecteezy.com/vector-art/3444481-chess-icon-vector-chess-icon-vector-illustration
    enum PieceType {PAWN, KNIGHT, BISHOP, ROOK, QUEEN, KING}
    enum PieceColor {WHITE, BLACK}
    private PieceType pieceType;
    private PieceColor pieceColor;

    public ChessPiece(@NonNull Context context, PieceType pieceType, PieceColor pieceColor) {
        super(context);
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
    }

    public ChessPiece(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.pieceType = PieceType.PAWN;
        this.pieceColor = PieceColor.WHITE;
    }

    public void draw() {
        int imageResource = getPieceImageResource(pieceType, pieceColor);
        setImageResource(imageResource);
    }

    private int getPieceImageResource(PieceType pieceType, PieceColor pieceColor) {
        switch (pieceType) {
            case PAWN:
                if(pieceColor == PieceColor.WHITE)
                    return R.drawable.pawn_white;
                else
                    return R.drawable.pawn_black;
            case KNIGHT:
                if(pieceColor == PieceColor.WHITE)
                    return R.drawable.knight_white;
                else
                    return R.drawable.knight_black;
            case BISHOP:
                if(pieceColor == PieceColor.WHITE)
                    return R.drawable.bishop_white;
                else
                    return R.drawable.bishop_black;
            case ROOK:
                if(pieceColor == PieceColor.WHITE)
                    return R.drawable.rook_white;
                else
                    return R.drawable.rook_black;
            case QUEEN:
                if(pieceColor == PieceColor.WHITE)
                    return R.drawable.queen_white;
                else
                    return R.drawable.queen_black;
            case KING:
                if(pieceColor == PieceColor.WHITE)
                    return R.drawable.king_white;
                else
                    return R.drawable.king_black;
            default:
                return -1;
        }
    }
}
