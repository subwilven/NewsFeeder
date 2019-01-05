package com.islam.newsfeeder.ui.home;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.POJO.Resource;
import com.islam.newsfeeder.data.articles.ArticleRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {

    private final ArticleRepository mRepository;
    //here is the articles
    private LiveData<Resource<List<Article>>> mArticles;
    // here is the articales grouped accroding to the providers
    private LiveData<Resource<Map<Provider, List<Article>>>> mProviderWithArticles;

    public HomeViewModel(ArticleRepository mArticleRepository) {
        mRepository = mArticleRepository;
        loadArticles();
    }

    private void loadArticles() {
        if (mArticles == null)
            mArticles = mRepository.getArticles();
        mProviderWithArticles = Transformations.map(mArticles, new Function<Resource<List<Article>>, Resource<Map<Provider, List<Article>>>>() {
            @Override
            public Resource<Map<Provider, List<Article>>> apply(Resource<List<Article>> input) {
                if (input.getData() != null)
                    return groupArticlesByProvider(input);
                else
                    return Resource.replace(input, null);

            }
        });
    }

    public LiveData<Resource<Map<Provider, List<Article>>>> getArticles() {


        return mProviderWithArticles;
    }

    public void reload() {
        mArticles = mRepository.getArticles();
    }

    private Resource<Map<Provider, List<Article>>> groupArticlesByProvider(Resource<List<Article>> input) {
        List<Article> allArticles = input.getData();
        Map<Provider, List<Article>> providerListMap = new HashMap<>();
        if (allArticles != null)
            for (int i = 0; i < allArticles.size(); i++) {
                Provider currentProvider = allArticles.get(i).getProvider();
                //check if this provider has an articles or this is the first time
                List<Article> providerArticles = providerListMap.get(currentProvider);
                if (providerArticles == null) { // no such provider entered before

                    //create list of articles to the new provider
                    List<Article> articleList = new ArrayList<>();
                    //and the current article
                    articleList.add(allArticles.get(i));

                    //set the new provider
                    providerListMap.put(currentProvider, articleList);
                } else {
                    //add the new article to the existed list
                    providerArticles.add(allArticles.get(i));
                }
            }

        return Resource.replace(input, providerListMap);
    }
}
