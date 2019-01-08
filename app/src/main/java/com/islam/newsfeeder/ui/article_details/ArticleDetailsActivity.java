package com.islam.newsfeeder.ui.article_details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.Constants;

public class ArticleDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);


        if (savedInstanceState == null) {
            Article article = (Article) getIntent().getSerializableExtra(Constants.BUNDLE_ARTICLE);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, ArticleDetailsFragment.getInstance(article))
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }
}