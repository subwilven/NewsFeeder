package com.islam.newsfeeder.pojo.network;

import android.support.annotation.Nullable;

import com.islam.newsfeeder.pojo.Article;

import java.util.List;

public class ArticleResponse extends ApiResponse<List<Article>> {

    private List<Article> articles;

    @Nullable
    @Override
    public List<Article> getData() {
        return articles;
    }

    @Override
    public void setData(@Nullable List<Article> data) {
        articles = data;

    }

}
