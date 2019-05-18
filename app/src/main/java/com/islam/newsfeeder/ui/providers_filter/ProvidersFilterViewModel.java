package com.islam.newsfeeder.ui.providers_filter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.pojo.Provider;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.pojo.network.ProvidersResponse;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

public class ProvidersFilterViewModel extends ViewModel {

    private final ArticleRepository mRepository;

    private BehaviorSubject<List<Provider>> userProvidersList = BehaviorSubject.create();
    // fired whhen user click on save button
    private PublishSubject<Boolean> onSaveClicked = PublishSubject.create();

    private PublishSubject<Integer> showToast = PublishSubject.create();

    //fired when user try to add new providers to show loading dialog until the providers list fetched
    private PublishSubject<Boolean> showLoadingDialog = PublishSubject.create();

    //fired when user try to add new providers
    private PublishSubject<Boolean> showProvidersListDialog = PublishSubject.create();

    //fired when user choose a new provider from the list
    private PublishSubject<Provider> onProviderAdded = PublishSubject.create();

    //hold all the providers from the server to allow user to add to his providers list
    private List<Provider> allProvidersList;

    public ProvidersFilterViewModel(ArticleRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void init(List<Provider> providers) {
        if (userProvidersList.getValue() == null)
            userProvidersList.onNext(providers);
    }

    public void setOnSaveClicked(boolean b) {
        boolean isThereCheckedProviders = checkIfThereIsAtLeastOneCheckedProvider();
        if (isThereCheckedProviders)
            onSaveClicked.onNext(b);
        else
            showToast.onNext(R.string.should_be_at_least_one_checked_provider);
    }

    private boolean checkIfThereIsAtLeastOneCheckedProvider() {
        boolean exist = false;
        List<Provider> providers = userProvidersList.getValue();
        for (int i = 0; i < providers.size(); i++) {
            if (providers.get(i).isChecked()) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void showProvidersList() {
        //to prevent loading the list every time
        if (allProvidersList == null) {
            fetchAllProviders();
        } else {
            showProvidersListDialog.onNext(true);
        }
    }

    public void fetchAllProviders(){
        mRepository.fetchAllProviders()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(__-> showLoadingDialog.onNext(true))
                .subscribe(new SingleObserver<ProvidersResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ProvidersResponse response) {
                        //hide loading dialog and show providers list dialog
                        showLoadingDialog.onNext(false);
                        allProvidersList = response.getData();
                        showProvidersListDialog.onNext(true);
                    }

                    @Override
                    public void onError(Throwable e) {
//                    showLoadingDialog.onNext(false);
//                    if (error.equals(Constants.ERROR_NO_CONNECTION))
//                        showToast.onNext(R.string.no_network_connection);
                    }
                });
    }


    public void addNewProvider(int position) {

        Provider newProvider = allProvidersList.get(position);
        if (userProvidersList.getValue().indexOf(newProvider) == -1) {
            newProvider.setChecked(true);
            userProvidersList.getValue().add(newProvider);
            onProviderAdded.onNext(newProvider);
        }
    }


    //-------------------------------------------
    // setters and getters

    public Observable<Boolean> getShowLoadingDialog() {
        return showLoadingDialog;
    }

    public Observable<Boolean> getShowProvidersListDialog() {
        return showProvidersListDialog;
    }

    public List<Provider> getAllProvidersList() {
        return allProvidersList;
    }

    public BehaviorSubject<List<Provider>> getProvidersList() {
        return userProvidersList;
    }

    public void updateProvidersStatus(int index, boolean b) {
        userProvidersList.getValue().get(index).setChecked(b);
    }

    public Observable<Boolean> getOnSaveClicked() {
        return onSaveClicked;
    }

    public Observable<Provider> getOnProviderAdded() {
        return onProviderAdded;
    }

    public Observable<Integer> getShowToast() {
        return showToast;
    }

    public int getProvidersListSize() {
        return userProvidersList.getValue().size();
    }
}
