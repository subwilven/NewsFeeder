package com.islam.newsfeeder.dagger.repository;

import com.islam.newsfeeder.data.articles.ArticleDao;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RepositoryModel.class,DatabaseDaoModel.class})
public interface RepositoryComponent {
    PocketRepository getPocketRepository();

    ArticleRepository getArticleRepository();

    ViewModelFactory getViewModelFactory();
}
