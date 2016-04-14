package com.zeyuan.kyq.fragment.main;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.MainListAdapter;
import com.zeyuan.kyq.adapter.MainPagerAdapter;
import com.zeyuan.kyq.app.BasePage;
import com.zeyuan.kyq.app.GlobalUserStepData;
import com.zeyuan.kyq.bean.FindSymtomBean;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.http.bean.UserStepBean;
import com.zeyuan.kyq.page.sideeffect.MainCasePage;
import com.zeyuan.kyq.presenter.FindSymptomPresenter;
import com.zeyuan.kyq.presenter.MainInfoPresenter;
import com.zeyuan.kyq.presenter.PatientDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.DateTime;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserStepHelper;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.DiagnosisResultActivity;
import com.zeyuan.kyq.view.EditStepActivity;
import com.zeyuan.kyq.view.FindSymtomActivity;
import com.zeyuan.kyq.view.PatientDetailActivity;
import com.zeyuan.kyq.view.RecordSymptomActivity;
import com.zeyuan.kyq.view.RecordZhibiaoActivity;
import com.zeyuan.kyq.view.SCListActivity;
import com.zeyuan.kyq.view.StepDetailActivity;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.MyListView;
import com.zeyuan.kyq.widget.MyScrollView;
import com.zeyuan.kyq.widget.WaveHelper;
import com.zeyuan.kyq.widget.WaveView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**Fragment ‘主页’ 展示*/
public class MainFragment extends TabFragment implements View.OnClickListener, MyScrollView.OnScrollListener, ViewDataListener, AdapterView.OnItemClickListener, MainCasePage.OnMoreClickListener {
    private static final String TAG = "MainFragment";
    //    private View rootView;
    //    private TextView showPopu;
//    private RelativeLayout ll;
    //    private CircleImageView leftImage;
//    private CircleImageView rightImage;
//    private CircleImageView bootomImage;
    private MyScrollView myScrollView;
    private TextView check_item_self;
    private ImageView check_item_self_top;
    private RelativeLayout pos1, pos2;//用来判断位置的
    private MyListView mMyListView;
    /**
     * 4个listview
     */
//    private MyListView recoverAndNutritonListView;
//    private MyListView similarCaseLisView;


//    private MyListView sideEffectListView;
//    private MyListView knowledgeListView;
    /**
     * 4个adapter
     */
//    private MainSimilarCaseAdapter similarAdapter;//相似案例
//    private MainRecoverCenterAdapter recoverCenterAdapter;//康复与营养的adapter
//    private SideEffectAdapter sideEffectAdapter;//副作用
//    private KnowledgeAdapter knowledgeAdapter;//知识库

//    private MainInfoPresenter mainInfoPresenter = new MainInfoPresenter(this);//之所以不实例化是因为 怕他得不到回收

    //    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1:
//                    MainPageInfoBean mainInfo = (MainPageInfoBean) msg.obj;
//                    setData(mainInfo);
//                    break;
//            }
//        }
//
//
//    };
    private WaveHelper mWaveHelper;
    private ViewPager mViewPager;


//    /**
//     * @param mainInfo 数据绑定在ui上
//     */
//    private void setData(MainPageInfoBean mainInfo) {
//        similarAdapter = new MainSimilarCaseAdapter(context, mainInfo.getSimilarcaseNum());
//        similarCaseLisView.setAdapter(similarAdapter);
//        recoverCenterAdapter = new MainRecoverCenterAdapter(context, mainInfo.getRecoverCenterNum());
////        recoverAndNutritonListView.setAdapter(recoverCenterAdapter);
//
//        sideEffectAdapter = new SideEffectAdapter(context);
//        sideEffectListView.setAdapter(sideEffectAdapter);
//
//        knowledgeAdapter = new KnowledgeAdapter(context);
//        knowledgeListView.setAdapter(knowledgeAdapter);
//
//
//        similarCaseLisView.setOnItemClickListener(this);
//        recoverAndNutritonListView.setOnItemClickListener(this);
//        sideEffectListView.setOnItemClickListener(this);
//        knowledgeListView.setOnItemClickListener(this);


//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initView();
        setListener();
        initData();

        return rootView;
    }

    private void setListener() {
        change_nurse.setOnClickListener(this);
        avatar.setOnClickListener(this);
        check_item_self.setOnClickListener(this);
        home_tjzz.setOnClickListener(this);
        home_gxjd.setOnClickListener(this);
        home_wdbl.setOnClickListener(this);
        home_zbjl.setOnClickListener(this);
//        bootomImage.setOnClickListener(this);
//        leftImage.setOnClickListener(this);
//        rightImage.setOnClickListener(this);
//        sc_more_info.setOnClickListener(this);
    }

    /**
     * 从网络中获取数据
     */
    private void initData() {
        cancerId = UserinfoData.getCancerID(context);
        String periodId = UserinfoData.getPeriodID(context);
        String headUrl = UserinfoData.getAvatarUrl(context);
        stepId = UserinfoData.getStepID(context);
        String discoverTime = UserinfoData.getDiscoverTime(context);
        if (TextUtils.isEmpty(discoverTime) || TextUtils.isEmpty(stepId) || TextUtils.isEmpty(cancerId) || TextUtils.isEmpty(periodId) || TextUtils.isEmpty(headUrl)) {
            new PatientDetailPresenter(this).getData();
        } else {
            new MainInfoPresenter(this).getData();
        }
    }

    private void getMainData() {
        new MainInfoPresenter(this).getData();
    }


    //    private TextView cancer_dec;//头像下面关于癌症的描述
    private TextView cancer_time;//描述下的时间
//    private int[] resource = {R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4};
//    private ImageView[] images;

    //ggz-
//    private TextView tv_add_symptom;
//    private TextView tv_check;
//    private TextView tv_my_info;
//    ggz+
    private Button home_tjzz;
    private Button home_gxjd;
    private Button home_wdbl;
    private Button home_zbjl;
//~

    private List<BasePage> views;
    private MainListAdapter adapter;
    //    private TextView sc_more_info;
    private List<MainPageInfoBean.MainItemEntity> listData;
    private TextView step;//显示用户在哪个阶段 如：靶向阶段，
    private TextView step_medica;//显示用户在哪个阶段 如：靶向阶段，
    private TextView change_nurse;//小医生的换一换
    private TextView drugs;//page1的计划用药
    private TextView my_little_nurse;//计划用药 4个字 也可能是 副作用 和温馨提示
    private TextView use_durgs;//计划用药 4个字 也可能是 副作用 和温馨提示

    //    private FlowLayout fl;
    private TextView red_dots;

    /**
     * 初始化view
     */
    private void initView() {
//        fl = (FlowLayout) findViewById(R.id.fl);
        change_nurse = (TextView) findViewById(R.id.change_nurse);
        red_dots = (TextView) findViewById(R.id.red_dots);
        use_durgs = (TextView) findViewById(R.id.use_durgs);
        my_little_nurse = (TextView) findViewById(R.id.my_little_nurse);
        drugs = (TextView) findViewById(R.id.drugs);
        step = (TextView) findViewById(R.id.step);
        step_medica = (TextView) findViewById(R.id.step_medica);
//        sc_more_info = (TextView) findViewById(R.id.sc_more_info);
//        tv_add_symptom = (TextView) findViewById(R.id.tv_add_symptom);
//        tv_check = (TextView) findViewById(R.id.tv_check);
//        tv_my_info = (TextView) findViewById(R.id.tv_my_info);
        WaveView waveView = (WaveView) findViewById(R.id.wave);
        waveView.setBorder(0, R.color.light_red);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        waveView.setWaterLevelRatio(0.95f);
        mWaveHelper = new WaveHelper(waveView);
        mViewPager = (ViewPager) findViewById(R.id.vg_simalar_case);
//        ll = (RelativeLayout) findViewById(R.id.current_step);
        mMyListView = (MyListView) findViewById(R.id.listview);
        listData = new ArrayList<>();
        mMyListView.setOnItemClickListener(this);
//        rootView.findViewById(R.id.current_step).setOnClickListener(this);
        avatar = (CircleImageView) rootView.findViewById(R.id.avatar);//头像
        myScrollView = (MyScrollView) rootView.findViewById(R.id.my_sv);//整个滑动器
        cancer_time = (TextView) rootView.findViewById(R.id.cancer_time);//描述下的时间
//        myScrollView.setOnScrollListener(this);
        check_item_self = (TextView) rootView.findViewById(R.id.check_itself);//中间症状自查的按钮
        check_item_self_top = (ImageView) rootView.findViewById(R.id.check_itself_top);//顶部症状自查的按钮
        check_item_self_top.setOnClickListener(this);

        pos1 = (RelativeLayout) rootView.findViewById(R.id.rl1);//上面的布局
        pos2 = (RelativeLayout) rootView.findViewById(R.id.rl_2);//下面的布局
//        initAnimator();

//        leftImage = (CircleImageView) findViewById(R.id.my_info);
//        rightImage = (CircleImageView) findViewById(R.id.check);
//        bootomImage = (CircleImageView) findViewById(R.id.add_symptom);


        //ggz+
        home_tjzz = (Button) findViewById(R.id.home_tjzz);
        home_gxjd = (Button) findViewById(R.id.home_gxjd);
        home_wdbl = (Button) findViewById(R.id.home_wdbl);
        home_zbjl = (Button) findViewById(R.id.home_zbjl);

        cancer_time.setTypeface(Typeface.createFromAsset(context.getAssets(), "font/BEBASNEUE.OTF"));
        //~

        use_durgs.setOnClickListener(this);
        adapter = new MainListAdapter(context, listData);
        mMyListView.setAdapter(adapter);
    }

//    private void initAnimator() {
//        AnimationDrawable animationDrawable = (AnimationDrawable) check_item_self_top.getBackground();
//        animationDrawable.start();
//    }

    private void bindPagerView(List<MainPageInfoBean.SimilarcaseNumEntity> list) {
        views = new ArrayList<>();
        for (MainPageInfoBean.SimilarcaseNumEntity entity : list) {
            MainCasePage page = new MainCasePage(context, entity);
            page.setOnMoreClickListener(MainFragment.this);
            views.add(page);
        }
        MainPagerAdapter adapter = new MainPagerAdapter(views);
        mViewPager.setAdapter(adapter);
        if (list != null && list.size() > 1) {
            mViewPager.setCurrentItem(1);
        }
//        mViewPager.setCurrentItem(15);//只是为了效果
    }

    //        private TextView similar_case_title;//相似案例
//    private TextView recoverAndNutriton_title;//营养与课堂
//    private TextView sideEffect_title;//副作用
//    private TextView knowledge;//知识库

    private void initTitle() {
//        similar_case_title.setText("相似案例");
//        similar_case_title.setTextColor(getResources().getColor(R.color.light_green));
//        recoverAndNutriton_title.setText("康复与营养");
//        recoverAndNutriton_title.setTextColor(getResources().getColor(R.color.purple));
//        sideEffect_title.setText("潜在副作用");
//        sideEffect_title.setTextColor(getResources().getColor(R.color.light_yellow));
//
//        knowledge.setText("知识库");
//        setDrawableLeft(similar_case_title, R.mipmap.simalr_case);
//        setDrawableLeft(recoverAndNutriton_title,R.mipmap.recover_center);
//        setDrawableLeft(sideEffect_title,R.mipmap.nutrients_team);
    }

//    private void setDrawableLeft(TextView v,int id) {
//
//        Drawable drawable= getResources().getDrawable(id);
///// 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        v.setCompoundDrawables(drawable,null,null,null);
//    }

    private boolean flag = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.avatar:
//                scaleAvatar();//放大下头像
                startActivity(new Intent(context, PatientDetailActivity.class));

//                if (flag) {//需求删除了这个动画
//                    flag = false;
//                    openMenu();
//                } else {
//                    flag = true;
//                    closeMenu();
//                }
                break;

            case R.id.check_itself:
            case R.id.check_itself_top: {
//                return;
                startActivity(new Intent(context, FindSymtomActivity.class));
                break;
            }

//            case R.id.current_step: {
//                startActivity(new Intent(getActivity(), StepDetailActivity.class));
//                break;
//            }


            case R.id.home_zbjl: {//指标记录
//                Intent intent = new Intent(context, AddSymptomActivity.class);
                startActivity(new Intent(context, RecordZhibiaoActivity.class));
//                Toast.makeText(context,"暂不清楚对应页面",Toast.LENGTH_SHORT).show();
                break;
            }
//            case R.id.add_symptom:
            case R.id.home_tjzz: {//添加症状
//                Intent intent = new Intent(context, AddSymptomActivity.class);
                startActivity(new Intent(context, RecordSymptomActivity.class));
                break;
            }

//            case R.id.check:
            case R.id.home_gxjd: {//编辑阶段
//                Intent intent = new Intent(context, UpdatePatientDetailPresenter.class);
                startActivity(new Intent(context, EditStepActivity.class));
                break;
            }
//            case R.id.my_info:
            case R.id.home_wdbl://患者详情
//                startActivity(new Intent(context, PatientDetailActivity.class));
//                startActivity(new Intent(context, PatientDetailActivity.class));
                startActivity(new Intent(context, StepDetailActivity.class));
                break;
//            case R.id.sc_more_info: {
//                startActivity(new Intent(context, MainMoreActivity.class));
//
//                break;
//            }
            case R.id.change_nurse: {//小医生的换一换
                //ggz-
                //switchYS(1);
                //ggz+
                showNextXys();
                //~
                break;
            }

            case R.id.use_durgs: {
//ggz-
//                switch (position) {
//                    case 0:
//                    case 1: {
//                        AddPlanMedicine();
//                        break;
//                    }
//
//                    case 2: {
//
//                        break;
//                    }
//                }
                //ggz+
                if (mCurXysType == 2) {//计划用药的立即用药点击
                    if (!TextUtils.isEmpty(PlanID)) {
                        showAddStepDialog();
//                        ljyyOnClick();
                        return;
                    }
//                    AddPlanMedicine();
                } else if (mCurXysType == 4) {//温馨提示的查看解决办法
//                    Toast.makeText(context, "温馨提示，请求数据接口GetCommonDetails，跳转至结果详情ReturnDetailsActivity", Toast.LENGTH_SHORT).show();
                    resultDetail();
                }
                //~
                break;
            }
            default: {
                Toast.makeText(context, "unkonw onclick id", Toast.LENGTH_SHORT).show();
            }
            // throw new RuntimeException();
        }
    }

    private void resultDetail() {
        new FindSymptomPresenter(this).getData();
    }

//    public void scrollTop() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                myScrollView.fullScroll(ScrollView.FOCUS_UP);
//            }
//        }, 0);
//    }

//    private void AddPlanMedicine() {
////        addStep();
////        addGuideImage();
////        new SetPlanMedicinePresenter(this).getData();
//        Toast.makeText(context, "缺少个立即用药的接口药物的id 是:" + PlanID, Toast.LENGTH_SHORT).show();
//
//
//    }

    //    private int position = 0;
//    private static final int PLAN = 0;//计划用药
//    private static final int TISHI = 1;//温馨提示
//
//    private void switchYS(int i) {
//        switch (position) {
//            case PLAN: {
//                initPlan();
//                position++;
//                break;
//            }
//            case TISHI: {
//                initTishi();
//                position++;
//                break;
//            }
////            case 2: {
////                position++;
////                break;
////            }
//        }
//        if (position == 2) {
//            position = PLAN;
//        }
//    }
    private String stepId;
    private String cancerId;
    private int mXysIndex = -1;
    private int mCurXysType = -1;

    private void setHSPerformId(String performid) {
//        String performid = GlobalData.performid;
        if (!TextUtils.isEmpty(performid)) {
            huShi.get(1).setPerformid(performid);
        }
        showNextXys();
    }


    private void setHSPlanId(String planId) {
        huShi.get(0).setStepID(planId);
        showNextXys();
    }


    private void showNextXys() {
        mXysIndex = (++mXysIndex) % huShi.size();
        MainPageInfoBean.NurseNumEntity item = huShi.get(mXysIndex);

        //判断数据类型
//        if (mXysIndex == 0)//计划用药类型
//        {
//            if ("1".equals(item.getPageid()) && "0".equals(item.getStepID())) {//没有计划用药
//                mCurXysType = 1;
//            } else {
//                mCurXysType = 2;
//            }
//        } else if ((mXysIndex == 1) && "0".equals(item.getPerformid())) {//温馨提示
//            mCurXysType = 3;
//        } else {
//            mCurXysType = 4;
//        }

        if ("1".equals(item.getPageid())) {//这个是计划用药类型
            if ("0".equals(item.getStepID())) {
                mCurXysType = 1;
            } else {
                mCurXysType = 2;
            }
        } else if ("3".equals(item.getPageid())) {//温馨提示
            if ("0".equals(item.getPageid())) {
                mCurXysType = 3;
            } else {
                mCurXysType = 4;
            }
        } else if ("5".equals(item.getPageid())) {//这个是公告类型 现在公告都是有数据
//            if()
            mCurXysType = 5;

        }


        switch (mCurXysType) {

            case 1://计划用药，无数据
                //zp
                HsNodata(R.string.plan_drugs, R.string.plan_txt);
                break;
            case 2://计划用药，有数据
                planHasData(item);
                break;
            case 3://温馨提示无数据
                HsNodata(R.string.wx_tips, R.string.tips);
                break;
            case 4://温馨提示有数据
                tishiHasData(item);
                break;
            case 5:
                my_little_nurse.setText(R.string.notice);
                red_dots.setVisibility(View.GONE);
                use_durgs.setVisibility(View.GONE);
                drugs.setText(item.getContent());
                break;
        }
    }

    private void tishiHasData(MainPageInfoBean.NurseNumEntity item) {
        PerformID = item.getPerformid();
        String tishi = getString(R.string.advice_tips, MapDataUtils.getPerform(PerformID));
        my_little_nurse.setText(R.string.wx_tips);
        drugs.setText(tishi);
        red_dots.setVisibility(View.GONE);
        use_durgs.setVisibility(View.VISIBLE);
        use_durgs.setText(R.string.find_plan);
    }

    private void planHasData(MainPageInfoBean.NurseNumEntity item) {
        my_little_nurse.setText(R.string.plan_drugs);
        PlanID = item.getStepID();
        drugs.setText(MapDataUtils.getAllStepName(PlanID));
        use_durgs.setVisibility(View.VISIBLE);
        use_durgs.setText(R.string.use_now);
        red_dots.setVisibility(View.VISIBLE);
    }

    /**
     * 小护士 某个项没有数据的时候
     *
     * @param plan_drugs
     * @param plan_txt
     */
    private void HsNodata(int plan_drugs, int plan_txt) {
        my_little_nurse.setText(plan_drugs);
        drugs.setText(plan_txt);
        use_durgs.setVisibility(View.GONE);
        red_dots.setVisibility(View.GONE);
    }

//    private void scaleAvatar() {
//        ObjectAnimator animator = ObjectAnimator.ofFloat(avatar, "scaleX", 1f, 1.2f, 1f);
//        ObjectAnimator animator1 = ObjectAnimator.ofFloat(avatar, "scaleY", 1f, 1.2f, 1f);
//        AnimatorSet set = new AnimatorSet();
//        set.setDuration(200);
//        set.play(animator).with(animator1);
//        set.start();
//    }

    @Override
    public void onScroll(int scrollY) {
//        int height = pos1.getHeight() + pos2.getHeight();
//        if (height > scrollY) {
//            check_item_self.setVisibility(View.GONE);
//        } else {
//            check_item_self.setVisibility(View.VISIBLE);
//        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (parent == similarCaseLisView) {
//            startActivity(new Intent(context, ForumDetailActivity.class));
//        }
//        if (parent == recoverAndNutritonListView) {
//            startActivity(new Intent(context, ForumDetailActivity.class));
//        }
//        if (parent == sideEffectListView) {
//
//        }
//        if (parent == knowledgeListView) {
//
//        }
//    }

//    /**
//     * 点击头像 图片展开
//     */
//    private void openMenu() {
//        AnimatorSet set = new AnimatorSet();
//        /**
//         * 左边的图片动画
//         */
//        leftImage.setVisibility(View.VISIBLE);
//        ObjectAnimator animatorlLeftImage = ObjectAnimator.ofFloat(leftImage, "TranslationX", avatar.getLeft() - avatar.getWidth() / 2 + leftImage.getWidth() / 2, 0);
//        animatorlLeftImage.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                tv_add_symptom.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
////        animatorlLeftImage.start();
//        /**
//         * 右边的图片
//         */
//        rightImage.setVisibility(View.VISIBLE);
//        ObjectAnimator animatorRightImage = ObjectAnimator.ofFloat(rightImage, "TranslationX", -(avatar.getLeft() - avatar.getWidth() / 2 + leftImage.getWidth() / 2), 0);
//        animatorRightImage.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                tv_check.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
////        animatorRightImage.start();
//
//        /**
//         * 底部的图片
//         */
//        bootomImage.setVisibility(View.VISIBLE);
//        ll.setVisibility(View.INVISIBLE);
//        ObjectAnimator bootomAnimation = ObjectAnimator.ofFloat(bootomImage, "TranslationY", -(avatar.getTop() + avatar.getWidth() / 2 + bootomImage.getWidth() / 2), 0);
//        bootomAnimation.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                tv_my_info.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
////        bootomAnimation.start();
//
//        set.play(animatorlLeftImage).with(animatorRightImage).with(bootomAnimation);
//        set.start();
//    }
//
//    //
////
//    private void closeMenu() {
//        AnimatorSet set = new AnimatorSet();
//        /**
//         * 左边的图片动画
//         */
//        ObjectAnimator leftAnimation = ObjectAnimator.ofFloat(leftImage, "TranslationX", 0, (avatar.getLeft() - avatar.getWidth() / 2 + leftImage.getWidth() / 2));
//        leftAnimation.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                tv_add_symptom.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                leftImage.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
////        leftAnimation.start();
//
//        /**
//         * 右边图片的动画
//         */
//        rightImage.setVisibility(View.VISIBLE);
//        ObjectAnimator rigthAnimation = ObjectAnimator.ofFloat(rightImage, "TranslationX", 0, -(avatar.getLeft() - avatar.getWidth() / 2 + rightImage.getWidth() / 2));
//        rigthAnimation.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                tv_check.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                rightImage.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
////        rigthAnimation.start();
//
//        /**
//         * 下边图片的动画
//         */
//        ObjectAnimator bootomAnimation = ObjectAnimator.ofFloat(bootomImage, "TranslationY", 0, -(avatar.getLeft() - avatar.getWidth() / 2 + rightImage.getWidth() / 2));
//        bootomAnimation.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//                tv_my_info.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                bootomImage.setVisibility(View.GONE);
//                ll.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        });
//
//        set.play(leftAnimation).with(rigthAnimation).with(bootomAnimation);
//        set.start();
//    }

    //    private String StepID = UserinfoData.getStepID(context);
//    private String CityID = "440300";
//    private String ProvinceID = "440000";
    private String PlanID;//立即用药的id


    private String PerformID;//查看解决方案的id
    private String Type = "2";//type为1通过 查症状进副作用结果详情，
    // type为2通过 副作用列表/小医生进副作用结果详情
    private String CureConfID;

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(context));
        if (tag == 0) {//获取主页详情
//            map.put(Contants.StepID, UserinfoData.getStepID(context));//先不考虑用户本地没有这些数据的情况
            map.put(Contants.StepID, stepId);//先不考虑用户本地没有这些数据的情况
            map.put(Contants.CityID, UserinfoData.getCityID(context));
            map.put(Contants.ProvinceID, UserinfoData.getProvinceID(context));
            map.put(Contants.CancerID, UserinfoData.getCancerID(context));
            map.put(Contants.CureConfID, MapDataUtils.getAllCureconfID(stepId));

            map.put("v", DataUtils.getVersionName(context));
//          map.put("v", "0.9");//just test
            map.put("t", "2");//1是ios 2是android
        }

        if (tag == NetNumber.SET_PLAN_MEDICINE) {//添加到计划用药
            map.put(Contants.PlanStepID, PlanID);
        }
//        if (tag == 3) {//跳转到副作用 结果详情
//            map.put(Contants.CureConfID, CureConfID);
//            map.put(Contants.PerformID, PerformID);
//            map.put(Contants.Type, Type);
//        }

        if (tag == NetNumber.FIND_SYMPTOM) {
            map.put(Contants.StepID, UserinfoData.getStepID(context));
            map.put(Contants.CancerID, UserinfoData.getCancerID(context));
            map.put(Contants.PerformID, PerformID);
        }

        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        if (position == 0) {
            MainPageInfoBean infoBean = (MainPageInfoBean) t;
            bindView(infoBean);
            showViewVisiable();
            MainPageInfoBean.UpEntity upEntity = infoBean.getUp();
            if (upEntity == null) {
                return;
            }
            //强制更新
            if ("1".equals(upEntity.getU())) {
                updateDialog(upEntity, true);
            } else if ("2".equals(upEntity.getU())) {
                //更新提醒 1天一次
                if (UserinfoData.compareTime(context)) {
                    UserinfoData.saveRemindTime(context, System.currentTimeMillis() + "");
                    updateDialog(upEntity, false);
                }
            }


        }


        if (position == NetNumber.PATIENT_DETAIL) {
            PatientDetailBean patientDetailBean = (PatientDetailBean) t;
            if (Contants.RESULT.equals(patientDetailBean.iResult)) {
                UserinfoData.saveUserData(context, patientDetailBean);
                stepId = UserinfoData.getStepID();
                getMainData();
            }
        }
        if (position == NetNumber.FIND_SYMPTOM) {
            FindSymtomBean bean = (FindSymtomBean) t;
            if (Contants.RESULT.equals(bean.getIResult())) {
                toResult(bean);
            }
        }


        switch (position) {

            case UserStepHelper.ReqCode_UserStepAdd: {
                mUserStepBeanNew = null;
                List<UserStepBean> lstDataMdf = (List<UserStepBean>) t;

                if (lstDataMdf.get(0).isEndTimeLast()) {
//                    new AlertView("定制内容已更新", "抗癌圈已为您重新匹配最相关的文章、相似案例和圈子等内容，您仍可在“更多圈子”页面继续关注曾经的圈子。", null, new String[]{"知道了"}, null, context, AlertView.Style.Alert, null).setCancelable(true).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("定制内容已更新");
                    builder.setMessage("抗癌圈已为您重新匹配最相关的文章、相似案例和圈子等内容，您仍可在“更多圈子”页面继续关注曾经的圈子。");
                    builder.setPositiveButton("知道了", null);
                    builder.create().show();


                } else {
                    Toast.makeText(context, "阶段添加成功！", Toast.LENGTH_SHORT).show();
                }
                UserStepBean beans = GlobalUserStepData.findLastUserStep();
                if (beans != null) {
                    String stepId = beans.getStepID();
                    UserinfoData.saveStepID(context, stepId);
                }
                UserinfoData.planId = null;//清空下计划用药的id
                initData();

            }
            break;


            case UserStepHelper.ReqCode_UserStepDel:
                Toast.makeText(context, "阶段删除成功！", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    UserStepBean bean = (UserStepBean) t;
                    if (bean.$isEditor()) {
                        mUserStepBeanNew = null;
                    }
                }
                break;
            case UserStepHelper.ReqCode_UserStepMdf0:
                mUserStepBeanMdf = null;
                if (mUserStepBeanNew != null && mUserStepBeanNew.getStepUID() == null) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ggzUserStepAdd();
                        }
                    }, 300);
                } else {
                    UserStepBean beans = GlobalUserStepData.findLastUserStep();
                    if (beans != null) {
                        String stepId = beans.getStepID();
                        UserinfoData.saveStepID(context, stepId);
                    }
                }
                Toast.makeText(context, "阶段更新成功！", Toast.LENGTH_SHORT).show();
                break;
            case UserStepHelper.ReqCode_UserStepMdf1ConfirmDelSpace: {
//                new AlertView("提醒", "需要删除空窗期，及空窗期内的指标症状数据，您确定吗", null, new String[]{"取消", "确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (position == 1) {
//                            UserStepHelper.reqUserStepMdf1DelSpace();
//                        }
//                    }
//                }).setCancelable(true).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提醒");
                builder.setMessage("需要删除空窗期，及空窗期内的指标症状数据，您确定吗?");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserStepHelper.reqUserStepMdf1DelSpace();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserStepHelper.reqUserStepAll(MainFragment.this);
                    }
                });
                builder.create().show();


                break;
            }
            case UserStepHelper.ReqCode_UserStepMdf2ConfirmDelChild: {
//                new AlertView("提醒", "阶段时间修改，需要删除指标或症状数据", null, new String[]{"取消", "确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (position == 1) {
//                            UserStepHelper.reqUserStepMdf2DelChild();
//                        }
//                    }
//                }).setCancelable(true).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("提醒");
                builder.setMessage("阶段时间修改，需要删除指标或症状数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserStepHelper.reqUserStepMdf2DelChild();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserStepHelper.reqUserStepAll(MainFragment.this);
                    }
                });
                builder.create().show();


            }
            break;

//            case UserStepHelper.ReqCode_UserStepAll: {
////                ggzUserStepAdd();
//                ljyyOnClick();
//                break;
//            }

        }
    }

    private void toResult(FindSymtomBean bean) {
        Intent intent = new Intent(context, DiagnosisResultActivity.class);
        intent.putExtra(Contants.FindSymtomBean, bean);
        intent.putExtra(Contants.PerformID, PerformID);
        startActivity(intent);//跳转到诊断结果
    }


//    private void addStep() {
//        UserStepHelper.reqUserStepAll(this);
//    }

    /**
     * 打开浏览器
     * @param url
     */
    private void openUrl(String url) {
        Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
        startActivity(it);
    }

    /**
     * @param entity
     * @param isQZ   是否是强制更新 true强制 false 不是
     */
    private void updateDialog(final MainPageInfoBean.UpEntity entity, final boolean isQZ) {
        String isQuit = "取消";
        if (isQZ) {
            isQuit = "退出";
        }
        new AlertView("有新版本", entity.getM(), null, new String[]{isQuit, "确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0: {
                        if (isQZ) {
                            System.exit(0);
                        }
                        break;
                    }
                    case 1: {
                        openUrl(entity.getL());
                        if (isQZ) {
                            System.exit(0);
                        }
                        break;
                    }
                }
            }
        }).show();
    }

//    private void saveData(PatientDetailBean patientDetailBean) {
//        UserinfoData.saveCancerID(context, patientDetailBean.getCancerID());
//        UserinfoData.saveDiscoverTime(context, patientDetailBean.getDiscoverTime());
//        UserinfoData.savePeriodID(context, patientDetailBean.getPeriodID());
//        UserinfoData.saveAvatarUrl(context, patientDetailBean.getHeadimgurl());
//        UserinfoData.saveInfoname(context, patientDetailBean.getInfoName());
//        UserinfoData.saveProvinceID(context, patientDetailBean.getProvince());
//        UserinfoData.saveCityID(context, patientDetailBean.getCity());
//    }

    private void bindView(MainPageInfoBean bean) {
        showAvatar();
//        showNextXys();
//        String stepUid = bean.getStepUID();//这儿对stepuid 做全局变量处理 后台需求
//         bean.getCureConfID();
//        GlobalData.stepUid = stepUid;
//        String stepId = UserinfoData.getStepID();
        CureConfID = MapDataUtils.getAllCureconfID(stepId);
        String stepName = MapDataUtils.getCureValues(CureConfID);
        step.setText(stepName + context.getString(R.string.main_step));//设置 靶向药阶段
//        if (!Contants.NO_DATA.equals(stepId) && !TextUtils.isEmpty(stepId)) {
//            UserinfoData.saveStepID(context, stepId);
//        }

//        if (!TextUtils.isEmpty(stepId)) {
//            int id = Integer.valueOf(stepId);
//            if (id > 100 * 10000) {//说明是用户乱选的 显示下面的StepUserSelectIDs
//                List<String> selectIDs = bean.getStepUserSelectIDs();
//            } else {
        step_medica.setText(MapDataUtils.getAllStepName(stepId));//设置阶段所使用的药物。
//            }
//        }

        List<MainPageInfoBean.NurseNumEntity> list = bean.getNurseNum();
        huShi = new ArrayList<>();
        if (list != null && list.size() > 0) {
            huShi.addAll(list);
            showNextXys();
        }
        setShowDay(UserinfoData.getDiscoverTime(context));//显示天数
        List<MainPageInfoBean.SimilarcaseNumEntity> list1 = bean.getSimilarcaseNum();
        if (list1 != null && list1.size() > 0) {//绑定viewpager 即相似案例
            findViewById(R.id.layout_viewpager).setVisibility(View.VISIBLE);
            bindPagerView(list1);
        } else {
            findViewById(R.id.layout_viewpager).setVisibility(View.GONE);
        }
        bindListView(bean);//绑定所有的listview数据

        if (!UserinfoData.getFirstGuide(context)) {
            addGuideImage();
        }
    }

//    private void showAvatar() {
//        String imageUrl = UserinfoData.getAvatarUrl(context);
//        if (!TextUtils.isEmpty(imageUrl)) {
//        Glide.with(context).load(imageUrl).signature(new IntegerVersionSignature(1)).error(R.mipmap.default_avatar).into(avatar);
//    }
//
//}

    private List<MainPageInfoBean.NurseNumEntity> huShi;

//    /**
//     * 根据后台数据设置小医生
//     */
//    private void setHushi() {
//        initPlan();//
//    }

//    /**
//     * 温馨提示
//     */
//    private void initTishi() {
//        MainPageInfoBean.NurseNumEntity item = huShi.get(1);
//        my_little_nurse.setText(R.string.wx_tips);
//        String performID = item.getPerformid();
//        if ("0".equals(performID)) {//说明没有症状
//            drugs.setText(R.string.tips);
//            use_durgs.setVisibility(View.GONE);
//        } else {
//            String adviceTips = String.format(context.getResources().getString(R.string.advice_tips), MapDataUtils.getPerform(performID));
//            drugs.setText(adviceTips);
//            use_durgs.setText(R.string.find_plan);
//        }
//    }

//    /**
//     * 初始化 计划用药
//     */
//    private void initPlan() {
//        MainPageInfoBean.NurseNumEntity item = huShi.get(0);
//        my_little_nurse.setText(R.string.plan_drugs);
//        String stepID = item.getStepID();
//        if ("0".equals(stepID)) {//说明没有计划用药
//            drugs.setText(R.string.plan_txt);
//            use_durgs.setVisibility(View.GONE);
//        } else {
//            PlanID = stepID;
//            drugs.setText(MapDataUtils.getAllStepName(stepID));
//            use_durgs.setText(R.string.use_now);
//            use_durgs.setVisibility(View.VISIBLE);
//        }
//    }


    /**
     * 绑定文章的listview
     *
     * @param bean
     */
    private void bindListView(MainPageInfoBean bean) {
        List<MainPageInfoBean.MainItemEntity> listRecover = bean.getRecoverCenterNum();
        List<MainPageInfoBean.MainItemEntity> listEffect = bean.getEffect();
        List<MainPageInfoBean.MainItemEntity> listKnowlege = bean.getKnowlege();
        listData.clear();
        if (listEffect != null && listEffect.size() > 0) {
//			listData.clear();
            listData.addAll(listEffect);
            LogUtil.i(TAG, "副作用的条目：" + listRecover.size());
        }
        if (listKnowlege != null && listKnowlege.size() > 0) {
            listData.addAll(listKnowlege);
            LogUtil.i(TAG, "知识库的条目：" + listRecover.size());
        }
        if (listRecover != null && listRecover.size() > 0) {
            listData.addAll(listRecover);
            LogUtil.i(TAG, "康复中心的条目：" + listRecover.size());
        }
        LogUtil.i(TAG, "装的数据是：" + listData.toString());
        if (listData != null && listData.size() > 0) {
//            adapter = new MainListAdapter(context, listData);
//            mMyListView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            //ggz+滚蛋到顶部
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    myScrollView.fullScroll(ScrollView.FOCUS_UP);
//                }
//            }, 300);
            //~
        }
    }

    private Handler mHandler = new Handler();

    private void setShowDay(String time) {
        if (TextUtils.isEmpty(time)) {
            return;
        }
        String showTime = timeToDate(time);
        setTextChange(showTime);//设置抗癌天数
    }

    /**
     * 设置个动画使值在3秒内跑完
     *
     * @param time
     */
    private void setTextChange(String time) {
        int data = Integer.valueOf(time);
        ValueAnimator animator = ValueAnimator.ofInt(0, data);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int time = (int) animation.getAnimatedValue();
                String showTime = String.valueOf(time);
                setDataText(showTime);
            }
        });
        animator.setDuration(1000).start();
    }

    /**
     * 设置 字体大小不一致
     *
     * @param showTime
     */
    private void setDataText(String showTime) {
//        ggz-
//        Spannable wordToSpan = new SpannableString(showTime.concat("天"));
//      int index = wordToSpan.length();
//      wordToSpan.setSpan(new AbsoluteSizeSpan(DensityUtils.spToPx(context, 36)), 0, index - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//设置大小不一样的字体
//      wordToSpan.setSpan(new AbsoluteSizeSpan(DensityUtils.spToPx(context, 24)), index - 1, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        cancer_time.setText(wordToSpan);
        //~

        //ggz+
        Spannable wordToSpan = new SpannableString(showTime);
        wordToSpan.setSpan(new AbsoluteSizeSpan(DensityUtils.spToPx(context, 22)), 0, wordToSpan.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        cancer_time.setText(wordToSpan);
        //~
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
    }

    private void showViewVisiable() {
        myScrollView.setVisibility(View.VISIBLE);
        View v = findViewById(R.id.pd);
        v.setVisibility(View.GONE);
    }

    @Override
    public void showError(int tag) {

    }

    /**
     * 根据后台传的discovertime 是离1970的秒数 返回离现在的天数
     *
     * @return
     */
    public String timeToDate(String loadingtime) {
        Long time1 = Long.valueOf(loadingtime);
        long currentTime = System.currentTimeMillis();
        long time = currentTime / 1000;
        long times = time - time1;
        long dates = times / (24 * 60 * 60);
        String date = String.valueOf(dates);
        return date;
    }


    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();
        if (avatar != null) {
            showAvatar();
        }
        String performid = UserinfoData.performid;//设置症状的id
        if (!TextUtils.isEmpty(performid)) {
            setHSPerformId(performid);
        }

        String planid = UserinfoData.planId;
//        LogUtil.i(TAG, PlanID + "");
        if (!TextUtils.isEmpty(planid)) {//设置计划用药的id
            PlanID = planid;
            setHSPlanId(PlanID);
        } else {
//            if (huShi != null && huShi.size() > 0) {
//                String planid = huShi.get(0).getStepID();
//                if (!TextUtils.isEmpty(planid)) {
//                    PlanID = planid;
//                }
//            }
        }

        //设置stepid 和  什么 阶段
        //用户在其他界面修改了阶段药物  及时更新
        String stepID = UserinfoData.getStepID();
        String CureConfID = MapDataUtils.getAllCureconfID(stepID);
        String stepName = MapDataUtils.getCureValues(CureConfID);
        if (step != null && !TextUtils.isEmpty(stepName)) {
            step.setText(stepName + context.getString(R.string.main_step));//设置 靶向药阶段
        }
//        if (!Contants.NO_DATA.equals(stepId) && !TextUtils.isEmpty(stepId)) {
//            UserinfoData.saveStepID(context, stepId);
//        }

//        if (!TextUtils.isEmpty(stepId)) {
//            int id = Integer.valueOf(stepId);
//            if (id > 100 * 10000) {//说明是用户乱选的 显示下面的StepUserSelectIDs
//                List<String> selectIDs = bean.getStepUserSelectIDs();
//            } else {
        //用户在其他界面修改了阶段药物
        if (step_medica != null && !TextUtils.isEmpty(stepID)) {
            step_medica.setText(MapDataUtils.getAllStepName(stepID));//设置阶段所使用的药物。
        }
        if (cancer_time != null) {
            String discoverTime = UserinfoData.getDiscoverTime(context);
            if (!TextUtils.isEmpty(discoverTime)) {
                setShowDay(discoverTime);
            }
        }
        //用户是否更改了 癌肿和阶段信息 是 则重新请求数据
        if ((!TextUtils.isEmpty(cancerId) && !cancerId.equals(UserinfoData.getCancerID(context)) || !TextUtils.isEmpty(stepId) && !stepId.equals(UserinfoData.getStepID()))) {
            initData();
        }


        UserStepHelper.reqUserStepAll(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String index = adapter.getItem(position);//这个是文章的index
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
        intent.putExtra(Contants.ARTICLIE_ID, index);
        startActivity(intent);
    }

    /**
     * 去相似案例列表
     */
    @Override
    public void onClickListener() {
        startActivity(new Intent(context, SCListActivity.class));
//        new GetSimilarListPresenter(this).getData();
    }

    //待添加的阶段对象
    private UserStepBean mUserStepBeanNew;
    private UserStepBean mUserStepBeanMdf;

    /**
     * 点击立即用药的时候调用这个方法//ggz:W
     */
    private void ggzUserStepAdd() {
//        mUserStepBeanMdf = GlobalUserStepData.findLastUserStep();

//        //1.把前一个阶段的结束时间改为前一天(存在之前阶段的话)
//        if (mUserStepBeanMdf != null) {
//            if (DateTime.from(mUserStepBeanMdf.getCompareDateBeg() * 1000).toDateString().equals(DateTime.now().toDateString())) {
//                Toast.makeText(context, "添加失败，今天已有一个最新阶段", Toast.LENGTH_SHORT).show();
//                return;
//            } else {
//                mUserStepBeanMdf.setEndTime(DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds() - 1);
//                mUserStepBeanMdf.$isChanged(true);
//            }
//        }
//
//        //2. 新加一个阶段的结束时间改为最新, 起始时间为当前时间
//        mUserStepBeanNew = new UserStepBean();
//
//        mUserStepBeanNew.$isEditor(true);
//        mUserStepBeanNew.setIsMedicineValid(1);//设置为有效
//        mUserStepBeanNew.setStepID(PlanID);//ggz:W 设置药阶段
//        mUserStepBeanNew.setBeginTime(DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds());
//        mUserStepBeanNew.setEndTime(Long.parseLong(UserStepBean.TIME_LAST));
//        GlobalUserStepData.getUserStepList().add(mUserStepBeanNew);

        //3.验证添加

        final List<UserStepBean> lstBeanAdd = new ArrayList<>(1);
        lstBeanAdd.add(mUserStepBeanNew);

        //先验证请求的数据是否有效
        final UserStepHelper.ValidUserStepModify result = UserStepHelper.validUserStepAdd(GlobalUserStepData.getUserStepList(), lstBeanAdd);
        if (result.valid) {
            if (mUserStepBeanMdf == null) {//if (mUserStepBeanMdf != null) {
                String text = UserStepHelper.reqUserStepAddCancerPlan(this, lstBeanAdd);
                if (text != null) {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            } else {
                ggzUserStepMdf(mUserStepBeanMdf);//先更新，再添加
            }
        } else {
            final List<UserStepBean> lstNeedDelSpace = result.mNeedDelSpace;
            if (lstNeedDelSpace != null && !lstNeedDelSpace.isEmpty()) {
//                new AlertView("继续保存？", "继续保存会删除相邻的" + lstNeedDelSpace.size() + "个空窗期及其所含的记录", null, new String[]{"取消", "继续保存"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
//                    @Override
//                    public void onItemClick(Object o, int position) {
//                        if (position == 1) {
//                            deleteEmptyStep(lstNeedDelSpace, lstBeanAdd);
//                        }
//                    }
//                }).setCancelable(true).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
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
                Toast.makeText(context, result.errmsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void deleteEmptyStep(List<UserStepBean> lstNeedDelSpace, final List<UserStepBean> lstBeanAdd) {
        //先删除空窗期
        for (int i = 0; i < lstNeedDelSpace.size(); i++) {
            UserStepHelper.reqUserStepDel(MainFragment.this, lstNeedDelSpace.get(i));
        }
        //再执行添加操作
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String errmsg = UserStepHelper.reqUserStepAddCancerPlan(MainFragment.this, lstBeanAdd);
                if (errmsg != null) {
                    Toast.makeText(context, errmsg, Toast.LENGTH_SHORT).show();
                }
            }
        }, 300);
    }

    private void ggzUserStepMdf(UserStepBean bean) {
        List<UserStepBean> lstBeanMdf = new ArrayList<>(1);
        lstBeanMdf.add(bean);
        if (!lstBeanMdf.isEmpty()) {
            String errmsg = UserStepHelper.reqUserStepMdf(this, lstBeanMdf);//, lstBean
            if (errmsg != null) {
                Toast.makeText(context, errmsg, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void ljyyOnClick() {
        mUserStepBeanMdf = GlobalUserStepData.findLastUserStep();

        //1.把前一个阶段的结束时间改为前一天(存在之前阶段的话)
        if (mUserStepBeanMdf != null) {
            if (DateTime.from(mUserStepBeanMdf.getCompareDateBeg() * 1000).toDateString().equals(DateTime.now().toDateString())) {
                Toast.makeText(context, "添加失败，今天已有一个最新阶段", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mUserStepBeanMdf.setEndTime(DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds() - 1);
                mUserStepBeanMdf.$isChanged(true);
            }
        }
//        if (mUserStepBeanNew == null) {
        //2. 新加一个阶段的结束时间改为最新, 起始时间为当前时间
        mUserStepBeanNew = new UserStepBean();
        mUserStepBeanNew.$isEditor(true);
        mUserStepBeanNew.setIsMedicineValid(1);//设置为有效
        mUserStepBeanNew.setStepID(PlanID);//ggz:W 设置药阶段
        mUserStepBeanNew.setBeginTime(DateTime.from(DateTime.now().toDateString(), "yyyy-MM-dd").toTimeSeconds());
        mUserStepBeanNew.setEndTime(Long.parseLong(UserStepBean.TIME_LAST));
        GlobalUserStepData.getUserStepList().add(mUserStepBeanNew);
//        }
        ggzUserStepAdd();
    }

    private int[] imageResource = {R.mipmap.main_guide1, R.mipmap.main_guide2, R.mipmap.main_guide3, R.mipmap.main_guide4};
    private int guideIndex = 0;

    /**
     * 添加引导图片
     */
    public void addGuideImage() {
        if (guideIndex == imageResource.length) {
            UserinfoData.saveFirstGuide(context);
            return;
        }
        final ViewGroup decorView = (ViewGroup) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);//查找通过setContentView上的根布局
//        if(view==null)return;
//        if(!TextUtils.isEmpty(appContext.getProperty("foodTips"))){
        //引导过了
//            return;
//        }
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        final ViewGroup rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_main_guide, decorView, false);
//
//        rootView.setLayoutParams(new FrameLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
//        ));

//        ViewParent viewParent = view.getParent();
//        if(viewParent instanceof FrameLayout){
//            final FrameLayout frameLayout = (FrameLayout)viewParent;
////            if(guideResourceId!=0){//设置了引导图片
        final ImageView guideImage = new ImageView(getActivity());
//                //guideImage.setAlpha(5);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        guideImage.setLayoutParams(params);
        guideImage.setScaleType(ImageView.ScaleType.FIT_XY);
        guideImage.setImageResource(imageResource[guideIndex]);
        guideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decorView.removeView(guideImage);
                guideIndex++;
                addGuideImage();
            }
        });
        decorView.addView(guideImage);//添加引导图片
//        decorView.addView(rootView);
//            }
//        }
    }

    //    private AlertView alertView;
    private void showAddStepDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("确定使用计划？");
        builder.setMessage("开始用后，系统会为您重新匹配最相关的文章、相似案例和圈子等内容！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ljyyOnClick();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();
//        if (alertView == null) {
//            alertView = new AlertView(context);
//        }
//        new AlertView("确定使用计划？", "开始用后，系统会为您重新匹配最相关的文章、相似案例和圈子等内容！", null, new String[]{"取消", "确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
//            @Override
//            public void onItemClick(Object o, int position) {
//                if (position == 1) {
//                    UserStepHelper.reqUserStepDel((ViewDataListener) getContext(), bean);
//                }
//            }
//        }).setCancelable(true).show();
    }

}
