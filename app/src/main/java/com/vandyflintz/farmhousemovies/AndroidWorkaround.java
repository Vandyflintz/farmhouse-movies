package com.vandyflintz.farmhousemovies;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

public class AndroidWorkaround {

    static void assistActivity(Activity activity){
        new AndroidWorkaround(activity);
    }

    private View rootview;
    private ViewGroup contentcontainer;
    private ViewTreeObserver viewTreeObserver;
    private ViewTreeObserver.OnGlobalLayoutListener listener;
    private Rect contentAreaofWindowBounds = new Rect();
    private FrameLayout.LayoutParams layoutParams;
    private int usableheightprevious = 0;

    public AndroidWorkaround(Activity activity) {
        contentcontainer = (ViewGroup)activity.findViewById(android.R.id.content);
        rootview = contentcontainer.getChildAt(0);
        layoutParams = (FrameLayout.LayoutParams)rootview.getLayoutParams();
        listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                possiblyResizeChildofContent();
            }
        };

    }


    public void onPause(){
        if(viewTreeObserver.isAlive()){
            viewTreeObserver.removeOnGlobalLayoutListener(listener);
        }
    }

    public void onResume(){
        if(viewTreeObserver == null || !viewTreeObserver.isAlive()){
            viewTreeObserver = rootview.getViewTreeObserver();
        }
        viewTreeObserver.addOnGlobalLayoutListener(listener);
    }

    public void onDestroy(){
        rootview = null;
        contentcontainer= null;
        viewTreeObserver = null;
    }

    private void possiblyResizeChildofContent(){
        contentcontainer.getWindowVisibleDisplayFrame(contentAreaofWindowBounds);
        int usableheightnow = contentAreaofWindowBounds.height();

        if(usableheightnow != usableheightprevious){
            layoutParams.height = usableheightnow;
            rootview.layout(contentAreaofWindowBounds.bottom, contentAreaofWindowBounds.left,
                    contentAreaofWindowBounds.right, contentAreaofWindowBounds.top);
            rootview.requestLayout();
            usableheightprevious = usableheightnow;
        }

    }

}
