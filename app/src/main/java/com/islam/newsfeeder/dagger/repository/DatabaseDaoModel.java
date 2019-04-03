package com.islam.newsfeeder.dagger.repository;

import com.islam.newsfeeder.data.NewsFeederDatabase;
import com.islam.newsfeeder.data.articles.ArticleDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class DatabaseDaoModel {

    @Singleton
    @Provides
    static ArticleDao provideArticleDao() {
        return NewsFeederDatabase.getInstance().articleDao();
    }

}
