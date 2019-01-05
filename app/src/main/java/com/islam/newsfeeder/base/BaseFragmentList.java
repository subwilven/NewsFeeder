package com.islam.newsfeeder.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.islam.newsfeeder.R;

public abstract class BaseFragmentList extends Fragment {

    protected RecyclerView recyclerView;
    private ProgressBar progressBar;
    private View noDataLayout;
    private View noConnectionLayout;

    public abstract void onCreateView(View view, Bundle savedInstanceState);

    protected abstract void setUpObservers();

    public abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayoutId(), container, false);

        progressBar = rootView.findViewById(R.id.progress_bar);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        noDataLayout = rootView.findViewById(R.id.no_data_layout);
        noConnectionLayout = rootView.findViewById(R.id.no_connection_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        onCreateView(rootView, savedInstanceState);
        setUpObservers();
        return rootView;
    }

//    public void updateScreenStatus(int status) {
//        switch (status) {
//            case STATUS_LAYOUT_LOADING:
//                Utils.setVisibility(View.VISIBLE, progressBar);
//                Utils.setVisibility(View.GONE, noConnectionLayout, noDataLayout);
//                break;
//            case STATUS_LAYOUT_NO_CONNECTION:
//                Utils.setVisibility(View.VISIBLE, noConnectionLayout);
//                Utils.setVisibility(View.GONE, progressBar, noDataLayout);
//                break;
//            case STATUS_LAYOUT_NO_DATA:
//                Utils.setVisibility(View.VISIBLE, noDataLayout);
//                Utils.setVisibility(View.GONE, progressBar, noConnectionLayout);
//                break;
//            case STATUS_SNACK_BAR_NO_CONNECTION:
//                Utils.setVisibility(View.GONE, progressBar, noConnectionLayout, noDataLayout);
//                //((MainActivity) getActivity()).showSnackBar();
//                break;
//            case STATUS_LAYOUT_DATA:
//                Utils.setVisibility(View.GONE, progressBar, noConnectionLayout, noDataLayout);
//        }
//
//    }

    @Override
    public void onDestroyView() {
        //because they leak
        noConnectionLayout = null;
        noDataLayout = null;
        progressBar = null;
        super.onDestroyView();
    }
}
