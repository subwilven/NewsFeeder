package com.islam.newsfeeder.data.articles;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.POJO.ApiResponse;
import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.data.server_connection_helper.NetworkBoundResource;
import com.islam.newsfeeder.util.Constants;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleRepository {

    private static ArticleRepository INSTANCE = null;
    private final ArticleDao mArticleDao;

    private ArticleRepository(ArticleDao articleDao) {
        this.mArticleDao = articleDao;
    }

    public static ArticleRepository getInstance(ArticleDao articleDao) {
        if (INSTANCE == null) {
            synchronized (ArticleRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ArticleRepository(articleDao);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<Resource<List<Article>>> getArticles() {
        return new NetworkBoundResource<List<Article>>(MyApplication.getInstance()) {
            @Override
            protected void saveToDatabase(@NonNull List<Article> articles) {
                mArticleDao.clearAllData();
                mArticleDao.insert(articles);
            }

            @NonNull
            @Override
            protected LiveData<List<Article>> loadFromDb() {
                return mArticleDao.getAllArticles();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<Article>>> serveRequest() {

                final MutableLiveData<ApiResponse<List<Article>>> data = new MutableLiveData<>();

                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
                httpClient.addInterceptor(logging);

                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.basicUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

                ArticleApi articleApi = retrofit.create(ArticleApi.class);
                Call<ApiResponse<List<Article>>> connection = articleApi.getArticles(BuildConfig.NEWS_API_KEY);

                connection.enqueue(new Callback<ApiResponse<List<Article>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Article>>> call, Response<ApiResponse<List<Article>>> response) {
                        data.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Article>>> call, Throwable t) {

                    }
                });
                return data;
            }
        }.getAsLiveData();
    }

}
