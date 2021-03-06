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

public class ProvidersFilterActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener{

    private ChipGroup chipGroup;
    private ProvidersFilterViewModel mViewModel;
    private MaterialDialog loadingDialog;
    private MaterialDialog providersListDialog;

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

        setUpProviders();
    }

    public void setUpProviders() {
        mViewModel.getProvidersList().observe(this, new Observer<List<Provider>>() {
            @Override
            public void onChanged(@Nullable List<Provider> providers) {
                initChipsProviders(providers);
            }
        });

        mViewModel.getOnSaveClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                //save the new list of providers
                PreferenceUtils.saveProvidersInShared(ProvidersFilterActivity.this,
                        mViewModel.getProvidersList().getValue());
                //finish the filter activity
                finish();
            }
        });

        mViewModel.getShowToastAtLeastOnProvider().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                ActivityUtils.showToast(ProvidersFilterActivity.this,R.string.should_be_at_least_one_checked_provider);
            }
        });

        mViewModel.getShowLoadingDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean)
                    showLoadingDialog();
                else
                    loadingDialog.dismiss();
            }
        });

        mViewModel.getShowProvidersListDialog().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                showProvidersListDialog();
            }
        });

        mViewModel.getOnProviderAdded().observe(this, new Observer<Provider>() {
            @Override
            public void onChanged(@Nullable Provider provider) {
                addNewChip(provider, mViewModel.getProvidersList().getValue().size() - 1);
            }
        });

        mViewModel.getShowToastNoConnection().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                ActivityUtils.showToast(ProvidersFilterActivity.this,R.string.no_network_connection);
            }
        });
    }


    private void showLoadingDialog() {
        if (loadingDialog == null)
            loadingDialog =
                    DialogUtils.createLoadingDialog(this, R.string.loading, R.string.please_wait);
        loadingDialog.show();
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
