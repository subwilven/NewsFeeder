package com.islam.newsfeeder;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.islam.newsfeeder.dagger.network.article.DaggerNetworkArticleComponent;
import com.islam.newsfeeder.dagger.network.article.NetworkArticleComponent;
import com.islam.newsfeeder.dagger.network.pocket.DaggerNetworkPocketComponent;
import com.islam.newsfeeder.dagger.network.pocket.NetworkPocketComponent;
import com.islam.newsfeeder.dagger.repository.DaggerDatabaseDaoComponent;
import com.islam.newsfeeder.dagger.repository.DaggerRepositoryComponent;
import com.islam.newsfeeder.dagger.repository.DatabaseDaoComponent;
import com.islam.newsfeeder.dagger.repository.RepositoryComponent;


public class MyApplication extends Application {

    private static MyApplication mInstance;
    NetworkArticleComponent networkArticleComponent;
    NetworkPocketComponent networkPocketComponent;
    RepositoryComponent repositoryComponent;
    DatabaseDaoComponent databaseDaoComponent;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public NetworkArticleComponent getNetworkArticleComponent() {
        if ((networkArticleComponent == null))
            networkArticleComponent = DaggerNetworkArticleComponent.create();
        return networkArticleComponent;
    }

    public NetworkPocketComponent getNetworkPocketComponent() {
        if ((networkPocketComponent == null))
            networkPocketComponent = DaggerNetworkPocketComponent.create();
        return networkPocketComponent;
    }

    public RepositoryComponent getRepositoryComponent() {
        if ((repositoryComponent == null))
            repositoryComponent = DaggerRepositoryComponent.create();
        return repositoryComponent;
    }

    public DatabaseDaoComponent getDatabaseDaoComponent() {
        if ((databaseDaoComponent == null))
            databaseDaoComponent = DaggerDatabaseDaoComponent.create();
        return databaseDaoComponent;
    }

//    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
//        ConnectivityReceiver.listener = listener;
//    }

}