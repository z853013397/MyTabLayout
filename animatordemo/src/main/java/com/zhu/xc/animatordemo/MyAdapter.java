package com.zhu.xc.animatordemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by xc on 2016/5/26.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Context mContext;
    private List<View> viewList;
    private OnAllViewListener mOnAllViewListener;


    public void setOnAllViewListener(OnAllViewListener mOnAllViewListener) {
        this.mOnAllViewListener = mOnAllViewListener;
    }

    public  interface OnAllViewListener{
        void onItemClick(int position,List<View> viewList);
    }

    public MyAdapter (Context context) {
        mContext = context;
        viewList = new ArrayList<>();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item,parent,false));
        if(!viewList.contains(viewHolder.itemView))
        viewList.add(viewHolder.itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
//        holder.img.setImageResource();

        if(mOnAllViewListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mOnAllViewListener.onItemClick(position,viewList);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}
