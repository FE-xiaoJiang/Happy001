package com.example.happy001;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.facebook.litho.LithoView;

import org.json.JSONException;
import org.json.JSONObject;

public class RenderNode {
    int pid;
    int id;
    JSONObject props;
    MyView myView;
    LayoutManager layout = LayoutManager.getInstance();
    RelativeLayout.LayoutParams layoutParams;
    LithoView lithoView;
    public RenderNode(int pid, int id, JSONObject props, Context context) {
        this.pid = pid;
        this.id = id;
        this.props = props;
        myView = new MyView(context);
        try {
            layoutParams = new RelativeLayout.LayoutParams(props.getInt("width"), props.getInt("height"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        myView.setLayoutParams(layoutParams);
//        lithoView = LithoView.create()
    }

    public void updateProps(JSONObject props) {
        this.props = props;
    }

    public void update() {
        try {
            myView.setExampleString(props.getString("text"));
            myView.setBackgroundColor(Color.parseColor(props.getString("backgroundColor")));
            layoutParams.topMargin = props.getInt("top");
            layout.addView(this.myView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
