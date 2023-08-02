package com.moritzsus.chessflow.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.moritzsus.chessflow.model.ChessGameState;

public class ChessBoardViewModel extends ViewModel {
    private ChessGameState chessGameState;
    private LiveData<String> fenLiveData;

    public ChessBoardViewModel () {
        chessGameState = new ChessGameState();
        fenLiveData = chessGameState.getFenStringLiveData();
    }

    public ChessBoardViewModel(String fen) {
        chessGameState = new ChessGameState(fen);
        fenLiveData = chessGameState.getFenStringLiveData();
    }

    public void updateFenString(String fen) {
        chessGameState.updateFenString(fen);
    }

    public LiveData<String> getFenLiveData() {
        return fenLiveData;
    }
}
