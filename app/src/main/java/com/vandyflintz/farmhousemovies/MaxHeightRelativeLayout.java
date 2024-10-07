package com.vandyflintz.farmhousemovies;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

import androidx.annotation.Dimension;
import androidx.annotation.NonNull;

public class MaxHeightRelativeLayout extends RelativeLayout {

    private int maxHeightDP;

    public MaxHeightRelativeLayout(Context context) {
        super(context);
    }

    public MaxHeightRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.MaxHeightRelativeLayout, 0,0);
        try{
          maxHeightDP = typedArray.getInteger(R.styleable.MaxHeightRelativeLayout_maxHeightDP, 0);
        }finally {
           typedArray.recycle();
        }

    }

    public MaxHeightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int maxHeightPx = dp(maxHeightDP);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(maxHeightPx, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setMaxHeightDP(int maxHeightDP){
        this.maxHeightDP = maxHeightDP;
        invalidate();
    }

    public  static  int dp(int px){
        return (int)(px * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float dpToPx(@NonNull Context context, @Dimension(unit= Dimension.DP) int dp){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

}

