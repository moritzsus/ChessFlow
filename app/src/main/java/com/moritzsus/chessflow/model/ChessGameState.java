package com.moritzsus.chessflow.model;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessGameState {
    private static class BoardCoordinate {
        public BoardCoordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public int x;
        public int y;

        @Override
        public boolean equals(@Nullable Object obj) {
            BoardCoordinate bc = (BoardCoordinate) obj;
            assert bc != null;
            return (x == bc.x && y == bc.y);
        }
    }
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
        //TODO refactor with BoardCoordinate class
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
        BoardCoordinate targetSquare = new BoardCoordinate(toX, toY);

        List<BoardCoordinate> possibleSquares = new ArrayList<>();

        // add all squares on which the knight can got to on an empty board
        if(fromX - 2 >= 0 && fromY - 1 >= 0)
            possibleSquares.add(new BoardCoordinate(fromX - 2, fromY - 1));
        if(fromX - 1 >= 0 && fromY - 2 >= 0)
            possibleSquares.add(new BoardCoordinate(fromX - 1, fromY - 2));
        if(fromX + 1 <= 7 && fromY - 2 >= 0)
            possibleSquares.add(new BoardCoordinate(fromX + 1, fromY - 2));
        if(fromX + 2 <= 7 && fromY - 1 >= 0)
            possibleSquares.add(new BoardCoordinate(fromX + 2, fromY - 1));
        if(fromX + 2 <= 7 && fromY + 1 <= 7)
            possibleSquares.add(new BoardCoordinate(fromX + 2, fromY + 1));
        if(fromX + 1 <= 7 && fromY + 2 <= 7)
            possibleSquares.add(new BoardCoordinate(fromX + 1, fromY + 2));
        if(fromX - 1 >= 0 && fromY + 2 <= 7)
            possibleSquares.add(new BoardCoordinate(fromX - 1, fromY + 2));
        if(fromX - 2 >= 0 && fromY + 1 <= 7)
            possibleSquares.add(new BoardCoordinate(fromX - 2, fromY + 1));

        // see if targetSquare is in possibleSquares and no piece of the same color is already on that square
        return (possibleSquares.contains(targetSquare) && chessBoard[targetSquare.x][targetSquare.y].getPieceColor() != chessPiece.getPieceColor());
    }

    private boolean isBishopMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {
        BoardCoordinate targetSquare = new BoardCoordinate(toX, toY);

        List<BoardCoordinate> possibleSquares = new ArrayList<>();

        // add possible squares on all 4 diagonals (4 while loops)
        int x = fromX + 1;
        int y = fromY + 1;
        while(x <= 7 && y <= 7) {
            if(chessBoard[x][y].getPieceType() == ChessPiece.PieceType.NONE) {
                // if no piece on square -> add square to possibleSquares and keep going
                possibleSquares.add(new BoardCoordinate(x, y));
                x++;
                y++;
            }
            else {
                // if piece on square -> add square if opposite color, then break out of loop
                if(chessBoard[x][y].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(x, y));
                }
                break;
            }
        }

        x = fromX + 1;
        y = fromY - 1;
        while(x <= 7 && y >= 0) {
            if(chessBoard[x][y].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(x, y));
                x++;
                y--;
            }
            else {
                if(chessBoard[x][y].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(x, y));
                }
                break;
            }
        }

        x = fromX - 1;
        y = fromY + 1;
        while(x >= 0 && y <= 7) {
            if(chessBoard[x][y].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(x, y));
                x--;
                y++;
            }
            else {
                if(chessBoard[x][y].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(x, y));
                }
                break;
            }
        }

        x = fromX - 1;
        y = fromY - 1;
        while(x >= 0 && y >= 0) {
            if(chessBoard[x][y].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(x, y));
                x--;
                y--;
            }
            else {
                if(chessBoard[x][y].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(x, y));
                }
                break;
            }
        }

        return (possibleSquares.contains(targetSquare));
    }

    private boolean isRookMoveLegal(ChessPiece chessPiece, int fromX, int fromY, int toX, int toY) {
        BoardCoordinate targetSquare = new BoardCoordinate(toX, toY);

        List<BoardCoordinate> possibleSquares = new ArrayList<>();

        // add possible squares in all 4 directions (4 while loops)
        int x = fromX + 1;
        while (x <= 7) {
            if(chessBoard[x][fromY].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(x, fromY));
                x++;
            }
            else {
                // if piece on square -> add square if opposite color, then break out of loop
                if(chessBoard[x][fromY].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(x, fromY));
                }
                break;
            }
        }

        x = fromX - 1;
        while (x >= 0) {
            if(chessBoard[x][fromY].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(x, fromY));
                x--;
            }
            else {
                if(chessBoard[x][fromY].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(x, fromY));
                }
                break;
            }
        }

        int y = fromY + 1;
        while (y <= 7) {
            if(chessBoard[fromX][y].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(fromX, y));
                y++;
            }
            else {
                if(chessBoard[fromX][y].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(fromX, y));
                }
                break;
            }
        }

        y = fromY - 1;
        while (y >= 0) {
            if(chessBoard[fromX][y].getPieceType() == ChessPiece.PieceType.NONE) {
                possibleSquares.add(new BoardCoordinate(fromX, y));
                y--;
            }
            else {
                if(chessBoard[fromX][y].getPieceColor() != chessPiece.getPieceColor()) {
                    possibleSquares.add(new BoardCoordinate(fromX, y));
                }
                break;
            }
        }

        return (possibleSquares.contains(targetSquare));
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
