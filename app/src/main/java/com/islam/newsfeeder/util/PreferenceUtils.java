package com.islam.newsfeeder.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.islam.newsfeeder.POJO.Provider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.islam.newsfeeder.util.Constants.KEY_JOB_SCHEDULER_STATUS;
import static com.islam.newsfeeder.util.Constants.KEY_PROVIDERS;
import static com.islam.newsfeeder.util.Constants.SHARE_POCKET_file;
import static com.islam.newsfeeder.util.Constants.SHARE_PROVIDERS_file;

public final class PreferenceUtils {

    private PreferenceUtils() {
    }

    public static SharedPreferences getProviderSharedPreference(Context context) {
        return context.getSharedPreferences(SHARE_PROVIDERS_file, Context.MODE_PRIVATE);
    }

    public static SharedPreferences getPocketSharedPreference(Context context) {
        return context.getSharedPreferences(SHARE_POCKET_file, Context.MODE_PRIVATE);
    }

    public static void savePocketData(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getPocketSharedPreference(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPocketData(Context context, String key) {
        return getPocketSharedPreference(context).getString(key, null);
    }

    public static void saveProvidersInShared(Context context, List<Provider> providers) {
        SharedPreferences sharedPreferences = getProviderSharedPreference(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //convert provider object to array of strings to be stored in the shared
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for (Provider provider : providers) {
            objStrings.add(gson.toJson(provider));
        }

        String[] myStringList = objStrings.toArray(new String[objStrings.size()]);
        editor.putString(KEY_PROVIDERS, TextUtils.join("‚‗‚", myStringList)).apply();
        editor.apply();
    }

    public static List<Provider> getProvidersFromShared(Context context) {
        SharedPreferences sharedPreferences = getProviderSharedPreference(context);
        String data = sharedPreferences.getString(KEY_PROVIDERS, null);


        if (data != null) {
            Gson gson = new Gson();
            ArrayList<String> objStrings =
                    new ArrayList<>(Arrays.asList(TextUtils.split(data, "‚‗‚")));

            ArrayList<Provider> providers = new ArrayList<>();

            for (String jObjString : objStrings) {
                Provider value = gson.fromJson(jObjString, Provider.class);
                providers.add(value);
            }
            return providers;
        } else {
            //in case there is any stored providers before
            //get the default
            return getDefaultProviders();
        }

    }

    public static void saveIsAlarmManagerRunning(Context context, boolean b) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(KEY_JOB_SCHEDULER_STATUS, b);
        editor.apply();
    }

    public static boolean getIsAlarmRunning(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_JOB_SCHEDULER_STATUS, false);
    }

    private static ArrayList<Provider> getDefaultProviders() {
        ArrayList<Provider> providers = new ArrayList<>();
        providers.add(new Provider("bbc-news", "BBC News", true));
        providers.add(new Provider("cnn", "CNN", true));
        providers.add(new Provider("aftenposten", "Aftenposten", true));
        return providers;
    }

}
