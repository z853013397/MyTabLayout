package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.CaseDetailBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.view.DrugActivity;
import com.zeyuan.kyq.view.EffectActivity;

import java.io.Serializable;
import java.util.List;


/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-15
 * Time: 11:04
 * FIXME
 */


public class MedicineNumAdapter extends BaseAdapter {
    List<CaseDetailBean.MedicineNumEntity> datas;
    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public MedicineNumAdapter(Context mContext, List<CaseDetailBean.MedicineNumEntity> datas) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CaseDetailBean.MedicineNumEntity getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holderCommon;
        if (convertView == null) {
            holderCommon = new ViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.layout_casedetails, null);
            holderCommon.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holderCommon.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
            holderCommon.effective = (TextView) convertView.findViewById(R.id.effective);
            holderCommon.useage = (TextView) convertView.findViewById(R.id.use_durgs);
            holderCommon.side_effect = (LinearLayout) convertView.findViewById(R.id.side_effect);//副作用
            holderCommon.ll_details = (LinearLayout) convertView.findViewById(R.id.ll_details);//详细说明

            convertView.setTag(holderCommon);
        } else {
            holderCommon = (ViewHolder) convertView.getTag();
        }

        final CaseDetailBean.MedicineNumEntity item = datas.get(position);

        if (position == datas.size() - 1) {
            convertView.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        } else {
            convertView.findViewById(R.id.below_detail_line).setVisibility(View.GONE);
        }

        holderCommon.ll_details.setOnClickListener(new View.OnClickListener() {//详细说明
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DrugActivity.class);
                intent.putExtra(Contants.Drug, item);
                mContext.startActivity(intent);
            }
        });
        holderCommon.useage.setText(item.getUsage());
        holderCommon.tv_title.setText(item.getCommonName() + "介绍");
        holderCommon.tv_desc.setText(item.getSuit());
        List<String> sideEf = item.getMaybeSideEffect();
        StringBuilder temp = new StringBuilder();
        if (sideEf.size() > 3) {
            for (int i = 0; i < 3; i++) {
                if (i == 0) {
                    temp = temp.append(GlobalData.performValues.get(sideEf.get(i)));
                    continue;
                }
                temp = temp.append("," + GlobalData.performValues.get(sideEf.get(i)));
            }
        } else {
            for (int i = 0; i < sideEf.size(); i++) {
                if (i == 0) {
                    temp = temp.append(GlobalData.performValues.get(sideEf.get(i)));
                    continue;
                }
                temp = temp.append("," + GlobalData.performValues.get(sideEf.get(i)));
            }
        }
        if (TextUtils.isEmpty(temp)) {
//            effective.setText(R.string.none);
//            side_effect.setClickable(false);
        } else {
            holderCommon.effective.setText(temp);
//        effective.setText(item.getMaybeSideEffect().toString());
            holderCommon.side_effect.setOnClickListener(new View.OnClickListener() {//副作用的点击
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EffectActivity.class).
                            putExtra(Contants.List, (Serializable) item.getMaybeSideEffect())
                            .putExtra(Contants.CureConfID, item.getCureConfID());
//                .putExtra(Contants.CureConfID,ZYApplication.cureId.get("458"));
                    mContext.startActivity(intent);
                }
            });
        }


//        String imageurl = datas.get(position).getThumbURL();
//        if(!TextUtils.isEmpty(imageurl)) {
//            Glide.with(mContext).load(imageurl).signature(new IntegerVersionSignature(1)).into(holderCommon.img);//显示图片做缓存
//        }
//
//        String title = datas.get(position).getTitle();
//        if(!TextUtils.isEmpty(title)) {
//            holderCommon.title.setText(title);
//        }
//        String watch = datas.get(position).get();
//        if(!TextUtils.isEmpty(watch)) {
//            holderCommon.watch.setText(watch);
//        }

        return convertView;
    }

//    public void update( List<CaseDetailBean.MedicineNumEntity> datas) {
//        this.datas = datas;
//        notifyDataSetChanged();
//    }


    class ViewHolder {
        TextView useage;
        TextView effective;
        TextView tv_desc;
        TextView tv_title;
        LinearLayout side_effect;
        LinearLayout ll_details;
    }
}
