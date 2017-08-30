package com.example.chandoras.newsdroid;

import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


/**
 * Created by chandoras on 8/27/17.
 */

public class NewsDataRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<NewsData> allDatas;
    LayoutInflater inflater;

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public NewsDataRecycler(Context context, ArrayList<NewsData> allDatas) {
        this.context = context;
        this.allDatas = allDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.news_data, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {


        MyViewHolder myViewHolder = (MyViewHolder) holder;
        NewsData data = allDatas.get(position);

        final String urlToImage = data.getUrlToImage();
        final String description = data.getDescription();
        final String title = data.getTitle();
        final String url = data.getUrl();
        final String source = data.getSource();


        myViewHolder.titleTextView.setText(title);
        Glide.with(context).load(urlToImage).into(myViewHolder.imageView);

        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("TITLE", title);
                intent.putExtra("DESC", description);
                intent.putExtra("URL", url);
                intent.putExtra("URL_IMAGE", urlToImage);
                intent.putExtra("SOURCE", source);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return allDatas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);
            titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
