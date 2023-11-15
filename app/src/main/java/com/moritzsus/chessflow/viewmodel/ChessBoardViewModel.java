package com.moritzsus.chessflow.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.moritzsus.chessflow.model.ChessGameState;
import com.moritzsus.chessflow.model.ChessPiece;

import java.util.Objects;

public class ChessBoardViewModel extends ViewModel {
    private final ChessGameState chessGameState;
    private final LiveData<String> fenLiveData;
    private final LiveData<ChessPiece[][]> chessBoardWithPiecesLiveData;
    private boolean pieceSelected = false;
    private boolean dragAndDrop = false;
    private boolean firstDrag = true;
    private ChessPiece selectedPiece;
    private int selectedPieceStartX;
    private int selectedPieceStartY;

    public ChessBoardViewModel () {
        selectedPiece = new ChessPiece(ChessPiece.PieceType.NONE, ChessPiece.PieceColor.NONE);
        chessGameState = new ChessGameState();
        fenLiveData = chessGameState.getFenStringLiveData();
        chessBoardWithPiecesLiveData = chessGameState.getChessBoardWithPiecesLiveData();
    }

    public ChessBoardViewModel(String fen) {
        selectedPiece = new ChessPiece(ChessPiece.PieceType.NONE, ChessPiece.PieceColor.NONE);
        chessGameState = new ChessGameState(fen);
        fenLiveData = chessGameState.getFenStringLiveData();
        chessBoardWithPiecesLiveData = chessGameState.getChessBoardWithPiecesLiveData();
    }

    public void updateFenString(String fen) {
        chessGameState.updateFenString(fen);
    }

    public LiveData<String> getFenLiveData() {
        return fenLiveData;
    }

    public LiveData<ChessPiece[][]> getChessBoardWithPiecesLiveData() {
        return chessBoardWithPiecesLiveData;
    }

    public void onNormalClick(float x, float y, float cellSize) {
        //transpose to be in chessBoard format from model
        int boardX = (int) (y / cellSize);
        int boardY = (int) (x / cellSize);

        if(!pieceSelected) { // first click on piece to move
            ChessPiece cp = Objects.requireNonNull(chessBoardWithPiecesLiveData.getValue())[boardX][boardY];

            if(cp.getPieceType() != ChessPiece.PieceType.NONE) {
                pieceSelected = true;
                selectedPieceStartX = boardX;
                selectedPieceStartY = boardY;
            }
        }
        else { //piece selected -> clicked on target square
            //TODO check if move is legal (check in ChessGameState?)
            chessGameState.movePiece(selectedPieceStartX, selectedPieceStartY, boardX, boardY);

            pieceSelected = false;
        }
        dragAndDrop = false;
        Log.d("d", "Position: " + boardX + " " + boardY);

        //chessGameState.printMatrix();
    }

    public ChessPiece onDragChessPiece(float x, float y, float cellSize) {

        //transpose to be in chessBoard format from model
        int boardX = (int) (y / cellSize);
        int boardY = (int) (x / cellSize);

        // reset selected piece if user switches to drag and drop after first clicked on a piece once
        if(!dragAndDrop && pieceSelected) {
            pieceSelected = false;
        }

        if(!pieceSelected) {
            selectedPiece = Objects.requireNonNull(chessBoardWithPiecesLiveData.getValue())[boardX][boardY];

            // deletes visual representation of dragged piece from starting square -> only is displayed where it is being dragged
            if(firstDrag) {
                chessGameState.placePiece(new ChessPiece(ChessPiece.PieceType.NONE, ChessPiece.PieceColor.NONE), boardX, boardY);
                dragAndDrop = true;
                firstDrag = false;
            }

            if(selectedPiece.getPieceType() != ChessPiece.PieceType.NONE) {
                pieceSelected = true;
                selectedPieceStartX = boardX;
                selectedPieceStartY = boardY;
            }
        }
        return selectedPiece;
    }

    public void onDropChessPiece(float x, float y, float cellSize) {
        firstDrag = true;

        if(!pieceSelected)
            return;
        //transpose to be in chessBoard format from model
        int boardX = (int) (y / cellSize);
        int boardY = (int) (x / cellSize);

        chessGameState.placePiece(selectedPiece, selectedPieceStartX, selectedPieceStartY);

        //TODO check for rules, legal moves
        chessGameState.movePiece(selectedPieceStartX, selectedPieceStartY, boardX, boardY);
        pieceSelected = false;
        dragAndDrop = false;
    }
}
