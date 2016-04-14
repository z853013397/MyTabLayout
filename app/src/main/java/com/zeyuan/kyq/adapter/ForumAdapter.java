package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
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
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;

import java.util.List;

/**
 * Created by Administrator on 2015/9/16.
 * 这个是帖子列表的adapter 区分置顶 精华和 普通
 */
public class ForumAdapter extends BaseAdapter {

    private static final int ZHIDING = 1;//置顶1 精华2 普通0
    private static final int JINGHUA = 2;
    private static final int PUTONG = 0;
    private static final String TAG = "ForumAdapter";

    private Context context;
    private LayoutInflater inflater;
    private List<ForumListBean.ForumnumEntity> data;
    private int length = 0;

    public ForumAdapter(Context context, List<ForumListBean.ForumnumEntity> data) {
        this.data = data;
        this.context = context;
        inflater = LayoutInflater.from(context);
//        getZDLength(data);
    }

    /**
     * 得到置顶帖子的数量
     *
     * @param data
     */
    private void getZDLength(List<ForumListBean.ForumnumEntity> data) {
        length = 0;
        for (int i = 0, j = data.size(); i < j; i++) {
            if (ZHIDING == getItemViewType(i)) {
                length++;
            }
        }
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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        switch (data.get(position).getPostType()) {
            case "1": {
                return ZHIDING;
            }
            case "2": {
                return JINGHUA;
            }
            case "0": {
                return PUTONG;
            }
            default:
                throw new RuntimeException("switch has not choose!!");
        }
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(data.get(position).getIndex());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ForumListBean.ForumnumEntity item = data.get(position);
        ViewHolder viewHolder;
        int type = getItemViewType(position);
        switch (type) {
            case ZHIDING: {//置顶帖子
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_top_forum, null);
                    viewHolder = new ViewHolder();
                    Drawable drawable = null;
//                    switch (position) {
//                        case 0:
//                            drawable = context.getResources().getDrawable(R.drawable.top_forum_shape1);
//                            break;
//                        case i: {
//                            break;
//
//                        }
//                    }
                    LogUtil.i(TAG, "置顶帖子的数量是：" + length);
                    if (position == length - 1) {
                        drawable = context.getResources().getDrawable(R.drawable.top_forum_shape2);
                    } else if (position == 0) {
                        drawable = context.getResources().getDrawable(R.drawable.top_forum_shape1);
                    }
                    if (drawable != null) {
                        convertView.setBackgroundDrawable(drawable);
                    }

                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    //find views
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }

                String title = item.getTitle();
                viewHolder.title.setText(title);

                break;
            }
            case JINGHUA: {//精华
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_forum, null);
                    viewHolder = new ViewHolder();
                    viewHolder.reply_number = (TextView) convertView.findViewById(R.id.reply_number);
                    viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    viewHolder.zhiding = (ImageView) convertView.findViewById(R.id.jinghua);//这个是精华
                    viewHolder.has_photo = (ImageView) convertView.findViewById(R.id.has_photo);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                //请设置12个空格
                String title = item.getTitle();
                StringBuilder sb = new StringBuilder(title);
//                for (int i = 0; i < 12; i++) {//6个字
//                    sb.insert(0, " ");
//                }
                viewHolder.title.setText(sb);
                viewHolder.forum_host_name.setText(item.getAuthor());
                viewHolder.reply_number.setText(item.getReplyNum());
                Glide.with(context).load(item.getHeadimgurl()).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)
                        .into(viewHolder.avatar);

                break;
            }
            case PUTONG: {//普通
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.item_forum, null);
                    viewHolder = new ViewHolder();
                    viewHolder.reply_number = (TextView) convertView.findViewById(R.id.reply_number);
                    viewHolder.forum_host_name = (TextView) convertView.findViewById(R.id.forum_host_name);
                    viewHolder.title = (TextView) convertView.findViewById(R.id.title);
                    viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                    viewHolder.zhiding = (ImageView) convertView.findViewById(R.id.jinghua);//这个是精华
                    viewHolder.has_photo = (ImageView) convertView.findViewById(R.id.has_photo);//这个是精华
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
//                viewHolder.zhiding.setVisibility(View.GONE);
                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                p.setMargins(DensityUtils.dpToPx(context, 16), DensityUtils.dpToPx(context, 10), 0, 0);
//                viewHolder.has_photo.setLayoutParams(p);
                viewHolder.reply_number.setText(item.getReplyNum());
                viewHolder.forum_host_name.setText(item.getAuthor());
                String title = item.getTitle();
                StringBuilder sb = new StringBuilder(title);
//                for (int i = 0; i < 6; i++) {//6个字
//                    sb.insert(0, " ");
//                }
                viewHolder.title.setText(sb);
                Glide.with(context).load(item.getHeadimgurl()).signature(new IntegerVersionSignature(1))
                        .error(R.mipmap.default_avatar)
                        .into(viewHolder.avatar);


                break;
            }
        }


        return convertView;
    }

    class ViewHolder {
        TextView reply_number;
        TextView forum_host_name;
        TextView title;
        ImageView avatar;
        ImageView has_photo;
        ImageView zhiding;
    }

    public void update(List data) {
        this.data = data;
        getZDLength(data);
        notifyDataSetChanged();
    }
}
