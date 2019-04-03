package com.islam.newsfeeder.ui.article_details;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

import javax.inject.Inject;

public class ArticlesDetailsViewModel extends ViewModel {


    private final PocketRepository mRepository;

    //fired when user try to do action while no internet connection
    private SingleLiveEvent<Boolean> showToastNoConnection = new SingleLiveEvent<>();

    public ArticlesDetailsViewModel(PocketRepository mRepository) {
        this.mRepository = mRepository;
    }

    private final MutableLiveData<Article> articleData = new MutableLiveData<>();
    private final SingleLiveEvent<Boolean> shouldOpenCustomTab = new SingleLiveEvent<>();

    public void init(Article article) {
        articleData.setValue(article);
    }


    public void addArticleToReadLater() {
        mRepository.addArticleToReadLater(articleData.getValue(), new CallBacks.NetworkCallBack() {
            @Override
            public void onSuccess(Object item) {

            }

            @Override
            public void onFailed(String error) {
                if (error.equals(Constants.ERROR_NO_CONNECTION))
                    showToastNoConnection.setValue(true);
            }
        });
    }


    //-------------------------------------------
    // setters and getters

    public LiveData<Article> getArticleData() {
        return articleData;
    }

    public LiveData<Boolean> getShouldOpenCustomTab() {
        return shouldOpenCustomTab;
    }

    public void setShouldOpenCustomTab(boolean b) {
        shouldOpenCustomTab.setValue(true);
    }

    public LiveData<Boolean> getShowToastNoConnection() {
        return showToastNoConnection;
    }
}
