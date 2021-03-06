package com.samahop.samahope.widgets;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PreCachingLayoutManager extends LinearLayoutManager {

    // let's cache more than the default layout space so
    // images load faster when dealing with the doctor card layout
    private static final int DEFAULT_EXTRA_LAYOUT_SPACE = 600;

    public PreCachingLayoutManager(Context context) {
        super(context);
    }

    @Override
    protected int getExtraLayoutSpace(RecyclerView.State state) {
        return DEFAULT_EXTRA_LAYOUT_SPACE;
    }
}