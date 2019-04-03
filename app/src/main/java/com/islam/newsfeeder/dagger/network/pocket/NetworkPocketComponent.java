package com.islam.newsfeeder.dagger.network.pocket;

import com.islam.newsfeeder.dagger.network.BaseNetworkModel;
import com.islam.newsfeeder.data.pocket.PocketApi;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {BaseNetworkModel.class,NetworkPocketModel.class})
public interface NetworkPocketComponent {
    PocketApi getPocketApi();
}