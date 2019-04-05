package com.islam.newsfeeder.pojo.network;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class Resource<T> {

    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_LOADING = "LOADING";
    @NonNull
    private final boolean hasConnection;
    @NonNull
    private final String status;
    @Nullable
    private final T data;
    @Nullable
    private final String message;

    private Resource(@NonNull String status, @Nullable T data, @Nullable String message, boolean hasConnection) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.hasConnection = hasConnection;
    }

    private Resource(Resource resource, T data) {
        this.data = data;
        this.status = resource.status;
        this.message = resource.message;
        this.hasConnection = resource.hasConnection;
    }

    public static <T> Resource<T> success(T data, boolean hasConnection) {
        return new Resource<>(STATUS_SUCCESS, data, null, hasConnection);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(STATUS_ERROR, data, msg, false);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(STATUS_LOADING, data, null, false);
    }

    public static <T> Resource<T> replace(@Nullable Resource resource, T data) {
        return new Resource<T>(resource, data);
    }


    @NonNull
    public boolean isHasConnection() {
        return hasConnection;
    }

    @NonNull
    public String getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getMessage() {
        return message;
    }
}