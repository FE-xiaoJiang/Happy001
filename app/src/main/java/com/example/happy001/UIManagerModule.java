package com.example.happy001;

import android.content.Context;

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
            JSONObject nodeParam = _params.getJSONObject(0);
            int pid = nodeParam.getInt("pid");
            int id = nodeParam.getInt("id");
            domManager.createNode(pid, id, context);
        }
    }
}
