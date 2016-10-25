package com.heaven7.android.groceries.demo.framework;

import android.app.Activity;
import android.content.Context;

/**
 * Created by heaven7 on 2016/10/25.
 */
public  class AbstractManager {

    private WorkContext mWorkContext;

    /*public*/ void attachBaseContext(WorkContext context) {
         this.mWorkContext = context;
    }
    public LogicManager getLogicManager(){
        return mWorkContext.getLogicManager();
    }
    public NetworkManager getNetworkManager(){
        return mWorkContext.getNetworkManager();
    }
    public  UIManager getUIManager(){
        return mWorkContext.getUIManager();
    }

    public Context getContext(){
        return mWorkContext.getContext() ;
    }
    public Activity getActivity() {
        return (Activity)mWorkContext.getContext();
    }

    //============================= subclass may override ==============================

    protected void onInitialize(Context context) {

    }


}
