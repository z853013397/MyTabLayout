package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.view.MainMoreActivity;

import java.util.List;

/**
 * Created by Administrator on 2015/12/9.
 * <p/>
 * 所有的东西全写在一个列表项目里面 出错误了
 */
public class MainListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<MainPageInfoBean.MainItemEntity> datas;

    public MainListAdapter(Context mContext, List<MainPageInfoBean.MainItemEntity> datas) {
        this.datas = datas;
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    private static final int TITLE = 0;
    private static final int TITLE_IMG = 1;
    private static final int COMMON_CONTENT = 2;

    @Override
    public int getCount() {
        if (datas != null && datas.size() == 0) {
            return 0;
        }
        return datas.size() + 3;
    }

    @Override
    public String getItem(int position) {

        return datas.get(getPostion(position)).getIndex();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holderImg;
        ViewHolder3 holderCommon;
        ViewHolder2 holder2;
        int index = getPostion(position);
        MainPageInfoBean.MainItemEntity itemEntity = null;
        if (index != -1) {
            itemEntity = datas.get(index);
        }
        int type = getItemViewType(position);
        if (type == TITLE) { //标题
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.main_item_title, null);
//                RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DensityUtils.dpToPx(mContext,36));
//                convertView.setLayoutParams(p);
                holder2 = new ViewHolder2();
                holder2.more_info = (TextView) convertView.findViewById(R.id.more_info);
                holder2.main_title_decribe = (TextView) convertView.findViewById(R.id.main_title_decribe);
                holder2.line = convertView.findViewById(R.id.view5);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
            if (position == 2 * 4) {
                holder2.main_title_decribe.setText(R.string.main_kangfu);
                holder2.line.setVisibility(View.VISIBLE);
                holder2.main_title_decribe.setTextColor(Color.parseColor("#f39a1a"));
                holder2.more_info.setTextColor(Color.parseColor("#f39a1a"));
//                holder2.more_info.setCompoundDrawables(null,null,holder2.more_info.getContext().getDrawable(R.mipmap.home_listview_more0),null);
                holder2.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, MainMoreActivity.class).putExtra(MainMoreActivity.INDEX, 0));
                    }
                });
                setDrawableLeft(holder2.main_title_decribe, R.mipmap.recover_icon);

                setDrawableRight(holder2.more_info, R.mipmap.home_listview_more0);
            }
            if (position == 0 * 4) {
                holder2.line.setVisibility(View.GONE);

                holder2.main_title_decribe.setText(R.string.main_effect);
                holder2.main_title_decribe.setTextColor(Color.parseColor("#cb73c0"));
                holder2.more_info.setTextColor(Color.parseColor("#cb73c0"));
                holder2.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, MainMoreActivity.class).putExtra(MainMoreActivity.INDEX, 1));
                    }
                });
                setDrawableRight(holder2.more_info, R.mipmap.home_listview_more2);
                setDrawableLeft(holder2.main_title_decribe, R.mipmap.effect_icon);
            }

            if (position == 1 * 4) {
                holder2.line.setVisibility(View.VISIBLE);

                holder2.main_title_decribe.setText(R.string.main_knowledge);
                holder2.main_title_decribe.setTextColor(Color.parseColor("#3ed4db"));
                holder2.more_info.setTextColor(Color.parseColor("#3ed4db"));
                holder2.more_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mContext.startActivity(new Intent(mContext, MainMoreActivity.class).putExtra(MainMoreActivity.INDEX, 2));

                    }
                });
                setDrawableRight(holder2.more_info, R.mipmap.home_listview_more1);
                setDrawableLeft(holder2.main_title_decribe, R.mipmap.knowledge_icon);

            }


        } else if (type == TITLE_IMG) {//这个是所有类型中的第一个标题标题
            if (convertView == null) {
                holderImg = new ViewHolder();
                convertView = mLayoutInflater.inflate(R.layout.main_item_img, null);
                holderImg.img = (ImageView) convertView.findViewById(R.id.img);
                holderImg.title = (TextView) convertView.findViewById(R.id.img_title);
                convertView.setTag(holderImg);
            } else {
                holderImg = (ViewHolder) convertView.getTag();
            }
            int locPosition = getPostion(position);
//            LogUtil.i("MainActivity", locPosition);
            String imgUrl = itemEntity.getImageUrl();

//            LogUtil.i("MainActivity", "头部的图片地址是：" + imgUrl);

            String title = itemEntity.getTitle();
            if (!TextUtils.isEmpty(imgUrl)) {
                Glide.with(mContext).load(imgUrl)
                        .error(R.drawable.default_img)//这个是加载失败的
                        .into(holderImg.img);
            }

            holderImg.title.setText(title);
//            }


        } else if (COMMON_CONTENT == type) {
            // /这个是图片和标题并列
            if (convertView == null) {
                holderCommon = new ViewHolder3();
                convertView = mLayoutInflater.inflate(R.layout.include_main_item, null);
                holderCommon.img = (ImageView) convertView.findViewById(R.id.img);
                holderCommon.title = (TextView) convertView.findViewById(R.id.title);
                holderCommon.watch = (TextView) convertView.findViewById(R.id.watch);
                convertView.setTag(holderCommon);
            } else {
                holderCommon = (ViewHolder3) convertView.getTag();
            }

            int locPosition = getPostion(position);
//            LogUtil.i("MainActivity", locPosition);
            String imgUrl = itemEntity.getImageUrl();
            String title = itemEntity.getTitle();
            String watch = itemEntity.getReplynum();
//            LogUtil.i("MainActivity","watch is :" + watch);
            if (!TextUtils.isEmpty(imgUrl)) {
                Glide.with(mContext).load(imgUrl)
//                    .signature(new IntegerVersionSignature(1))
                        .error(R.drawable.default_img)//这个是加载失败的
                        .into(holderCommon.img);
            }

            holderCommon.title.setText(title);
            holderCommon.watch.setText(watch);
        }
        return convertView;
    }


    class ViewHolder {
        ImageView img;
        TextView title;
    }

    class ViewHolder2 {
        TextView main_title_decribe;
        View line;
        TextView more_info;
    }

    class ViewHolder3 {
        ImageView img;
        TextView title;
        TextView watch;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 4 == 0) {
            return TITLE;
        } else if (position % 4 == 1) {
            return TITLE_IMG;
        } else {
            return COMMON_CONTENT;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }


    @Override
    public boolean isEnabled(int position) {
        if (getItemViewType(position) == TITLE) {
            return false;
        }
        return super.isEnabled(position);
    }


    private void setDrawableRight(TextView v, int id) {
        Drawable drawable = mContext.getResources().getDrawable(id);
        //这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        v.setCompoundDrawables(null, null, drawable, null);
        v.setCompoundDrawablePadding(DensityUtils.dpToPx(mContext, 4));
    }

    private void setDrawableLeft(TextView v, int id) {
        Drawable drawable = mContext.getResources().getDrawable(id);
        //这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        v.setCompoundDrawables(drawable, null, null, null);
        v.setCompoundDrawablePadding(DensityUtils.dpToPx(mContext, 4));
    }

    public void update(List datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    private int getPostion(int position) {
        if (position % 4 == 0) {
            return -1;
        }
        return position - (position / 4) - 1;
    }
}
