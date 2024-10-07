package com.vandyflintz.farmhousemovies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class CircularImageView extends NetworkImageView {
    Context mcontext;

    public CircularImageView(Context context) {
        super(context);
        mcontext = context;

    }

    public CircularImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet,0);
        mcontext = context;
    }

    public CircularImageView(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bitmap){
        if(bitmap==null)return;
        setImageDrawable(new BitmapDrawable(mcontext.getResources(),getCircularBitmap(bitmap)));
    }

    public Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        int width = bitmap.getWidth();
        if(bitmap.getWidth() > bitmap.getHeight())
            width = bitmap.getHeight();
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, width, width);
            final RectF rectF = new RectF(rect);
            final float roundpixel = width/2;

            paint.setAntiAlias(true);
            canvas.drawARGB(0,0,0,0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundpixel, roundpixel, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

}
