package com.islam.newsfeeder.data.articles;

import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.islam.newsfeeder.pojo.Article;

import java.util.List;

@Dao
public abstract class ArticleDao {

    @Query("SELECT * FROM article")
    public abstract DataSource.Factory<Integer, Article> getAllArticles();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<Article> objs);

    @Update
    public abstract void update(Article obj);

    @Delete
    abstract public void delete(Article obj);

    @Query("DELETE FROM article")
    public abstract void clearAllData();

}
