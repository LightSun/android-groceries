package com.heaven7.android.groceries.developing.netwok;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface NetworkConstant {

    int METHOD_DEPRECATED_GET_OR_POST = -1;
    int METHOD_GET = 0;
    int METHOD_POST = 1;
    int METHOD_PUT = 2;
    int METHOD_DELETE = 3;
    int METHOD_HEAD = 4;
    int METHOD_OPTIONS = 5;
    int METHOD_TRACE = 6;
    int METHOD_PATCH = 7;

    /***
     * cache the data when needed, often is indicate by request param.
     */
    int CACHE_TYPE_NEEDED = 1;
    /**
     * cache only no network
     */
    int CACHE_TYPE_ONLY_NO_NETWORK = 2;

    @IntDef({
            METHOD_DEPRECATED_GET_OR_POST,
            METHOD_GET,
            METHOD_POST,
            METHOD_PUT,
            METHOD_DELETE,
            METHOD_HEAD,
            METHOD_OPTIONS,
            METHOD_TRACE,
            METHOD_PATCH,
    })
    @Retention(RetentionPolicy.CLASS)
    @interface MethodType {

    }

    @IntDef({
            CACHE_TYPE_NEEDED,
            CACHE_TYPE_ONLY_NO_NETWORK,
    })
    @Retention(RetentionPolicy.CLASS)
    @interface CacheType {

    }


}
