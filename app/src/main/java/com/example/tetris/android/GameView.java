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
            /*
                it needs to be done here, not in constructor in order
                to get the sizes
             */
            // Log.d("SIZE", String.valueOf(w) + ' ' + h);
            gameBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas gameCanvas = new Canvas(gameBitmap);
            if(gameEngine != null) // this if is for AndroidStudio
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
            /*
             * initially type of action was if-ed in "hold down at the bottom to speed up" case,
             * but it lead to a bug where you could touch the screen at the bottom, swipe up
             * and release too high to be caught in the proper if branch again,
             * causing the game to speed up without your finger being actually kept on the screen.
             * Since "touch the screen higher to move left/right" case returns false, this event
             * won't be produced in this case (even if it was, it wouldn't break anything)
             * "hold down" case returns true, so it should always be followed up by this
             * (... unless the onPause happened in between, but this is handled elsewhere)
             */
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
                // the if above prevents the tetromino from moving if the screen was touched
                // at the bottom, but then the finger was moved up
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
