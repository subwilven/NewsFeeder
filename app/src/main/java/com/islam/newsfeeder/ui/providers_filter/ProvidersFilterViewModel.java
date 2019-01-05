package com.islam.newsfeeder.ui.providers_filter;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.util.other.SingleLiveEvent;

import java.util.List;

public class ProvidersFilterViewModel extends ViewModel {
    private MutableLiveData<List<Provider>> providersList = new MutableLiveData<>();
    // fired whhen user click on save button
    private SingleLiveEvent<Boolean> onSaveClicked = new SingleLiveEvent<>();
    //fired when user try to save and there is no any checked provider
    private SingleLiveEvent<Boolean> showToastAtLeastOnProvider = new SingleLiveEvent<>();

    public ProvidersFilterViewModel() {
    }

    public void init(List<Provider> providers) {
        if (providersList.getValue() == null)
            providersList.setValue(providers);
    }

    public LiveData<List<Provider>> getProvidersList() {
        return providersList;
    }

    public void updateProvidersStatus(int index, boolean b) {
        providersList.getValue().get(index).setChecked(b);
    }

    public LiveData<Boolean> getShowToastAtLeastOnProvider() {
        return showToastAtLeastOnProvider;
    }

    public LiveData<Boolean> getOnSaveClicked() {
        return onSaveClicked;
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
        List<Provider> providers = providersList.getValue();
        for (int i = 0; i < providers.size(); i++) {
            if (providers.get(i).isChecked()) {
                exist = true;
                break;
            }
        }
        return exist;
    }
}
