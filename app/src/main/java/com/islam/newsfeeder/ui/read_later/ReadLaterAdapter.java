package com.islam.newsfeeder.ui.read_later;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.islam.newsfeeder.POJO.network.ReadLaterArticle;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.ActivityUtils;
import com.islam.newsfeeder.util.CallBacks;
import com.islam.newsfeeder.util.other.RoundedCornersTransformation;

import java.util.List;

public class ReadLaterAdapter extends RecyclerView.Adapter<ReadLaterAdapter.ViewHolder> {

    //this used to set radius to the image
    private final RoundedCornersTransformation cornersTransformation =
            new RoundedCornersTransformation(7, 0);
    private final CallBacks.AdapterCallBack<ReadLaterArticle> mCallBack;
    private List<ReadLaterArticle> mArticleList;


    public ReadLaterAdapter(CallBacks.AdapterCallBack<ReadLaterArticle> callBack) {
        mCallBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.item_read_later_article;
        View view = inflater.inflate(layoutID, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReadLaterArticle article = mArticleList.get(position);
        if (article.getImageUrl() != null && !article.getImageUrl().isEmpty())
            ActivityUtils.loadImage(holder.imageView,150,150,
                    article.getImageUrl(),
                    cornersTransformation);
        else
            //if the article has no image show the placeholder
            holder.imageView.setImageResource(R.drawable.placeholder);

        // if the article has no title show the given title (the title that has been send once the user saved the article)
        if (!article.getTitle().isEmpty())
            holder.titleTextView.setText(article.getTitle());
        else
            holder.titleTextView.setText(article.getGivenTitle());

        holder.desciptionTextView.setText(article.getDescription());
    }

    @Override
    public int getItemCount() {
        if (mArticleList == null)
            return 0;
        return mArticleList.size();
    }

    public void setData(List<ReadLaterArticle> data) {
        this.mArticleList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView imageView;
        private final TextView titleTextView;
        private final TextView desciptionTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_read_later_article_image);
            titleTextView = itemView.findViewById(R.id.item_read_later_article_title);
            desciptionTextView = itemView.findViewById(R.id.item_read_later_article_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCallBack.onItemClicked(mArticleList.get(getAdapterPosition()));
        }
    }
}

