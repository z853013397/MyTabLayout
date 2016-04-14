package com.zeyuan.kyq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.GlobalData;

import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class SymptomAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    List<String> data;

    public SymptomAdapter(Context context, List<String> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public String getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_symptom_textview, null);
            viewHolder = new ViewHolder();
            viewHolder.simptom = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String string = GlobalData.performValues.get(data.get(position));
        viewHolder.simptom.setText(string);
        return convertView;
    }

    class ViewHolder {
        TextView simptom;
    }

    public void update(List<String> data) {
        this.data = data;
        this.notifyDataSetChanged();
    }
}
