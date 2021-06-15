package com.example.happy001.uimanager;

import android.content.Context;

import com.example.happy001.dom.DomManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UIManagerModule {
    private DomManager domManager;
    public UIManagerModule() {
        domManager = new DomManager();
    }
    public void createNode(JSONArray _params, Context context) throws JSONException {
        // 批量创建节点
        for (int i = 0; i < _params.length(); i++) {
            JSONObject nodeParam = _params.getJSONObject(i);
            int pid = nodeParam.getInt("pid");
            int id = nodeParam.getInt("id");
            JSONObject props = nodeParam.getJSONObject("props");
            domManager.createNode(pid, id, props, context);
        }
    }
    public void updateNode(JSONArray _params, Context context) throws JSONException {
        // 批量创建节点
        for (int i = 0; i < _params.length(); i++) {
            JSONObject nodeParam = _params.getJSONObject(i);
            int pid = nodeParam.getInt("pid");
            int id = nodeParam.getInt("id");
            JSONObject props = nodeParam.getJSONObject("props");
            domManager.updateNode(pid, id, props, context);
        }
    }
}
