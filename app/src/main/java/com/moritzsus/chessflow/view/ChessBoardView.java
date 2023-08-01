package com.moritzsus.chessflow.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ChessBoardView extends View {
    private final int textSize = 35;
    private Paint lightSquarePaint;
    private Paint darkSquarePaint;

    public ChessBoardView(Context context) {
        super(context);
        init();
    }

    public ChessBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
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

        int rows = 8;
        int columns = 8;
        int notationTextPadding = 2;
        int cellSize = Math.min(getWidth(), getHeight()) / rows;

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
}
