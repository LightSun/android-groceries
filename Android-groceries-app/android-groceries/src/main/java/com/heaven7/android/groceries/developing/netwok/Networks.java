package com.heaven7.android.groceries.developing.netwok;

/**
 * Created by heaven7 on 2016/10/26.
 */
public class Networks {

    private static NetworkSupplier sSupplier;

    public static void initialize(NetworkSupplier supplier) {
        sSupplier = supplier;
    }
    public static NetworkSupplier getNetworkSupplier(){
        return sSupplier;
    }


}
