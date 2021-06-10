package com.example.happy001;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

import androidx.constraintlayout.widget.ConstraintLayout;

public class RenderManager {
    ArrayList<RenderNode> mRenderNodes;
    int count = 1;
    public RenderManager() {
        mRenderNodes = new ArrayList<>();
    }
    public void createNode(int pid, int id, JSONObject props, Context context) {
        RenderNode uiNode = new RenderNode(pid, id, props, context);
        mRenderNodes.add(uiNode);
    }
    public void updateNode(int pid, int id, JSONObject props, Context context) {
        RenderNode uiNode = getRenderNodeById(id);
        if (uiNode != null) {
            uiNode.updateProps(props);
        }
        count++;
    }
    public RenderNode getRenderNodeById(int id) {
        for (int i = 0; i < mRenderNodes.size(); i++) {
            if (mRenderNodes.get(i).id == id) {
                return mRenderNodes.get(i);
            }
        }
        return null;
    }
    public void batch() {
        LayoutManager layout = LayoutManager.getInstance();
        for (int i = 0; i < mRenderNodes.size(); i++) {
            RenderNode uiNode = mRenderNodes.get(i);
            uiNode.update();
            MyView _v = uiNode.myView;
            Log.d("========", mRenderNodes.size()+" "+500 * (i + 1) + "count:"+ count);
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(
                    200 + 400 * (i + 1), 200 * (count)
            );
            _v.setLayoutParams(layoutParams);
            layout.addView(_v);
        }
    }
}
