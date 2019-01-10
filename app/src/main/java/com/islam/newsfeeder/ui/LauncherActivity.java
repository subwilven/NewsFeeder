package com.islam.newsfeeder.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.islam.newsfeeder.BuildConfig;
import com.islam.newsfeeder.POJO.network.PocketResponse;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.data.articles.PocketApi;
import com.islam.newsfeeder.util.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.islam.newsfeeder.util.Constants.redirectUri;

public class LauncherActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)) {
            // use the parameter your API exposes for the code (mostly it's "code")
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // get access token
                // we'll do that in a minute
            } else if (uri.getQueryParameter("error") != null) {
                // show an error message here
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POCKET_API)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                PocketApi articleApi = retrofit.create(PocketApi.class);
                Call<PocketResponse.TokenResponse> connection = articleApi.requestToken(
                        BuildConfig.KEY_POCKET_CONSUMER, redirectUri);

                connection.enqueue(new Callback<PocketResponse.TokenResponse>() {
                    @Override
                    public void onResponse(Call<PocketResponse.TokenResponse> call, Response<PocketResponse.TokenResponse> response) {

                    }

                    @Override
                    public void onFailure(Call<PocketResponse.TokenResponse> call, Throwable t) {

                    }

                });
            }

        });
    }


}