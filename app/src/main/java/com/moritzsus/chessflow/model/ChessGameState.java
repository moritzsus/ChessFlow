package com.moritzsus.chessflow.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

public class ChessGameState {
    private MutableLiveData<ChessPiece[][]> chessBoardWithPiecesLiveData = new MutableLiveData<>();
    private ChessPiece[][] chessBoard = new ChessPiece[8][8];
    private MutableLiveData<String> fenLiveData = new MutableLiveData<>();
    private String[] fenGroups;
    Map<String, ChessPiece.PieceType> fenToPieceTypeMap = new HashMap<>();

    // TODO init function
    public ChessGameState() {
        String fen = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
        fenLiveData.setValue(fen);
        fenGroups = fen.split(" ");
        fenToPieceTypeMap.put("k", ChessPiece.PieceType.KING);
        fenToPieceTypeMap.put("q", ChessPiece.PieceType.QUEEN);
        fenToPieceTypeMap.put("r", ChessPiece.PieceType.ROOK);
        fenToPieceTypeMap.put("b", ChessPiece.PieceType.BISHOP);
        fenToPieceTypeMap.put("n", ChessPiece.PieceType.KNIGHT);
        fenToPieceTypeMap.put("p", ChessPiece.PieceType.PAWN);
        updateChessBoardFromFen();
    }

    public ChessGameState(String fen) {
        //TODO fen string validation
        fenLiveData.setValue(fen);
        fenGroups = fen.split(" ");
        fenToPieceTypeMap.put("k", ChessPiece.PieceType.KING);
        fenToPieceTypeMap.put("q", ChessPiece.PieceType.QUEEN);
        fenToPieceTypeMap.put("r", ChessPiece.PieceType.ROOK);
        fenToPieceTypeMap.put("b", ChessPiece.PieceType.BISHOP);
        fenToPieceTypeMap.put("n", ChessPiece.PieceType.KNIGHT);
        fenToPieceTypeMap.put("p", ChessPiece.PieceType.PAWN);
        updateChessBoardFromFen();
    }

    public void updateFenString(String fen) {
        fenLiveData.setValue(fen);
        fenGroups = fen.split(" ");
        updateChessBoardFromFen();
    }

    public LiveData<String> getFenStringLiveData() {
        return fenLiveData;
    }

    public LiveData<ChessPiece[][]> getChessBoardWithPiecesLiveData() { return chessBoardWithPiecesLiveData; }

    public String getFenGroupOne () {
        return fenGroups[0];
    }

    public String getFenGroupTwo () {
        return fenGroups[1];
    }

    public String getFenGroupThree () {
        return fenGroups[2];
    }

    public String getFenGroupFour () {
        return fenGroups[3];
    }

    public String getFenGroupFive () {
        return fenGroups[4];
    }

    public String getFenGroupSix () {
        return fenGroups[5];
    }

    public ChessPiece[][] getChessBoardMatrix() {
        return chessBoard;
    }

    private void updateChessBoardFromFen() {
        int row = 0;
        int col = 0;

        for(int i = 0; i < fenGroups[0].length(); i++) {
            char c = fenGroups[0].charAt(i);

            if(c == '/') { // new row
                row++;
                col = 0;
            }
            else if(Character.isDigit(c)) { // skip c columns -> fill empty fields with NONE
                for(int j = 0; j < Character.getNumericValue(c); j++) {
                    ChessPiece.PieceType pt = ChessPiece.PieceType.NONE;
                    ChessPiece.PieceColor pc = ChessPiece.PieceColor.NONE;
                    ChessPiece cp = new ChessPiece(pt, pc);
                    chessBoard[row][col] = cp;

                    col++;
                }
            }
            else { // place given pieceType in matrix
                ChessPiece.PieceColor pc = Character.isUpperCase(c) ? ChessPiece.PieceColor.WHITE : ChessPiece.PieceColor.BLACK;
                ChessPiece.PieceType pt = fenToPieceTypeMap.get(Character.toString(Character.toLowerCase(c)));
                ChessPiece cp = new ChessPiece(pt, pc);
                chessBoard[row][col] = cp;

                col++;
            }
        }
        chessBoardWithPiecesLiveData.setValue(chessBoard);
    }

    //TODO updateFenFromBoard ?

    public boolean movePiece(int fromX, int fromY, int toX, int toY) { //returns if successfully -> legal
        ChessPiece cp = chessBoard[fromX][fromY];
        if(cp.getPieceType() == ChessPiece.PieceType.NONE)
            return false;

        boolean isLegal = isMoveLegal(cp, fromX, fromY, toX, toY);

        // TODO maybe use placePiece here?
        if(isLegal) {
            //Log.d("d", "LEGAL");
            chessBoard[fromX][fromY] = new ChessPiece(ChessPiece.PieceType.NONE, ChessPiece.PieceColor.NONE);
            chessBoard[toX][toY] = cp;

            chessBoardWithPiecesLiveData.setValue(chessBoard);
        }

        return isLegal;
    }

    // TODO why placePiece?
    public void placePiece(ChessPiece piece, int x, int y) {
        chessBoard[x][y] = piece;
    }

    private boolean isMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {
        switch(chessPiece.getPieceType()) {
            case PAWN:
                return isPawnMoveLegal(chessPiece, fromX, fromY, toX, toY);
            case KNIGHT:
                return isKnightMoveLegal(chessPiece, fromX, fromY, toX, toY);
            case BISHOP:
                return isBishopMoveLegal(chessPiece, fromX, fromY, toX, toY);
            case ROOK:
                return isRookMoveLegal(chessPiece, fromX, fromY, toX, toY);
            case QUEEN:
                return isQueenMoveLegal(chessPiece, fromX, fromY, toX, toY);
            case KING:
                return isKingMoveLegal(chessPiece, fromX, fromY, toX, toY);
            default:
                return false;
        }
    }

    private boolean isPawnMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {
        boolean isWhite = chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE;

        if(isWhite) {
            // pawn moving forward?
            if(fromX <= toX)
                return false;

            // pawn moves only 1 square or 2 if on starting position?
            if((fromX > toX + 2) || (fromX > toX + 1 && (fromX != 6)))
                return false;

            // is pawn blocked? checks only on same Y file -> capturing below
            if(chessBoard[fromX - 1][fromY].getPieceType() != ChessPiece.PieceType.NONE && fromY == toY)
                return false;

            // capturing (no en passant)
            if(fromY > toY + 1 || fromY < toY - 1)
                return false;

            // can only capture other color
            if(fromY != toY) {
                ChessPiece cpToCapture = chessBoard[toX][toY];
                return cpToCapture.getPieceColor() == ChessPiece.PieceColor.BLACK;
            }
        }
        else {
            // pawn moving forward?
            if(fromX >= toX)
                return false;

            // pawn moves only 1 square or 2 if on starting position?
            if((fromX < toX - 2) || (fromX < toX - 1 && (fromX != 1)))
                return false;

            // is pawn blocked? checks only on same Y file -> capturing below
            if(chessBoard[fromX + 1][fromY].getPieceType() != ChessPiece.PieceType.NONE && fromY == toY)
                return false;

            // capturing (no en passant)
            if(fromY > toY + 1 || fromY < toY - 1)
                return false;

            // can only capture other color
            if(fromY != toY) {
                ChessPiece cpToCapture = chessBoard[toX][toY];
                return cpToCapture.getPieceColor() == ChessPiece.PieceColor.WHITE;
            }
        }
        // pawn moves forward with no other pieces blocking it
        return true;
    }


    private boolean isKnightMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {

        return true;
    }

    private boolean isBishopMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {

        return true;
    }

    private boolean isRookMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {

        return true;
    }

    private boolean isQueenMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {

        return true;
    }

    private boolean isKingMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {

        return true;
    }

    //TODO del later? only for debugging
    public void printMatrix() {
        for(int i = 0; i < 8; i++) {
            String m = "";
            for(int j = 0; j < 8; j++) {
                m += chessBoard[i][j].getPieceType() + " " + chessBoard[i][j].getPieceColor() + ", ";
            }
            Log.d("d", m);
        }
    }
}
