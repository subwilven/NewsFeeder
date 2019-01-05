package com.islam.newsfeeder.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.islam.newsfeeder.MyApplication;
import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.data.articles.ArticleDao;

@Database(entities = {Article.class}, version = 3)
public abstract class NewsFeederDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "news_feeder";
    private static final Object sLock = new Object();
    private static NewsFeederDatabase INSTANCE = null;

    public static NewsFeederDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(MyApplication.getInstance().getApplicationContext(),
                            NewsFeederDatabase.class, NewsFeederDatabase.DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract ArticleDao articleDao();
}
