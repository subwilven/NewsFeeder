package com.islam.newsfeeder.ui.read_later;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islam.newsfeeder.R;
import com.islam.newsfeeder.databinding.ItemReadLaterArticleBinding;
import com.islam.newsfeeder.pojo.ReadLaterArticle;
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
        ItemReadLaterArticleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.item_read_later_article, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReadLaterArticle article = mArticleList.get(position);
        holder.bind(article);
        // if the article has no title show the given title (the title that has been send once the user saved the article)
//        if (!article.getTitle().isEmpty())
//            holder.titleTextView.setText(article.getTitle());
//        else
//            holder.titleTextView.setText(article.getGivenTitle());

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

        ItemReadLaterArticleBinding binding;

        ViewHolder(ItemReadLaterArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCallBack.onItemClicked(mArticleList.get(getAdapterPosition()));
        }

        void bind(ReadLaterArticle article) {
            binding.setArticle(article);
            binding.setRoundedCorner(cornersTransformation);
            binding.executePendingBindings();
        }
    }
}

