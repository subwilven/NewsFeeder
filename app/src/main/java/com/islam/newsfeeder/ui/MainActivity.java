package com.islam.newsfeeder.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.recievers.AlarmReceiver;
import com.islam.newsfeeder.ui.articles_list.ArticlesListFragment;
import com.islam.newsfeeder.ui.read_later.ReadLaterFragment;
import com.islam.newsfeeder.util.PreferenceUtils;

import static com.islam.newsfeeder.util.Constants.INTERVAL_UPDATE_DATABASE;

public class MainActivity extends AppCompatActivity {

    private static String FRAGMENT_CURRENT = "";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(ArticlesListFragment.class, ArticlesListFragment.TAG);
                    return true;
                case R.id.navigation_dashboard:
                    replaceFragment(ReadLaterFragment.class, null);
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

        //if this the first launch
        if (savedInstanceState == null)
            replaceFragment(ArticlesListFragment.class, ArticlesListFragment.TAG);
        //check if the job has scheduled before
        boolean isRunning = PreferenceUtils.getIsAlarmRunning(this);
        if (!isRunning)
            scheduleAlarm();
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

    public void scheduleAlarm() {

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);

        final PendingIntent pIntent = PendingIntent.getBroadcast(this,
                AlarmReceiver.REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis() + INTERVAL_UPDATE_DATABASE,// fire arter 10 min from now
                INTERVAL_UPDATE_DATABASE,//repeat every 10min
                pIntent);

        PreferenceUtils.saveIsAlarmManagerRunning(this, true);
    }


}
