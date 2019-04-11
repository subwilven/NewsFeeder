package com.islam.newsfeeder.ui.articles_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.base.BaseFragmentList;
import com.islam.newsfeeder.dagger.view_model.DaggerViewModelFactoryComponent;
import com.islam.newsfeeder.pojo.Article;
import com.islam.newsfeeder.pojo.network.NetworkState;
import com.islam.newsfeeder.ui.providers_filter.ProvidersFilterActivity;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

public class ArticlesListFragment extends BaseFragmentList implements SwipeRefreshLayout.OnRefreshListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    public final static String TAG = "ArticlesListFragment";
    private ArticlesViewModel mViewModel;
    private ArticlesAdapter mAdapter;

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        ViewModelFactory viewModelFactory = DaggerViewModelFactoryComponent.create().getViewModelFactory();
        mViewModel = ViewModelProviders.of(getActivity(), viewModelFactory).get(ArticlesViewModel.class);

        bindViews();

        setHasOptionsMenu(true);
    }

    public void bindViews() {
        mAdapter = new ArticlesAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void setUpObservers() {
        mViewModel.getArticles().observe(getViewLifecycleOwner(), new Observer<PagedList<Article>>() {
            @Override
            public void onChanged(@Nullable PagedList<Article> pagedListResource) {
                if (pagedListResource.size() == 0)
                    return;
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.submitList(pagedListResource);
            }
        });

        mViewModel.getNetworkState().observe(getViewLifecycleOwner(), new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                mAdapter.setNetworkState(networkState);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_article_list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_filter:
                launchFilterActivity();
                return true;
        }
        return false;
    }

    private void launchFilterActivity() {
        startActivity(new Intent(getContext(), ProvidersFilterActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        //listen for any changes to the shared preference
        PreferenceUtils.getProviderSharedPreference(getContext())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        //remove the listener
        PreferenceUtils.getProviderSharedPreference(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onRefresh() {
        mViewModel.reload();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        mViewModel.reload();
    }
}
