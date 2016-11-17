package com.boarbeard.bggauctions.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.boarbeard.bggauctions.R;

public class SampleViewHolders extends RecyclerView.ViewHolder implements
    View.OnClickListener
{
    public TextView itemName;
    public TextView topBid;
    public ImageView imageView;

    public SampleViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        itemName = (TextView) itemView.findViewById(R.id.ItemName);
        imageView = (ImageView) itemView.findViewById(R.id.GeekItemImageView);
        topBid = (TextView) itemView.findViewById(R.id.TopBid);
    }

    @Override
    public void onClick(View view)
    {
        Toast.makeText(view.getContext(),
            "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT)
            .show();
    }
}