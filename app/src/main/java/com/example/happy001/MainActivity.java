package com.example.happy001;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private UIManagerModule uiManagerModule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());
        ConstraintLayout cl = findViewById(R.id.rootview);
        LayoutInflater inflater = getLayoutInflater();
        View myView = new MyView(this);
//        View myView = inflater.inflate(R.layout.sample_my_view, null);
//        LinearLayout.LayoutParams myViewLp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getPixelsFromDp(500));
//        myView.setLayoutParams(myViewLp);
        cl.addView(myView);
        LayoutManager.getInstance().setLayout(cl); // 父布局管理

        initJSFramework();
        String filePath = "index.android.js";// "asset:/index.android.js";
        String _source = getJsFileSource(filePath);
        Log.d("js_source", _source);
        String result = runScriptFromUri(filePath, _source);
        Log.d("script result", result);
    }

    public void registerNativeModules() {
        uiManagerModule = new UIManagerModule();
    }

    private int getPixelsFromDp(int size){

        DisplayMetrics metrics =new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    public String getJsFileSource(String fileName) {
        String source = "";
        try {
            InputStream is = getResources().getAssets().open(fileName);
            int length = is.available();
            byte[] buf = new byte[length];
            is.read(buf);
            source = new String(buf);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return source;
    }

    public void js2nativeFun(String moduleName, String moduleFunc, String callId, byte[] params) {
        Object _params = null;
        try {
            _params = new JSONTokener(new String(params)).nextValue();
            if (_params instanceof JSONObject) {
                JSONObject _paramsObj = (JSONObject) _params;
                Log.d("TAG", "js2nativeFun: "+ moduleName + " " + moduleFunc + " " + callId + " " + _paramsObj);
            } else if (_params instanceof JSONArray){
                JSONArray _paramsArr = (JSONArray) _params;
                Log.d("TAG", "js2nativeFun: "+ moduleName + " " + moduleFunc + " " + callId + " " + _paramsArr);
                if (moduleName == "UIManagerModule" && moduleFunc == "createNode") {
                    uiManagerModule.createNode(_paramsArr, this);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native void initJSFramework();
    public native String runScriptFromUri(String file, String jsSource);
}