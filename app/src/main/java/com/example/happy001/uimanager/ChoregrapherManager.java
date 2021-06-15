package com.example.happy001.uimanager;

import android.view.Choreographer;

public class ChoregrapherManager {
    private static ChoregrapherManager instance;
    static ChoregrapherManager getInstance() {
        if (null == instance) {
            instance = new ChoregrapherManager();
        }
        return instance;
    }
    public void postFrameCallback(final HappyChoregrapherManager.FrameCallback callback)
    {
        Choreographer.FrameCallback frameCallback = new Choreographer.FrameCallback()
        {
            @Override
            public void doFrame(long frameTimeNanos)
            {
                if (callback != null)
                {
                    callback.doFrame(frameTimeNanos);
                }
            }
        };
        Choreographer.getInstance().postFrameCallback(frameCallback);
    }
}

//class HappyChoregrapherManager {
//    boolean	mHasPostedCallback	= false;
//    HappyChoreographerDispatcher happyChoreographerDispatcher;
//    HappyChoregrapherManager() {
//        this.mCallbackQueues = new ArrayDeque<>();
//        this.happyChoreographerDispatcher = new HappyChoreographerDispatcher();
//    }
//
//    public void postFrameCallback(FrameCallback callback) {
//        mCallbackQueues.add(callback);
//
//        if (!mHasPostedCallback) {
//            ChoregrapherManager.getInstance().postFrameCallback(this.happyChoreographerDispatcher);
//            mHasPostedCallback = true;
//        }
//    }
//
//    public interface FrameCallback {
//        public void doFrame(long frameTimeNanos);
//    }
//    final ArrayDeque<FrameCallback> mCallbackQueues;
//    private class HappyChoreographerDispatcher implements FrameCallback {
//
//        @Override
//        public void doFrame(long frameTimeNanos) {
//            // flush mCallbackQueues
//            int length = mCallbackQueues.size();
//            for (int i = 0; i < length; i++) {
//                mCallbackQueues.removeFirst().doFrame(frameTimeNanos);
//            }
//        }
//    }
//}