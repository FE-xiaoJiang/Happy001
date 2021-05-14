package com.example.happy001;

import android.content.Context;
import android.util.SparseArray;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

public class RenderManager {
    ArrayList<RenderNode> mRenderNodes;
    public RenderManager() {
        mRenderNodes = new ArrayList<>();
    }
    public void createNode(int pid, int id, Context context) {
        RenderNode uiNode = new RenderNode(pid, id, context);
        mRenderNodes.add(uiNode);
    }
    public void batch() {
        LayoutManager layout = LayoutManager.getInstance();
        for (int i = 0; i < mRenderNodes.size(); i++) {
            layout.addView(mRenderNodes.get(i).myView);
        }
    }
}
