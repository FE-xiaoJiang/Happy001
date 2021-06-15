package com.example.happy001.uimanager;

import android.view.View;
import android.widget.RelativeLayout;

public class LayoutManager {
    private static LayoutManager instance;
    RelativeLayout layout;
    public static LayoutManager getInstance() {
        if (instance == null) {
            instance = new LayoutManager();
        }
        return instance;
    }
    public void setLayout(RelativeLayout cl) {
        layout = cl;
    }
    public void addView(View v) {
        this.layout.addView(v);
    }
}
