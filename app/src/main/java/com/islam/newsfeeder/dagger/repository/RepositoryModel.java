package com.islam.newsfeeder.dagger.repository;

import com.islam.newsfeeder.data.articles.ArticleDao;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.data.articles.ArticleService;
import com.islam.newsfeeder.data.pocket.PocketRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class RepositoryModel {

    @Singleton
    @Provides
    static ArticleRepository provideArticleRepository(ArticleDao articleDao) {
        return new ArticleRepository(ArticleService.getInstance(), articleDao);
    }

    @Singleton
    @Provides
    static PocketRepository providePocketRepository() {
        return new PocketRepository();
    }
}
