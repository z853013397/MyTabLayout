package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.presenter.ReplyForumPresent;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.widget.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/29.
 */
public class ReplyForumItemAdapter extends BaseAdapter implements ViewDataListener {

    private static final String TAG = "ReplyForumItemAdapter";
    private List<ReplyListBean.ReplyNumEntity> data;
    private Context context;
    private LayoutInflater inflater;

    public ReplyForumItemAdapter(Context context, List<ReplyListBean.ReplyNumEntity> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public interface ShowInput {
        void showInput(int position);
    }

    private ShowInput mShowInput;

    public void setShowInput(ShowInput mShowInput) {
        this.mShowInput = mShowInput;
    }

    @Override
    public ReplyListBean.ReplyNumEntity getItem(int position) {
//        return data.get(position);
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_reply_forum, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (CircleImageView) convertView.findViewById(R.id.host_avatar);
            viewHolder.release_forum_name = (TextView) convertView.findViewById(R.id.release_forum_name);
            viewHolder.release_forum_time = (TextView) convertView.findViewById(R.id.release_forum_time);
            viewHolder.reply_textview = (TextView) convertView.findViewById(R.id.reply_textview);
            viewHolder.is_host = (ImageView) convertView.findViewById(R.id.is_host);
            viewHolder.reply_content = (TextView) convertView.findViewById(R.id.reply_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //初始化
        ReplyListBean.ReplyNumEntity item = data.get(position);
        String imageUrl = item.getHeadimgurl();
        Glide.with(context).load(imageUrl).signature(new IntegerVersionSignature(1)).error(R.mipmap.default_avatar).into(viewHolder.img);

        viewHolder.reply_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mShowInput != null) {
                    mShowInput.showInput(position);
                }
            }
        });

        viewHolder.release_forum_name.setText(item.getFromUser());

        String replyTime = item.getReplyTime();
        if (!TextUtils.isEmpty(replyTime)) {
            viewHolder.release_forum_time.setText(getDateFromData(replyTime));
        }
        viewHolder.is_host.setVisibility(View.GONE);
        String content = item.getContent();
        String touser = item.getToUser();
        if (TextUtils.isEmpty(touser) || item.isHost()) {
            viewHolder.reply_content.setText(item.getContent());
        } else {
            String temp = "回复@" + touser + ":" + content;
            viewHolder.reply_content.setText(temp);
        }

        return convertView;
    }

    /**
     * 回复某个人
     *
     * @param tag
     * @return
     */
    private String Content;//内容
    private String InfoID;
    private String fromuser;//我的infromname。
    private String index;//帖子的id
    private String toinfoid;//平论人的id
    private String touser;//评论这个人的infroname

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
//         map.put(Contants.index,index)
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {

    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {

    }

    class ViewHolder {
        CircleImageView img;//头像
        TextView release_forum_name;//回帖人的名字
        TextView release_forum_time;//回复的时间
        TextView reply_textview;//回复
        ImageView is_host;//设置显示来显示是否为楼主
        TextView reply_content;//回复的内容
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    public void update(List data) {
        this.data = data;
        notifyDataSetChanged();
    }


    private String getDateFromData(String date) {
        date = date.concat("000");
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        long dates = Long.parseLong(date);
        return format.format(new Date(dates));
    }


    private void replyForum() {
        new ReplyForumPresent(this).getData();
    }
}
