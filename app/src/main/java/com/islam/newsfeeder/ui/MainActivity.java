package com.islam.newsfeeder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.services.UpdateDatabaseWorker;
import com.islam.newsfeeder.ui.articles_list.ArticlesListFragment;
import com.islam.newsfeeder.ui.read_later.ReadLaterFragment;
import com.islam.newsfeeder.util.PreferenceUtils;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import static com.islam.newsfeeder.util.Constants.BUNDLE_OPEN_REAL_LATER_FRAGMENT;

public class MainActivity extends AppCompatActivity {

    private static String FRAGMENT_CURRENT = "";
    BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(ArticlesListFragment.class, ArticlesListFragment.TAG);
                    return true;
                case R.id.navigation_read_later:
                    replaceFragment(ReadLaterFragment.class, null);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // if the user tried to save article to read later and he hasn't signed in yet bring him to the the second fragment
        if (intent.getBooleanExtra(BUNDLE_OPEN_REAL_LATER_FRAGMENT, false)) {
            navigation.setSelectedItemId(R.id.navigation_read_later);
            replaceFragment(ReadLaterFragment.class, null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setElevation(0f);

        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //if this the first launch
        if (savedInstanceState == null) {
            replaceFragment(ArticlesListFragment.class, ArticlesListFragment.TAG);
        }

        scheduleWorkManager();
    }

    /**
     * replace the fragment when bottom navigation bar in clicked
     *
     * @param fragmentClass The fragment Which should be loaded
     * @param tag           The Tag or the fragment
     */
    public void replaceFragment(Class<?> fragmentClass, String tag) {

        //if it the same running fragment do nothing
        if (FRAGMENT_CURRENT.equals(fragmentClass.getName()))
            return;

        FRAGMENT_CURRENT = fragmentClass.getName();
        // create instance of the fragment
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        //replace the ragment
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).commit();
    }

    /**
     * called if the we have not scheduled any alarms yet to fire syncing database service
     */
    public void scheduleWorkManager() {

        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();


        PeriodicWorkRequest periodicWorkRequest =
                new PeriodicWorkRequest.Builder(UpdateDatabaseWorker.class, 3, TimeUnit.HOURS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance()
                .enqueueUniquePeriodicWork("update_database",
                        ExistingPeriodicWorkPolicy.KEEP,
                        periodicWorkRequest);

    }


}
