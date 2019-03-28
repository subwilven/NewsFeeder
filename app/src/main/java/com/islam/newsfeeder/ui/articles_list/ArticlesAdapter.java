package com.islam.newsfeeder.ui.articles_list;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.POJO.NetworkState;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.other.RoundedCornersTransformation;

public class ArticlesAdapter extends PagedListAdapter<Article, ArticlesAdapter.ViewHolder> {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;

    //this used to set radius to the image
    private final RoundedCornersTransformation cornersTransformation =
            new RoundedCornersTransformation(7, 0);
    private final CallBacks.AdapterCallBack<Article> mCallBack;
    private NetworkState networkState;


    protected ArticlesAdapter(CallBacks.AdapterCallBack<Article> mCallBack) {
        super(Article.diffUtil);
        this.mCallBack = mCallBack;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_PROGRESS:
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_progress, parent, false);
                return new ViewHolder(view);
            default:
                View view1 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_article, parent, false);
                return new ViewHolder(view1);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        if (getItemViewType(i) == TYPE_ITEM) {
            Article article = getItem(i);
            if (article != null) {
                ActivityUtils.loadImage(holder.moviePosterImageView,
                        article.getImageUrl(),
                        cornersTransformation);
                holder.titleTextView.setText(article.getTitle());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }


    public void setNetworkState(NetworkState newNetworkState) {
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = newNetworkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != newNetworkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    private boolean hasExtraRow() {
        if (networkState != null && networkState.getStatus() != NetworkState.STATUS_LOADING) {
            return true;
        } else {
            return false;
        }
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
            mCallBack.onItemClicked(getItem(getAdapterPosition()));
        }
    }
}
