package com.islam.newsfeeder.ui.providers_filter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.data.articles.ArticleRepository;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.Constants;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

import java.util.List;

public class ProvidersFilterViewModel extends ViewModel {

    private final ArticleRepository mRepository;

    private MutableLiveData<List<Provider>> userProvidersList = new MutableLiveData<>();
    // fired whhen user click on save button
    private SingleLiveEvent<Boolean> onSaveClicked = new SingleLiveEvent<>();
    //fired when user try to save and there is no any checked provider
    private SingleLiveEvent<Boolean> showToastAtLeastOnProvider = new SingleLiveEvent<>();

    //fired when user try to do action while no internet connection
    private SingleLiveEvent<Boolean> showToastNoConnection = new SingleLiveEvent<>();

    //fired when user try to add new providers to show loading dialog until the providers list fetched
    private SingleLiveEvent<Boolean> showLoadingDialog = new SingleLiveEvent<>();

    //fired when user try to add new providers
    private SingleLiveEvent<Boolean> showProvidersListDialog = new SingleLiveEvent<>();

    //fired when user choose a new provider from the list
    private SingleLiveEvent<Provider> onProviderAdded = new SingleLiveEvent<>();

    //hold all the providers from the server to allow user to add to his providers list
    private List<Provider> allProvidersList;
    // to determine to remove the last one or not when try to add new provider (the last one should be that on which allow user to add more)
    private boolean isTheLastChipAdded;

    public ProvidersFilterViewModel(ArticleRepository mRepository) {
        this.mRepository = mRepository;
    }

    public void init(List<Provider> providers) {
        if (userProvidersList.getValue() == null)
            userProvidersList.setValue(providers);
    }

    public void setOnSaveClicked(boolean b) {
        boolean isThereCheckedProviders = checkIfThereIsAtLeastOneCheckedProvider();
        if (isThereCheckedProviders)
            onSaveClicked.setValue(b);
        else
            showToastAtLeastOnProvider.setValue(true);
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
            //show loading dialog
            showLoadingDialog.setValue(true);
            mRepository.fetchAllProviders(new CallBacks.NetworkCallBack<List<Provider>>() {
                @Override
                public void onSuccess(List<Provider> items) {
                    //hide loading dialog and show providers list dialog
                    showLoadingDialog.setValue(false);
                    allProvidersList = items;
                    showProvidersListDialog.setValue(true);

                }

                @Override
                public void onFailed(String error) {
                    showLoadingDialog.setValue(false);
                    if (error.equals(Constants.ERROR_NO_CONNECTION))
                        showToastNoConnection.setValue(true);
                }
            });
        } else {
            showProvidersListDialog.setValue(true);
        }
    }


    public void addNewProvider(int position) {

        Provider newProvider = allProvidersList.get(position);
        if (userProvidersList.getValue().indexOf(newProvider) == -1) {
            newProvider.setChecked(true);
            userProvidersList.getValue().add(newProvider);
            onProviderAdded.setValue(newProvider);
        }
    }


    //-------------------------------------------
    // setters and getters

    public LiveData<Boolean> getShowLoadingDialog() {
        return showLoadingDialog;
    }

    public LiveData<Boolean> getShowProvidersListDialog() {
        return showProvidersListDialog;
    }

    public List<Provider> getAllProvidersList() {
        return allProvidersList;
    }

    public LiveData<List<Provider>> getProvidersList() {
        return userProvidersList;
    }

    public void updateProvidersStatus(int index, boolean b) {
        userProvidersList.getValue().get(index).setChecked(b);
    }

    public LiveData<Boolean> getShowToastAtLeastOnProvider() {
        return showToastAtLeastOnProvider;
    }

    public LiveData<Boolean> getOnSaveClicked() {
        return onSaveClicked;
    }

    public LiveData<Provider> getOnProviderAdded() {
        return onProviderAdded;
    }

    public boolean isTheLastChipIsAdded() {
        return isTheLastChipAdded;
    }

    public void setTheLastChipIsAdded(boolean theLastChipIsAdded) {
        this.isTheLastChipAdded = theLastChipIsAdded;
    }

    public LiveData<Boolean> getShowToastNoConnection() {
        return showToastNoConnection;
    }
}
