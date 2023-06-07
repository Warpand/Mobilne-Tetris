package com.example.tetris.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

import com.example.tetris.core.GameInfoPackage;
import com.example.tetris.core.GameObserver;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class RivalScoreView extends androidx.appcompat.widget.AppCompatTextView implements GameObserver {
    private final AtomicBoolean outdated = new AtomicBoolean(false);
    private final AtomicInteger score = new AtomicInteger(0);

    public RivalScoreView(Context context) {
        super(context);
    }

    public RivalScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RivalScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void notifyChange(GameInfoPackage infoPackage) {
        score.set(infoPackage.getRivalScore());
        outdated.set(true);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(outdated.compareAndSet(true, false)) {
            if (score.get() >= 0) {
                String oldText = getText().toString();
                String text = oldText.substring(0, oldText.indexOf('\n')) + '\n' + score.get();
                super.setText(text);
            }
            else {
                setTextColor(Color.RED);
                // Log.d("SCOREVIEW", String.valueOf(score.get()));
            }
        }
        super.onDraw(canvas);
        invalidate();
    }
}
