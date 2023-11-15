package com.tamz.soko2023;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by kru13
 */
public class SokoView extends View {
    Bitmap[] bmp;

    int lW = 10;
    int lH = 10;

    int width;
    int height;
    int w;
    int h;
    int swipes = 0;

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

    private int index;

    private int hero;
    private int previousHeroElement = 0;
    int vertical;

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
        this.vertical = 10;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / lW;
        height = h / lH;
        this.w = w;
        this.h = h;
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
        int m = 0;
        this.swipes++;

        switch (move) {
            case MOVE_LEFT:
                m = -1;
                break;
            case MOVE_RIGHT:
                m = 1;
                break;
            case MOVE_UP:
                m = -this.vertical;
                break;
            case MOVE_DOWN:
                m = this.vertical;
                break;
        }

        Log.d("Move", String.valueOf(m));

        int expectedIndex = this.hero + m;
        if (expectedIndex < 0 || expectedIndex > this.level.length)
            return;
        if (this.level[expectedIndex] == 0 || this.level[expectedIndex] == 3) {
            this.level[this.hero] = this.previousHeroElement;
            this.previousHeroElement = this.level[expectedIndex];
            this.level[expectedIndex] = 4;
            this.hero = expectedIndex;
        }

        if (this.level[expectedIndex] == 2) {
            int expectedBoxIndex = expectedIndex + m;
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
            int expectedBoxIndex = expectedIndex + m;
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
        if(checkForWin()) {
            this.saveScore();
            this.index++;
            int len = (int) MainActivity.levelList.stream().count();
            if (index > len)
                index = 0;
            this.setLevel(MainActivity.levelList.get(index), index);
        }

        this.invalidate();
    }

    private boolean checkForWin() {
        for (int i : this.level)
            if (i == 3)
                return false;
        return true;
    }

    public void setLevel(Level level, int index) {
        Log.d("Set", "Set Level method");
        this.level = level.getMap().clone();
        this.vertical = level.getWidth();
        this.lW = level.getWidth();
        this.lH = level.getHeight();
        width = w / lW;
        height = h / lH;

        int i = 0;
        this.index = index;
        for (int x : this.level) {
            if (x == 4) {
                hero = i;
                break;
            }
            i++;
        }
        this.invalidate();

        this.swipes = 0;
    }

    public void reset() {
        Log.d("Reset", "reset method");
        this.setLevel(MainActivity.levelList.get(this.index), this.index);
        this.swipes = 0;
    }

    private void saveScore() {
        Level l = MainActivity.levelList.get(this.index);
        if(MainActivity.getScore(l.getTitle()) < this.swipes)
            return;

        l.setScore(this.swipes);
        SQLiteDatabase db = MainActivity.dbHelper.getWritableDatabase();

        if(MainActivity.exists(l.getTitle())) {
            ContentValues values = new ContentValues();
            values.put("score", this.swipes);

            String selection = "title = ?";

            String[] selectionArgs = {l.getTitle()};


            db.update("Level", values, selection, selectionArgs);
        } else {
            ContentValues values = new ContentValues();
            values.put("title", l.getTitle());
            values.put("score", this.swipes);
            db.insertWithOnConflict(
                    "Level",
                    null,
                    values,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }
    }
}
