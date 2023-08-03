package com.moritzsus.chessflow.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.moritzsus.chessflow.model.ChessGameState;
import com.moritzsus.chessflow.model.ChessPiece;

public class ChessBoardViewModel extends ViewModel {
    private ChessGameState chessGameState;
    private LiveData<String> fenLiveData;
    private LiveData<ChessPiece[][]> chessBoardWithPiecesLiveData;

    public ChessBoardViewModel () {
        chessGameState = new ChessGameState();
        fenLiveData = chessGameState.getFenStringLiveData();
        chessBoardWithPiecesLiveData = chessGameState.getChessBoardWithPiecesLiveData();
    }

    public ChessBoardViewModel(String fen) {
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
}
