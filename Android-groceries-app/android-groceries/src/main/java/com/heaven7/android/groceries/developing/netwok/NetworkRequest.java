package com.heaven7.android.groceries.developing.netwok;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heaven7 on 2016/10/26.
 */
public class NetworkRequest {

    private int method;
    private String url;
    private Map<String, String> requestParams;

    private int cacheType;
    private NetworkResponse response;
    private boolean forceRefresh;

    private NetworkRequest(){}
    public static Builder newBuilder(){
        return new Builder();
    }

    public void execute() {
        //TODO check params file request. headers
        final NetworkSupplier supplier = Networks.getNetworkSupplier();
        supplier.getNetworkRequestExecutor().executeRequest(supplier.getCacheUrls(), this);
    }

    public static class Builder {
        //cache url
        private int method;
        private String url;
        private Map<String, String> requestParams;

        private int cacheType;
        private NetworkResponse response;
        private boolean forceRefresh;

        public Builder method(@NetworkConstant.MethodType int method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder requestParams(Map<String, String> map) {
            this.requestParams = map;
            return this;
        }

        public Builder addRequestParam(String key, String value) {
            if (requestParams == null) {
                requestParams = new HashMap<>();
            }
            requestParams.put(key, value);
            return this;
        }

        public Builder cacheType(@NetworkConstant.CacheType int cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        public Builder response(NetworkResponse response) {
            this.response = response;
            return this;
        }
        public Builder forceRefresh() {
            this.forceRefresh = true;
            return this;
        }
        public NetworkRequest build() {
            NetworkRequest request = new NetworkRequest();
            request.method = method;
            request.cacheType = cacheType;
            request.requestParams = requestParams;
            request.url = url;
            request.response = response;
            request.forceRefresh = forceRefresh;
            return request;
        }
    }

}
