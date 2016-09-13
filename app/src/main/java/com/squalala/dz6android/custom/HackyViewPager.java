package com.squalala.dz6android.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Back Packer
 * Date : 25/04/15
 */
public class HackyViewPager extends ViewPager {


    private boolean isLocked;

    public HackyViewPager(Context context) {
        super(context);
        isLocked = false;
    }


    public HackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isLocked = false;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isLocked) {
            try {
                return super.onInterceptTouchEvent(ev);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isLocked) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    public void toggleLock() {
        isLocked = !isLocked;
    }


    public void setLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }


    public boolean isLocked() {
        return isLocked;
    }

}