package com.islam.newsfeeder.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.ActivityUtils;

import java.util.List;
import java.util.Map;

import static com.islam.newsfeeder.util.Constants.STATUS_LAYOUT_DATA;
import static com.islam.newsfeeder.util.Constants.STATUS_LAYOUT_LOADING;
import static com.islam.newsfeeder.util.Constants.STATUS_LAYOUT_NO_CONNECTION;
import static com.islam.newsfeeder.util.Constants.STATUS_LAYOUT_NO_DATA;
import static com.islam.newsfeeder.util.Constants.STATUS_SNACK_BAR_NO_CONNECTION;

public abstract class BaseFragmentList extends Fragment {

    protected RecyclerView recyclerView;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    private View noDataLayout;
    private View noConnectionLayout;

    public abstract void onCreateView(View view, Bundle savedInstanceState);

    protected abstract void setUpObservers();

    public abstract int getLayoutId();

    public static int getScreenStatus(Resource<?> result) {

        boolean hasData = false;
        if (result.getData() instanceof List<?>) {
            if (((List) result.getData()).size() > 0) {
                hasData = true;
            }
        } else if (result.getData() instanceof Map<?, ?>) {
            hasData = !((Map) result.getData()).isEmpty();
        }
        if (result.getStatus().equals(Resource.STATUS_LOADING) && result.getData() == null) {
            return STATUS_LAYOUT_LOADING;
        }
        if (result.isHasConnection()) {
            if (result.getData() != null && hasData) {
                return STATUS_LAYOUT_DATA;
            } else if (result.getStatus().equals(Resource.STATUS_SUCCESS)) {
                return STATUS_LAYOUT_NO_DATA;
            }
        } else {
            if (result.getData() != null && hasData
                    && result.getStatus().equals(Resource.STATUS_SUCCESS)) {
                return STATUS_SNACK_BAR_NO_CONNECTION;
            } else if (result.getStatus().equals(Resource.STATUS_SUCCESS)) {
                return STATUS_LAYOUT_NO_CONNECTION;
            }
        }
        return -1;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);

        mSwipeRefreshLayout = rootView.findViewById(R.id.swiperefresh);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        noDataLayout = rootView.findViewById(R.id.no_data_layout);
        noConnectionLayout = rootView.findViewById(R.id.no_connection_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        onCreateView(rootView, savedInstanceState);
        setUpObservers();
        return rootView;
    }

    public void updateScreenStatus(int status) {
        switch (status) {
            case STATUS_LAYOUT_LOADING:
                mSwipeRefreshLayout.setRefreshing(true);
                ActivityUtils.setVisibility(View.GONE, noConnectionLayout, noDataLayout);
                break;
            case STATUS_LAYOUT_NO_CONNECTION:
                ActivityUtils.setVisibility(View.VISIBLE, noConnectionLayout);
                ActivityUtils.setVisibility(View.GONE, noDataLayout);
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case STATUS_LAYOUT_NO_DATA:
                ActivityUtils.setVisibility(View.VISIBLE, noDataLayout);
                ActivityUtils.setVisibility(View.GONE, noConnectionLayout);
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case STATUS_SNACK_BAR_NO_CONNECTION:
                ActivityUtils.setVisibility(View.GONE, noConnectionLayout, noDataLayout);
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case STATUS_LAYOUT_DATA:
                ActivityUtils.setVisibility(View.GONE, noConnectionLayout, noDataLayout);
                mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onDestroyView() {
        //because they leak
        noConnectionLayout = null;
        noDataLayout = null;
        mSwipeRefreshLayout = null;
        super.onDestroyView();
    }
}
