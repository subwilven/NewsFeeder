package com.islam.newsfeeder.ui.article_details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.dagger.view_model.DaggerViewModelFactoryComponent;
import com.islam.newsfeeder.ui.MainActivity;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.PreferenceUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.islam.newsfeeder.util.Constants.BUNDLE_ARTICLE;
import static com.islam.newsfeeder.util.Constants.BUNDLE_OPEN_REAL_LATER_FRAGMENT;
import static com.islam.newsfeeder.util.Constants.KEY_ACCESS_TOKEN;

public class ArticleDetailsFragment extends Fragment implements View.OnClickListener {

    ArticlesDetailsViewModel mViewModel;
    private TextView contentTextView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView publishedAtTextView;
    private ImageView imageView;
    private int mMutedColor = 0xFF333333;

    private final Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            if (bitmap != null) {
                Palette palettep = Palette.generate(bitmap, 12);
                mMutedColor = palettep.getDarkMutedColor(0xFF333333);
                imageView.setImageBitmap(bitmap);
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(mMutedColor);
                //   updateStatusBar();
            }
        }

        @Override
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.placeholder));
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public ArticleDetailsFragment() {
    }

    public static ArticleDetailsFragment getInstance(Article article) {

        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_ARTICLE, article);

        ArticleDetailsFragment fragment = new ArticleDetailsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_details, container, false);
        ViewModelFactory viewModelFactory = DaggerViewModelFactoryComponent.create().getViewModelFactory();
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(ArticlesDetailsViewModel.class);
        mViewModel.init((Article) getArguments().getSerializable(BUNDLE_ARTICLE));

        ArticleDetailsActivity activity = ((ArticleDetailsActivity) getActivity());
        activity.setSupportActionBar(view.findViewById(R.id.toolbar));
        if (activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        contentTextView = view.findViewById(R.id.article_details_content);
        titleTextView = view.findViewById(R.id.article_details_title);
        authorTextView = view.findViewById(R.id.article_details_author);
        publishedAtTextView = view.findViewById(R.id.article_details_published_at);
        imageView = view.findViewById(R.id.article_details_image);

        //set on click listeners
        view.findViewById(R.id.fab_read_later).setOnClickListener(this);
        view.findViewById(R.id.article_details_btn_read_more).setOnClickListener(this);

        setUpObservers();
        return view;
    }

    private void setUpObservers() {
        mViewModel.getArticleData().observe(getViewLifecycleOwner(), new Observer<Article>() {
            @Override
            public void onChanged(@Nullable Article article) {
                contentTextView.setText(article.getContent());
                titleTextView.setText(article.getTitle());
                authorTextView.setText(article.getAuthor());
                publishedAtTextView.setText(ActivityUtils.calculateTimeDiff(article.getPublishedAt()));
                ActivityUtils.loadImage(target,
                        article.getImageUrl(),
                        null);
            }
        });

        mViewModel.getShouldOpenCustomTab().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if (b)
                    ActivityUtils.openCustomTab(getContext(),
                            mMutedColor,
                            mViewModel.getArticleData().getValue().getArticleUrl());
            }
        });

        mViewModel.getShowToastNoConnection().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                Toast.makeText(getContext(), getString(R.string.no_network_connection), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.article_details_btn_read_more:
                mViewModel.setShouldOpenCustomTab(true);
                break;
            case R.id.fab_read_later:
                if (PreferenceUtils.getPocketData(getContext(), KEY_ACCESS_TOKEN) != null) {
                    mViewModel.addArticleToReadLater();
                    Toast.makeText(getContext(), getString(R.string.added_to_real_later), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), getString(R.string.you_must_sign_in_frist), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra(BUNDLE_OPEN_REAL_LATER_FRAGMENT, true);
                    startActivity(intent);
                }
                break;
        }

    }


}
