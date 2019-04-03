package com.islam.newsfeeder.dagger.network.article;

import com.islam.newsfeeder.dagger.network.BaseNetworkModel;
import com.islam.newsfeeder.data.articles.ArticleApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BaseNetworkModel.class,NetworkArticleModel.class})
public interface NetworkArticleComponent {
    ArticleApi getArticleApi();
}
