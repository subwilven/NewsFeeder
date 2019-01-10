package com.islam.newsfeeder.util.other;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.islam.newsfeeder.data.NewsFeederDatabase;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.ui.article_details.ArticlesDetailsViewModel;
import com.islam.newsfeeder.ui.home.HomeViewModel;
import com.islam.newsfeeder.ui.providers_filter.ProvidersFilterViewModel;
import com.islam.newsfeeder.ui.saved_article.SavedArticleViewModel;

/**
 * Created by eslam on 13-May-18.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;
    private final ArticleRepository mArticleRepository;
    private final PocketRepository mPocketRepository;

    public ViewModelFactory(ArticleRepository articleRepository, PocketRepository pocketRepository) {
        this.mArticleRepository = articleRepository;
        mPocketRepository = pocketRepository;
    }

    public static ViewModelFactory getInstance() {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            ArticleRepository.getInstance(NewsFeederDatabase.getInstance().articleDao())
                            , PocketRepository.getInstance());
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SavedArticleViewModel.class)) {
            //noinspection unchecked
            return (T) new SavedArticleViewModel(mPocketRepository);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            //noinspection unchecked
            return (T) new HomeViewModel(mArticleRepository);
        } else if (modelClass.isAssignableFrom(ArticlesDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new ArticlesDetailsViewModel(mPocketRepository);
        } else if (modelClass.isAssignableFrom(ProvidersFilterViewModel.class)) {
            //noinspection unchecked
            return (T) new ProvidersFilterViewModel(mArticleRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}