package com.islam.newsfeeder.POJO.network;

import com.google.gson.annotations.SerializedName;

public class ReadLaterArticle {

    @SerializedName("resolved_title")
    private String title;
    @SerializedName("top_image_url")
    private String imageUrl;
    @SerializedName("excerpt")
    private String description;
    @SerializedName("resolved_url")
    private String articleUrl;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
}
