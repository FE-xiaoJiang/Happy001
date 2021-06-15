package com.example.happy001;

import android.content.Context;
import android.util.Log;

import com.example.happy001.uimanager.UIManagerModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.InputStream;

public class Engine {

    static {
        System.loadLibrary("native-lib");
    }

    private UIManagerModule uiManagerModule;
    Context context;
    public Engine(Context context) {
        uiManagerModule = new UIManagerModule();
        this.context = context;
    }

    public String getJsFileSource(String fileName) {
        String source = "";
        try {
            InputStream is = context.getResources().getAssets().open(fileName);
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
                if ("UIManagerModule".equals(moduleName) && "createNode".equals(moduleFunc)) {
                    uiManagerModule.createNode(_paramsArr, this.context);
                } else if ("UIManagerModule".equals(moduleName) && "updateNode".equals(moduleFunc)) {
//                    Thread.sleep(5000);
                    uiManagerModule.updateNode(_paramsArr, this.context);
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
