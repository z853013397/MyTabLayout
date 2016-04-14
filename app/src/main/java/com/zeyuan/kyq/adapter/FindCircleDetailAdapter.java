package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.CityCancerForumBean;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.MyCheckBox;

import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
public class FindCircleDetailAdapter extends BaseAdapter {
    private List<String> focusCircle;
    private LayoutInflater inflater;
    private List<CityCancerForumBean.NumEntity> Num;
    private Context context;

    public FindCircleDetailAdapter(Context context, List<CityCancerForumBean.NumEntity> Num) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.Num = Num;
//        chooseList = new ArrayList<>();
        focusCircle = UserinfoData.getFocusCircle(context);
    }

    @Override
    public int getCount() {
        return Num.size();
    }

    @Override
    public String getItem(int position) {
        return Num.get(position).getCircleID();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public interface FollowCircleListener {
        void followCircle(BaseAdapter adapter, boolean isFollow, int position);
    }

    private FollowCircleListener listener;


    public void setListener(FollowCircleListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_find_circle, null);
            holder.topic_number = (TextView) convertView.findViewById(R.id.topic_number);
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.cancer = (TextView) convertView.findViewById(R.id.cancer);

//            holder.img = (ImageView) view.findViewById(R.id.img);
            holder.isfollow = (MyCheckBox) convertView.findViewById(R.id.isfollow);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final CityCancerForumBean.NumEntity item = Num.get(position);

        holder.topic_number.setText(context.getString(R.string.forum_num) + item.getForumNum());
        holder.number.setText(context.getString(R.string.person_num) + item.getUsernum());
        String title = item.getCircleID();

        if (!TextUtils.isEmpty(title)) {
            String circleName = MapDataUtils.getCircleValues(title);
            holder.title.setText(circleName);

        } else {
            holder.title.setText("");
        }//item.isFollow()

        String cancer = item.getCancerID();
        if (!TextUtils.isEmpty(cancer)) {
            String cancerName = MapDataUtils.getCancerValues(cancer);
            holder.cancer.setVisibility(View.VISIBLE);
            holder.cancer.setText(cancerName);
        }else {
            holder.cancer.setVisibility(View.GONE);
        }

        holder.isfollow.setChecked(item.isFollow());

//        if (chooseList.contains(position)) {
//            holder.isfollow.setChecked(true);
//        }
//        if(focusCircle.contains(title)){
//            holder.isfollow.setChecked(true);
//        } else {
//            holder.isfollow.setChecked(false);
//        }
        holder.isfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isChecked = holder.isfollow.isChecked();
                if (!isChecked) {//如果选中 变为不选中状态 即为取消关注
//                    holder.isfollow.setChecked(false);
//                    holder.isfollow.setText(R.string.has_focus);
                    item.setIsFollow(false);
                    if (listener != null) {
                        listener.followCircle(FindCircleDetailAdapter.this, false, position);
                    }
                } else {
//                    holder.isfollow.setText(R.string.need_focus);

                    item.setIsFollow(true);
//                    holder.isfollow.setChecked(true);
                    if (listener != null) {
                        listener.followCircle(FindCircleDetailAdapter.this, true, position);
                    }
                }
            }
        });

//        holder.isfollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
////                checkPosition = position;
//
//
//                if (isChecked) {
//                    if (listener != null) {
//                        listener.followCircle(FindCircleDetailAdapter.this, true, position);
//                    }
////                    if (!chooseList.contains(position)) {
////                        chooseList.add(position);
////                    }
////                    followOrcancel = "1";
//                } else {
//                    if (listener != null) {
//                        listener.followCircle(FindCircleDetailAdapter.this, false, position);
//                    }
////                    if (chooseList.contains(position)) {
////                       int index =  chooseList.indexOf(position);
////                        chooseList.remove(index);
////                    }
////                    followOrcancel = "2";
//                }
////                folowCircle();
//            }
//        });


        return convertView;
    }

//    private int checkPosition = -1;
//    private String followOrcancel;
//    private List<Integer> chooseList;
//
//    @Override
//    public Map getParamInfo(int tag) {
//        String circleID = getItem(checkPosition);
//        Map<String, String> map = new HashMap<>();
//        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
//        map.put(Contants.CircleID, circleID);
//        map.put(Contants.followOrcancel, followOrcancel);//1关注 2.取消
//        String type = "";
//        if (Integer.valueOf(circleID) < 10000) {
//            type = "1";
//        } else {
//            type = "2";
//        }
//        map.put("type", type);//1.同城圈 2.抗癌圈  circle< 10000则是同城圈 >=10000则是抗癌圈
//
//        return map;
//    }
//
//    @Override
//    public void toActivity(Object t, int position) {
//        LogUtil.i("SearchCircleActivity", t.toString());
//        FollowBean bean = (FollowBean) t;
//        if (Contants.OK_DATA.equals(bean.getIResult())) {
//            Toast.makeText(context, R.string.do_success, Toast.LENGTH_SHORT).show();
//        } else {
////            Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
//
//        }
//
//
//    }
//
//    @Override
//    public void showLoading(int tag) {
//
//    }
//
//    @Override
//    public void hideLoading(int tag) {
//
//    }
//
//    @Override
//    public void showError(int tag) {
//
//    }

    static class ViewHolder {
        TextView topic_number;//话题
        TextView number;//人数
        TextView cancer;//癌肿

        TextView title;//深圳圈
        MyCheckBox isfollow;//关注
    }

    public void update(List data) {
        focusCircle = UserinfoData.getFocusCircle(context);
        Num = data;
        notifyDataSetChanged();
    }

    public void update() {
        focusCircle = UserinfoData.getFocusCircle(context);
        notifyDataSetChanged();
    }


//    private void folowCircle() {
//        new FollowCirclePresenter(this).getData();
//    }
}
