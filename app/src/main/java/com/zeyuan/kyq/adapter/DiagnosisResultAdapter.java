package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.FindSymtomBean;

import java.util.List;

/**
 * Created by Administrator on 2015/10/6.
 */
public class DiagnosisResultAdapter extends BaseAdapter {
    private List<FindSymtomBean.CommPolicyEntity> CommPolicy;
    private Context context;
    private LayoutInflater inflater;
    public DiagnosisResultAdapter(Context context,List<FindSymtomBean.CommPolicyEntity> CommPolicy) {
        this.context = context;
        this.CommPolicy = CommPolicy;
        inflater= LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return CommPolicy.size();
    }

    @Override
    public String getItem(int position) {
        return CommPolicy.get(position).getCommPolicyName();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_diagnosis_result, null);
            holder.number = (TextView) view.findViewById(R.id.number);
            holder.diagnosis_result = (TextView) view.findViewById(R.id.diagnosis_result);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }

        holder.number.setText(String.valueOf(position + 1));
        holder.diagnosis_result.setText(CommPolicy.get(position).getCommPolicyName());



        return view;
    }
    static class ViewHolder {
        TextView number;
        TextView diagnosis_result;
    }
}
