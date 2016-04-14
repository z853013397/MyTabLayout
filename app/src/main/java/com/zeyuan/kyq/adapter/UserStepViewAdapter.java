package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.SlaverCancer;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2015/10/8.
 * 阶段详情的adapter
 */
public class UserStepViewAdapter extends BaseExpandableListAdapter {
    public interface ChangeListener {
        void edit(int groupIndex, int childIndex);

        void delete(int groupIndex, int childIndex);
    }

    private ChangeListener listener;

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    private List<UserStepBean> groupDatas;
    private List<String> childDatas;
    private Context context;
    private LayoutInflater inflater;

    public UserStepViewAdapter(Context context, List<UserStepBean> groupDatas, List<String> childDatas) {
        this.context = context;
        this.groupDatas = groupDatas;
        this.childDatas = childDatas;
        inflater = LayoutInflater.from(context);
    }


    public UserStepViewAdapter(Context context, List<UserStepBean> groupDatas) {
        this.context = context;
        this.groupDatas = groupDatas;
        inflater = LayoutInflater.from(context);

    }

    private List<UserStepChildBean> getChildDataList(int groupPosition) {
        return groupDatas.get(groupPosition).getChildList();
    }

    @Override
    public int getGroupCount() {
        return groupDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<UserStepChildBean> lstChild = getChildDataList(groupPosition);
        if (lstChild != null)
            return lstChild.size();
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupDatas.get(groupPosition);
    }

    @Override
    public UserStepChildBean getChild(int groupPosition, int childPosition) {
        List<UserStepChildBean> lstChild = getChildDataList(groupPosition);
        if (lstChild != null)
            return lstChild.get(childPosition);
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.item_group, null);
            holder.img = (ImageView) convertView.findViewById(R.id.indicator);
//            holder.green_line_bottom = convertView.findViewById(R.id.green_line_bottom);

            //在这儿找控件
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.step_medica = (TextView) convertView.findViewById(R.id.step_medica);
            holder.step_number = (TextView) convertView.findViewById(R.id.step_number).findViewById(R.id.number);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        holder.step_number.setText("" + (getGroupCount() - groupPosition));


        UserStepBean group = groupDatas.get(groupPosition);
//        if (groupPosition == groupDatas.size() - 1) {
//            holder.green_line_bottom.setVisibility(View.VISIBLE);
//        } else {
//            holder.green_line_bottom.setVisibility(View.GONE);
//        }

        String begStr = group.isBegTimeFirst() ? "更早" : DateTime.from(group.getCompareDateBeg() * 1000).toDateString();
        String endStr = group.isEndTimeLast() ? "至今" : DateTime.from(group.getCompareDateEnd() * 1000).toDateString();
        String time = begStr.concat(" 至 ").concat(endStr);
        holder.time.setText(time);

        if (group.isSpace()) {
            holder.step_medica.setText("未知阶段");
        } else {
            String medica = MapDataUtils.getAllStepName(group.getStepID());
            holder.step_medica.setText(medica);
        }


//        String beginTime = ("" +group.getCompareDateBeg()).concat("000");//加000 是因为后台给的是秒
//        String endTime = ("" + group.getEndTime()).concat("000");
//        String time = getDate(beginTime).concat("到").concat(getDate(endTime));
//        if (!TextUtils.isEmpty(medica)) {
//            holder.step_medica.setText(medica);
//        } else {
//            holder.step_medica.setText("空窗期");
//        }

        if (isExpanded) {
            holder.img.setBackgroundResource(R.mipmap.up);
        } else {
            holder.img.setBackgroundResource(R.mipmap.down);
        }

        return convertView;
    }

//    private static final int QUOTA = 1;
//    private static final int SYMPTOM = 2;

//    @Override
//    public int getChildType(int groupPosition, int childPosition) {
//        UserStepChildBean bean = getChild(groupPosition, childPosition);
//        if (bean.isQuota()) {
//            return QUOTA;
//        }
//        return SYMPTOM;
//    }

    class GroupViewHolder {
        //        View green_line_bottom;
        ImageView img;
        TextView step_medica;//阶段的药物
        TextView time;//阶段的时间
        TextView step_number;
    }


//    @Override
//    public int getChildTypeCount() {
//        return 2;
//    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ZhibiaoViewHolder zhibiaoHolder;
        SymptomViewHolder symptomHolder;
        final UserStepChildBean temp = groupDatas.get(groupPosition).getChildList().get(childPosition);
        String stepUid = groupDatas.get(groupPosition).getStepUID();
        temp.setStepUid(stepUid);
        int type = getChildType(groupPosition, childPosition);
        if (temp.isQuota()) {//指标类型
//view1
//            if (convertView == null) {
            zhibiaoHolder = new ZhibiaoViewHolder();
            convertView = inflater.inflate(R.layout.include_item_child_zhibiao, null);
            zhibiaoHolder.red_line1 = (TextView) convertView.findViewById(R.id.red_line1);
            zhibiaoHolder.red_line2 = (TextView) convertView.findViewById(R.id.red_line2);

            zhibiaoHolder.add_content = (LinearLayout) convertView.findViewById(R.id.add_content);
            zhibiaoHolder.zhibiao_time = (TextView) convertView.findViewById(R.id.time);
            zhibiaoHolder.zhibiao_cea = (TextView) convertView.findViewById(R.id.cea_record);
            zhibiaoHolder.main_length = (TextView) convertView.findViewById(R.id.main_length);
            zhibiaoHolder.transfer_pos = (TextView) convertView.findViewById(R.id.transfer_pos);
            zhibiaoHolder.zhibiao_edit = (ImageView) convertView.findViewById(R.id.update2);
            zhibiaoHolder.zhibiao_delete = (ImageView) convertView.findViewById(R.id.delete2);

//                convertView.setTag(zhibiaoHolder);
//            } else {
//                zhibiaoHolder = (ZhibiaoViewHolder) convertView.getTag();
//            }

            zhibiaoHolder.zhibiao_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.edit(groupPosition, childPosition);
                    }
                }
            });


            zhibiaoHolder.zhibiao_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.delete(groupPosition, childPosition);
                    }
                }
            });


            //初始化
            String time = "" + temp.getRecordTime();
            String cea = temp.getCEA();
            String transferPos = temp.getTransferPos();//转移
            String MasterCancerLength = temp.getMasterCancerLength();//主肿瘤长度
            String MasterCancerWidth = temp.getMasterCancerWidth();//宽度
            String showtime = DataUtils.getDate(time);
            if (!TextUtils.isEmpty(showtime)) {
                zhibiaoHolder.zhibiao_time.setText(showtime);
            }
            if (!TextUtils.isEmpty(cea)) {//cea
                zhibiaoHolder.zhibiao_cea.setText(cea);
            } else {
                zhibiaoHolder.zhibiao_cea.setText(R.string.no_datas);
            }
            if (!TextUtils.isEmpty(transferPos)) {//transpos
                transferPos = MapDataUtils.getTransPosValues(transferPos);
                zhibiaoHolder.transfer_pos.setText(transferPos);
            } else {
                zhibiaoHolder.transfer_pos.setText(R.string.no_datas);
            }

            if (!TextUtils.isEmpty(MasterCancerLength) && !TextUtils.isEmpty(MasterCancerWidth)) {//transpos
                zhibiaoHolder.main_length.setText(MasterCancerLength + "*" + MasterCancerWidth);
            } else {
                zhibiaoHolder.main_length.setText(R.string.no_datas);
            }

            List<SlaverCancer> slavercancer = temp.getSlaverCancerNum();
            if (slavercancer != null && slavercancer.size() > 0) {
                for (int i = 0, j = slavercancer.size(); i < j; i++) {
                    View view = inflater.inflate(R.layout.step_detail_layout, null);
                    zhibiaoHolder.add_content.addView(view, i + 5);//5是他所在的位置
                    TextView fu_length = (TextView) view.findViewById(R.id.fu_length);
                    TextView name = (TextView) view.findViewById(R.id.name);
                    SlaverCancer slc = slavercancer.get(i);
                    fu_length.setText(slc.getSlaveLen() + "*" + slc.getSlaveWidth());
                    name.setText(slc.getSlaveName());
                }
            }
            if (childPosition == 0) {
                zhibiaoHolder.red_line1.setVisibility(View.INVISIBLE);
            } else {
                zhibiaoHolder.red_line1.setVisibility(View.VISIBLE);
            }

            if (childPosition == (getChildrenCount(groupPosition) - 1)) {// 最后一个项目
                zhibiaoHolder.red_line2.setVisibility(View.GONE);
            } else {
                zhibiaoHolder.red_line2.setVisibility(View.VISIBLE);
            }

        } else {
//view2
//            if (convertView == null) {//这个是症状
            symptomHolder = new SymptomViewHolder();
            convertView = inflater.inflate(R.layout.include_item_child_symptom, null);

            symptomHolder.red_line1 = (TextView) convertView.findViewById(R.id.red_line1);
            symptomHolder.red_line2 = (TextView) convertView.findViewById(R.id.red_line2);
            symptomHolder.symptom_time = (TextView) convertView.findViewById(R.id.symptom_time);
            symptomHolder.symptom_desc = (TextView) convertView.findViewById(R.id.symptom);
            symptomHolder.symptom_tips = (TextView) convertView.findViewById(R.id.gene_decr);
            symptomHolder.symptom_edit = (ImageView) convertView.findViewById(R.id.update);
            symptomHolder.symptom_delete = (ImageView) convertView.findViewById(R.id.delete);

//                convertView.setTag(symptomHolder);
//            } else {
//                symptomHolder = (SymptomViewHolder) convertView.getTag();
//            }
            if (childPosition == 0) {
                symptomHolder.red_line1.setVisibility(View.INVISIBLE);
            } else {
                symptomHolder.red_line1.setVisibility(View.VISIBLE);
            }

            if (childPosition == (getChildrenCount(groupPosition) - 1)) {// 最后一个项目
                symptomHolder.red_line2.setVisibility(View.GONE);
            } else {
                symptomHolder.red_line2.setVisibility(View.VISIBLE);
            }

            symptomHolder.symptom_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.edit(groupPosition, childPosition);
                    }
                }
            });

            symptomHolder.symptom_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.delete(groupPosition, childPosition);
                    }
                }
            });

            //初始化
            String performid = temp.getPerformStep();
            String remark = temp.getRemark();
            String time = "" + temp.getRecordTime();
            time = DataUtils.getDate(time);
            performid = MapDataUtils.getPerfromValues(performid);
            if (!TextUtils.isEmpty(performid)) {
                symptomHolder.symptom_desc.setText(performid);
            }

            if (!TextUtils.isEmpty(time)) {
                symptomHolder.symptom_time.setText(time);
            }

            if (!TextUtils.isEmpty(remark)) {
                symptomHolder.symptom_tips.setText(remark);
            }

        }


        return convertView;
    }

    class ZhibiaoViewHolder {
        TextView red_line1;
        TextView red_line2;
        LinearLayout add_content;
        TextView zhibiao_time;
        TextView zhibiao_cea;//cea
        TextView main_length;//主肿瘤
        TextView transfer_pos;//转移情况
        ImageView zhibiao_edit;
        ImageView zhibiao_delete;
    }

    class SymptomViewHolder {
        TextView red_line1;
        TextView red_line2;
        TextView symptom_time;
        TextView symptom_desc;
        TextView symptom_tips;
        ImageView symptom_edit;
        ImageView symptom_delete;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    public void update(List<UserStepBean> groupDatas, List<String> childDatas) {
        if (groupDatas != null && groupDatas.size() > 0) {
            this.groupDatas = groupDatas;
        }
        if (childDatas != null && childDatas.size() > 0) {
            this.childDatas = childDatas;
        }
        notifyDataSetChanged();
    }

    /**
     * 这样处理是后台给的数据是秒
     *
     * @param date
     * @return
     */
    private String getDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        long dates = Long.parseLong(date);
        return format.format(new Date(dates));
    }
}
