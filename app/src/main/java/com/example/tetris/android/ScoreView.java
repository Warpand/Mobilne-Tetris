package com.example.tetris.android;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.example.tetris.core.GameInfoPackage;
import com.example.tetris.core.GameObserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ScoreView extends androidx.appcompat.widget.AppCompatTextView implements GameObserver {
    private final AtomicBoolean outdated = new AtomicBoolean(false);
    private final AtomicInteger score = new AtomicInteger(0);

    public ScoreView(Context context) {
        super(context);
    }

    public ScoreView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ScoreView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    public void notifyChange(GameInfoPackage infoPackage) {
        score.set(infoPackage.getScore());
        outdated.set(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(outdated.compareAndSet(true, false)) {
            String text = "Score:\n" + score.get();
            super.setText(text);
        }
        super.onDraw(canvas);
        invalidate();
    }
}
