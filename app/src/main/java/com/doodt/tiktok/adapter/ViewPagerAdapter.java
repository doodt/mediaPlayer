package com.doodt.tiktok.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.doodt.tiktok.R;
import com.doodt.tiktok.entity.MediaModule;
import com.doodt.tiktok.holder.RecyclerItemNormalHolder;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "RecyclerBaseAdapter";

    private List<MediaModule> itemDataList = null;
    private Context context = null;

    public ViewPagerAdapter(Context context, List<MediaModule> itemDataList) {
        this.itemDataList = itemDataList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.layout_viewpager2_item, parent, false);
        final RecyclerView.ViewHolder holder = new RecyclerItemNormalHolder(context, v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        RecyclerItemNormalHolder recyclerItemViewHolder = (RecyclerItemNormalHolder) holder;
        recyclerItemViewHolder.setRecyclerBaseAdapter(this);
        recyclerItemViewHolder.onBind(position, itemDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    public void setListData(List<MediaModule> data) {
        itemDataList = data;
        notifyDataSetChanged();
    }
}
