package com.zeyuan.kyq.page.kangfu;

import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zeyuan.kyq.adapter.ArticleListAdapter;
import com.zeyuan.kyq.app.BasePage;
import com.zeyuan.kyq.bean.ArticleListBean;
import com.zeyuan.kyq.presenter.MainmorePresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.view.ArticleDetailActivity;
import com.zeyuan.kyq.view.ViewDataListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/23.
 * <p/>
 * 副作用
 */
public class SideEffectPage extends BasePage implements ViewDataListener, AdapterView.OnItemClickListener {
    private static final String TAG = "SideEffectPage";
    //    private Context mContext;
    private ArticleListAdapter mArticleListAdapter;
    private List datas;

    public SideEffectPage(Context context, String type) {
        super(context);
//        this.context = context;
        this.type = type;
        initData();
    }

    @Override
    public View getView(LayoutInflater inflater) {
        datas = new ArrayList();
        ListView listView = new ListView(context);
        listView.setSmoothScrollbarEnabled(false);
        listView.setScrollbarFadingEnabled(false);
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setOnItemClickListener(this);
        mArticleListAdapter = new ArticleListAdapter(context, datas);
        listView.setAdapter(mArticleListAdapter);
        return listView;
    }

    @Override
    public void initView(View rootView) {

    }

    @Override
    public void initData() {
        new MainmorePresenter(this).getData();
    }

    private String type;
//    private String ChannelID;


    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(context));
            map.put(Contants.CancerID, UserinfoData.getCancerID(context));
            String stepId = UserinfoData.getStepID(context);
            map.put(Contants.StepID, stepId);
            map.put(Contants.CureConfID, MapDataUtils.getAllCureconfID(stepId));
            map.put(Contants.ChannelID, type);//康复与营养传3 副作用传1 知识库传2
        }
        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, t.toString());
        ArticleListBean bean = (ArticleListBean) t;

        if (Contants.RESULT.equals(bean.getIResult())) {//这个说明后台是有数据的
            List<ArticleListBean.ArticlenumEntity> list = bean.getArticlenum();
            if (list != null && list.size() > 0) {
                datas.clear();
                datas.addAll(list);
                LogUtil.i(TAG, datas.size());
                mArticleListAdapter.update(datas);
            }
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String articleID = mArticleListAdapter.getItem(position).getArticleID();
        context.startActivity(new Intent(context, ArticleDetailActivity.class).putExtra(Contants.ARTICLIE_ID, articleID));
    }
}
