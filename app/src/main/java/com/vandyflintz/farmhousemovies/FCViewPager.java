package com.vandyflintz.farmhousemovies;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class FCViewPager extends ViewPager {

    public boolean enableswipe;

    public FCViewPager(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public FCViewPager(Activity activity) {
        super(activity);
        init();
    }


    private void init(){
        enableswipe = true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        return enableswipe && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return enableswipe && super.onTouchEvent(event);
    }

    public void setEnableswipe(boolean enableswipe){
        this.enableswipe = enableswipe;
        //Toast.makeText(getContext(), String.valueOf(enableswipe), Toast.LENGTH_LONG).show();
    }
}
