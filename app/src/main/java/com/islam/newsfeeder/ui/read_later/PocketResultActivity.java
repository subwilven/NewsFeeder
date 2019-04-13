package com.islam.newsfeeder.ui.read_later;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.dagger.repository.DaggerRepositoryComponent;
import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.ui.MainActivity;

import static com.islam.newsfeeder.util.Constants.BUNDLE_OPEN_REAL_LATER_FRAGMENT;

public class PocketResultActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getAction() == Intent.ACTION_VIEW) {
            MyApplication.getInstance().getRepositoryComponent()
                    .getPocketRepository().getAccessToken();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BUNDLE_OPEN_REAL_LATER_FRAGMENT, true);
        startActivity(intent);
        finish();

    }
}