package com.zeyuan.kyq.view;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.google.gson.Gson;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.FuCancerHelper;
import com.zeyuan.kyq.bean.SlaverCancer;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.GeneDialog;
import com.zeyuan.kyq.http.bean.UserStepChildBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.SharePrefUtil;
import com.zeyuan.kyq.utils.UserStepHelper;
import com.zeyuan.kyq.utils.UserinfoData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2015/10/8.
 * <p/>
 * 指标记录
 */
public class RecordZhibiaoActivity extends BaseActivity implements View.OnClickListener, ViewDataListener {
    private static final String TAG = "ZhibiaoRecordActivity";
    private UserStepChildBean mUserStepChildBean;
    private boolean isEdit = false;//true 这是修改指标的 false 新增指标的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhibiao_record);
        initTitlebar(getString(R.string.index_record));
        mUserStepChildBean = (UserStepChildBean) getIntent().getSerializableExtra(Contants.StepCfg.EDIT_ZHIBIAO_TAG);
        if (mUserStepChildBean != null) {
            isEdit = true;
        } else {
            mUserStepChildBean = loadFormSDCard();//非编辑时获取sd卡数据
        }
        initView();
        UserStepHelper.reqUserStepAll(this);//防止直接添加症状时阶段数据为空
    }

    private Button save;//保存
    private TextView time;//检查日期
    private EditText cea_record;//cea指标
    private EditText master_len;//主肿瘤长度
    private EditText master_width;//主肿瘤宽度
    private EditText slave_len;//副肿瘤长度
    private EditText slave_width;//副肿瘤宽度
    private TextView show_tranpos;
    private LinearLayout ll_add_ly;
    private TextView add_cancer_symp;//添加副肿瘤
    private TextView delete_cancer_symp;//删除副肿瘤

    private void initView() {
        add_cancer_symp = (TextView) findViewById(R.id.add_cancer_symp);
        delete_cancer_symp = (TextView) findViewById(R.id.delete_cancer_symp);
        ll_add_ly = (LinearLayout) findViewById(R.id.ll_add_ly);
        save = (Button) findViewById(R.id.save);
        show_tranpos = (TextView) findViewById(R.id.show_tranpos);
        save.setOnClickListener(this);
        time = (TextView) findViewById(R.id.time);
        time.setOnClickListener(this);
        cea_record = (EditText) findViewById(R.id.cea_record);
        master_len = (EditText) findViewById(R.id.master_len);
        master_width = (EditText) findViewById(R.id.master_width);
        slave_len = (EditText) findViewById(R.id.slave_len);
        slave_width = (EditText) findViewById(R.id.slave_width);
        LayoutTransition mLayoutTransition = new LayoutTransition();
        ll_add_ly.setLayoutTransition(mLayoutTransition);
        add_cancer_symp.setOnClickListener(this);
        delete_cancer_symp.setOnClickListener(this);
        show_tranpos.setOnClickListener(this);
        helpers = new ArrayList<>();
//        if (isEdit) {//说明是编辑指标
        if (mUserStepChildBean != null) {
            String times = "" + mUserStepChildBean.getRecordTime();
            String cea = mUserStepChildBean.getCEA();
            String transferPos = mUserStepChildBean.getTransferPos();//转移
            String MasterCancerLength = mUserStepChildBean.getMasterCancerLength();//主肿瘤长度
            String MasterCancerWidth = mUserStepChildBean.getMasterCancerWidth();//宽度
            String showtime = DataUtils.getDate(times);

//            time.setEnabled(true);
            if (isEdit) {
                if (!TextUtils.isEmpty(showtime)) {
                    time.setText(showtime);
                }
                if (!TextUtils.isEmpty(cea)) {//cea
                    cea_record.setText(cea);
                } else {
                    cea_record.setText(R.string.no_datas);
                }
                if (!TextUtils.isEmpty(transferPos)) {//transpos
                    transferPos = MapDataUtils.getTransPosValues(transferPos);
                    show_tranpos.setText(transferPos);
                } else {
                    show_tranpos.setText(R.string.no_datas);
                }
            }
            if (!TextUtils.isEmpty(MasterCancerLength) && !TextUtils.isEmpty(MasterCancerWidth)) {//transpos
                master_len.setText(MasterCancerLength);
                master_width.setText(MasterCancerWidth);
            } else {
//                zhibiaoHolder.main_length.setText(R.string.no_datas);
            }
            List<SlaverCancer> fu_cancerList = mUserStepChildBean.getSlaverCancerNum();
            if (fu_cancerList != null && fu_cancerList.size() > 0) {
                for (SlaverCancer cancer : fu_cancerList) {
                    addFuView(cancer);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save: {//右边的保存
                commitData();
                break;
            }
            case R.id.time: {//右边的时间弹出
                if (isEdit) {
                    showToast("时间不能修改哦");
                    break;
                }
                showTimePicker();
                break;
            }
            case R.id.show_tranpos: {
                showTrsDialog();
                break;
            }

            case R.id.add_cancer_symp: {
                if (helpers == null || helpers.isEmpty()) {
                    //需判断是否填写了主肿瘤
                    String str_master_width = getEditText(master_width);
                    String str_master_length = getEditText(master_len);
                    if (TextUtils.isEmpty(str_master_length) || TextUtils.isEmpty(str_master_width)) {
                        showToast("主肿瘤没有填写，不能添加副肿瘤");
                        return;
                    }
                } else {
                    FuCancerHelper fctemp;
                    for (int i = 0; i < helpers.size(); i++) {
                        fctemp = helpers.get(i);
                        if (TextUtils.isEmpty(fctemp.getFu_name())) {
                            fctemp.fu_name.requestFocus();
                            showToast("请输入副肿瘤名字");
                            return;
                        }
                        if (0 == fctemp.getFu_length()) {
                            fctemp.fu_length.requestFocus();
                            showToast("请输入副肿瘤长度");
                            return;
                        }
                        if (0 == fctemp.getFu_width()) {
                            fctemp.fu_width.requestFocus();
                            showToast("请输入副肿瘤宽度");
                            return;
                        }
                    }
                }

                addFuView(null);
                break;
            }
            case R.id.delete_cancer_symp: {
                deleteFuView();
                break;
            }
        }
    }

    private String getEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void deleteFuView() {
        int childCount = ll_add_ly.getChildCount();
        if (childCount > 2) {
            ll_add_ly.removeView(ll_add_ly.getChildAt(childCount - 1));
            helpers.remove(helpers.size() - 1);//移除最后一个view中的数据
        }

    }

    private List<FuCancerHelper> helpers;

    private void addFuView(SlaverCancer cancer) {
        View mLayout = LayoutInflater.from(this).inflate(R.layout.layout_cancer_size, null);
        TextView fu_width = (TextView) mLayout.findViewById(R.id.slave_width);
        TextView fu_length = (TextView) mLayout.findViewById(R.id.slave_len);
        EditText fu_name = (EditText) mLayout.findViewById(R.id.fu_name);
        //如果是新增 则添加到list中 在保存的时候遍历取值
        //如果是编辑 还是添加到list中 装好原来的数据
        if (cancer != null) {
            fu_width.setText("" + cancer.getSlaveWidth());
            fu_length.setText("" + cancer.getSlaveLen());
            fu_name.setText("" + cancer.getSlaveName());
        }
        FuCancerHelper helper = new FuCancerHelper(fu_length, fu_width, fu_name);
        helpers.add(helper);
        ll_add_ly.addView(mLayout);
    }

    private List<String> transPosList;
    private String TransferPos;

    private void showTrsDialog() {
        transPosList = new ArrayList<>();
        Set<String> set = GlobalData.transferpos.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            transPosList.add(key);
        }
        GeneDialog dialog = new GeneDialog(transPosList, null, Contants.diolog_type);
        dialog.setDialogFragmentListener(new DialogFragmentListener() {
            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                TransferPos = data;
                String mUserStepChildBean = MapDataUtils.showTransData(data);
                if (!TextUtils.isEmpty(mUserStepChildBean)) {
                    show_tranpos.setText(mUserStepChildBean);
                }
            }
        });
        dialog.show(getFragmentManager(), "transpos");
    }

    /**
     * 显示选择时间的日期控件dialog
     */
    private DateTime dateTimeNew;

    private void showTimePicker() {
        DateTime dateTime = DateTime.now();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                DateTime dateTimeNewTemp = DateTime.from(year, monthOfYear, dayOfMonth);
                if (dateTimeNewTemp.isRealPastTime()) {
                    dateTimeNew = dateTimeNewTemp;
                    time.setText(dateTimeNew.toDateString());
                } else {
                    showToast("不能选择未来日期");
                }
//                dateTimeNew = DateTime.from(year, monthOfYear, dayOfMonth);
//                time.setText(dateTimeNew.toDateString());
            }
        }, dateTime.getYear(), dateTime.getMonth(), dateTime.getDay()).show();
    }

    /**
     * 暂时无用
     *
     * @param date
     * @return
     */
    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    /**
     * 要提交的数据
     */
//    private String commitTime;
    private String commitcea_record;//cea指标
//    private String commitMaster_len;//主肿瘤长度
//    private String commitMaster_width;//主肿瘤宽度
//    private String commitSlave_len;//副肿瘤长度
//    private String commitSlave_width;//副肿瘤宽度

    /**
     * 提交数据，并判断下数据是否为空
     */
    UserStepChildBean result;

    public void commitData() {
        //判断下数据是否为空
        if (getString()) return;

//        commitSlave_len = slave_len.getText().toString().trim();
//        if (TextUtils.isEmpty(commitSlave_len)) {
//            showToast("请输入副肿瘤长度");
//            return;
//        }
//        commitSlave_width = slave_width.getText().toString().trim();
//        if (TextUtils.isEmpty(commitSlave_width)) {
//            showToast("请输入副肿瘤宽度");
//            return;
//        }
//        new UpdateZhibiaoRecordPresenter(this).getData();

        result = new UserStepChildBean();
        {
            String cea = cea_record.getText().toString();
            result.setCEA(cea);


            String str_master_length = master_len.getText().toString();
            if (TextUtils.isEmpty(str_master_length)) {
                result.setMasterCancerLength("0");
            } else {
                result.setMasterCancerLength(str_master_length);
            }
//            result.setMasterCancerName("主肿瘤");//主肿瘤 不可以修改

            //设置主肿瘤的宽度
//            String str_master_width = master_width.getText().toString();
            String str_master_width = getEditText(master_width);
            if (TextUtils.isEmpty(str_master_width)) {
                result.setMasterCancerWidth("0");
            } else {
                result.setMasterCancerWidth(str_master_width);
            }


            if (isEdit) {
                String quaotaId = mUserStepChildBean.getQuotaID();
                result.setQuotaID(mUserStepChildBean.getQuotaID());
            }
//            result.setQuotaID(bean.getQuotaID());
            if (isEdit) {
                if (TextUtils.isEmpty(TransferPos)) {
                    result.setTransferPos(mUserStepChildBean.getTransferPos());
                } else {
                    result.setTransferPos(TransferPos);//
                }
            } else {
                result.setTransferPos(TransferPos);//
            }

            if (TextUtils.isEmpty(cea) && (TextUtils.isEmpty(str_master_length) || TextUtils.isEmpty(str_master_width)) && TextUtils.isEmpty(TransferPos)) {
                showToast("CEA指标，肿瘤大小，肿瘤转移情况，必须填一项");
                return;
//                new AlertView("提示", "CEA指标，肿瘤大小，肿瘤转移情况，必须填一项", null, new String[]{"好的"}, null, this, AlertView.Style.Alert, null).setCancelable(true).show();
            }


            result.setPerformORQuota(UserStepChildBean.PERFORM_OR_QUOTA__QUOTA);
            if (dateTimeNew == null) {
                result.setRecordTime(mUserStepChildBean.getRecordTime());
            } else {
                result.setRecordTime(dateTimeNew.toTimeSeconds());
            }

            if (helpers != null && helpers.size() > 0) {
                List<SlaverCancer> slaverCancers = new ArrayList<>();
                SlaverCancer cancer = null;
                for (FuCancerHelper helper : helpers) {
                    cancer = helper.getCancer();
                    if (cancer == null) {
                        showToast("副肿瘤数据不能为空");
                        break;
                    }
                    slaverCancers.add(cancer);
                }
                if (slaverCancers.size() > 0) {
                    result.setSlaverCancerNum(slaverCancers);//ggz:w
                }
            }
            //这里通过循环来取
        }

        //提交数据
        if (isEdit) {
            UserStepHelper.reqUserQuotaMdf(this, mUserStepChildBean.getStepUid(), result);
        } else {
            UserStepHelper.reqUserQuotaAdd(this, result);
        }
    }

    private boolean getString() {
        if (dateTimeNew == null && !isEdit) {
            showToast("请输入检查日期");
            return true;
        }

//        commitcea_record = cea_record.getText().toString().trim();
//        if (TextUtils.isEmpty(commitcea_record)) {
//            showToast("请输入cea指标");
//            return true;
//        }

//        commitMaster_len = master_len.getText().toString().trim();
//        if (TextUtils.isEmpty(commitMaster_len)) {
//            showToast("请输入主肿瘤长度");
//            return true;
//        }
//
//        commitMaster_width = master_width.getText().toString().trim();
//        if (TextUtils.isEmpty(commitMaster_width)) {
//            showToast("请输入主肿瘤宽度");
//            return true;
//        }


        return false;
    }


//    private void editZhibiao() {
//        UserStepChildBean result = new UserStepChildBean();
//        {
//            result.setCEA(cea_record.getText().toString());
//            result.setMasterCancerLength(master_len.getText().toString());
//            result.setMasterCancerName("主肿瘤");
//            result.setMasterCancerWidth(master_width.getText().toString());
//            String quaotaId = mUserStepChildBean.getQuotaID();
//            if (!TextUtils.isEmpty(quaotaId)) {
//                result.setQuotaID(mUserStepChildBean.getQuotaID());
//            }
//            result.setTransferPos(TransferPos);//
//            result.setPerformORQuota(UserStepChildBean.PERFORM_OR_QUOTA__QUOTA);
//            result.setRecordTime(dateTimeNew.toTimeSeconds());
////            result.setSlaverCancerNum();//ggz:w
//            //这里通过循环来取
//        }
//
//        UserStepHelper.reqUserQuotaMdf(this, mUserStepChildBean.getStepUid(), result);
//
//    }

    /**
     * 网络访问层从activity中获得需要请求的参数
     * tag用来标记是哪个的数据访问
     *
     * @param tag
     * @return
     */
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
//        if (tag == 0) {
//            map.put("InfoID", "123");
//            map.put("StepUID", "123");
//            map.put("quotaCEA", commitcea_record);
//            map.put("MasterLen", commitMaster_len);
//            map.put("MasterWidth", commitMaster_width);
//            map.put("MasterName", "123");
//            map.put("quotaSlaverCancerNum", "123");
//            map.put("SlaveName0", "123");
//            map.put("SlaveLen0", commitSlave_len);
//            map.put("SlaveWidth0", commitSlave_width);
//            map.put("SlaveLen1", "123");
//            map.put("SlaveName1", "123");
//            map.put("SlaveWidth1", "123");
//            map.put("tranferposnum", "123");
//            map.put("transferpos0", "123");
//        }
        return map;
    }

    /**
     * 数据访问层把数据返回给activity t表示数据封装成的类，position用来标记是哪个数据
     *
     * @param t
     * @param position
     */
    @Override
    public void toActivity(Object t, int position) {
        switch (position) {
            case UserStepHelper.ReqCode_UserQuotaAdd:
                Toast.makeText(RecordZhibiaoActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                saveToSDCard(result);
                finish();
                break;
            case UserStepHelper.ReqCode_UserQuotaMdf:
                saveToSDCard(result);
                Toast.makeText(RecordZhibiaoActivity.this, "添加更新！", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
//        String data = (String) t;
//        LogUtil.i(TAG, data);
    }

    public static final String SP_ZHIBIAO_LAST = "sp_zhibiao_last_";

    private void saveToSDCard(UserStepChildBean bean) {
        String json = new Gson().toJson(bean);
        SharePrefUtil.saveString(this, SP_ZHIBIAO_LAST + UserinfoData.getInfoID(), json);
    }

    private UserStepChildBean loadFormSDCard() {
        String json = SharePrefUtil.getString(this, SP_ZHIBIAO_LAST + UserinfoData.getInfoID(), null);
        if (!TextUtils.isEmpty(json)) {
            return new Gson().fromJson(json, UserStepChildBean.class);
        }
        return null;
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
}
