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
import android.widget.CompoundButton;
import android.widget.Toast;

import com.islam.newsfeeder.POJO.Provider;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import java.util.List;

public class ProvidersFilterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private ChipGroup chipGroup;
    private ProvidersFilterViewModel mViewModel;

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
    }

    public void initChipsProviders(List<Provider> providers) {
        for (int i = 0; i < providers.size(); i++) {
            Chip chip = new Chip(ProvidersFilterActivity.this);

            chip.setText(providers.get(i).getName());
            chip.setCheckable(true);
            //set the index of provider in the tag to retrive it on click
            chip.setTag(i);
            chip.setChecked(providers.get(i).isChecked());
            chip.setOnCheckedChangeListener(ProvidersFilterActivity.this);
            chipGroup.addView(chip);
        }

        Chip chip = new Chip(ProvidersFilterActivity.this);
        chip.setChipIcon(getDrawable(R.drawable.ic_add_black_24dp));

        chip.setText(getString(R.string.add_source));
        chipGroup.addView(chip);
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
