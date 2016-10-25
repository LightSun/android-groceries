package com.heaven7.android.groceries.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by heaven7 on 2016/10/25.
 */
//TODO
public class ActivityStack {

    @TargetApi(14)
    public ActivityStack(Context context) {
        ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks(new ActivityLifeCycleCallbackImpl());
    }

    private class ActivityLifeCycleCallbackImpl implements Application.ActivityLifecycleCallbacks{

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
              //add to list
        }
        @Override
        public void onActivityStarted(Activity activity) {

        }
        @Override
        public void onActivityResumed(Activity activity) {
            //current activity
        }
        @Override
        public void onActivityPaused(Activity activity) {

        }
        @Override
        public void onActivityStopped(Activity activity) {

        }
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }
        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
