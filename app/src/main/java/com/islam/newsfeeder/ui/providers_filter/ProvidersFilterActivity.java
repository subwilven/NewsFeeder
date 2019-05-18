package com.islam.newsfeeder.ui.providers_filter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.databinding.ActivityFilterBinding;
import com.islam.newsfeeder.pojo.Provider;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.DialogUtils;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProvidersFilterActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener {

    private ChipGroup chipGroup;
    private ProvidersFilterViewModel mViewModel;
    private MaterialDialog loadingDialog;
    private MaterialDialog providersListDialog;


    private CompositeDisposable mSubscription = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFilterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_filter);
        binding.setLifecycleOwner(this);

        ViewModelFactory viewModelFactory = MyApplication.getInstance().getRepositoryComponent().getViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProvidersFilterViewModel.class);
        mViewModel.init(PreferenceUtils.getProvidersFromShared(this));

        getSupportActionBar().setElevation(0f);
        getSupportActionBar().setTitle(R.string.sources);
        chipGroup = findViewById(R.id.chipGroup);

        binding.setViewModel(mViewModel);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpObsevers();
    }

    @Override
    protected void onPause() {
        mSubscription.clear();
        super.onPause();
    }

    public void setUpObsevers() {

        mSubscription.add(mViewModel.getProvidersList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::initChipsProviders));


        mSubscription.add(mViewModel.getOnSaveClicked()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> {
                    PreferenceUtils.saveProvidersInShared(ProvidersFilterActivity.this,
                            mViewModel.getProvidersList().getValue());
                    finish();
                }));


        mSubscription.add(mViewModel.getShowLoadingDialog()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::showLoadingDialog));


        mSubscription.add(mViewModel.getShowProvidersListDialog()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(__ -> showProvidersListDialog()));


        mSubscription.add(mViewModel.getOnProviderAdded()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(provider -> addNewChip(provider,
                        mViewModel.getProvidersListSize() - 1)));


        mSubscription.add(mViewModel.getShowToast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stringRes ->
                        ActivityUtils.showToast(ProvidersFilterActivity.this, getString(stringRes))));
    }


    private void showLoadingDialog(boolean aBoolean) {
        if (loadingDialog == null)
            loadingDialog =
                    DialogUtils.createLoadingDialog(this, R.string.loading, R.string.please_wait);
        if (aBoolean) {
            loadingDialog.show();
        } else {
            loadingDialog.dismiss();
        }
    }

    private void showProvidersListDialog() {
        if (providersListDialog == null)
            providersListDialog = DialogUtils.createListDialog(this, R.string.add_new_source, mViewModel.getAllProvidersList(), new MaterialDialog.ListCallback() {
                @Override
                public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                    mViewModel.addNewProvider(position);
                }
            });
        providersListDialog.show();
    }

    public void initChipsProviders(List<Provider> providers) {
        for (int i = 0; i < providers.size(); i++) {
            addNewChip(providers.get(i), i);
        }

    }

    public Chip createChipView() {
        Chip chip = new Chip(ProvidersFilterActivity.this);
        chip.setCheckedIconVisible(false);
        chip.setMinHeight(80);
        chip.setChipStrokeWidth(3f);
        chip.setTextSize(16f);
        chip.setChipCornerRadius(50);
        chip.setChipStrokeColorResource(R.color.colorPrimary);
        chip.setChipBackgroundColor(getResources().getColorStateList(R.color.chip_background, getTheme()));
        chip.setTextColor(getResources().getColorStateList(R.color.chip_text, getTheme()));

        chip.setCheckable(true);
        return chip;
    }

    private void addNewChip(Provider provider, int index) {
        Chip newChip = createChipView();
        newChip.setText(provider.getName());
        //set the index of provider in the tag to retrive it on click
        newChip.setTag(index);
        newChip.setChecked(provider.isChecked());
        newChip.setOnCheckedChangeListener(ProvidersFilterActivity.this);
        chipGroup.addView(newChip);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                mViewModel.setOnSaveClicked(true);
                return true;
        }
        return false;
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int index = (int) compoundButton.getTag();
        mViewModel.updateProvidersStatus(index, b);
    }

}
