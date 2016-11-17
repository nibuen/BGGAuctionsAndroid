package com.boarbeard.bggauctions.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boarbeard.bggauctions.R;
import com.boarbeard.bggauctions.model.GeekListComment;
import com.boarbeard.bggauctions.model.GeekListItem;
import com.boarbeard.bggauctions.parser.BidParser;
import com.boarbeard.bggauctions.util.ImageUtils;
import com.bumptech.glide.Glide;

import java.util.List;

public class SampleRecyclerViewAdapter extends RecyclerView.Adapter<SampleViewHolders>
{
    private List<GeekListItem> itemList;
    private Context context;

    public SampleRecyclerViewAdapter(Context context,
        List<GeekListItem> itemList)
    {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SampleViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.geek_list_item, null);
        SampleViewHolders rcv = new SampleViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(SampleViewHolders holder, int position)
    {
        holder.itemName.setText(itemList.get(position).getObjectName());

        List<GeekListComment> comments = itemList.get(position).getComments();
        if(comments.size() >= 1) {
            holder.topBid.setText(BidParser.parse(comments.get(comments.size() - 1).content));
        }

        Glide.with(context)
                .load(ImageUtils.createThumbnailJpgUrl(itemList.get(position).imageId()))
                .override(500, 400)
                .fitCenter()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount()
    {
        return this.itemList.size();
    }
}