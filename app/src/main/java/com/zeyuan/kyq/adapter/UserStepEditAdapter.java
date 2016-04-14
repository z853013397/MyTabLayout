package com.zeyuan.kyq.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.fragment.dialog.CureTypeDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserStepHelper;
import com.zeyuan.kyq.view.EditStepActivity;
import com.zeyuan.kyq.view.GuideActivity;
import com.zeyuan.kyq.view.MainActivity;
import com.zeyuan.kyq.view.ViewDataListener;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2015/10/8.
 */
public class UserStepEditAdapter extends BaseListAdapter<UserStepEditAdapter.ViewHolder, UserStepBean> {

    public UserStepEditAdapter(Activity activity, int itemLayoutId) {
        super(activity, itemLayoutId);
    }
    public String cancerId;
    public void setCancerId (String cancerId) {
        this.cancerId = cancerId;
    }
    public class ViewHolder {
        View v;
        TextView step_number;
        TextView start_time;
        TextView end_time;//
        TextView step_medica;//药物
        RadioGroup rg;//是否有效
        RadioButton no_effective;
        RadioButton effective;
    }

    @Override
    protected ViewHolder getHolder(View v) {
        ViewHolder holder = new ViewHolder();
        {
            holder.v = v;
            holder.step_number = (TextView) v.findViewById(R.id.step_number).findViewById(R.id.number);
            holder.step_medica = (TextView) v.findViewById(R.id.step_medica);
            holder.start_time = (TextView) v.findViewById(R.id.start_time);
            holder.end_time = (TextView) v.findViewById(R.id.end_time);
            holder.rg = (RadioGroup) v.findViewById(R.id.rg);
            holder.no_effective = (RadioButton) v.findViewById(R.id.no_effective);
            holder.effective = (RadioButton) v.findViewById(R.id.effective);
        }
        return holder;
    }

    //bi

    public UserStepBean mEditorBean;

    @Override
    protected void setHolderView(int position, ViewHolder holder, UserStepBean userStepBean) {

        //设置第几个阶段
        userStepBean.$number((getCount() - position));
//        holder.step_number.setText("第" + userStepBean.$number() + "阶段");
        holder.step_number.setText(""+ userStepBean.$number());



        //设置开始时间
        if (!userStepBean.isBegTimeFirst()) {
            if (userStepBean.isBegTimeNull()) {
                holder.start_time.setText("");
            } else {
                holder.start_time.setText(DateTime.from(userStepBean.getCompareDateBeg() * 1000).toDateString());
            }
        } else {
            holder.start_time.setText("更早");
        }
        //设置结束时间
        if (!userStepBean.isEndTimeLast()) {
            if (userStepBean.isEndTimeNull()) {
                holder.end_time.setText("");
            } else {
                holder.end_time.setText(DateTime.from(Long.valueOf(userStepBean.getCompareDateEnd()) * 1000).toDateString());
            }
        } else {
            holder.end_time.setText("至今");
        }
        //设置药物
        final String stepID = userStepBean.getStepID();
        if (!TextUtils.isEmpty(stepID)) {
            holder.step_medica.setText(DataUtils.loadStringToShowString(stepID));
        } else {
            holder.step_medica.setText("");

        }

        //是否有效
        if (userStepBean.getIsMedicineValid() == 0) {
            holder.rg.check(R.id.no_effective);
        } else {
            holder.rg.check(R.id.effective);
        }

        holder.v.setOnClickListener(new OnItemButtonClickListener("", position));
        BaseListAdapter.OnItemButtonLongClickListener longClickListener = new BaseListAdapter.OnItemButtonLongClickListener("", position);
        holder.v.setOnLongClickListener(longClickListener);
        holder.step_medica.setOnLongClickListener(longClickListener);
        holder.start_time.setOnLongClickListener(longClickListener);
        holder.end_time.setOnLongClickListener(longClickListener);
        holder.rg.setOnLongClickListener(longClickListener);
        holder.step_medica.setOnLongClickListener(longClickListener);


        holder.step_medica.setOnClickListener(new OnItemButtonClickListener("", position));//药物的点击事件
        holder.start_time.setOnClickListener(new OnItemButtonClickListener("", position));//开始时间的点击事件
        holder.end_time.setOnClickListener(new OnItemButtonClickListener("", position));//结束时间的点击事件

        holder.effective.setOnClickListener(new OnItemButtonClickListener("", position));
        holder.no_effective.setOnClickListener(new OnItemButtonClickListener("", position));

        //系统空窗期
        if (userStepBean.isSpace()) {
            holder.step_medica.setText("未知阶段");
            holder.rg.setEnabled(false);
            holder.no_effective.setEnabled(false);
            holder.effective.setEnabled(false);

            holder.step_medica.setEnabled(false);
            holder.start_time.setEnabled(false);
            holder.end_time.setEnabled(false);
        } else {

            if ((mEditorBean == null || userStepBean.$isEditor())) {
                holder.rg.setEnabled(true);
                holder.step_medica.setEnabled(true);
                holder.start_time.setEnabled(true);
                holder.end_time.setEnabled(true);
                holder.no_effective.setEnabled(true);
                holder.effective.setEnabled(true);

            } else {
                holder.rg.setEnabled(false);
                holder.no_effective.setEnabled(false);
                holder.effective.setEnabled(false);

                holder.step_medica.setEnabled(false);
                holder.start_time.setEnabled(false);
                holder.end_time.setEnabled(false);
            }
        }
    }

    private static final String dateFormart = "yyyy-MM-dd";

    @Override
    protected void onItemButtonClick(final int position, final View v) {
        final UserStepBean bean = getItemX(position);
        switch (v.getId()) {
            case R.id.step_medica: {
                CureTypeDialog dialog = new CureTypeDialog();
                if (!TextUtils.isEmpty(cancerId)) {
                    dialog.setCancerID(cancerId);
                }
                dialog.setDrugsNameListener(new DialogFragmentListener() {
                    @Override
                    public void getDataFromDialog(DialogFragment dialog, String data, int positions) {
                        String showData = DataUtils.loadStringToShowString(data);
                        bean.$isEditor(true);
                        mEditorBean = bean;
                        bean.setStepID(data);
                        ((TextView) v).setText(showData);
                        notifyDataSetInvalidated();
                    }
                });
                FragmentManager fm = ((EditStepActivity) getContext()).getFragmentManager();//warnning 注意强转
                dialog.show(fm, "medica");
            }
            break;
            case R.id.start_time: {
                if (bean.isBegTimeFirst()) {
//                    Toast.makeText(getContext(), "最新阶段不能修改结束日期哦～～", Toast.LENGTH_SHORT).show();
                } else {
                    final TextView timetext = (TextView) v;
                    final String datetime = timetext.getText().toString();
//                    if (TextUtils.isEmpty(datetime)) {
//                        Toast.makeText(getContext(), "请选择开始日期", Toast.LENGTH_SHORT).show();
//                    } else {
                    DateTime dateTime = null;
                    if (bean.isBegTimeNull()) {
                        dateTime = DateTime.now();
                    } else {
//                        dateTime = DateTime.from(bean.getCompareDateEnd() * 1000);
                        dateTime = DateTime.from(bean.getCompareDateBeg() * 1000);
                    }
//                        final DateTime dateTimeTxt = DateTime.from(datetime, dateFormart);
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            DateTime dateTimeNew = DateTime.from(year, monthOfYear, dayOfMonth);
                            if (!datetime.equals(dateTimeNew.toDateString())) {
                                bean.$isEditor(true);
                                mEditorBean = bean;
                                bean.setBeginTime(dateTimeNew.toTimeMillis() / 1000);
                                timetext.setText(dateTimeNew.toString(dateFormart));

//                              判断当前如果时至今阶段的话，并且有前一个阶段，则前一个阶段的结束时间跟着变
                                if (TextUtils.isEmpty(bean.getStepUID())) {
                                    if (bean.isEndTimeLast()) {
                                        if (bean.$number() > 1) {
                                            UserStepBean beanNext = getItemX(position + 1);
                                            if (!beanNext.isSpace()) {
                                                if (bean.getCompareDateBeg() > beanNext.getCompareDateBeg()) {
                                                    beanNext.setEndTime(bean.getCompareDateBeg() - 1);
                                                }
                                            }
                                        }
                                    }
                                }
                                notifyDataSetInvalidated();
                            }
                        }
                    }, dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()).show();
//                    }
                }
            }
            break;
            case R.id.end_time: {
                if (bean.isEndTimeLast()) {
                    Toast.makeText(getContext(), "最新阶段不能修改结束日期哦～～", Toast.LENGTH_SHORT).show();
                } else {
                    final TextView timetext = (TextView) v;
                    final String datetime = timetext.getText().toString();
//                    if (TextUtils.isEmpty(datetime)) {
//                        Toast.makeText(getContext(), "请选择结束日期", Toast.LENGTH_SHORT).show();
//                    } else {
//                    final DateTime dateTimeTxt = DateTime.from(datetime, dateFormart);
                    DateTime dateTime = null;
                    if (bean.isEndTimeNull()) {
                        dateTime = DateTime.now();
                    } else {
                        dateTime = DateTime.from(bean.getCompareDateEnd() * 1000);
                    }
                    new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            DateTime dateTimeNew = DateTime.from(year, monthOfYear, dayOfMonth);
                            if (!datetime.equals(dateTimeNew.toDateString())) {
                                bean.$isEditor(true);
                                mEditorBean = bean;
                                bean.setEndTime(dateTimeNew.addDay(1).toTimeMillis() / 1000 - 1);//后一天－1s
                                timetext.setText(dateTimeNew.toString(dateFormart));
                                notifyDataSetInvalidated();
                            }
                        }
                    }, dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()).show();
//                    }
                }
            }
            break;
            case R.id.no_effective: {
                RadioButton radio = (RadioButton) v;
                bean.$isEditor(true);
                mEditorBean = bean;
                bean.setIsMedicineValid(radio.isChecked() ? 0 : 1);
                notifyDataSetInvalidated();
            }
            break;
            case R.id.effective: {
                RadioButton radio = (RadioButton) v;
                bean.$isEditor(true);
                mEditorBean = bean;
                bean.setIsMedicineValid(radio.isChecked() ? 1 : 0);
                notifyDataSetInvalidated();
            }
            break;
//            case R.id.rg: {
//                Toast.makeText(getContext(), " RadioGroup rg = (RadioGroup) v;", Toast.LENGTH_SHORT).show();
//                RadioGroup rg = (RadioGroup) v;
//                bean.$isEditor(true);
//                mEditorBean = bean;
//                bean.setIsMedicineValid((rg.getCheckedRadioButtonId() == R.id.effective) ? 1 : 0);
//                notifyDataSetInvalidated();
//            }
//            break;
            default:
                if (mEditorBean != null && !bean.$isEditor()) {
//                    if (TextUtils.isEmpty(mEditorBean.getStepUID())) {
//                        Toast.makeText(getContext(), "如需新增阶段请先保存", Toast.LENGTH_SHORT).show();
//                    } else {
                    Toast.makeText(getContext(), "如需修改其他阶段请先保存", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
        }
    }

    @Override
    protected boolean onItemButtonLongClick(int position, View v) {
        final UserStepBean bean = getItemX(position);
        if ((position == 0) && bean.isEndTimeLast()) {
            Toast.makeText(getContext(), "当前阶段无法删除", Toast.LENGTH_SHORT).show();
        } else if (mEditorBean != null && !bean.$isEditor()) {
            Toast.makeText(getContext(), "如需删除其他阶段请先保存", Toast.LENGTH_SHORT).show();
        } else {
            //弹出确认对话框
//            new AlertView("确定删除？", "删除阶段会同时删除其包含的症状记录及指标记录", null, new String[]{"取消", "删除"}, null, getContext(), AlertView.Style.Alert, new OnItemClickListener() {
//                @Override
//                public void onItemClick(Object o, int position) {
//                    if (position == 1) {
//                        UserStepHelper.reqUserStepDel((ViewDataListener) getContext(), bean);
//                    }
//                }
//            }).setCancelable(true).show();


            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("确定删除？");
            builder.setMessage("删除阶段会同时删除其包含的症状记录及指标记录");
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    UserStepHelper.reqUserStepDel((ViewDataListener) getContext(), bean);
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();




        }
        return true;
    }

//    public abstract class RadioGroupOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
//
//        private int position;
//
//        public RadioGroupOnCheckedChangeListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            onCheckedChanged(group, checkedId, position);
//        }
//
//        public abstract void onCheckedChanged(RadioGroup group, int checkedId, int position);
//    }
}
//        holder.rg.setOnCheckedChangeListener(new RadioGroupOnCheckedChangeListener(position) {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId, int position) {
//                UserStepBean bean = getItemX(position);
//                bean.$isEditor(true);
//                mEditorBean = bean;
//                bean.setIsMedicineValid((group.getCheckedRadioButtonId() == R.id.effective) ? 1 : 0);
//                notifyDataSetInvalidated();
//            }
//        });
