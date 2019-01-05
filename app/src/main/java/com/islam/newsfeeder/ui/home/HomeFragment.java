package com.islam.newsfeeder.ui.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.base.BaseFragmentList;
import com.islam.newsfeeder.ui.article_details.ArticleDetailsFragment;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragmentList implements SwipeRefreshLayout.OnRefreshListener, CallBacks.AdapterCallBack<Article> {

    public final static String TAG = "HomeFragment";
    HomeViewModel mViewModel;
    ProvidersAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(getActivity(), ViewModelFactory.getInstance()).get(HomeViewModel.class);
        mAdapter = new ProvidersAdapter(this);
        recyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setUpObservers() {
        mViewModel.getArticles().observe(getViewLifecycleOwner(), new Observer<Resource<Map<String, List<Article>>>>() {
            @Override
            public void onChanged(@Nullable Resource<Map<String, List<Article>>> listResource) {
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

    @Override
    public void onItemClicked(Article item) {
        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();

        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null)
                .add(R.id.container, ArticleDetailsFragment.getInstance(item)).commit();

    }
}
