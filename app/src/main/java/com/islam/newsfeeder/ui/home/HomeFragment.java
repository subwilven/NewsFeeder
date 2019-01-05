package com.islam.newsfeeder.ui.home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.base.BaseFragmentList;
import com.islam.newsfeeder.ui.article_details.ArticleDetailsFragment;
import com.islam.newsfeeder.ui.providers_filter.ProvidersFilterActivity;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragmentList implements SwipeRefreshLayout.OnRefreshListener,
        CallBacks.AdapterCallBack<Article>, SharedPreferences.OnSharedPreferenceChangeListener {

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

        setHasOptionsMenu(true);
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
    public void onDestroy() {
        //remove the listener
        PreferenceUtils.getProviderSharedPreference(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        mViewModel.reload();
    }
}
