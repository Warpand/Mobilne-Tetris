package com.example.tetris.core;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GameDrawingManagerImpl implements GameDrawingManager {
    private final GameLogicManager logicManager;
    private final Canvas myCanvas;
    private final Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private float w;
    private float h;
    private float tileW;
    private float tileH;

    GameDrawingManagerImpl(GameLogicManager logicManager) {
        this.logicManager = logicManager;
        myCanvas = new Canvas();
        paint = new Paint();
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(80f);
    }

    private float translateWidth(int w) {
        return w * tileW;
    }

    private float translateHeight(int h) {
        return (Constants.boardHeight - h - 1) * tileH;
    }
    @Override
    public void draw() {
        if(canvas == null)
            return;
        myCanvas.drawColor(Color.LTGRAY);
        for(int i = 0; i < Constants.boardWidth; i++) {
            for(int j = 0; j < Constants.boardHeight; j++) {
                if(logicManager.getTile(i, j)) {
                    myCanvas.drawRect(
                            translateWidth(i),
                            translateHeight(j),
                            translateWidth(i + 1),
                            translateHeight(j - 1),
                            paint);
                }
            }
        }
        if(!logicManager.isGameOver()) {
            for (Pair p : logicManager.getCurrentTetromino().getBlocks()) {
                myCanvas.drawRect(
                        translateWidth(p.x),
                        translateHeight(p.y),
                        translateWidth(p.x + 1),
                        translateHeight(p.y - 1),
                        paint);
            }
        }
        for(float x = 0; x < w; x += tileW)
            myCanvas.drawLine(x, 0, x, h, paint);
        for(float y = 0; y < h; y += tileH)
            myCanvas.drawLine(0, y, w, y, paint);
        paint.setColor(Color.YELLOW);
        myCanvas.drawLine(0, h - Constants.speedUpThreshold * tileH, w,
                h - Constants.speedUpThreshold * tileH, paint);
        if(logicManager.isGameOver()) {
            paint.setColor(Color.RED);
            myCanvas.drawText("GAME OVER", w / 2f, h / 2f, paint);
        }
        paint.setColor(Color.BLACK);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public void setDrawingBuffer(Canvas canvas, int w, int h) {
        this.canvas = canvas;
        this.w = (float)w;
        this.h = (float)h;
        tileW = this.w / (float)Constants.boardWidth;
        tileH = this.h / (float)Constants.boardHeight;
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        myCanvas.setBitmap(bitmap);
    }
}
