package com.islam.newsfeeder.dagger.view_model;

import com.islam.newsfeeder.dagger.repository.DatabaseDaoModel;
import com.islam.newsfeeder.dagger.repository.RepositoryModel;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ViewModelFactoryModel.class, DatabaseDaoModel.class, RepositoryModel.class})
public interface ViewModelFactoryComponent {
    ViewModelFactory getViewModelFactory();
}
