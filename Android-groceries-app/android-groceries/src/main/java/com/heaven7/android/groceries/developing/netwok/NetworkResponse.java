package com.heaven7.android.groceries.developing.netwok;

/**
 * Created by heaven7 on 2016/10/26.
 */
public abstract class NetworkResponse<T> {

    /**
     * request url
     */
    private String mRequestUrl;
    /**
     * consume time in millisecond.
     */
    private long mConsumeTime;

    public String getRequestUrl() {
        return mRequestUrl;
    }
    public void setRequestUrl(String url) {
        this.mRequestUrl = url;
    }

    public long getConsumeTime() {
        return mConsumeTime;
    }
    public void setConsumeTime(long consumeTime) {
        this.mConsumeTime = consumeTime;
    }

    /**
     * called on the normal response
     * @param response the response
     */
    public abstract void onResponse(T response);

    /**
     * called on response error. often is network error.
     * @param msg the message of this error.
     */
    public abstract void onErrorResponse(String msg);


    public static abstract class NetworkStringResponse extends NetworkResponse<String> {
    }

}
