package com.islam.newsfeeder.services;

import android.content.Context;
import android.support.annotation.NonNull;

import com.islam.newsfeeder.pojo.Article;
import com.islam.newsfeeder.dagger.repository.DaggerDatabaseDaoComponent;
import com.islam.newsfeeder.data.articles.ArticleService;
import com.islam.newsfeeder.util.CallBacks;

import java.util.List;
import java.util.concurrent.Executors;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UpdateDatabaseWorker extends Worker {

    public UpdateDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ArticleService articleService = ArticleService.getInstance();
        articleService.fetchArticles(1, new CallBacks.NetworkCallBack<List<Article>>() {
            @Override
            public void onSuccess(List<Article> response) {
                Executors.newFixedThreadPool(1).execute(new Runnable() {
                    @Override
                    public void run() {
                        DaggerDatabaseDaoComponent.create().provideArticleDao().clearAllData();
                        DaggerDatabaseDaoComponent.create().provideArticleDao().insert(response);
                    }
                });
            }

            @Override
            public void onFailed(String error) {

            }
        });
        return Result.success();
    }
}
