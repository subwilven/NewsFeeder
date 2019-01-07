package com.islam.newsfeeder.data.server_connection_helper;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.POJO.network.ApiResponse;
import com.islam.newsfeeder.util.NetworkUtils;

import java.util.ArrayList;

public abstract class NetworkBoundResource<ResultType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();
    private Context mContext;

    @MainThread
    public NetworkBoundResource(Application context) {
        mContext = context.getApplicationContext();
        result.setValue(Resource.loading(null));
        LiveData<ResultType> dbSource = loadFromDb();
        if (hasNetworkConnection()) {
            result.addSource(dbSource, data -> {
                result.removeSource(dbSource);
                fetchFromNetwork();
                if (((ArrayList) data).size() == 0) {
                    result.setValue(Resource.loading(data));
                }
            });
        } else {
            result.addSource(dbSource,
                    newData -> result.setValue(Resource.success(newData, false)));
        }
    }

    // Called to save the result of the API response into the database
    @WorkerThread
    protected abstract void saveToDatabase(@NonNull ResultType item);

    @MainThread
    protected boolean hasNetworkConnection() {
        return NetworkUtils.haveNetworkConnection(mContext);
    }

    // Called to get the cached data from the database
    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<ResultType>> serveRequest();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed() {
    }


    private void fetchFromNetwork() {
        LiveData<ApiResponse<ResultType>> apiResponse = serveRequest();
        result.addSource(apiResponse, response -> {
            //
            if (apiResponse.getValue() != null && apiResponse.getValue().getData() != null) {
                saveResultAndReInit(response);
                result.removeSource(apiResponse);
            }

        });
    }


    @SuppressLint("StaticFieldLeak")
    @MainThread
    private void saveResultAndReInit(ApiResponse<ResultType> response) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                saveToDatabase(response.getData());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                result.addSource(loadFromDb(),
                        newData -> result.setValue(Resource.success(newData, true)));
            }
        }.execute();
    }

    public final LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}