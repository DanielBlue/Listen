package com.maoqi.listen.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by maoqi on 2017/6/5.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    int left;
    int top;
    int right;
    int bottom;


    /**
     *
     * @param v top and bottom
     * @param h left and right
     */
    public SpacesItemDecoration(int v,int h) {
        this.top = v;
        this.bottom = v;
        this.left= h;
        this.right= h;

    }

    public SpacesItemDecoration(int left,int top,int right,int bottom) {
        this.top = top;
        this.bottom = bottom;
        this.left= left;
        this.right= right;
    }

    public SpacesItemDecoration(int space) {
        this.top = space;
        this.bottom = space;
        this.left= space;
        this.right= space;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = this.left;
        outRect.right = this.right;
        outRect.bottom = this.bottom;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildPosition(view) == 0)
            outRect.top = this.top;
    }
}
