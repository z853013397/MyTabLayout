package com.zeyuan.kyq.view;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.fragment.main.CircleFragment;
import com.zeyuan.kyq.fragment.main.MainFragment;
import com.zeyuan.kyq.fragment.main.MoreFragment;
import com.zeyuan.kyq.fragment.main.TabFragment;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;

/**
 * 固定方向哈
 * 主页面
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";

    private Button[] mTabs;
    private Button mainButton;
    private Button circleButton;
    private Button moreButton;
    private RadioGroup bottomRG;
    private MainFragment mainFragment;
    private CircleFragment circleFragment;
    private MoreFragment moreFragment;
    private TabFragment[] fragments;
    private int index;
    private int currentTabIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            initView(savedInstanceState);
        } else {
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.remove(circleFragment).remove(moreFragment).remove(mainFragment).commit();
//            startActivity(new Intent(this, SplashActivity.class));
//            finish();
        }
        LogUtil.i(TAG, "savedInstanceState == null?" + (savedInstanceState == null));
//        intFragment(savedInstanceState);
//        LogUtil.i(TAG, "测试数据： " + ZYApplication.cancerCure.get("42"));//测试数据
//        LogUtil.i(TAG, "测试其他数据： " + ZYApplication.values.get("1"));
    }

    private void initView(Bundle savedInstanceState) {
        bottomRG = (RadioGroup) findViewById(R.id.ll);
//        mTabs = new Button[3];
//        mTabs[0] = (Button) findViewById(R.id.btn_main);
//        mTabs[1] = (Button) findViewById(R.id.btn_circle);
//        mTabs[2] = (Button) findViewById(R.id.btn_more);
//        mTabs[0].setSelected(true);
        bottomRG.setOnCheckedChangeListener(this);
        bottomRG.check(R.id.btn_main);
        if (savedInstanceState != null) {
            finish();
        }
    }

    private void intFragment(Bundle savedInstanceState) {
        mainFragment = (MainFragment) getFragmentManager().findFragmentByTag("mainFragment");
        if (mainFragment == null) {
            mainFragment = new MainFragment();

        }

        circleFragment = (CircleFragment) getFragmentManager().findFragmentByTag("circleFragment");
        if (circleFragment == null) {
            circleFragment = new CircleFragment();
        }

        moreFragment = (MoreFragment) getFragmentManager().findFragmentByTag("moreFragment");
        if (moreFragment == null) {
            moreFragment = new MoreFragment();
        }

        fragments = new
                TabFragment[]{mainFragment, circleFragment, moreFragment};

        if (savedInstanceState == null) {

            getFragmentManager().beginTransaction().add(R.id.main_content, mainFragment, "mainFragment").commit();
        }
    }


    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main:
                index = 0;
                break;
            case R.id.btn_circle:
                index = 1;
                break;
            case R.id.btn_more:
                index = 2;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.main_content, fragments[index]);
            }
            trx.show(fragments[index]).commit();
//            fragments[index].scrollTop();
//            if(fragments[index] == mainFragment){
//                mainFragment.scrollTop();
//            }
//            if (fragments[index] == circleFragment) {//这个是调用圈子的上啦刷新功能呢。
//                circleFragment.startRefrsh();
//            }
        }
        mTabs[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        currentTabIndex = index;
    }


    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**RadioGroup选择切换监听处理*/
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        switch (checkedId) {
            case R.id.btn_main:{
                if (mainFragment == null) {//1说明刚进入
                        mainFragment = new MainFragment();
                if (!mainFragment.isAdded()) {
                        ft.add(R.id.main_content, mainFragment, "mainFragment").commit();
                    }
            }

                {//2其他页面切换到这个fragment
                    if (circleFragment != null && circleFragment.isVisible()) {
                        ft.hide(circleFragment).show(mainFragment).commit();
                    }
                    if (moreFragment != null && moreFragment.isVisible()) {
                        ft.hide(moreFragment).show(mainFragment).commit();
                    }
                }
                break;
            }
            case R.id.btn_circle: {
                if (circleFragment == null) {//1说明刚进入
                    circleFragment = new CircleFragment();
                    if (!circleFragment.isAdded()) {
                        ft.add(R.id.main_content, circleFragment, "mainFragment");
                    }
                }
                if (mainFragment != null && mainFragment.isVisible()) {
                    ft.hide(mainFragment).show(circleFragment).commit();
                }
                if (moreFragment != null && moreFragment.isVisible()) {
                    ft.hide(moreFragment).show(circleFragment).commit();
                }


                break;
            }
            case R.id.btn_more: {

                if (moreFragment == null) {//1说明刚进入
                    moreFragment = new MoreFragment();
                    if (!moreFragment.isAdded()) {
                        ft.add(R.id.main_content, moreFragment, "mainFragment");
                    }
                }
                if (mainFragment != null && mainFragment.isVisible()) {
                    ft.hide(mainFragment).show(moreFragment).commit();
                }
                if (circleFragment != null && circleFragment.isVisible()) {
                    ft.hide(circleFragment).show(moreFragment).commit();
                }
                break;
            }
        }
    }
}
