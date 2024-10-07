package com.vandyflintz.farmhousemovies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class BlurredLayout extends RelativeLayout {
    Bitmap resizedBitmap;
    int offset = 0;

    private int index;
    private int pos;
    private int color;

    public BlurredLayout(Context context) {
        super(context);
        callDelayedUpdate();
    }



    public BlurredLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        final Handler handler = new Handler();
        callDelayedUpdate();
    }

    public BlurredLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        callDelayedUpdate();
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    private void callDelayedUpdate() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawBlurImage();
            }
        }, 50);
    }

    public void drawBlurImage() {

        if (resizedBitmap == null) {
            resizedBitmap = Bitmap.createBitmap(MainProfile.blurredBitmap, 0, 0,
                    getWidth(), getHeight());
            resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, this.getWidth(), this.getHeight(), true);
            invalidate();
        }
        // Drawable drawable = new BitmapDrawable(getContext().getResources(), resizedBitmap);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (resizedBitmap != null) {
            if(index==pos) {
                canvas.drawBitmap(resizedBitmap, offset, 0, null);
            }else{
                canvas.drawBitmap(resizedBitmap, offset -(getWidth()+  dpToPx(32)), 0, null);
            }

        }
        canvas.drawColor(color);
        super.onDraw(canvas);
    }

    public void updateView(int offset,int pos) {

        this.offset = offset;

        this.pos = pos;


        invalidate();
    }

    public void setIndex(int index) {

        this.index = index;
    }
    public void setColor(int color) {

        this.color = color;
    }
}
