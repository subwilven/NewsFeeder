package com.islam.newsfeeder.util;

public final class Constants {

    public final static String URL_NEWS_API = "https://newsapi.org/v2/";
    public final static String URL_POCKET_API = "https://getpocket.com/v3/";
    public final static String redirectUri = "pocketapp83166://open.my.app";

    public static final int STATUS_LAYOUT_DATA = 0;
    public static final int STATUS_LAYOUT_NO_DATA = 1;
    public static final int STATUS_LAYOUT_NO_CONNECTION = 2;
    public static final int STATUS_LAYOUT_LOADING = 3;
    public static final int STATUS_SNACK_BAR_NO_CONNECTION = 4;

    public static final String SHARED_PROVIDERS_file = "providers";
    public static final String SHARED_POCKET_file = "pocket";

    public static final String KEY_PROVIDERS = "providers";
    public static final String KEY_JOB_SCHEDULER_STATUS = "job_scheduler";
    public static final String KEY_REQUEST_TOKEN = "request_token";
    public static final String KEY_ACCESS_TOKEN = "access_token";

    public static final String BUNDLE_ARTICLE = "article";
    public static final String BUNDLE_OPEN_REAL_LATER_FRAGMENT = "openReadLaterArticles";

    public static final int INTERVAL_UPDATE_DATABASE = 1000 * 60 * 10;

    public static final int ID_FOREGROUND_SERVICE = 101;

    public static final int PAGE_SIZE_DATABASE = 20;
    public static final int PAGE_SIZE_NETWORK = 20;

    public static final String NOTIFICATION_CHANNEL_ID = "com.islam.newsfeeder";
    public static final String NOTIFICATION_CHANNEL_NAME = "Sync";

    public final static String NETWORK_STATUS_OK = "ok";
    public final static String NETWORK_STATUS_ERROR = "error";

    public final static String ERROR_NO_CONNECTION = "no_connection";

    private Constants() {
    }

}
