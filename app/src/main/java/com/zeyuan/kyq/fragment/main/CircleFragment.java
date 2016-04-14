package com.zeyuan.kyq.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.CircleAdapter;
import com.zeyuan.kyq.adapter.ForumAdapter;
import com.zeyuan.kyq.bean.ForumListBean;
import com.zeyuan.kyq.bean.MyCircleBean;
import com.zeyuan.kyq.presenter.MyCirclePresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.CircleActivity;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.view.MyActivity;
import com.zeyuan.kyq.view.ReleaseForumActivity;
import com.zeyuan.kyq.view.MoreCircleActivity;
import com.zeyuan.kyq.view.ViewDataListener;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.xlistview.XListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 全部圈子内的帖子
 */
public class CircleFragment extends TabFragment implements OnItemClickListener, View.OnClickListener, XListView.IXListViewListener, ViewDataListener {
    private static final String TAG = "CircleFragment";
    private RecyclerView mRecyclerView; //顶部关注的圈子
    //    private RefreshLayout mRefreshLayout;
    private XListView xListView;//寻常的帖子的listview
    //    private View footerLayout;//底部刷新的layout
    //    private TextView textMore;//加载更多的textview
//    private ProgressBar progressBar;//底部显示的进度条
    private ArrayList<Map<String, Object>> mData = new ArrayList<>();
    private ImageView addCircle;//头部的添加圈子
    private ForumAdapter forumAdapter;//寻常帖子的adapter
    private List<ForumListBean.ForumnumEntity> forumListDatas;//装帖子列表的数据

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_circle, container, false);
        initView();
        setListener();
        initData();
        return rootView;
    }

    public void initData() {
        new MyCirclePresenter(this).getData();
    }


    private void setListener() {
//        loc1.setOnClickListener(this);
        addCircle.setOnClickListener(this);
        set_forum.setOnClickListener(this);
        avatar.setOnClickListener(this);
    }

    //    private TextView loc1;
    private ImageButton set_forum;

    private void initView() {
        /**
         * 初始化横向的recycleview
         */
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);


        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.circle));//设置标题
//        loc1 = (TextView) findViewById(R.id.loc1);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        showAvatar();

        addCircle = (ImageView) findViewById(R.id.add_circle);
        set_forum = (ImageButton) findViewById(R.id.release_forum);
//        mRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_container);
        xListView = (XListView) findViewById(R.id.listview);
//        initFootLayout();
//        mListView.addFooterView(footerLayout);
//        mRefreshLayout.setChildView(xListView);
        xListView.setPullRefreshEnable(true);
        xListView.setPullLoadEnable(true);
        xListView.setAutoLoadEnable(true);
        xListView.setXListViewListener(this);
        xListView.setRefreshTime(getTime());
        xListView.setOnItemClickListener(this);
        forumListDatas = new ArrayList<>();
        forumAdapter = new ForumAdapter(context, forumListDatas);
        xListView.setAdapter(forumAdapter);


//        forumAdapter = new ForumAdapter(getActivity(),);
//        xListView.setAdapter(forumAdapter);
//        mRefreshLayout.setColorSchemeResources(R.color.google_blue,
//                R.color.google_green,
//                R.color.google_red,
//                R.color.google_yellow);
//
//        //使用SwipeRefreshLayout的下拉刷新监听
//        mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                mRefreshLayout.setLoading(false);//这样可以设置在下拉刷新的时候禁止下拉加载
//                simulateFetchingData();
//            }
//        });
//
//        shouldShowRequestPermissionRationale()
//
//        //使用自定义的RefreshLayout加载更多监听
//        mRefreshLayout.setOnLoadListener(new RefreshLayout.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                simulateLoadingData();
//            }
//        });
    }

//    private void showAvatar() {
//        String avatarUrl = UserinfoData.getAvatarUrl(context);
//        if (!TextUtils.isEmpty(avatarUrl)) {
//            Glide.with(this).load(avatarUrl).signature(new IntegerVersionSignature(1)).into(headerImage);
//        }
//    }

    /**
     * 初始化底部的刷新功能
     */
//    private void initFootLayout() {
//        footerLayout = LayoutInflater.from(getActivity()).inflate(R.layout.refresh_listview_footer, null);
//        textMore = (TextView) footerLayout.findViewById(R.id.text_more);
//        progressBar = (ProgressBar) footerLayout.findViewById(R.id.load_progress_bar);
//        textMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                simulateLoadingData();
//            }
//        });
//    }


    /**
     * 模拟一个耗时操作，获取完数据后刷新ListView
     */
//    private void simulateFetchingData() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getNewTopData();
//                mRefreshLayout.setRefreshing(false);
//                mAdapter.notifyDataSetChanged();
//                textMore.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "Refresh Finished!", Toast.LENGTH_SHORT).show();
//            }
//        }, 2000);
//    }


    /**
     * 模拟一个耗时操作，加载完更多底部数据后刷新ListView
     * simulate update ListView and stop load more after after a time-consuming task
     */
//    private void simulateLoadingData() {
//        textMore.setVisibility(View.GONE);
//        progressBar.setVisibility(View.VISIBLE);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                getNewBottomData();
//                mRefreshLayout.setLoading(false);
//                mAdapter.notifyDataSetChanged();
//                textMore.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), "Load Finished!", Toast.LENGTH_SHORT).show();
//            }
//        }, 2000);
//    }

    /**
     * 模拟上拉加载更多时获得更多数据
     * simulate load more data to bottom
     */
//    private void getNewBottomData() {
//        int size = mData.size();
//        for (int i = 0; i < 3; i++) {
//            Map<String, Object> listItem = new HashMap<>();
//            listItem.put("img", R.mipmap.ic_launcher);
//            listItem.put("text", "New Bottom Item " + (size + i));
//            mData.add(listItem);
//        }
//    }


//    /**
//     * 模拟下拉刷新时获取新数据
//     * simulate getting new data when pull to refresh
//     */
//    private void getNewTopData() {
//        Map<String, Object> listItem = new HashMap<>();
//        listItem.put("img", R.mipmap.ic_launcher);
//        listItem.put("text", "New Top Item " + mData.size());
//        mData.add(0, listItem);
//    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtil.i(TAG, position);
        startActivity(new Intent(context, ForumDetailActivity.class).putExtra(Contants.index, String.valueOf(id)));//put something
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.release_forum://这个是点击发布帖子
                startActivity(new Intent(context, ReleaseForumActivity.class));
                break;

            case R.id.add_circle:
                startActivityForResult(new Intent(context, MoreCircleActivity.class), 0);
                break;
//            case R.id.loc1:
//                startActivity(new Intent(context, CircleActivity.class));
//                break;

            case R.id.avatar:
                startActivity(new Intent(context, MyActivity.class));

                break;
        }
    }

    private boolean isRefresh = false;//true 表示是下拉刷新的数据 false 为正常数据

    /**
     * 这个是下拉刷新调用的接口
     */
    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                initData();
            }
        }, 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(TAG, "onresume");
        if (xListView != null) {
            startRefrsh();
        }
    }

    /**
     * 这个是上拉加载时加载的数据
     */
    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 3000);
    }

    /**
     * 停止刷新 恢复原状
     */
    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime(getTime());
    }

    /**
     * 这个是上拉加载时显示的时间
     *
     * @return
     */
    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    public void startRefrsh() {
        if (xListView != null) {
            xListView.autoRefresh();
        }
    }

//
//    private String StepID = UserinfoData.getStepID(context);
//    private String CancerID = UserinfoData.getCancerID(context);
//    private String CureConfID = MapDataUtils.getAllCureconfID(StepID);
//    private String ProvinceID = UserinfoData.getProvinceID(context);
//    private String CityID = UserinfoData.getCityID(context);


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        String StepID = UserinfoData.getStepID(context);
        String CancerID = UserinfoData.getCancerID(context);
        String CureConfID = MapDataUtils.getAllCureconfID(StepID);
        String ProvinceID = UserinfoData.getProvinceID(context);
        String CityID = UserinfoData.getCityID(context);
        if (tag == 0) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            if (!TextUtils.isEmpty(StepID)) {
                map.put(Contants.StepID, StepID);
            }
//            map.put(Contants.StepID, StepID);
            if (!TextUtils.isEmpty(CityID)) {
                map.put(Contants.CityID, CityID);
            }
            if (!TextUtils.isEmpty(ProvinceID)) {
                map.put(Contants.ProvinceID, ProvinceID);
            }
            if (!TextUtils.isEmpty(CureConfID)) {
                map.put(Contants.CureConfID, CureConfID);
            }
            if (!TextUtils.isEmpty(CancerID)) {
                map.put(Contants.CancerID, CancerID);
            }
//            map.put(Contants.CancerID, CancerID);
//            map.put(Contants.CureConfID, CureConfID);
//            map.put(Contants.ProvinceID, ProvinceID);
        }
        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        if (position == 0) {
//            if (!isRefresh) {
            bindView((MyCircleBean) t);//第一次加载
//                isRefresh = false;
//            }else {//下拉刷新
//                refreshView((MyCircleBean) t);
//            }
        }


    }

    private void refreshView(MyCircleBean bean) {
        if (Contants.OK_DATA.equals(bean.getIResult())) {
            onLoad();

            bindListView(bean);
        }
    }

    private void bindListView(MyCircleBean bean) {
        List list = bean.getAllPostNum();
        if (list != null && list.size() > 0) {
            forumListDatas.clear();
            forumListDatas.addAll(list);
//            if (forumListDatas.size() > 3) {//just for test
//                forumListDatas.get(0).setPostType("1");
//                forumListDatas.get(1).setPostType("1");
//            }
            forumAdapter.update(forumListDatas);//这儿是列表
        }
    }

    private void bindView(MyCircleBean t) {
        MyCircleBean bean = t;
        LogUtil.i(TAG, bean.toString());
        if (Contants.OK_DATA.equals(bean.getIResult())) {
            bindListView(bean);
            final List<String> lists = bean.getFollowCircleNum();
            if (lists != null && lists.size() >= 0) {
                if (lists.size() > 0) {//存圈子
                    List<String> list1 = new ArrayList<>();
                    for (String entity : lists) {
                        list1.add(entity);
                    }
                    UserinfoData.saveFocusCirlce(context, list1);
                }
                CircleAdapter adapter = new CircleAdapter(lists, context);
                adapter.setOnItemClickLitener(new CircleAdapter.OnItemClickListerner() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if (position == 0) {
                            startRefrsh();
                            return;
                        }
                        toCircle(lists.get(position -1));
//                        startActivity(new Intent(context, CircleActivity.class).putExtra(Contants.CircleID, lists.get(position)));
                    }
                });
                mRecyclerView.setAdapter(adapter);
            }

            if (xListView != null) {
                onLoad();
            }
        } else {
            Toast.makeText(context, R.string.loadding_failed, Toast.LENGTH_SHORT).show();
        }
    }


    private void toCircle(String circleID) {
        Intent intent = new Intent(context, CircleActivity.class);
        intent.putExtra(Contants.CircleID, circleID);
        startActivity(intent);
    }

    @Override
    public void showLoading(int tag) {
    }

    @Override
    public void hideLoading(int tag) {

    }

    @Override
    public void showError(int tag) {
        Toast.makeText(context, R.string.loadding_failed, Toast.LENGTH_SHORT).show();
    }

}
