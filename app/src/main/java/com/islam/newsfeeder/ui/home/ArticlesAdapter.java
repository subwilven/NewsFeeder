package com.islam.newsfeeder.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.other.RoundedCornersTransformation;

import java.util.List;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ViewHolder> {

    //this used to set radius to the image
    private final RoundedCornersTransformation cornersTransformation =
            new RoundedCornersTransformation(7, 0);
    private List<Article> mArticleList;
    private final CallBacks.AdapterCallBack<Article> mCallBack;


    public ArticlesAdapter(CallBacks.AdapterCallBack<Article> callBack) {
        mCallBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.item_article;
        View view = inflater.inflate(layoutID, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article article = mArticleList.get(position);
        ActivityUtils.loadImage(holder.moviePosterImageView,
                article.getImageUrl(),
                cornersTransformation);
        holder.titleTextView.setText(article.getTitle());
    }

    @Override
    public int getItemCount() {
        if (mArticleList == null)
            return 0;
        return mArticleList.size();
    }

    public void setData(List<Article> data) {
        this.mArticleList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView moviePosterImageView;
        private final TextView titleTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePosterImageView = itemView.findViewById(R.id.item_article_image);
            titleTextView = itemView.findViewById(R.id.item_article_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCallBack.onItemClicked(mArticleList.get(getAdapterPosition()));
        }
    }
}
