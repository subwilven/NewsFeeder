package com.islam.newsfeeder.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.ui.home.HomeFragment;
import com.islam.newsfeeder.ui.saved_article.SavedArticleFragment;

public class MainActivity extends AppCompatActivity {

    private static String FRAGMENT_CURRENT = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(HomeFragment.class, HomeFragment.TAG);
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(SavedArticleFragment.class, null);
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

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


}
