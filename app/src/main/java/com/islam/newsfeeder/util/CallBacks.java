package com.islam.newsfeeder.util;

public final class CallBacks {

    public interface AdapterCallBack<T> {
        void onItemClicked(T item);
    }

    public interface NetworkCallBack<T> {
        void onSuccess(T item);

        void onFailed(String error);
    }
}
