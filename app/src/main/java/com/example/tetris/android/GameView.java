package com.example.tetris.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.tetris.core.Constants;
import com.example.tetris.core.GameEngine;
import com.example.tetris.core.GameEvent;

public class GameView extends View {
    private Paint basicPaint;
    private Bitmap gameBitmap;
    private GameEngine gameEngine;
    private float w;
    private float h;

    private void init() {
        basicPaint = new Paint();
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public GameView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        init();
    }

    public void setGameEngine(GameEngine ge) {
        this.gameEngine = ge;
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        int h = MeasureSpec.getSize(heightSpec);
        if(gameBitmap == null) {
            gameBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas gameCanvas = new Canvas(gameBitmap);
            if(gameEngine != null) // for AndroidStudio
                gameEngine.setDrawingBuffer(gameCanvas, w, h);
            this.w = (float)w;
            this.h = (float)h;
        }
        setMeasuredDimension(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawBitmap(gameBitmap, 0, 0, basicPaint);
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
            gameEngine.registerEvent(new GameEvent.SetSpeedEvent(false));
            return false;
        }
        if(event.getY() >= h - Constants.speedUpThreshold * h / Constants.boardHeight
                && event.getAction() == MotionEvent.ACTION_DOWN) {
            // portion of the view corresponding to the lowest speedUpThreshold blocks
            // is for speeding up...
            gameEngine.registerEvent(new GameEvent.SetSpeedEvent(true));
        }
        else {
            // ... rest is for moving the tetromino
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if (event.getX() < w / 2.0)
                    gameEngine.registerEvent(new GameEvent.LeftClickEvent());
                else
                    gameEngine.registerEvent(new GameEvent.RightClickEvent());
                return false;
            }
        }
        return true;
    }
}
