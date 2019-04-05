package com.islam.newsfeeder.pojo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


@Entity(tableName = "article")
public class Article implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("url")
    @ColumnInfo(name = "article_url")
    private String articleUrl;

    @SerializedName("urlToImage")
    @ColumnInfo(name = "image_url")
    private String imageUrl;

    @Embedded
    @SerializedName("source")
    private Provider provider;
    private String publishedAt;
    private String content;

    @Ignore
    public Article() {
    }

    public Article(@NonNull int id, String author,
                   String title,
                   String description,
                   String articleUrl,
                   String imageUrl,
                   Provider provider,
                   String publishedAt,
                   String content) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.description = description;
        this.articleUrl = articleUrl;
        this.imageUrl = imageUrl;
        this.provider = provider;
        this.publishedAt = publishedAt;
        this.content = content;
    }

    public static DiffUtil.ItemCallback<Article> diffUtil = new DiffUtil.ItemCallback<Article>() {
        @Override
        public boolean areItemsTheSame(@NonNull Article article, @NonNull Article t1) {
            return article.id == t1.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Article article, @NonNull Article t1) {
            return false;
        }
    };

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String articleUrl) {
        this.articleUrl = articleUrl;
    }

    public String getUrlToImage() {
        return imageUrl;
    }

    public void setUrlToImage(String urlToImage) {
        this.imageUrl = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
