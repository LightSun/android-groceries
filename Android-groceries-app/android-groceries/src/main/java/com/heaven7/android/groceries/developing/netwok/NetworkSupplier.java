package com.heaven7.android.groceries.developing.netwok;

import java.util.Set;

/**
 * Created by heaven7 on 2016/10/26.
 */
public interface NetworkSupplier {

     NetworkRequestExecutor getNetworkRequestExecutor();
     Set<String> getCacheUrls();

}
