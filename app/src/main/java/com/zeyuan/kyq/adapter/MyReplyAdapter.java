package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.MyReplyListBean;
import com.zeyuan.kyq.bean.SimilarCaseBean;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 * 这个是帖子列表的adapter 区分置顶 精华和 普通
 */
public class MyReplyAdapter extends BaseAdapter {


    private Context context;
    private LayoutInflater inflater;
    private List<MyReplyListBean.ReplyNumEntity> data;

    public MyReplyAdapter(Context context, List<MyReplyListBean.ReplyNumEntity> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position).getIndex();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_forum__comment_back, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
            viewHolder.reply_number = (TextView) convertView.findViewById(R.id.reply_number);//这个就是时间
            viewHolder.reply = (TextView) convertView.findViewById(R.id.reply);
            viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);

            //find views
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyReplyListBean.ReplyNumEntity item = data.get(position);

        String time = item.getReplyTime();
        viewHolder.reply_number.setText(DataUtils.getDateDetail(time));

        String title = item.getTitle();
        viewHolder.reply.setText("原文：" + title);


        String content = item.getContent();
        String fromUser = item.getFromUser();
        String toUser = item.getToUser();



        viewHolder.title.setText(fromUser+"回复"+toUser+ ":" +content);

        String name = item.getFromUser();


        viewHolder.forum_host_name.setText(name);
        String url = item.getHeadImgurl();
        Glide.with(context).load(url).signature(new IntegerVersionSignature(1)).into(viewHolder.avatar);
        return convertView;
    }

    class ViewHolder {
        TextView reply_number;
        TextView forum_host_name;
        TextView title;
        ImageView avatar;
        TextView reply;
    }

    public void update(List data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
