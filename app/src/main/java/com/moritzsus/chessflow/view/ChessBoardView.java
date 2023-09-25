package com.moritzsus.chessflow.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.MotionEventCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.moritzsus.chessflow.R;
import com.moritzsus.chessflow.model.ChessPiece;
import com.moritzsus.chessflow.viewmodel.ChessBoardViewModel;

public class ChessBoardView extends View {
    private ChessBoardViewModel chessBoardViewModel;
    int rows = 8;
    int columns = 8;
    int notationTextPadding = 2;
    int cellSize;
    private final int textSize = 35;
    private Paint lightSquarePaint;
    private Paint darkSquarePaint;
    private Bitmap chessPieceBitmap;
    Canvas canvas;

    public ChessBoardView(Context context) {
        super(context);
        init();
    }

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        chessBoardViewModel = new ViewModelProvider((ViewModelStoreOwner) getContext()).get(ChessBoardViewModel.class);
        chessBoardViewModel.getChessBoardWithPiecesLiveData().observe((LifecycleOwner) getContext(), new Observer<ChessPiece[][]>() {
            @Override
            public void onChanged(ChessPiece[][] board) {
                invalidate();
            }
        });

        lightSquarePaint = new Paint();
        darkSquarePaint = new Paint();

        lightSquarePaint.setColor(Color.rgb(210, 180, 140));
        lightSquarePaint.setTextSize(textSize);
        darkSquarePaint.setColor(Color.rgb(100, 60, 20));
        darkSquarePaint.setTextSize(textSize);

        setOnTouchListener(new OnTouchListener() {
            boolean dragAndDrop = false;
            float startX = 0.f;
            float startY = 0.f;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float moveDeadzone = 10.0f;

                float x = motionEvent.getX();
                float y = motionEvent.getY();
                int action = motionEvent.getActionMasked();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        startX = x;
                        startY = y;
                        //Log.d("d", "DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(!dragAndDrop) {
                            // only start drag and drop after deadzone
                            if(Math.abs(startX - x) > moveDeadzone || Math.abs(startY - y) > moveDeadzone) {
                                dragAndDrop = true;
                            }
                        }
                        //TODO check for delay, because first dragAndDrop frame gets skipped (change if, else order?)
                        else {
                            ChessPiece selectedPiece = chessBoardViewModel.onDragChessPiece(x, y, cellSize);
                            if(selectedPiece == null) {
                                Log.d("n", "NULL");
                            }
                            else {
                                Log.d("", selectedPiece.getPieceColor() + " " + selectedPiece.getPieceType());
                                //TODO drag animation
                                //TODO check if drag in grid -> crash if out of grid
                                //drawChessPiece(selectedPiece,(int) x, (int) y);
                            }


                            Log.d("d", "MOVE " + x + " " + y);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(!dragAndDrop)
                            chessBoardViewModel.onNormalClick(x, y, cellSize);
                        else
                            chessBoardViewModel.onDropChessPiece(x, y, cellSize);
                        //Log.d("d", "UP");
                        dragAndDrop = false;
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        cellSize = Math.min(getWidth(), getHeight()) / rows;

        drawBoard();
        drawChessPieces();
    }

    private void drawBoard() {
        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {

                int left = col * cellSize;
                int right = left + cellSize;
                int top = row * cellSize;
                int bottom = top + cellSize;

                if((row + col) % 2 == 0) {
                    canvas.drawRect(left, top, right, bottom, lightSquarePaint);
                    if(col == 0) {
                        int notation = rows - row;
                        canvas.drawText(Integer.toString(notation), left + notationTextPadding, top + notationTextPadding + textSize, darkSquarePaint);
                    }

                    if(row == (rows - 1)) { // last row
                        int ascii = 65 + col;
                        String notation = "" + (char) ascii;
                        canvas.drawText(notation, right - notationTextPadding - textSize, bottom - notationTextPadding, darkSquarePaint);
                    }
                }
                else {
                    canvas.drawRect(left, top, right, bottom, darkSquarePaint);
                    if(col == 0) {
                        int notation = rows - row;
                        canvas.drawText(Integer.toString(notation), left + notationTextPadding, top + notationTextPadding + textSize, lightSquarePaint);
                    }

                    if(row == (rows - 1)) { // last row
                        int ascii = 65 + col;
                        String notation = "" + (char) ascii;
                        canvas.drawText(notation, right - notationTextPadding - textSize, bottom - notationTextPadding, lightSquarePaint);
                    }
                }
            }
        }
    }

    private void drawChessPieces() {
        ChessPiece[][] board = chessBoardViewModel.getChessBoardWithPiecesLiveData().getValue();

        for(int row = 0; row < rows; row++) {
            for(int col = 0; col < columns; col++) {
                ChessPiece cp = board[row][col];
                drawChessPiece(cp, col, row);
            }
        }
    }

    // x and y is either row and col if drawInGrid is true, or normal coordinates if false
    private void drawChessPiece(ChessPiece chessPiece, int x, int y) {
        if(chessPiece.getPieceType() == ChessPiece.PieceType.NONE) return;

        switch (chessPiece.getPieceType()) {
            case PAWN:
                if(chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE)
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawn_white);
                else
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pawn_black);
                break;
            case KNIGHT:
                if(chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE)
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight_white);
                else
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.knight_black);
                break;
            case BISHOP:
                if(chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE)
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bishop_white);
                else
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bishop_black);
                break;
            case ROOK:
                if(chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE)
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rook_white);
                else
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rook_black);
                break;
            case QUEEN:
                if(chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE)
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.queen_white);
                else
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.queen_black);
                break;
            case KING:
                if(chessPiece.getPieceColor() == ChessPiece.PieceColor.WHITE)
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.king_white);
                else
                    chessPieceBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.king_black);
                break;
        }

        Bitmap scaledChessPieceBitmap = Bitmap.createScaledBitmap(chessPieceBitmap, cellSize, cellSize, false);
        canvas.drawBitmap(scaledChessPieceBitmap, x * cellSize, y * cellSize, null);
    }
}
