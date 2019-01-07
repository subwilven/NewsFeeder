package com.islam.newsfeeder.ui.providers_filter;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.DialogUtils;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;

public class ProvidersFilterActivity extends AppCompatActivity implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private ChipGroup chipGroup;
    private ProvidersFilterViewModel mViewModel;
    private MaterialDialog loadingDialog;
    private MaterialDialog providersListDialog;
    //the last chip in the group
    private Chip addProviderChip;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(ProvidersFilterViewModel.class);
        mViewModel.init(PreferenceUtils.getProvidersFromShared(this));

        getSupportActionBar().setTitle(R.string.sources);
        chipGroup = findViewById(R.id.chipGroup);

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
                Toast.makeText(ProvidersFilterActivity.this, getString(R.string.should_be_at_least_one_checked_provider), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProvidersFilterActivity.this, getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
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

        //set the last chip to allow user  to add more providers
        addProviderChip = new Chip(ProvidersFilterActivity.this);
        addProviderChip.setChipIcon(getDrawable(R.drawable.ic_add_black_24dp));
        addProviderChip.setOnClickListener(this);
        addProviderChip.setText(getString(R.string.add_source));
        chipGroup.addView(addProviderChip);
        mViewModel.setTheLastChipIsAdded(true);
    }

    private void addNewChip(Provider provider, int index) {
        Chip newChip = new Chip(ProvidersFilterActivity.this);

        newChip.setText(provider.getName());
        newChip.setCheckable(true);
        //set the index of provider in the tag to retrive it on click
        newChip.setTag(index);
        newChip.setChecked(provider.isChecked());
        newChip.setOnCheckedChangeListener(ProvidersFilterActivity.this);
        if (mViewModel.isTheLastChipIsAdded()) {
            chipGroup.removeView(addProviderChip);
            chipGroup.addView(newChip);
            chipGroup.addView(addProviderChip);
        } else {
            chipGroup.addView(newChip);
        }

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

    @Override
    public void onClick(View view) {
        mViewModel.showProvidersList();
    }
}
