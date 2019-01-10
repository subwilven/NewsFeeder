package com.islam.newsfeeder.ui.articles_list;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.islam.newsfeeder.POJO.Article;
import com.islam.newsfeeder.R;
import com.islam.newsfeeder.util.CallBacks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProvidersAdapter extends RecyclerView.Adapter<ProvidersAdapter.ViewHolder> {

    private final List<String> mProviderList;
    private Map<String, List<Article>> mProviderArticlesMap;
    private final RecyclerView.RecycledViewPool viewPool;
    private final CallBacks.AdapterCallBack<Article> mCallBack;


    public ProvidersAdapter(CallBacks.AdapterCallBack<Article> callBack) {
        viewPool = new RecyclerView.RecycledViewPool();
        mProviderList = new ArrayList<>();
        mCallBack = callBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutID = R.layout.item_provider;
        View view = inflater.inflate(layoutID, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String provider = mProviderList.get(position);
        holder.nameTextView.setText(provider);
        holder.setRecyclerViewData(mProviderArticlesMap.get(provider));
    }

    @Override
    public int getItemCount() {
        return mProviderList.size();
    }

    public void setData(Map<String, List<Article>> data) {
        this.mProviderArticlesMap = data;
        mProviderList.clear();
        this.mProviderList.addAll(data.keySet());
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final RecyclerView recyclerView;
        private final TextView nameTextView;
        private final ArticlesAdapter articlesAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.item_provider_rv);
            nameTextView = itemView.findViewById(R.id.item_provider_name);
            itemView.setOnClickListener(this);

            recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setRecycledViewPool(viewPool);
            articlesAdapter = new ArticlesAdapter(mCallBack);
            recyclerView.setAdapter(articlesAdapter);
        }

        public void setRecyclerViewData(List<Article> articles) {
            articlesAdapter.setData(articles);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
