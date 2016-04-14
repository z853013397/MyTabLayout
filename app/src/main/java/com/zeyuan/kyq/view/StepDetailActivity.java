package com.zeyuan.kyq.view;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.UserStepViewAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalUserStepData;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.http.bean.UserStepComparator;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserStepHelper;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 阶段详情
 */


public class StepDetailActivity extends BaseActivity implements View.OnClickListener, ViewDataListener, UserStepViewAdapter.ChangeListener {
    private static final String TAG = "StepDetailActivity";
    private UserStepViewAdapter adapter;
    private ExpandableListView elv;
    private ImageView showStep, rotateStep;
//    private GetAllStepPresenter getAllStepPresenter = new GetAllStepPresenter(this);
//    private StepDetailPresenter mStepDetailPresenter = new StepDetailPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        initView();
        initTitlebar(getString(R.string.step_detail));
        setListener();
        initData();
    }

    private void initData() {
//        new GetAllStepPresenter(this).getData();
//        UserStepHelper.reqUserStepAll(this);
        UserStepHelper.reqUserStepAll(this);
    }

    private void setListener() {
        //share_step.setOnClickListener(this);
        //edit_step.setOnClickListener(this);

    }

    //    private Button show_dialog;
    //private Button share_step;
    //private Button edit_step;
    private ProgressBar pb;
    private TextView rigth_textview;

    public void initView() {
        rigth_textview = (TextView) findViewById(R.id.rigth_textview);
        rigth_textview.setText("编辑");
        rigth_textview.setVisibility(View.VISIBLE);
        rigth_textview.setOnClickListener(this);
        pb = (ProgressBar) findViewById(R.id.pb);
        //share_step = (Button) findViewById(R.id.share_step);
        //edit_step = (Button) findViewById(R.id.edit_step);
        showStep = (ImageView) findViewById(R.id.show_dialog);
        rotateStep = (ImageView) findViewById(R.id.inside_imageview);
        showStep.setOnClickListener(this);
        elv = (ExpandableListView) findViewById(R.id.expendlist);
        elv.setGroupIndicator(null);

//        initPopuWindow();
    }


    private float startf = 0f;
    private float endf = 45f;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.show_dialog:
//                ObjectAnimator.ofFloat(rotateStep, "rotation", startf, endf).setDuration(300).start();
//                startf = endf;
//                endf += 45f;

                ObjectAnimator.ofFloat(rotateStep, "rotation", 0f, 90f).setDuration(300).start();

                initPopuWindow();//弹出对话框
                break;
            case R.id.rigth_textview:
                startActivityForResult(new Intent(this, EditStepActivity.class), 0);
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;

            case R.id.update_step://添加阶段
                Intent intent = new Intent(this, EditStepActivity.class);
                intent.putExtra(EditStepActivity.ARGS_ADDSTEP, 1);
                startActivityForResult(intent, 0);
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.add_symptom://添加症状
                startActivityForResult(new Intent(this, RecordSymptomActivity.class), 0);
//                view.clearAnimation();
//                popupWindow.dismiss();
                dialog.dismiss();

                break;

            case R.id.zhibiao_record://指标记录
                startActivityForResult(new Intent(this, RecordZhibiaoActivity.class), 0);
//                view.clearAnimation();
//                popupWindow.dismiss();
                dialog.dismiss();
                break;
//            case R.id.share_step://这个是分享
//                LogUtil.i(TAG, "click share.");
//                break;
//            case R.id.edit_step://这个是编辑
//                Intent intent = new Intent();
//                intent.setClass(this, EditStepActivity.class);
//                startActivityForResult(intent, 0);
//                LogUtil.i(TAG, "click edit.");
//
//                break;
            default:
                throw new RuntimeException();
        }

    }

//    private void showDialog() {
//        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
//        int popupWidth = view.getMeasuredWidth();
//        int popupHeight = view.getMeasuredHeight();
//        int[] location = new int[2];
//        showStep.getLocationOnScreen(location);
//        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, popupWindow.getHeight());
//        animation.setDuration(3000);
//        view.startAnimation(animation);
//        popupWindow.showAtLocation(showStep, Gravity.NO_GRAVITY, (location[0] + showStep.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
//    }

    private AlertDialog dialog;

//    View view;
//    PopupWindow popupWindow;

    private void initPopuWindow() {
//        view = LayoutInflater.from(this).inflate(R.layout.step_dialog, null);
//        initDialog(view);
//        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//      popupWindow.setAnimationStyle(R.style.mystyle);


        dialog = new AlertDialog.Builder(this).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        View v = LayoutInflater.from(this).inflate(R.layout.step_dialog, null);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
        window.setWindowAnimations(R.style.mystyle);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setContentView(v);
        initDialog(v);
    }

    private void initDialog(View v) {
        TextView add_symptom = (TextView) v.findViewById(R.id.add_symptom);//添加症状
        TextView update_step = (TextView) v.findViewById(R.id.update_step);//更新阶段
        TextView zhibiao_record = (TextView) v.findViewById(R.id.zhibiao_record);//指标记录
        add_symptom.setOnClickListener(this);
        update_step.setOnClickListener(this);
        zhibiao_record.setOnClickListener(this);

    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == 0) {//getallstep
            return map;
        }
        if (tag == 1) {//这个是stepdetail
            map.put(Contants.StepUID, "1");
        }
        return map;
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void toActivity(Object t, int position) {
        switch (position) {
            case UserStepHelper.ReqCode_UserStepAll:
                //GlobalUserStepData.getUserStepList()
                List<UserStepBean> lstData = (List<UserStepBean>) t;
                Collections.sort(lstData, new UserStepComparator(true));
                bindData(lstData);
                UserStepBean beans = GlobalUserStepData.findLastUserStep();//保存最新阶段的stepid
                if (beans != null) {
                    String stepId = beans.getStepID();
                    UserinfoData.saveStepID(this, stepId);
                }
                //LogUtil.i(TAG, "getAllStep的数据：" + bean.toString());
                //List<AllStepBean.StepNumEntity> list = bean.getStepNum();
                //if (list != null && list.size() > 0) {
                //String stepID = list.get((list.size() - 1)).getStepID();
                //SharePrefUtil.saveString(this, Contants.StepID, stepID);
                //Toast.makeText(StepDetailActivity.this, "stepId更新成功:" + stepID + "  名字：" + GlobalData.cureValues.get(stepID), Toast.LENGTH_SHORT).show();
                //}
                break;
            case UserStepHelper.ReqCode_UserStepDetails:
                UserStepBean bean = (UserStepBean) t;
                if (bean.getChildList() == null || bean.getChildList().isEmpty()) {
                    showToast("这个阶段还没有记录哦～～");
                }

            case UserStepHelper.ReqCode_UserSymptDel:
            case UserStepHelper.ReqCode_UserQuotaDel: {
                adapter.notifyDataSetInvalidated();
                break;
            }
        }
        //if (position == 0) {
        //AllStepBean bean = (AllStepBean) t;

        //(List<UserStepBean>) t

//        }
//        if (position == 1) {//这个是stepdetail
//            String bean = (String) t;
//            LogUtil.i(TAG, "stepdetail:" + t);
//        }


    }

    private void getChildData(String userStepId) {
//        new StepDetailPresenter(this).getData();
        UserStepHelper.reqUserStepDetails(this, userStepId);
    }

    private void bindData(List<UserStepBean> lstBean) {
//        List<AllStepBean.StepNumEntity> groupDatas = bean.getStepNum();
        if (lstBean != null && lstBean.size() > 0) {
            adapter = new UserStepViewAdapter(this, lstBean);
            adapter.setChangeListener(this);
            elv.setAdapter(adapter);
        }
//        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                    LogUtil.i(TAG,groupPosition + "");
//
//                return false;
//            }
//        });
        /**
         * 监听group展开
         */
        elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                LogUtil.i(TAG, "onGroupExpand:");

                getChildData(GlobalUserStepData.getUserStepList().get(groupPosition).getStepUID());
                elv.setSelectedGroup(groupPosition);
            }
        });


//        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                getChildData(GlobalUserStepData.getUserStepList().get(groupPosition).getStepUID());
//                elv.setSelectedGroup(groupPosition);
//
//                return false;
//            }
//        });


    }

    @Override
    public void showLoading(int tag) {
//        pb.set
    }

    @Override
    public void hideLoading(int tag) {
        pb.setVisibility(View.GONE);
    }

    @Override
    public void showError(int tag) {

    }

    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        super.onDestroy();
    }


    private void toMdfSymptom(UserStepChildBean temp) {
        Intent intent = new Intent(this, RecordSymptomActivity.class);
        intent.putExtra(Contants.StepCfg.EDIT_SYMPTOM_TAG, temp);
        startActivityForResult(intent, 0);
    }

    /**
     * 跑去编辑指标记录
     *
     * @param temp
     */
    private void toMdfRecord(UserStepChildBean temp) {
        Intent intent = new Intent(this, RecordZhibiaoActivity.class);
        intent.putExtra(Contants.StepCfg.EDIT_ZHIBIAO_TAG, temp);
        startActivityForResult(intent, 0);
    }


    @Override
    public void edit(int groupIndex, int childIndex) {
        final UserStepBean group = (UserStepBean) adapter.getGroup(groupIndex);
        final UserStepChildBean child = (UserStepChildBean) group.getChildList().get(childIndex);
        if (child.isQuota()) {
            // to record
            toMdfRecord(child);
        } else {
            //to symptom
            toMdfSymptom(child);
        }
    }

    @Override
    public void delete(int groupIndex, int childIndex) {
        final UserStepBean group = (UserStepBean) adapter.getGroup(groupIndex);
        final UserStepChildBean child = (UserStepChildBean) group.getChildList().get(childIndex);
        new AlertView("提醒", "是否删除该记录", null, new String[]{"取消", "确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 1) {
                    if (child.isQuota()) {
                        UserStepHelper.reqUserQuotaDel(StepDetailActivity.this, group, child);
                    } else {
                        UserStepHelper.reqUserSymptDel(StepDetailActivity.this, group, child);
                    }
                }
            }
        }).setCancelable(true).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        UserStepHelper.reqUserStepAll(this);
//        if (resultCode == RESULT_OK) {
//        adapter.notifyDataSetInvalidated();
//        }
    }

}
