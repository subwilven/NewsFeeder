package com.islam.newsfeeder.pojo.network;

public class NetworkState {
    public static final int STATUS_LOADING = 0;
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAILED = 2;

    private int status;

    public NetworkState(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public static NetworkState LOADING (){
        return new NetworkState(STATUS_LOADING);
    }

    public static NetworkState FAILED (){
        return new NetworkState(STATUS_FAILED);
    }

    public static NetworkState SUCCESS (){
        return new NetworkState(STATUS_SUCCESS);
    }
}
