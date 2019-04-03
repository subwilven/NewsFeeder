package com.islam.newsfeeder.dagger.view_model;

import com.islam.newsfeeder.data.articles.ArticleDao;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.data.articles.ArticleService;
import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ViewModelFactoryModel {



    @Singleton
    @Provides
    static ViewModelFactory provideViewModelFactory(ArticleRepository articleRepository,
                                                    PocketRepository pocketRepository) {
        return new ViewModelFactory(articleRepository, pocketRepository);
    }
}
