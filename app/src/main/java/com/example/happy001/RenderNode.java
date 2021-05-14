package com.example.happy001;

import android.content.Context;

public class RenderNode {
    int pid;
    int id;
    MyView myView;
    public RenderNode(int pid, int id, Context context) {
        this.pid = pid;
        this.id = id;
        myView = new MyView(context);
    }

}
