package com.islam.newsfeeder.dagger.repository;

import com.islam.newsfeeder.data.articles.ArticleDao;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component( modules ={DatabaseDaoModel.class})
public interface DatabaseDaoComponent {
    ArticleDao provideArticleDao();
}
