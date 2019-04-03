package com.islam.newsfeeder.dagger.repository;

import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.data.pocket.PocketRepository;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModel.class,DatabaseDaoModel.class})
public interface RepositoryComponent {
    PocketRepository getPocketRepository();

    ArticleRepository getArticleRepository();
}
