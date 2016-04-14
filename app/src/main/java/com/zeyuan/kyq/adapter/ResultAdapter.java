package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;

import java.util.List;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-23
 * Time: 16:40
 * FIXME
 */


public class ResultAdapter extends BaseExpandableListAdapter {
    private static final int type1 = 0;
    private static final int type2 = 1;//可以展开
    private static final String TAG = "ResultAdapter";
    //    private CheckBoxCheckListener checkBoxCheckListener;
    private int selectChildPosition = -1;
    private int selectGroupPosition = -1;

    public interface CheckBoxCheckListener {
        void checkItem(BaseExpandableListAdapter adapter, int groupPosition, int childPosition, String item);
    }


    private CheckBoxCheckListener mCheckBoxCheckListener;


    public void setCheckBoxCheckListener(CheckBoxCheckListener checkBoxCheckListener) {
        mCheckBoxCheckListener = checkBoxCheckListener;
    }

    private List<CancerResuletBean.StepEntity> datas;
    private Context context;
    private LayoutInflater inflater;
    private boolean isCancer;
    public ResultAdapter(Context context, List<CancerResuletBean.StepEntity> datas,boolean isCancer) {
        this.context = context;
        this.datas = datas;
        this.isCancer = isCancer;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        CancerResuletBean.StepEntity item = datas.get(groupPosition);
        List list = item.getCombineStepid();
        if (list != null && list.size() > 0) {
            return list.size();
        } else {
            return 0;
        }
    }

    @Override
    public CancerResuletBean.StepEntity getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public CancerResuletBean.StepEntity.CombineStepidEntity getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getCombineStepid().get(childPosition);
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        int type = getGroupType(groupPosition);
        if (type == type2) {//这个是可折叠的项
//            if (convertView == null) {
            convertView = inflater.inflate(R.layout.cancer_result_item, null);
//            }
            TextView tv = (TextView) convertView.findViewById(R.id.content);
            TextView number = (TextView) convertView.findViewById(R.id.number);
            number.setText("方案" + (groupPosition + 1));
//            tv.setText(datas.get(groupPosition).getAlias());
            String alias = datas.get(groupPosition).getAlias();
            if (TextUtils.isEmpty(alias)) {
                String stepid = datas.get(groupPosition).getCombineid();
                stepid = MapDataUtils.getOtherStepValues(stepid);
                tv.setText(stepid);
            } else {
                tv.setText(alias);
            }
        }
        if (type == type1) {
//            if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_lianhe, null);
//            }
            TextView number = (TextView) convertView.findViewById(R.id.number);
            number.setText("方案" + (1 + groupPosition));
            TextView tv = (TextView) convertView.findViewById(R.id.content);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
            if(!isCancer) {
                cb.setVisibility(View.GONE);
            }
            String alias = datas.get(groupPosition).getAlias();
            if (TextUtils.isEmpty(alias)) {
                String stepid = datas.get(groupPosition).getCombineid();
                String temp = MapDataUtils.getAllStepName(stepid);
                if (TextUtils.isEmpty(temp)) {
                    tv.setText(MapDataUtils.getOtherStepValues(stepid));
                }else {
                    tv.setText(temp);
                }
            } else {
                tv.setText(alias);
            }
            if (selectGroupPosition == groupPosition) {
                cb.setChecked(true);
            }
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectGroupPosition = groupPosition;
                        selectChildPosition = -1;
                        String stepid = datas.get(groupPosition).getStepid();
                        if(TextUtils.isEmpty(stepid)) {
                            mCheckBoxCheckListener.checkItem(ResultAdapter.this, groupPosition, 0, datas.get(groupPosition).getCombineid());
                        }else {
                            mCheckBoxCheckListener.checkItem(ResultAdapter.this, groupPosition, 0, stepid);

                        }
                    } else {
                        selectGroupPosition = -1;
                        mCheckBoxCheckListener.checkItem(ResultAdapter.this, groupPosition, 0, "");
                    }
                    notifyDataSetChanged();
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.layout_lianhe, null);
//        convertView.setBackgroundDrawable(null);
        GradientDrawable gd = new GradientDrawable();//创建drawable
        gd.setColor(Color.parseColor("#F0F0F0"));
        convertView.findViewById(R.id.ll).setBackgroundDrawable(gd);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.cb);
        TextView tv = (TextView) convertView.findViewById(R.id.content);
        if(!isCancer) {
            cb.setVisibility(View.GONE);
        }
        TextView num = (TextView) convertView.findViewById(R.id.number);
        num.setVisibility(View.GONE);
        if (selectChildPosition == childPosition) {
            cb.setChecked(true);
        }
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectChildPosition = childPosition;
                    selectGroupPosition = -1;
                    mCheckBoxCheckListener.checkItem(ResultAdapter.this, groupPosition, childPosition, datas.get(groupPosition).getCombineStepid().get(childPosition).getStepid());
                } else {
                    mCheckBoxCheckListener.checkItem(ResultAdapter.this, groupPosition, 0, "");
                    selectChildPosition = -1;
                }
                notifyDataSetChanged();
            }
        });
        String id = datas.get(groupPosition).getCombineStepid().get(childPosition).getStepid();
        String temp  = MapDataUtils.getAllStepName(id);
        LogUtil.i(TAG,"第一次" + temp);

        if (TextUtils.isEmpty(temp)) {
            LogUtil.i(TAG,"第二次" + temp);
            temp = MapDataUtils.getOtherStepValues(id);
        }
        tv.setText(temp);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public int getGroupType(int groupPosition) {
        CancerResuletBean.StepEntity item = datas.get(groupPosition);
        List list = item.getCombineStepid();
        if (list != null && list.size() > 0) {
            return type2;
        } else {
            return type1;
        }
    }

    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    public void update(List datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setSelectChildPosition() {
        selectGroupPosition = -1;
        selectChildPosition = -1;
        notifyDataSetChanged();
    }

}