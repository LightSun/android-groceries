package com.heaven7.android.groceries.demo.framework;

/**
 * Created by heaven7 on 2016/10/25.
 */
public class WorkFactory {

    public static WorkContext createWorkContext(UIManager ui, LogicManager logic, NetworkManager network){
         return new WorkContext(ui , logic, network);
    }
}
