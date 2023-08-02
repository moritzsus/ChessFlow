package com.moritzsus.chessflow.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.moritzsus.chessflow.R;
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
    private Bitmap chessPiece;

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
        chessBoardViewModel.getFenLiveData().observe((LifecycleOwner) getContext(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                invalidate();
            }
        });

        lightSquarePaint = new Paint();
        darkSquarePaint = new Paint();

        lightSquarePaint.setColor(Color.WHITE);
        lightSquarePaint.setTextSize(textSize);
        darkSquarePaint.setColor(Color.BLACK);
        darkSquarePaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        cellSize = Math.min(getWidth(), getHeight()) / rows;

        drawBoard(canvas);
        drawChessPieces(canvas);
    }

    private void drawBoard(Canvas canvas) {
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

    // TODO Draw pieces at correct position given by a FEN-String from model
    // only to test piece representation for now
    private void drawChessPieces(Canvas canvas) {
        chessPiece = BitmapFactory.decodeResource(getResources(), R.drawable.king_white);
        Bitmap scaledChessPiece = Bitmap.createScaledBitmap(chessPiece, cellSize, cellSize, false);
        canvas.drawBitmap(scaledChessPiece, 0, 0, null);

        chessPiece = BitmapFactory.decodeResource(getResources(), R.drawable.king_black);
        Bitmap scaledChessPiece2 = Bitmap.createScaledBitmap(chessPiece, cellSize, cellSize, false);
        canvas.drawBitmap(scaledChessPiece2, cellSize, cellSize, null);
    }
}