package com.heaven7.android.groceries.demo.framework;

import android.content.Context;
import android.view.View;

/**
 * Created by heaven7 on 2016/10/25.
 */
public class WorkContext {

   private final LogicManager mLogicM;
   private final NetworkManager mNetworkM;
   private final UIManager mUiM;
   private Context mContext;

   public WorkContext(UIManager mUiM , LogicManager mLogicM, NetworkManager mNetworkM) {
      this.mLogicM = mLogicM;
      this.mNetworkM = mNetworkM;
      this.mUiM = mUiM;
      mUiM.attachBaseContext(this);
      mLogicM.attachBaseContext(this);
      mNetworkM.attachBaseContext(this);
   }

   /**
    * must call this first
    * @param root the root view
     */
   public void init(View root){
       final Context context = mContext = root.getContext();
      // getUIManager().attach(root);
       getUIManager().onInitialize(context);
       getLogicManager().onInitialize(context);
       getNetworkManager().onInitialize(context);
   }

   public LogicManager getLogicManager(){
      return mLogicM;
   }
   public NetworkManager getNetworkManager(){
      return mNetworkM;
   }
   public UIManager getUIManager(){
      return mUiM;
   }
   public Context getContext(){
      return mContext;
   }

}
