//package com.zeyuan.kyq.adapter;
//
//import android.app.DatePickerDialog;
//import android.app.DialogFragment;
//import android.app.FragmentManager;
//import android.content.Context;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.DatePicker;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//
//import com.zeyuan.kyq.R;
//import com.zeyuan.kyq.bean.AllStepBean;
//import com.zeyuan.kyq.fragment.dialog.ChooseMedicaDialog;
//import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
//import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
//import com.zeyuan.kyq.utils.Contants;
//import com.zeyuan.kyq.utils.DataUtils;
//import com.zeyuan.kyq.utils.LogUtil;
//import com.zeyuan.kyq.utils.SharePrefUtil;
//import com.zeyuan.kyq.utils.UserinfoData;
//import com.zeyuan.kyq.view.EditStepActivity;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by Administrator on 2015/10/8.
// */
//public class StepAdapter extends BaseAdapter {
//    private Map<String, String> dataMap;//根据修改传给后台的map
//    private int startTime = -1;
//    private int endTime = -1;
//
//
//    private List<AllStepBean.StepNumEntity> data;
//    private Context context;
//    private LayoutInflater inflater;
//
//    public StepAdapter(Context context, List<AllStepBean.StepNumEntity> data) {
//        this.context = context;
//        dataMap = new HashMap<>();
//        inflater = LayoutInflater.from(context);
//        this.data = data;
//    }
//
//    @Override
//    public int getCount() {
//        return data.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    //    private boolean flag = true;//这个tur表示是原来的阶段 false 是新增阶段
//    private int count = 0;//代表修改的个数
//    private boolean isChange = false;//表示是否增加了count的值 true 便是是 false 表示没有增加
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
////       boolean flags = true;
////        boolean isChange;
//        final boolean flag;//这个ture表示是原来的阶段上修改 false是新增阶段
//        final ViewHolder holder;
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.item_steps, null);
//            holder.step_medica = (TextView) convertView.findViewById(R.id.step_medica);
//            holder.start_time = (TextView) convertView.findViewById(R.id.start_time);
//            holder.end_time = (TextView) convertView.findViewById(R.id.end_time);
//            holder.rg = (RadioGroup) convertView.findViewById(R.id.rg);
//            holder.step_number = (TextView) convertView.findViewById(R.id.step_number);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        String stepUid = data.get(position).getStepUID();//这个是stepuid
//        if (TextUtils.isEmpty(stepUid)) {//说明这个是新增阶段
//            flag = false;
//        } else {
//            flag = true;
//        }
//
//        LogUtil.i("MainActivity", "是否是修改阶段" + flag);
//
//        /**
//         * 设置开始时间
//         */
//        final String begintime = data.get(position).getBeginTime();
//        if (!TextUtils.isEmpty(begintime)) {
//            holder.start_time.setText(getDate(begintime.concat("000")));
//        }
//        /**
//         * 设置结束时间
//         */
//        final String endtime = data.get(position).getEndTime();
//        if (!TextUtils.isEmpty(endtime)) {
//            holder.end_time.setText(getDate(endtime.concat("000")));
//        }
//        /**
//         * 设置药物
//         */
//        final String stepID = data.get(position).getStepID();
//        if (!TextUtils.isEmpty(stepID)) {
//            holder.step_medica.setText(DataUtils.loadStringToShowString(stepID));
//        } else {
//            holder.step_medica.setText("请选择药物");
//        }
//        /**
//         * 设置第几个阶段
//         */
//        final String stepUID = data.get(position).getStepUID();
//        if (!TextUtils.isEmpty(stepUID)) {
//            holder.step_number.setText("第" + stepUID + "阶段");
//        }
//        final String IsMedicineValid = data.get(position).getIsMedicineValid();
//        if ("0".equals(IsMedicineValid)) {
//            holder.rg.check(R.id.no_effective);
//        } else {
//            holder.rg.check(R.id.effective);
//        }
//        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.no_effective: {//无效
//                        if (IsMedicineValid == null || "1".equals(IsMedicineValid)) {//说明有修改
//                            dataMap.put(Contants.IsMedicineValid + position, Contants.noEffect);
//                            if (!dataMap.containsKey(Contants.StepUID + position)) {
//                                dataMap.put(Contants.StepUID + position, stepUID);
//                                count++;
//                            }
//                        }
//                        break;
//                    }
//                    case R.id.effective: {//有效
//                        if (IsMedicineValid == null || "0".equals(IsMedicineValid)) {//说明有修改
//                            dataMap.put(Contants.IsMedicineValid + position, Contants.hasEffect);
//                            if (!dataMap.containsKey(Contants.StepUID + position)) {
//                                dataMap.put(Contants.StepUID + position, stepUID);
//                                count++;
//                            }
//                        }
//                        break;
//                    }
//                }
//            }
//        });
//
//
//        /**
//         * 药物的点击事件
//         */
//        holder.step_medica.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                CureTypeDialog dialog = new CureTypeDialog();
////                ChooseMedicaDialog dialog = ChooseMedicaDialog.newInstance(ChooseMedicaDialog.TYPE_MEDICA);
//                dialog.setDrugsNameListener(new DialogFragmentListener() {
//                    @Override
//                    public void getDataFromDialog(DialogFragment dialog, String data, int positions) {
//                        String showData = DataUtils.loadStringToShowString(data);
//                        holder.step_medica.setText(showData);
//                        if (stepID == null || !stepID.equals(data)) {//说明修改了药物
//                            SharePrefUtil.saveString(context,Contants.StepID,data);
//                            dataMap.put(Contants.StepID + position, data);
////                            if (flag) {//false 为新增阶段
//                            if (!dataMap.containsKey(Contants.StepUID + position)) {
//                                dataMap.put(Contants.StepUID + position, stepUID);
//                                count++;
//                            }
//                        }
//                    }
//                });
//                FragmentManager fm = ((EditStepActivity) context).getFragmentManager();//warnning 注意强转
//                dialog.show(fm, "medica");
//            }
//        });
//
//
//        /**
//         * 开始时间的点击事件
//         */
//        holder.start_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        monthOfYear += 1;
////                        startTime = year * 10000 + monthOfYear * 100 + dayOfMonth;
//                        String startTime = new StringBuilder().append(year).append("-")
//                                .append((monthOfYear) < 10 ? "0" + (monthOfYear) : (monthOfYear))
//                                .append("-")
//                                .append((dayOfMonth < 10) ? "0" + dayOfMonth : dayOfMonth).toString();
//                        holder.start_time.setText(startTime);
//                        String loadingTime = DataUtils.showTimeToLoadTime(startTime);
//
//
//                        if (begintime == null || !begintime.equals(loadingTime)) {//说明修改了开始时间
//                            dataMap.put(Contants.StartTime + position, loadingTime);
////                            if (flag) {//false 为新增阶段
//                            if (!dataMap.containsKey(Contants.StepUID + position)) {
//                                dataMap.put(Contants.StepUID + position, stepUID);
//                                count++;
////                                }
//                            }
//                        }
//                    }
//                }, 2010, 04, 05).show();
//            }
//        });
////
//        /**
//         * 结束时间的点击事件
//         */
//        holder.end_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                        monthOfYear += 1;//特么我也不知道 为什么要加1 才正确
//                        endTime = year * 10000 + monthOfYear * 100 + dayOfMonth;
//
//
//                        String endTime = new StringBuilder().append(year).append("-")
//                                .append((monthOfYear) < 10 ? "0" + (monthOfYear) : (monthOfYear))
//                                .append("-")
//                                .append((dayOfMonth < 10) ? "0" + (dayOfMonth) : (dayOfMonth)).toString();
//                        holder.end_time.setText(endTime);
//                        String loadingEndTime = DataUtils.showTimeToLoadTime(endTime);
//                        if (endtime == null || !endtime.equals(loadingEndTime)) {//说明修改了结束时间
//                            dataMap.put(Contants.EndTime + position, loadingEndTime);
////                            if (flag) {//false 为新增阶段
//                            if (!dataMap.containsKey(Contants.StepUID + position)) {
//                                dataMap.put(Contants.StepUID + position, stepUID);
//                                count++;
////                                }
//                            }
////                            if (!isChange) {
////                                count++;
////                                isChange = true;
////                            }
//                        }
//
//                    }
//                }, 2010, 4, 5).show();
//            }
//        });
//
////        if ("0".equals(IsMedicineValid)) {
////            holder.rg.check(R.id.no_effective);
////        } else {
////            holder.rg.check(R.id.effective);
////        }
////        /**
////         * 此处不加判断的原因是触发他 值一定发生了改变
////         */
////        holder.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
////            @Override
////            public void onCheckedChanged(RadioGroup group, int checkedId) {
////                switch (checkedId) {
////                    case R.id.no_effective://无效
////                        dataMap.put(Contants.IsMedicineValid + position, Contants.noEffect);
////                        /**
////                         * 后台需求
////                         */
//////                        if (!dataMap.containsKey(Contants.EndTime + position))
//////                            dataMap.put(Contants.EndTime + position, endtime);
//////
//////                        if (!dataMap.containsKey(Contants.StepUnionID + position))
//////                            dataMap.put(Contants.StepUnionID + position, unionID);
//////
//////                        if (!dataMap.containsKey(Contants.StartTime + position))
//////                            dataMap.put(Contants.StartTime + position, begintime);
////
//////                        if (!dataMap.containsKey(Contants.StepUID + position)) {
//////                            dataMap.put(Contants.StepUID + position, stepUID);
//////                            count++;
//////                        }
////                        break;
////                    case R.id.effective://有效
////                        dataMap.put(Contants.IsMedicineValid + position, Contants.hasEffect);
////
////                        /**
////                         * 后台需求
////                         */
//////                        if (!dataMap.containsKey(Contants.EndTime + position))
//////                            dataMap.put(Contants.EndTime + position, endtime);
//////                        if (!dataMap.containsKey(Contants.StepUnionID + position))
//////                            dataMap.put(Contants.StepUnionID + position, unionID);
//////
//////                        if (!dataMap.containsKey(Contants.StartTime + position))
//////                            dataMap.put(Contants.StartTime + position, begintime);
////
////
//////                        if (!dataMap.containsKey(Contants.StepUID + position)) {
//////                            dataMap.put(Contants.StepUID + position, stepUID);
//////                            count++;
//////                        }
////
////                        break;
////                }
////            }
////        });
//
//
//        return convertView;
//    }
//
//    private boolean compareDate() {
//        if (startTime == -1 || endTime == -1) {
//            return true;
//        }
//
//
//        if (endTime - startTime > 0) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void update(List data) {
//        if (data != null && data.size() > 0) {
//            this.data = data;
//            notifyDataSetChanged();
//        }
//    }
//
//    public void addData(List data) {
//        if (data != null && data.size() > 0) {
//            this.data = data;
////            count++;
//            notifyDataSetChanged();
//        }
//    }
//
//
//    public void clearMap() {
//        dataMap.clear();
//    }
//
//    class ViewHolder {
//        TextView start_time;
//        TextView end_time;//
//        TextView step_medica;//药物
//        RadioGroup rg;//是否有效
//        TextView step_number;
//    }
//
//
//    /**
//     * 这样处理是后台给的数据是秒
//     *
//     * @param date 后台给的数据加上“000”
//     * @return 返回一个需要的日期字符串
//     */
//    private String getDate(String date) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        long dates = Long.parseLong(date);
//        return format.format(new Date(dates));
//    }
//
//    public Map<String, String> getDataMap() {
//        dataMap.put(Contants.InfoID, UserinfoData.getInfoID(context));
//        if (count > 0) {
//            dataMap.put(Contants.num, String.valueOf(count));
//        }
//        Iterator<Map.Entry<String, String>> entries = dataMap.entrySet().iterator();
//        while (entries.hasNext()) {
//            Map.Entry<String, String> entry = entries.next();
//            if (TextUtils.isEmpty(entry.getValue())) {
//                entries.remove();
//            }
//        }
//
//
//        return dataMap;
//    }
//}
