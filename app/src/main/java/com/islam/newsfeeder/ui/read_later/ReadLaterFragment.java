package com.islam.newsfeeder.ui.read_later;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.view.View;

import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.POJO.network.ReadLaterArticle;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.base.BaseFragmentList;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;

import static com.islam.newsfeeder.util.Constants.KEY_ACCESS_TOKEN;
import static com.islam.newsfeeder.util.Constants.redirectUri;

public class ReadLaterFragment extends BaseFragmentList implements View.OnClickListener,
        CallBacks.AdapterCallBack<ReadLaterArticle>, SwipeRefreshLayout.OnRefreshListener {

    private ReadLaterViewModel mViewModel;
    private View signInPocketLayout;
    private ReadLaterAdapter mAdapter;

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(ReadLaterViewModel.class);
        String accessToken = PreferenceUtils.getPocketData(getContext(), KEY_ACCESS_TOKEN);
        mViewModel.init(accessToken);
        signInPocketLayout = view.findViewById(R.id.pocket_sign_in);
        signInPocketLayout.findViewById(R.id.go_to_pocket).setOnClickListener(this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mAdapter = new ReadLaterAdapter(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void setUpObservers() {

        mViewModel.getOnAccessTokenReceived().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                observeTheArticles();
                mViewModel.setShouldShowPocketSignInLayout(false);
            }
        });

        mViewModel.getShouldShowPocketSignInLayout().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean)
                    signInPocketLayout.setVisibility(View.VISIBLE);
                else
                    signInPocketLayout.setVisibility(View.GONE);
            }
        });
    }

    private void observeTheArticles() {
        mViewModel.loadArticles();
        mViewModel.getArticlesList().observe(getViewLifecycleOwner(), new Observer<Resource<List<ReadLaterArticle>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<ReadLaterArticle>> readLaterArticles) {
                updateScreenStatus(getScreenStatus(readLaterArticles));
                if (readLaterArticles.getData() != null && readLaterArticles.getData().size() > 0)
                    mAdapter.setData(readLaterArticles.getData());
            }
        });
    }


    private void launchBrowser(String code) {
        String stringUri = "https://getpocket.com/auth/authorize?request_token="
                + code + "&redirect_uri=" + redirectUri;
        Uri uri = Uri.parse(stringUri);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(browserIntent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_read_later;
    }


    @Override
    public void onClick(View view) {
        mViewModel.loginAtPocket();
        observeReqestToken();
    }

    private void observeReqestToken() {
        mViewModel.getOnRequestTokenReceived().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String code) {
                launchBrowser(code);
            }
        });
    }

    @Override
    public void onItemClicked(ReadLaterArticle item) {
        ActivityUtils.openCustomTab(getContext(),
                0xFF333333,
                item.getArticleUrl());
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
