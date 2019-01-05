package com.islam.newsfeeder.util.other;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.islam.newsfeeder.data.NewsFeederDatabase;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.ui.article_details.ArticlesDetailsViewModel;
import com.islam.newsfeeder.ui.home.HomeViewModel;
import com.islam.newsfeeder.ui.saved_article.SavedArticleViewModel;

/**
 * Created by eslam on 13-May-18.
 */

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static volatile ViewModelFactory INSTANCE;
    private final ArticleRepository mArticleRepository;

    public ViewModelFactory(ArticleRepository articleRepository) {
        this.mArticleRepository = articleRepository;
    }

    public static ViewModelFactory getInstance() {

        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ViewModelFactory(
                            ArticleRepository.getInstance(NewsFeederDatabase.getInstance().articleDao()));
                }
            }
        }
        return INSTANCE;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SavedArticleViewModel.class)) {
            return (T) new SavedArticleViewModel(mArticleRepository);
        } else if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            //noinspection unchecked
            return (T) new HomeViewModel(mArticleRepository);
        } else if (modelClass.isAssignableFrom(ArticlesDetailsViewModel.class)) {
            //noinspection unchecked
            return (T) new ArticlesDetailsViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}