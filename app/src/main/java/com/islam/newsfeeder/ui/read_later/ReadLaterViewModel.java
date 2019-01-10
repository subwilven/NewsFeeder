package com.islam.newsfeeder.ui.read_later;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.data.pocket.PocketRepository;

public class ReadLaterViewModel extends ViewModel {


    private final PocketRepository mRepository;
    private LiveData<String> onRequestTokenReceived;

    public ReadLaterViewModel(PocketRepository mArticleRepository) {
        mRepository = mArticleRepository;
    }

    public void loginAtPocket() {
        onRequestTokenReceived = mRepository.login();
    }

    public LiveData<String> getOnRequestTokenReceived() {
        return onRequestTokenReceived;
    }
}
