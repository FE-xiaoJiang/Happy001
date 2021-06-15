package com.example.happy001.dom;

import android.content.Context;
import android.util.SparseArray;

import com.example.happy001.uimanager.HappyChoregrapherManager;
import com.example.happy001.uimanager.RenderManager;
import com.example.happy001.uimanager.RenderNode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DomManager {
    public interface IDomExecutor {
        void exec();
    }

    HappyChoregrapherManager happyChoregrapherManager;
    DispatchUIFrameCallback dispatchUIFrameCallback;
    boolean mIsDispatchUIFrameCallbackEnqueued = false;
    SparseArray<DomNode> mDomNodes;
    SparseArray<RenderNode> mRenderNodes;
    RenderManager renderManager;
    ArrayList<IDomExecutor> mDispatchRunnable = new ArrayList<>();
    public DomManager() {
        happyChoregrapherManager = new HappyChoregrapherManager();
        dispatchUIFrameCallback = new DispatchUIFrameCallback();
        mDomNodes = new SparseArray<>();
        mRenderNodes = new SparseArray<>();
        renderManager = new RenderManager();
    }
    public void createNode(int pid, int id, JSONObject props, Context context) throws JSONException {
        // 创建节点

        // 触发渲染vsync
        this.addDispatchTask(new IDomExecutor() {
            @Override
            public void exec() {
                renderManager.createNode(pid, id, props, context);
            }
        });
    }

    public void updateNode(int pid, int id, JSONObject newProps, Context context) throws JSONException {
        // 创建节点

        // 触发渲染vsync
        this.addDispatchTask(new IDomExecutor() {
            @Override
            public void exec() {
                renderManager.updateNode(pid, id, newProps, context);
            }
        });
    }

    public void addDispatchTask(IDomExecutor exector) {
        mDispatchRunnable.add(exector);
        if (!mIsDispatchUIFrameCallbackEnqueued) {
            happyChoregrapherManager.postFrameCallback(dispatchUIFrameCallback);
            mIsDispatchUIFrameCallbackEnqueued = true;
        }
    }

    public void flushPendingBatches() {
        mIsDispatchUIFrameCallbackEnqueued = false;
        // 循环遍历处理mDispatchRunnable
        Iterator<IDomExecutor> iterator = mDispatchRunnable.iterator();
        boolean shouldBatch = mDispatchRunnable.size() > 0;
        while (iterator.hasNext())
        {
            IDomExecutor iDomExecutor = iterator.next();
            iDomExecutor.exec();
            iterator.remove();
        }
        // batch，上屏
        if (shouldBatch)
        renderManager.batch();
    }

    private class DispatchUIFrameCallback implements HappyChoregrapherManager.FrameCallback {

        @Override
        public void doFrame(long frameTimeNanos) {
             flushPendingBatches();
        }
    }
}
