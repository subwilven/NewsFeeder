package com.islam.newsfeeder.ui.saved_article;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.islam.newsfeeder.data.pocket.PocketRepository;

public class PocketResultResult extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        String action = getIntent().getAction();
        if (action == Intent.ACTION_VIEW) {
            PocketRepository.getInstance().getAccessToken();
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}