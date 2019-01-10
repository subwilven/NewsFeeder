package com.islam.newsfeeder.ui.saved_article;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.data.pocket.PocketRepository;

public class SavedArticleViewModel extends ViewModel {


    private final PocketRepository mRepository;
    private LiveData<String> onRequestTokenReceived;

    public SavedArticleViewModel(PocketRepository mArticleRepository) {
        mRepository = mArticleRepository;
    }

    public void loginAtPocket() {
        onRequestTokenReceived = mRepository.login();
    }

    public LiveData<String> getOnRequestTokenReceived() {
        return onRequestTokenReceived;
    }
}
