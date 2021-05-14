package com.example.happy001;

import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

public class LayoutManager {
    private static LayoutManager instance;
    ConstraintLayout layout;
    public static LayoutManager getInstance() {
        if (instance == null) {
            instance = new LayoutManager();
        }
        return instance;
    }
    public void setLayout(ConstraintLayout cl) {
        layout = cl;
    }
    public void addView(View v) {
        this.layout.addView(v);
    }
}
