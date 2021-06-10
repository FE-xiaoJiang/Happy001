package com.example.happy001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;

public class SubActivity extends AppCompatActivity {

    private Engine engine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        ConstraintLayout cl = findViewById(R.id.subactivity);
        LayoutManager.getInstance().setLayout(cl); // 父布局管理
        engine = new Engine(this);

        engine.initJSFramework();
        String filePath = "sub.android.js";// "asset:/index.android.js";
        String _source = engine.getJsFileSource(filePath);
        Log.d("js_source", _source);
        String result = engine.runScriptFromUri(filePath, _source);
        Log.d("script result", result);
    }
}