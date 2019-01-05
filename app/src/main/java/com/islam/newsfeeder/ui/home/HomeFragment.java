package com.islam.newsfeeder.ui.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.base.BaseFragmentList;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragmentList implements SwipeRefreshLayout.OnRefreshListener {

    HomeViewModel mViewModel;
    ProvidersAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(HomeViewModel.class);
        mAdapter = new ProvidersAdapter();
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setUpObservers() {
        mViewModel.getArticles().observe(getViewLifecycleOwner(), new Observer<Resource<Map<Provider, List<Article>>>>() {
            @Override
            public void onChanged(@Nullable Resource<Map<Provider, List<Article>>> listResource) {
                if (listResource.getData() != null)
                    mAdapter.setData(listResource.getData());
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_list;
    }

    @Override
    public void onRefresh() {
        mViewModel.reload();
    }
}
