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
        //TODO rules -> moves legal?
        ChessPiece cp = chessBoard[fromX][fromY];
        if(cp.getPieceType() == ChessPiece.PieceType.NONE)
            return false;

        chessBoard[fromX][fromY] = new ChessPiece(ChessPiece.PieceType.NONE, ChessPiece.PieceColor.NONE);
        chessBoard[toX][toY] = cp;

        chessBoardWithPiecesLiveData.setValue(chessBoard);

        return true;
    }

    //TODO del later? only for debugging
    public void printMatrix() {
        for(int i = 0; i < 8; i++) {
            String m = "";
            for(int j = 0; j < 8; j++) {
                //m += chessBoard[i][j].getPieceName() + ", ";
            }
            Log.d("d", m);
        }
    }
}
