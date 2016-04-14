package com.zeyuan.kyq.view;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.CancerResuletBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.WSZLBean;
import com.zeyuan.kyq.fragment.GanGGFragment;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.GeneDialog;
import com.zeyuan.kyq.fragment.dialog.ListDialog;
import com.zeyuan.kyq.presenter.GetResultPresenter;
import com.zeyuan.kyq.presenter.UpdatePatientDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 肿瘤决策树的完善资料
 */
public class WSZLActivity extends BaseActivity implements ViewDataListener, View.OnClickListener, DialogFragmentListener {
    private static final String TAG = "WSZLActivity";
    private static final String FLAG = "--@--"; // 转移情况
    private static final int kEGOType1 = 1 << 0; // 转移情况
    private static final int kEGOType2 = 1 << 1; // 突变情况
    private static final int kEGOType3 = 1 << 2; // 曾经有效的药物
    private static final int kEGOType4 = 1 << 3; // 身体状况
    private static final int kEGOType5 = 1 << 4; // 肝功能评分 //////////先不加
    private static final int kEGOType6 = 1 << 5; // 自定义文本
    private static final int kEGOType7 = 1 << 6; // 单选
    private static final int kEGOType8 = 1 << 7; // 最后有效的靶向
    private static final int kEGOType9 = 1 << 8; // 最后有效的化疗
    private String[] bodyStatus = {"身体活动能力完全正常", "能自由走动及从事轻体力活动，但不能从事较重的体力活动", "能自由走动及生活自理，没有工作能力，一大半时间起床活动", "卧床不起，生活不能自理", "濒危"};
    private WSZLBean mWSZLBean;
    private static final String empty_BXY = "693";
    private static final String empty_HLY = "694";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wszl);
        initTitlebar(getString(R.string.confirm_zl));
        mWSZLBean = (WSZLBean) getIntent().getSerializableExtra(Contants.WSZLBean);
        initData();
        initView();
    }

    //    private TextView tv_left;//自定义文本的左边文本
//    private TextView tv_right;//自定义文本的右边文本
    private TextView gangg;//肝功能
    private TextView body_status;//身体状况
    private TextView cure_case;//
    private TextView finish;
    private LinearLayout contents;

    private TextView tv_gene;//突变情况
    private RelativeLayout ll_gene;//突变情况
    private LinearLayout ll_effect_case;//最后有效的靶向药
    private LinearLayout ll_body;//身体状况
    private LinearLayout ll_gan;//肝功能
    private LinearLayout second_drugs;//最后有效的化疗药 是否显示
    private TextView tv_second_drugs;//上面的文本

    private void initView() {
        contents = (LinearLayout) findViewById(R.id.contents);
        second_drugs = (LinearLayout) findViewById(R.id.ll_second_drugs);
        tv_second_drugs = (TextView) findViewById(R.id.tv_second_drugs);
        ll_gene = (RelativeLayout) findViewById(R.id.ll_gene);
        ll_effect_case = (LinearLayout) findViewById(R.id.ll_effect_case);
        ll_body = (LinearLayout) findViewById(R.id.ll_body);
        ll_gan = (LinearLayout) findViewById(R.id.ll_gan);
        body_status = (TextView) findViewById(R.id.body_status);
        gangg = (TextView) findViewById(R.id.gangg);
        finish = (TextView) findViewById(R.id.finish);
        tv_gene = (TextView) findViewById(R.id.tv_gene);

        gangg.setOnClickListener(this);
        finish.setOnClickListener(this);
        tv_gene.setOnClickListener(this);
        tv_second_drugs.setOnClickListener(this);
        body_status.setOnClickListener(this);
        cure_case = (TextView) findViewById(R.id.cure_case);


        String cureconfid = MapDataUtils.getAllCureconfID(StepID);
        if (cureconfid.equals(MapDataUtils.BXY_ID)) {//说明这个是靶向药
//            cure_case.setText(MapDataUtils.getCureValues(StepID));
            drug_type = 0;
        }

        if (MapDataUtils.HLY_ID.equals(cureconfid)) {//说明这个是化疗药
//            tv_second_drugs.setText(MapDataUtils.getCureValues(StepID));

            drug_type = 1;
        }


        cure_case.setOnClickListener(this);
        if (mWSZLBean != null) {
            bindView(mWSZLBean);
        }
    }

    private int drug_type = -1;//0是靶向药 ，1 是化疗药

    private void initData() {
//        new ConfirmSecondPresenter(this).getData();
    }

    private String CancerID = UserinfoData.getCancerID(this);//癌症的id
    private String StepID = UserinfoData.getStepID(this);//当前阶段ID
    //    private String PeriodType = "1";//分期类型，表示数字分期还是TNM分期  数字分期PeriodType为1，TNM分期PeriodType为2
    private String PeriodID = UserinfoData.getPeriodID(this);
    private String BodyStatusID = "2";//身体状况ID，暂时先随便填，后台没做判断
    //    private String SelfSelectNum = "1";//自定义单选的题目数量(可以有多个题目) QuestionID：题目的ID，对应于GetConfirmSecond的问题的ID(GetConfirmSecond会加上)
//    private String QuestionID0 = "1000";//问题id
//    private String AnswerID0 = "5002";// 用户选择答案选项的ID，对应于GetConfirmSecond的选项的ID(GetConfirmSecond会加上)
    private String GeneID;//用户选择基因的ID，多个基因ID间用逗号隔开(比如  2,3,4 )
    private String TargetID;//最后有效的靶向药ID (最后有效的靶向药只能选一个)
    private String ChemistryID;//最后有效的化疗药ID (最后有效的化疗药只能选一个)
    private String score;

    public void setScore(String score) {
        this.score = score;
        gangg.setText(score + "分");
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
//        if (tag == 0) {
//            map.put(Contants.StepID, StepID);
//            map.put(Contants.CancerID, CancerID);
//            map.put(Contants.PeriodID, PeriodID);
////            map.put(Contants.PeriodType, PeriodType);
//        }
        if (tag == 1) {
            map.put(Contants.CancerID, CancerID);
            map.put(Contants.StepID, StepID);
//            map.put(Contants.PeriodType, PeriodType);
            map.put(Contants.PeriodID, PeriodID);
            map.put(Contants.BodyStatusID, BodyStatusID);
            /**
             * 提交单选的数据
             */
            if (dxData != null && dxData.size() > 0) {// && dxData.size() == mWSZLBean.getSelfSelect().size()
                map.put(Contants.SelfSelectNum, dxData.size() + "");
                //遍历map
//                Iterator<String> iterator = dxData.keySet().iterator();
                int i = 0;
                for (Map.Entry<String, String> entry : dxData.entrySet()) {
                    map.put(Contants.QuestionID + i, entry.getKey());
                    map.put(Contants.AnswerID + i, entry.getValue());
                }


            } else {
//                Toast.makeText(WSZLActivity.this, "请", Toast.LENGTH_SHORT).show();
//                return null;
            }

            /**
             * 自定义文本
             */
            if (wbData != null && wbData.size() > 0) {

            }


//            map.put(Contants.SelfSelectNum, SelfSelectNum);
//            map.put(Contants.QuestionID0, QuestionID0);
//            map.put(Contants.AnswerID0, AnswerID0);
            if (!TextUtils.isEmpty(GeneID)) {
                map.put(Contants.GeneID, GeneID);
            }
            if (!TextUtils.isEmpty(ChemistryID)) {
                map.put(Contants.ChemistryID, ChemistryID);
            }
            if (!TextUtils.isEmpty(TargetID)) {
                map.put(Contants.TargetID, TargetID);
            }
        }
        LogUtil.i(TAG, map.toString());
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, "界面请求的数据：" + t.toString());

//        if (tag == 0) {// 请求得到数据
//            WSZLBean bean = (WSZLBean) t;
//            if (Contants.RESULT.equals(bean.getIResult())) {
//                bindView(bean);
//            }
//        }

        if (tag == NetNumber.UPDATE_PATIENT) {
            UserinfoData.saveUserData(this, bean);
        }


        if (tag == 1) {
            CancerResuletBean bean = (CancerResuletBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
//                    bindView(bean);
                startActivity(new Intent(this, ResultDetailActivity.class)//.putExtra(Contants.IS_CANCER, false)
                        .putExtra(Contants.CancerResuletBean, bean));
            }
        }
    }

    private void bindView(WSZLBean bean) {
        String set = bean.getUiSetBit();
        int setBit = Integer.valueOf(set);
//        setBit = 63;
        int temp7 = setBit >> 8;
        if (1 == temp7) {
            setBit = setBit - kEGOType9;// 最后有效的化疗药 显示
            second_drugs.setVisibility(View.VISIBLE);
//            if (drug_type == 1) {
//                tv_second_drugs.setText(MapDataUtils.getCureValues(StepID));
//                ChemistryID = StepID;
//            }

            lastChemistryIds = mWSZLBean.getLastChemistryID();
            if (lastChemistryIds == null || lastChemistryIds.isEmpty()) {
                return;
            }
            boolean hasBXY = lastChemistryIds.remove(empty_HLY);//这儿移除694
            if (hasBXY) {
                if (TextUtils.isEmpty(ChemistryID)) {//这个是为了 判断是否 自动选中
                    ChemistryID = empty_HLY;
                }
            }
            if (lastChemistryIds.contains(StepID)) {
                tv_second_drugs.setText(MapDataUtils.getAllStepName(StepID));
                ChemistryID = StepID;
            }


        }

        int temp8 = setBit >> 7;
        if (1 == temp8) {
            setBit = setBit - kEGOType8;//最后有效的靶向
            ll_effect_case.setVisibility(View.VISIBLE);

//            if (drug_type == 0) {
//                cure_case.setText(MapDataUtils.getCureValues(StepID));
//                TargetID = StepID;
//            }
            lastTargetIds = mWSZLBean.getLastTargetID();
            if (lastTargetIds == null || lastTargetIds.isEmpty()) {
                return;
            }
            boolean hasBXY = lastTargetIds.remove(empty_BXY);
            if (hasBXY) {
                if (TextUtils.isEmpty(TargetID)) {//这个是为了 判断是否 自动选中
                    TargetID = empty_BXY;
                }
            }
            if (lastTargetIds.contains(StepID)) {
                cure_case.setText(MapDataUtils.getCureValues(StepID));
                TargetID = StepID;
            }
        }


        int temp1 = setBit >> 6;//如果为1 说明有单选
        if (1 == temp1) {
            setBit = setBit - kEGOType7;
        }
        int temp2 = setBit >> 5;
        if (1 == temp2) {
            setBit = setBit - kEGOType6;
        }
        int gangg = setBit >> 4;
        if (1 == gangg) {//说明是要肝功能的
            ll_gan.setVisibility(View.VISIBLE);
            setBit = setBit - kEGOType5;
        }
        int bodyPos = setBit >> 3;
        if (1 == bodyPos) {//说明是有身体状况的
            ll_body.setVisibility(View.VISIBLE);
            setBit = setBit - kEGOType4;
        }
        int cC = setBit >> 2;//说明有事曾经有效药物的
        if (1 == cC) {
            ll_effect_case.setVisibility(View.VISIBLE);
            setBit = setBit - kEGOType3;
        }
        int gene = setBit >> 1;
        if (1 == gene) {//说明是有突变情况的
            ll_gene.setVisibility(View.VISIBLE);
        }

        List<WSZLBean.SelfContentEntity> list1 = bean.getSelfContent();//自定义文本
        wbData = new ArrayList<>();
        if (list1 != null && list1.size() > 0) {
            for (WSZLBean.SelfContentEntity item : list1) {
                addZDYWB(item);
            }
        }
        dxData = new HashMap<>();
        List<WSZLBean.SelfSelectEntity> list = bean.getSelfSelect();//说明是有自定义单选的
        if (list != null && list.size() > 0) {
            for (WSZLBean.SelfSelectEntity item : list) {
                dxData.put(item.getQuestionID(), "0");//这个是为了 未选中的情况下 传0 20160120
                addZDYDX(item);
            }
        }
    }

    private List<String> wbData;//文本的数据

    private void addZDYWB(WSZLBean.SelfContentEntity item) {
        View ZDY = LayoutInflater.from(this).inflate(R.layout.layout_wszl_content, null);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dpToPx(this, 60));
        p.setMargins(0, DensityUtils.dpToPx(this, 8), 0, 0);
        TextView tv_left = (TextView) ZDY.findViewById(R.id.tv_left);
        tv_left.setText(item.getContent());
        CheckBox cb = (CheckBox) ZDY.findViewById(R.id.tv_right);
        final String selfId = item.getSelfID();
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    wbData.add(selfId);
                } else {
                    if (wbData.contains(selfId)) {
                        wbData.remove(selfId);
                    }
                }
            }
        });
        contents.addView(ZDY, p);

    }


    private Map<String, String> dxData;
    private static final String MOREN = "35";

    /**
     * 添加自定义单选的view
     */
    private void addZDYDX(final WSZLBean.SelfSelectEntity item) {
        View ZDY = LayoutInflater.from(this).inflate(R.layout.layout_wszl, null);
//        ZDY.setBackgroundColor(Color.parseColor(""));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dpToPx(this, 54));
        p.setMargins(0, DensityUtils.dpToPx(this, 8), 0, 0);
        TextView tv_left = (TextView) ZDY.findViewById(R.id.tv_left);
        tv_left.setText(item.getContent());
        final TextView tv_right = (TextView) ZDY.findViewById(R.id.tv_right);
//        tv_left.setText();
        contents.addView(ZDY, p);
        if (MOREN.equals(item.getQuestionID())) {
            tv_right.setEnabled(false);
            tv_right.setVisibility(View.GONE);

        }

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.i(TAG, "danxuan onclick");
//                createDialog(item.getContent().split(FLAG), tv_right, item);
                createDXDialog(item, tv_right);
            }
        });
    }

    private String dxContent;//这个为了能选中

    /**
     * 自定义单选的dilog
     *
     * @param item
     * @param tv_right
     */
    private void createDXDialog(final WSZLBean.SelfSelectEntity item, final TextView tv_right) {
        final List<WSZLBean.SelfSelectEntity.AnswerEntity> list = item.getAnswer();
        final List<String> content = new ArrayList<>();
        for (WSZLBean.SelfSelectEntity.AnswerEntity entity : list) {
            content.add(entity.getContent());
        }
        int positon = -1;
        if (!TextUtils.isEmpty(dxContent)) {
            positon = content.indexOf(dxContent);
        }
        ListDialog dialog = new ListDialog(content, ListDialog.OTHER_DIALOG, positon);
        dialog.setTitles(item.getContent());
        dialog.setListener(new DialogFragmentListener() {
            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                LogUtil.i(TAG, data);
                dxContent = data;
                tv_right.setText(data);
                if (!TextUtils.isEmpty(data)) {
                    int index = content.indexOf(data);
                    dxData.put(item.getQuestionID(), list.get(index).getAnswerID());
                }
            }
        });
        dialog.show(getFragmentManager(), Contants.SELF_SELECT_DIALOG);


//        final String[] content = new String[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            content[i] = list.get(i).getContent();
//        }

//        AlertDialog.Builder builder = new AlertDialog.Builder(WSZLActivity.this);
//        builder.setTitle(item.getContent());
//        builder.setItems(content, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                tv_right.setText(content[which]);
//                dxData.put(item.getQuestionID(), list.get(which).getAnswerID());
//            }
//        });
//        builder.create().show();
    }


//    private void createDialog(final String[] strings, final TextView tv_right, WSZLBean.SelfSelectEntity item) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(WSZLActivity.this);
//        builder.setTitle("列表选择框");
//        builder.setItems(strings, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //which is position
//                tv_right.setText(strings[which]);
////                dxData.put()
//
//            }
//        });
//        builder.create().show();
//    }

    private CustomProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
        mProgressDialog = CustomProgressDialog.createCustomDialog(this);
        mProgressDialog.show();
    }

    @Override
    public void hideLoading(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private List<String> body_status_data;
    private String choose_body_status;

    private void createDialog(final String[] item, final TextView tv_right) {
        if (body_status_data == null) {
            body_status_data = new ArrayList<>();
            for (String items : item) {
                body_status_data.add(items);
            }
        }

        int position = -1;
        if (!TextUtils.isEmpty(choose_body_status)) {
            position = body_status_data.indexOf(choose_body_status);
        }
        ListDialog dialog = new ListDialog(body_status_data, ListDialog.Step_DIALOG, position);
        dialog.setTitles(getString(R.string.body_status));
        dialog.setListener(new DialogFragmentListener() {
            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                tv_right.setText(data);//身体状况

            }
        });
        dialog.show(getFragmentManager(), "body_satus");


//        AlertDialog.Builder builder = new AlertDialog.Builder(WSZLActivity.this);
//        builder.setTitle("列表选择框");
//        builder.setItems(item, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //which is position
//                tv_right.setText(item[which]);
//
//            }
//        });
//        builder.create().show();
    }


    private void createDialog(final String[] item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WSZLActivity.this);
        builder.setTitle("列表选择框");
        builder.setItems(item, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //which is position

            }
        });
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.body_status: {//身体状况
                createDialog(bodyStatus, body_status);
                break;
            }
            case R.id.gangg: {//肝功能
                addFragmet();
                break;
            }
            case R.id.cure_case: {
//                ChooseMedicaDialog dialog = ChooseMedicaDialog.newInstance(ChooseMedicaDialog.TYPE_MEDICA);
//                dialog.setDrugsNameListener(this);
//                dialog.show(getFragmentManager(), "cure_case");
                if (lastTargetIds != null && lastTargetIds.size() > 0) {

                    createLastTargetDialog();
                }
//                if (list != null && list.size() > 0) {//最后有效靶向药
//                    String[] strings = listToStrings(list);
//                    createSecondDialog(strings, cure_case, false);
//                }
                break;
            }

            case R.id.finish: {//确认为最新
                toResult();
                break;
            }
            case R.id.tv_gene: {//突变情况
//                createDialog(showGeneDialog("30"), tv_gene);
                showGeneDialog();

                break;
            }

            case R.id.tv_second_drugs: {//最后有效化疗药
//                List list = mWSZLBean.getLastChemistryID();
                if (lastChemistryIds != null && lastChemistryIds.size() > 0) {
                    createChemistryDialog();
                }
//                createSecondDialog(list, tv_second_drugs, true);
//                if (list != null && list.size() > 0) {
//                    String[] strings = listToStrings(list);
//                    createSecondDialog(strings, tv_second_drugs, true);
//                }


                break;
            }
        }
    }

    private List<String> lastTargetIds;

    /**
     * 最后有效靶向药的dialog
     */
    private void createLastTargetDialog() {
//        List<String> list = mWSZLBean.getLastTargetID();
//        boolean hasBXY = list.remove(empty_BXY);
//        if (hasBXY) {
//            if (TextUtils.isEmpty(TargetID)) {//这个是为了 判断是否 自动选中
//                TargetID = empty_BXY;
//            }
//        }
//        if (list.contains(StepID)) {
//            cure_case.setText(MapDataUtils.getCureValues(StepID));
//            TargetID = StepID;
//        }

        int position = -1;
        if (!TextUtils.isEmpty(TargetID)) {
            position = lastTargetIds.indexOf(TargetID);
        }
        ListDialog dialog = new ListDialog(lastTargetIds, ListDialog.Step_DIALOG, position);
        dialog.setTitles(getString(R.string.last_target));
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.TARGET_DIALOG);
    }


    private List<String> lastChemistryIds;

    /**
     * 最后有效化疗的dialog
     */
    private void createChemistryDialog() {
//        List<String> list = mWSZLBean.getLastChemistryID();
//        if (list == null && list.isEmpty()) {
//            return;
//        }
//        boolean hasBXY = list.remove(empty_HLY);
//        if (hasBXY) {
//            if (TextUtils.isEmpty(ChemistryID)) {//这个是为了 判断是否 自动选中
//                ChemistryID = empty_HLY;
//            }
//        }
        int position = -1;
        if (!TextUtils.isEmpty(ChemistryID)) {
            position = lastChemistryIds.indexOf(ChemistryID);
        }
        ListDialog dialog = new ListDialog(lastChemistryIds, ListDialog.Step_DIALOG, position);
        dialog.setTitles(getString(R.string.chemistry));
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.CHEMISTRY_DIALOG);
    }

    private void showGeneDialog() {
        List<String> data = GlobalData.gene.get(UserinfoData.getCancerID(this));

        GeneDialog dialog;
        if (chooseGeneList != null && chooseGeneList.size() > 0) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Contants.List, (Serializable) chooseGeneList);
            dialog = new GeneDialog(data, chooseGeneList, 0);
        } else {
            dialog = new GeneDialog(data, null, 0);
        }
        dialog.setDialogFragmentListener(this);
        dialog.show(getFragmentManager(), Contants.GENE_DIALOG);
    }

//    private String[] listToStrings(List<String> list) {
//
//        String[] strings = new String[list.size()];
//        for (int i = 0; i < list.size(); i++) {
//            strings[i] = list.get(i);
//        }
//        return strings;
//    }

    private void createSecondDialog(List<String> data, final TextView tv_right, final boolean isSecond) {
        ListDialog dialog = new ListDialog(data, 0, -1);
        dialog.show(getFragmentManager(), "secord");
    }


    private void createSecondDialog(final String[] strings, final TextView tv_right, final boolean isSecond) {
        final String[] strings1 = new String[strings.length];
        for (int i = 0, j = strings.length; i < j; i++) {
            strings1[i] = MapDataUtils.getAllStepName(strings[i]);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(WSZLActivity.this);
        builder.setTitle("列表选择框");
        builder.setItems(strings1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //which is position
                tv_right.setText(strings1[which]);
//                dxData.put()
                if (isSecond) {
                    ChemistryID = strings[which];
                } else {
                    TargetID = strings[which];
                }

            }
        });
        builder.create().show();
    }

    PatientDetailBean bean;

    private void toResult() {
        if (!TextUtils.isEmpty(GeneID)) {
            bean = new PatientDetailBean();
            bean.setGene(GeneID);
            new UpdatePatientDetailPresenter(bean, this).getData();
        }
        new GetResultPresenter(this).getData();
    }

    private void addFragmet() {
        GanGGFragment fragment = new GanGGFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fagment, fragment, "tag");
        ft.commit();
    }


    public void removefragmet() {
        Fragment fragment = getFragmentManager().findFragmentByTag("tag");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.remove(fragment);
        ft.commit();
    }

//    private String[] showGeneDialog(String cancerID) {
//        List<String> strings = ZYApplication.gene.get(cancerID);
//        String[] strings1;
//        if (strings != null && strings.size() > 0) {
//            strings1 = new String[strings.size()];
//
//            for (int i = 0; i < strings.size(); i++) {
//                String temp = strings.get(i);
//                strings1[i] = ZYApplication.geneValues.get(temp);
//            }
//            return strings1;
//        }
//        return null;
//    }

    private List<String> chooseGeneList;

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        //data即为上传的string
//        String showData = DataUtils.loadStringToShowString(data);
//        cure_case.setText(showData);//
        LogUtil.i(TAG, "传过来的数据" + data);
        FragmentManager fm = getFragmentManager();
        Fragment geneDialog = fm.findFragmentByTag(Contants.GENE_DIALOG);
        Fragment targetDialog = fm.findFragmentByTag(Contants.TARGET_DIALOG);
        Fragment chemistryDialog = fm.findFragmentByTag(Contants.CHEMISTRY_DIALOG);

        if (dialog == targetDialog) {//靶向方案
            LogUtil.i(TAG, data);
            TargetID = data;
            if (lastTargetIds.contains(empty_BXY) && TextUtils.isEmpty(data)) {
                TargetID = empty_BXY;
            }
            cure_case.setText(MapDataUtils.getAllStepName(data));
        }
        if (dialog == chemistryDialog) {//化疗方案
            LogUtil.i(TAG, data);
            ChemistryID = data;
            if (lastChemistryIds.contains(empty_HLY) && TextUtils.isEmpty(data)) {
                ChemistryID = empty_HLY;
            }
            tv_second_drugs.setText(MapDataUtils.getAllStepName(data));
//            new Handler();
        }

        if (dialog == geneDialog) {
            GeneID = data;
            if (chooseGeneList == null) {
                chooseGeneList = new ArrayList<>();
            }
            if (TextUtils.isEmpty(data)) {
                tv_gene.setText("");
                chooseGeneList.clear();
                return;
            }
            String[] temp = data.split(",");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < temp.length; i++) {
                chooseGeneList.add(temp[i]);
                if (i == 0) {
                    sb.append(GlobalData.geneValues.get(temp[i]));
                    continue;
                }
                sb.append("," + GlobalData.geneValues.get(temp[i]));
            }
//            if (TextUtils.isEmpty(sb)) {
//                tv_gene.setText("");
//            } else {
            tv_gene.setText(sb);
//            }
        }
    }

//    private int isBXY(String id) {
//        MapDataUtils.getAllCureconfID(id);
//        return 1;
//    }


}
