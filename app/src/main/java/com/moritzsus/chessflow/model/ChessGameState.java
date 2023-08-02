package com.moritzsus.chessflow.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ChessGameState {
    private MutableLiveData<String> fenLiveData = new MutableLiveData<>();
    private String[] fenGroups;

    public ChessGameState() {
        fenLiveData.setValue("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        fenGroups = fenLiveData.toString().split(" ");
    }

    public ChessGameState(String fen) {
        //TODO fen string validation
        fenLiveData.setValue(fen);
        fenGroups = fenLiveData.toString().split(" ");
    }

    public void updateFenString(String fen) {
        fenLiveData.setValue(fen);
        fenGroups = fen.split(" ");
    }

    public LiveData<String> getFenStringLiveData() {
        return fenLiveData;
    }

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
}
