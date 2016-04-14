package com.zeyuan.kyq.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.UserStepEditAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalUserStepData;
import com.zeyuan.kyq.bean.AllStepBean;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.http.bean.UserStepComparator;
import com.zeyuan.kyq.presenter.UpdateStepTimePresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.UserStepHelper;
import com.zeyuan.kyq.utils.UserinfoData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.zeyuan.kyq.adapter.StepAdapter;

/**
 * Created by Administrator on 2015/10/8.
 * 编辑阶段
 */
public class EditStepActivity extends BaseActivity implements View.OnClickListener, ViewDataListener {

    public static final String ARGS_ADDSTEP = "ADD_STEP";
    private static final String TAG = "EditStepActivity";
    private TextView righttv;//右边的保存
    private ListView listView;//中间的listview
    private UserStepEditAdapter adapter;
    private TextView addStep;

 //   private UpdateStepTimePresenter updatStepTimePresenter = new UpdateStepTimePresenter(this);

//    private GetAllStepPresenter mGetAllStepPresenter = new GetAllStepPresenter(this);
    private String cancerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_step);
        initTitlebar(getString(R.string.edit_step));
        cancerId = getIntent().getStringExtra(Contants.CancerID);
        initView();
        setListener();
        initData();
    }

    private void initData() {
//        datas = new ArrayList<>();
//        adapter = new UserStepEditAdapter(this, datas);
//        listView.setAdapter(adapter);

        UserStepHelper.reqUserStepAll(this);
//        mGetAllStepPresenter.getData();


    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                if (intent != null && (intent.getIntExtra(ARGS_ADDSTEP, 0) == 1)) {
                    addStep();
                }
            }
        }, 300);
    }

    //    private  boolean mIsNeedAddStep;
    private void save() {

        //updatStepTimePresenter.getData();

        List<UserStepBean> lstBean = adapter.getItemList();
        if (isDataChanged(lstBean)) {
            final List<UserStepBean> lstBeanAdd = new ArrayList<>();
            List<UserStepBean> lstBeanMdf = new ArrayList<>();
            UserStepBean temp;
            boolean isValid = true;
            for (int i = 0; i < lstBean.size(); i++) {
                temp = lstBean.get(i);

                if (temp.$isEditor() || temp.$isChanged()) {//

                    if (TextUtils.isEmpty(temp.getStepID())) {
                        Toast.makeText(EditStepActivity.this, "请选择阶段对应的药物", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (temp.isBegTimeNull()) {
                        Toast.makeText(EditStepActivity.this, "请选择开始日期", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    if (temp.isEndTimeNull()) {
                        Toast.makeText(EditStepActivity.this, "请选择结束日期", Toast.LENGTH_SHORT).show();
                        break;
                    }

                    if (TextUtils.isEmpty(temp.getStepUID())) {
                        lstBeanAdd.add(temp);
                    } else {
                        lstBeanMdf.add(temp);
                    }
                }
            }

            if (!lstBeanAdd.isEmpty()) {
                //先验证请求的数据是否有效
                UserStepHelper.ValidUserStepModify result = UserStepHelper.validUserStepAdd(GlobalUserStepData.getUserStepList(), lstBeanAdd);
                if (result.valid) {
                    if (lstBeanMdf.isEmpty()) {
                        String errmsg = UserStepHelper.reqUserStepAdd(this, adapter.getItemList(), lstBeanAdd);
                        if (errmsg != null) {
                            Toast.makeText(EditStepActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                        mIsNeedAddStep=true;
                        updateStep(lstBeanMdf);//先更新，再添加
                    }
                } else {
                    final List<UserStepBean> lstNeedDelSpace = result.mNeedDelSpace;
                    if (lstNeedDelSpace != null && !lstNeedDelSpace.isEmpty()) {


//                        new AlertView("继续保存？", "继续保存会删除相邻的" + lstNeedDelSpace.size() + "个空窗期及其所含的记录", null, new String[]{"取消", "继续保存"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                            @Override
//                            public void onItemClick(Object o, int position) {
//                                if (position == 1) {
//                                    //先删除空窗期
//                                    deleteEmptyStep(lstNeedDelSpace, lstBeanAdd);
//                                }
//                            }
//                        }).setCancelable(true).show();

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("继续保存？");
                        builder.setMessage("继续保存会删除相邻的" + lstNeedDelSpace.size() + "个空窗期及其所含的记录");
                        builder.setPositiveButton("继续保存", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEmptyStep(lstNeedDelSpace, lstBeanAdd);
                            }
                        });
                        builder.setNegativeButton("取消", null);
                        builder.create().show();

                    } else {
                        Toast.makeText(EditStepActivity.this, result.errmsg, Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                updateStep(lstBeanMdf);
            }
        } else

        {
            Toast.makeText(EditStepActivity.this, " 您还未修改任何信息", Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteEmptyStep(List<UserStepBean> lstNeedDelSpace, final List<UserStepBean> lstBeanAdd) {
        for (int i = 0; i < lstNeedDelSpace.size(); i++) {
            UserStepHelper.reqUserStepDel(EditStepActivity.this, lstNeedDelSpace.get(i));
        }
        //再执行添加操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String errmsg = UserStepHelper.reqUserStepAdd(EditStepActivity.this, adapter.getItemList(), lstBeanAdd);
                if (errmsg != null) {
                    Toast.makeText(EditStepActivity.this, errmsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, 300);
    }

    private void updateStep(List<UserStepBean> lstBeanMdf) {
        if (!lstBeanMdf.isEmpty()) {
            String errmsg = UserStepHelper.reqUserStepMdf(this, lstBeanMdf);//, lstBean
            if (errmsg != null) {
                Toast.makeText(EditStepActivity.this, errmsg, Toast.LENGTH_SHORT).show();
//                        return;
            }
        }
    }

//    android.os.Handler mHandler = new android.os.Handler();

    private boolean isDataChanged(List<UserStepBean> lstBean) {
        for (int i = 0; i < lstBean.size(); i++) {
            if (lstBean.get(i).$isEditor()) return true;
        }
        return false;
    }

    private void setListener() {
        righttv.setOnClickListener(this);
        addStep.setOnClickListener(this);
    }


    private void initView() {
        righttv = (TextView) findViewById(R.id.rigth_textview);
        righttv.setVisibility(View.VISIBLE);
        righttv.setText(getString(R.string.save));
        listView = (ListView) findViewById(R.id.listview);
        addStep = (TextView) findViewById(R.id.add_step);

        adapter = new UserStepEditAdapter(this, R.layout.item_steps);
        if (!TextUtils.isEmpty(cancerId)) {

            adapter.setCancerId(cancerId);
        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rigth_textview: {//这儿是保存
                save();
                break;
            }
            case R.id.add_step: {//这儿是添加阶段
                addStep();
                break;
            }
        }
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            return map;
        }


//        /**
//         * 更新阶段传值规则：
//         * 修改后相同阶段后面增加同样的数字标志
//         * 修改以前阶段的加上StepUid。新增的阶段不需要
//         * num表示修改的阶段数量
//         * stepuid stepid 和用户的关系
//         */
//        if (tag == 1) {
////            map.put("num", "1");
////            map.put("InfoID", "2");
////            map.put("StepID0", "5005,5006,5007");//药物的id 中间用逗号隔开
////            map.put("StepUID0", "2");//阶段的步骤
////            map.put("StartTime0", "2");
////            map.put("EndTime0", "200");
////            map.put("IsMedicineValid", "1");//有效为1无效为0
//            Map maps = adapter.getDataMap();
//            LogUtil.i(TAG, maps.toString());
//            map.putAll(maps);
////            map.put("num", "1");
//        }
        return map;
//        return null;
    }

    @Override
    public void toActivity(Object obj, int position) {
        switch (position) {
            case UserStepHelper.ReqCode_UserStepAll: {
                List<UserStepBean> lstData = (List<UserStepBean>) obj;
                Collections.sort(lstData, mUserStepComparator);

//                adapter.itemListAddAll(lstData);
                adapter.setItemList(lstData);
                adapter.notifyDataSetChanged();
            }
            break;

            case UserStepHelper.ReqCode_UserStepAdd: {
//                mIsNeedAddStep=false;
                adapter.mEditorBean = null;
                List<UserStepBean> lstDataMdf = (List<UserStepBean>) obj;
                Collections.sort(adapter.getItemList(), mUserStepComparator);
                adapter.notifyDataSetChanged();
                if (lstDataMdf.get(0).isEndTimeLast()) {
//                    new AlertView("定制内容已更新", "抗癌圈已为您重新匹配最相关的文章、相似案例和圈子等内容，您仍可在“更多圈子”页面继续关注曾经的圈子。", null, new String[]{"知道了"}, null, this, AlertView.Style.Alert, null).setCancelable(true).show();

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("定制内容已更新");
                    builder.setMessage("抗癌圈已为您重新匹配最相关的文章、相似案例和圈子等内容，您仍可在“更多圈子”页面继续关注曾经的圈子。");
                    builder.setNegativeButton("知道了", null);
                    builder.create().show();
                } else {
                    Toast.makeText(EditStepActivity.this, "阶段添加成功！", Toast.LENGTH_SHORT).show();
                }
                UserStepBean beans = GlobalUserStepData.findLastUserStep();
                if (beans != null) {
                    String stepId = beans.getStepID();
                    UserinfoData.saveStepID(this, stepId);
                }
            }
            break;


            case UserStepHelper.ReqCode_UserStepDel:
                Toast.makeText(EditStepActivity.this, "阶段删除成功！", Toast.LENGTH_SHORT).show();
                if (obj != null) {
                    UserStepBean bean = (UserStepBean) obj;
                    if (bean.$isEditor()) {
                        adapter.mEditorBean = null;
                    }
                }
                adapter.notifyDataSetChanged();
                break;
            case UserStepHelper.ReqCode_UserStepMdf0:
                if (adapter.mEditorBean.getStepUID() == null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            save();
                        }
                    }, 300);
                } else {
                    adapter.mEditorBean = null;
                    Collections.sort(adapter.getItemList(), mUserStepComparator);
                    adapter.notifyDataSetInvalidated();
                    UserStepBean beans = GlobalUserStepData.findLastUserStep();
                    if (beans != null) {
                        String stepId = beans.getStepID();
                        UserinfoData.saveStepID(this, stepId);
                    }
                }
                Toast.makeText(EditStepActivity.this, "阶段更新成功！", Toast.LENGTH_SHORT).show();
                break;
            case UserStepHelper.ReqCode_UserStepMdf1ConfirmDelSpace: {
//                Toast.makeText(EditStepActivity.this, "弹出对话框，需要删除空窗期，及空窗期内的指标症状数据，您确定吗？", Toast.LENGTH_SHORT).show();
//                new AlertView("提醒", "需要删除空窗期，及空窗期内的指标症状数据，您确定吗", null, new String[]{"取消", "确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (position == 1) {
//                            UserStepHelper.reqUserStepMdf1DelSpace();
//                        }
//                    }
//                }).setCancelable(true).show();
                //用户点击确定时调用
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提醒");
                builder.setMessage("需要删除空窗期，及空窗期内的指标症状数据，您确定吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserStepHelper.reqUserStepMdf1DelSpace();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();


                break;
            }
            case UserStepHelper.ReqCode_UserStepMdf2ConfirmDelChild: {
//                Toast.makeText(EditStepActivity.this, "弹出对话框，提示需要阶段时间修改，需要删除指标或症状数据，您确定吗？", Toast.LENGTH_SHORT).show();

//                new AlertView("提醒", "阶段时间修改，需要删除指标或症状数据", null, new String[]{"取消", "确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (position == 1) {
//                            UserStepHelper.reqUserStepMdf2DelChild();
//                        }
//                    }
//                }).setCancelable(true).show();


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("提醒");
                builder.setMessage("阶段时间修改，需要删除指标或症状数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserStepHelper.reqUserStepMdf2DelChild();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

                //用户点击确定时调用
                // UserStepHelper.reqUserStepMdf2DelChild();
                break;
            }
        }
//        if (position == 1) {
//            BaseBean bean = (BaseBean) t;
//            if (Contants.OK_DATA.equals(bean.iResult)) {
//                showToast("保存成功");
//                adapter.clearMap();
//                finish();
//            }
//            LogUtil.i(TAG, "upsteptime:" + bean.toString());
//        }

    }

//    private List<AllStepBean.StepNumEntity> datas;

    private void bindData(AllStepBean bean) {
        List data = bean.getStepNum();
        if (data != null && data.size() > 0) {
//            datas.addAll(data);
//            adapter.update(datas);
////            adapter = new StepAdapter(this, datas);
////            listView.setAdapter(adapter);
        }
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

    private UserStepComparator mUserStepComparator = new UserStepComparator(true);

    private void addStep() {
        new AlertView("请选择您要建立的阶段", null, "取消", null,
                new String[]{"最新阶段", "历史阶段"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (adapter.mEditorBean != null) {
                    Toast.makeText(EditStepActivity.this, "如需新增阶段请先保存", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (0 == position) {
                    //1.把前一个阶段的结束时间改为前一天(存在之前阶段的话)
                    if (!adapter.getItemList().isEmpty()) {
                        UserStepBean bean0 = adapter.getItemList().get(0);
                        if (DateTime.from(bean0.getCompareDateBeg() * 1000).toDateString().equals(DateTime.now().toDateString())) {
                            Toast.makeText(EditStepActivity.this, "添加失败，今天已有一个最新阶段", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
//                            bean0.setEndTime(DateTime.from(DateTime.now().addDay(-1).toDateString(), "yyyy-MM-dd").toTimeSeconds());
                            bean0.setEndTime(DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds() - 1);
                            bean0.$isChanged(true);
                        }
                    }
                    //2. 新加一个阶段的结束时间改为最新, 起始时间为当前时间
                    UserStepBean bean = new UserStepBean();
                    adapter.mEditorBean = bean;
                    bean.$isEditor(true);
                    bean.setIsMedicineValid(1);
                    bean.setBeginTime(DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds());
                    bean.setEndTime(Long.parseLong(UserStepBean.TIME_LAST));
                    adapter.getItemList().add(0, bean);
                    adapter.notifyDataSetChanged();
                } else if (1 == position) {
                    UserStepBean bean = new UserStepBean();
                    adapter.mEditorBean = bean;
                    bean.$isEditor(true);
                    bean.setIsMedicineValid(1);
                    adapter.getItemList().add(0, bean);
                    adapter.notifyDataSetChanged();
                }
            }
        }).setCancelable(true).show();
    }
}


//        new AlertView("提醒", "需要删除空窗期，及空窗期内的指标症状数据，您确定吗", null, new String[]{"取消", "确定"}, null, this, AlertView.Style.Alert, new OnItemClickListener() {
//            @Override
//            public void onItemClick(Object o, int position) {
//                if (position == 1) {
//                    UserStepHelper.reqUserStepMdf1DelSpace();
//                }
//            }
//        }).setCancelable(true).show();