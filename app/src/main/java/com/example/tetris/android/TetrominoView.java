package com.example.tetris.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.tetris.core.GameInfoPackage;
import com.example.tetris.core.GameObserver;
import com.example.tetris.core.Pair;
import com.example.tetris.core.Tetromino;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TetrominoView extends androidx.appcompat.widget.AppCompatImageView implements GameObserver {
    private final AtomicBoolean outdated = new AtomicBoolean(false);
    private final AtomicInteger nextTetrominoId = new AtomicInteger();

    private Canvas canvas = null;

    private Bitmap bitmap = null;

    private final Paint paint = new Paint();

    private float w;
    private float h;
    private float tileW;
    private float tileH;

    private float marginLeft;
    private float marginTop;

    private int minX;
    private int maxY;

    private static final int squaresW = 4;
    private static final int squaresH = 4;
    private static final Tetromino[] imageReference = new Tetromino[Tetromino.MAX_TETROMINO_TYPE + 1];

    static {
        for(int i = Tetromino.MIN_TETROMINO_TYPE; i <= Tetromino.MAX_TETROMINO_TYPE; i++)
            imageReference[i] = Tetromino.spawnTetromino(i);
    }

    public TetrominoView(Context context) {
        super(context);
    }

    public TetrominoView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TetrominoView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }
    
    @Override
    public void notifyChange(GameInfoPackage infoPackage) {
        nextTetrominoId.set(infoPackage.getNextTetrominoId());
        outdated.set(true);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        int w = MeasureSpec.getSize(widthSpec);
        int h = MeasureSpec.getSize(heightSpec);
        this.w = (float)w;
        this.h = (float)h;
        tileW = this.w / (float)squaresW;
        tileH = this.h / (float)squaresH;
        if(bitmap == null) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            super.setImageBitmap(bitmap);
        }
        setMeasuredDimension(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(outdated.compareAndSet(true, false)) {
            drawTetromino(nextTetrominoId.get());
        }
        super.onDraw(canvas);
        invalidate();
    }
    
    private float translateWidth(int w) {
        return (w - minX) * tileW + marginLeft;
    }

    private float translateHeight(int h) {
        return (maxY - h) * tileH + marginTop;
    }
    
    private void drawTetromino(int id) {
        int minX = 100, maxX = -100, minY = 100, maxY = -100;
        for(Pair p : imageReference[id].getBlocks()) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }
        this.minX = minX;
        this.maxY = maxY;
        marginLeft = (w - (float)(maxX - minX + 1) * tileW) / 2f;
        marginTop = (h - (float)(maxY - minY + 1) * tileH) / 2f;
        canvas.drawColor(Color.LTGRAY);
        for(Pair p : imageReference[id].getBlocks()) {
            canvas.drawRect(
                    translateWidth(p.x),
                    translateHeight(p.y),
                    translateWidth(p.x + 1),
                    translateHeight(p.y - 1),
                    paint
            );
        }
    }
}
