package com.example.happy001.uimanager;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

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
        for (int i = 0; i < mRenderNodes.size(); i++) {
            RenderNode uiNode = mRenderNodes.get(i);
            uiNode.update();
        }
    }
}
