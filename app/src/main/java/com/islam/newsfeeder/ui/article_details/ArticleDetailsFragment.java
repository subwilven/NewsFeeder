package com.islam.newsfeeder.ui.article_details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.ui.home.HomeFragment;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.other.ViewModelFactory;

import static com.islam.newsfeeder.util.Constants.BUNDLE_ARTICLE;

public class ArticleDetailsFragment extends Fragment implements View.OnKeyListener, View.OnClickListener {

    ArticlesDetailsViewModel mViewModel;
    private TextView contentTextView;
    private TextView titleTextView;
    private TextView authorTextView;
    private TextView providerNameTextView;
    private TextView publishedAtTextView;
    private ImageView imageView;

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
        mViewModel = ViewModelProviders.of(this, ViewModelFactory.getInstance()).get(ArticlesDetailsViewModel.class);
        mViewModel.init((Article) getArguments().getSerializable(BUNDLE_ARTICLE));

        contentTextView = view.findViewById(R.id.article_details_content);
        titleTextView = view.findViewById(R.id.article_details_title);
        authorTextView = view.findViewById(R.id.article_details_author);
        providerNameTextView = view.findViewById(R.id.article_details_provider_name);
        publishedAtTextView = view.findViewById(R.id.article_details_published_at);
        imageView = view.findViewById(R.id.article_details_image);
        imageView.setOnClickListener(this);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(this);

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
                providerNameTextView.setText(article.getProvider().getName());
                publishedAtTextView.setText(article.getPublishedAt());
                ActivityUtils.loadImage(imageView,
                        article.getImageUrl(),
                        null);
            }
        });

        mViewModel.getShouldOpenCustomTab().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean b) {
                if (b)
                    openCustomTab();
            }
        });
    }

    private void openCustomTab() {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

        builder.setToolbarColor(Color.BLUE);
        builder.setStartAnimations(getContext(), R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(getContext(), R.anim.slide_in_left, R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(getContext(), Uri.parse(mViewModel.getArticleData().getValue().getArticleUrl()));
    }


    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {

            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
            getActivity().getSupportFragmentManager()
                    .beginTransaction().show(fragment).commit();
            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        mViewModel.setShouldOpenCustomTab(true);
    }
}
