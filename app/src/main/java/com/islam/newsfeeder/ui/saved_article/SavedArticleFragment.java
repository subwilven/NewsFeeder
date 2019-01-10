package com.islam.newsfeeder.ui.saved_article;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.base.BaseFragmentList;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import static com.islam.newsfeeder.util.Constants.redirectUri;

public class SavedArticleFragment extends BaseFragmentList implements View.OnClickListener {

    SavedArticleViewModel mViewModel;
    Button gotoPocketButton;

    @Override
    public void onCreateView(View view, Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(SavedArticleViewModel.class);
        gotoPocketButton = view.findViewById(R.id.go_to_pocket);
        gotoPocketButton.setOnClickListener(this);
    }

    @Override
    protected void setUpObservers() {


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
        return R.layout.fragment_saved_articles;
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
}
