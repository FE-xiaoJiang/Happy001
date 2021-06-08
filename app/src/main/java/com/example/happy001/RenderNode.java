package com.example.happy001;

import android.content.Context;

import com.facebook.litho.LithoView;

import org.json.JSONException;
import org.json.JSONObject;

public class RenderNode {
    int pid;
    int id;
    JSONObject props;
    MyView myView;
    LithoView lithoView;
    public RenderNode(int pid, int id, JSONObject props, Context context) {
        this.pid = pid;
        this.id = id;
        this.props = props;
        myView = new MyView(context);
//        lithoView = LithoView.create()
    }

    public void updateProps(JSONObject props) {
        this.props = props;
    }

    public void update() {
        try {
            myView.setExampleString(props.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
