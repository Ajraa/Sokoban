package com.tamz.soko2023;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by kru13
 */
public class SokoView extends View {

    Bitmap[] bmp;

    int lW = 10;
    int lH = 10;

    int width;
    int height;

    private int level[] = {
            1,1,1,1,1,1,1,1,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,4,0,1,0,
            1,0,1,3,2,3,2,0,1,0,
            1,0,2,3,3,2,1,0,1,0,
            1,0,0,0,0,0,0,0,1,0,
            1,1,1,1,1,1,1,1,1,0,
            0,0,0,0,0,0,0,0,0,0
    };

    private int hero;
    private int previousHeroElement = 0;

    public SokoView(Context context) {
        super(context);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SokoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        bmp = new Bitmap[6];

        bmp[0] = BitmapFactory.decodeResource(getResources(), R.drawable.empty);
        bmp[1] = BitmapFactory.decodeResource(getResources(), R.drawable.wall);
        bmp[2] = BitmapFactory.decodeResource(getResources(), R.drawable.box);
        bmp[3] = BitmapFactory.decodeResource(getResources(), R.drawable.goal);
        bmp[4] = BitmapFactory.decodeResource(getResources(), R.drawable.hero);
        bmp[5] = BitmapFactory.decodeResource(getResources(), R.drawable.boxok);

        int i = 0;
        for (int x : this.level) {
            if (x == 4) {
                hero = i;
                break;
            }
            i++;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / lW;
        height = h / lH;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        for (int y = 0; y < lH; y++) {
            for (int x = 0; x < lW; x++) {
                canvas.drawBitmap(bmp[level[y*lW + x]], null,
                        new Rect(x*width,
                                y*height,
                                (x+1)*width,
                                (y+1)*height), null);
            }
        }
    }

    public void moveHero(Move move) {
        int expectedIndex = this.hero + move.getValue();
        if (expectedIndex < 0 || expectedIndex > this.level.length)
            return;
        if (this.level[expectedIndex] == 0 || this.level[expectedIndex] == 3) {
            this.level[this.hero] = this.previousHeroElement;
            this.previousHeroElement = this.level[expectedIndex];
            this.level[expectedIndex] = 4;
            this.hero = expectedIndex;
        }

        if (this.level[expectedIndex] == 2) {
            int expectedBoxIndex = expectedIndex + move.getValue();
            if (this.level[expectedBoxIndex] == 0 || this.level[expectedBoxIndex] == 3) {
                this.level[this.hero] = this.previousHeroElement;
                this.previousHeroElement = 0;
                this.level[expectedIndex] = 4;
                this.hero = expectedIndex;
                if (this.level[expectedBoxIndex] == 0)
                    this.level[expectedBoxIndex] = 2;
                else
                    this.level[expectedBoxIndex] = 5;
            }
        }

        if (this.level[expectedIndex] == 5) {
            int expectedBoxIndex = expectedIndex + move.getValue();
            if (this.level[expectedBoxIndex] == 0 || this.level[expectedBoxIndex] == 3) {
                this.level[this.hero] = this.previousHeroElement;
                this.previousHeroElement = 3;
                this.level[expectedIndex] = 4;
                this.hero = expectedIndex;
                if (this.level[expectedBoxIndex] == 0)
                    this.level[expectedBoxIndex] = 2;
                else
                    this.level[expectedBoxIndex] = 5;
            }
        }
        if(chechForWin())
            Log.d("WIN", "WIN");
        this.invalidate();
    }

    private boolean chechForWin() {
        for (int i : this.level)
            if (i == 3)
                return false;
        return true;
    }
}
