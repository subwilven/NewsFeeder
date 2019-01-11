package com.islam.newsfeeder.ui.read_later;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.islam.newsfeeder.data.pocket.PocketRepository;
import com.islam.newsfeeder.ui.MainActivity;

import static com.islam.newsfeeder.util.Constants.BUNDLE_OPEN_REAL_LATER_FRAGMENT;

public class PocketResultResult extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getAction() == Intent.ACTION_VIEW) {
            PocketRepository.getInstance().getAccessToken();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(BUNDLE_OPEN_REAL_LATER_FRAGMENT, true);
        startActivity(intent);
        finish();

    }
}