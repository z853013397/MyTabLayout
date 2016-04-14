package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.bean.ArticleDetailBean;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.presenter.ArticleDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.UserinfoData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-11
 * Time: 17:19
 * FIXME
 * 文章详情
 */


public class ArticleDetailActivity extends BaseActivity implements ViewDataListener, View.OnClickListener {
    private static final String TAG = "ArticleDetailActivity";
    private String articleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_article_detail);
        initTitlebar("文章详情");
        Intent intent = getIntent();
        articleId = intent.getStringExtra(Contants.ARTICLIE_ID);
        if (TextUtils.isEmpty(articleId)) {
            throw new RuntimeException("instanti error");
        }
        initData();
        initView();
    }

    //    private TextView content;
    private LinearLayout content;
    private TextView titles;

    private void initView() {
        content = (LinearLayout) findViewById(R.id.content);
        titles = (TextView) findViewById(R.id.titles);

//        content = (TextView) findViewById(R.id.content);
//        String str1 = "5L2T6YOo5Ly9546b5YiA6YCC5a6c6IK655mM44CB6IKd55mM44CB6IOw6IW655mM44CB6IK+5LiK6IW66IK/55ik44CB6IK+55mM44CB57q16ZqU6IK/55ik44CB6IW56Iac5ZCO6IK/55ik5Y+K55uG6IWU6IK/55ik562J55qE5rK755aX44CC5L2T6YOo5Ly9546b5YiA5Y+v5Y2V54us55So5LqO5bCP6IK/55ik5qC55rK75rK755aX77yM5Lmf5Y+v57uT5ZCI5aSa56eN5pS+55aX5oqA5pyv44CCDQogICAgICDkvZPpg6jkvL3njpvliIDlnKjmsrvnlpfml7bkuI3lvIDliIDjgIHkuI3purvphonvvIzml6DliJvkvKTjgIHlia/kvZznlKjovbvvvIzml6DpnIDkvY/pmaLvvIzlm6DmraTnibnliKvpgILlrpzlubTogIHkvZPlvLHkuI3og73ogJDlj5fmiYvmnK/nmoTmgqPogIXmiJblm6DmgZDmg6fmi5Lnu53miYvmnK/nmoTmgqPogIXnmoTmsrvnlpfjgII=";
//        byte[] b = str1.getBytes();
//        String s = new String(Base64.decode(b, Base64.DEFAULT));
////        content.setText(s);
//        LogUtil.i("MainActivity", "解码的数据是:" + s);
    }

    private void initData() {
        new ArticleDetailPresenter(this).getData();
    }

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put(Contants.ARTICLIE_ID, articleId);
        }

        return map;
    }

    @Override
    public void toActivity(Object t, int tag) {
        LogUtil.i(TAG, "文章详情数据：" + t.toString());
        ArticleDetailBean bean = (ArticleDetailBean) t;

        if (Contants.RESULT.equals(bean.getIResult())) {//这个说明后台是有数据的
            titles.setText(bean.getTitle());
            List<ArticleDetailBean.ArticlenumEntity> list = bean.getArticlenum();
            if (list != null && list.size() > 0) {
//                datas.clear();
//                datas.addAll(list);
//                LogUtil.i(TAG,datas.size());
//                mArticleListAdapter.update(datas);
                urls = new ArrayList();
                positons = new LinkedHashMap<>();
                for (int i = 0; i < list.size(); i++) {//收集所有的图片的urls
//                    ArticleDetailBean.ArticlenumEntity item = list.get(i);
//                    String type = item.getContentType();
//                    if ("2".equals(type)) {
//                        urls.add(item.getURL());
//                        positons.put(i, urls.size() - 1);
//                    }
                }
                getString(list);
                setProgressGone();
            }
        }

    }

    private List<String> urls;
    private Map<Integer, Integer> positons;
    private int index = 0;

    private void getString(List<ArticleDetailBean.ArticlenumEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            ArticleDetailBean.ArticlenumEntity bean = list.get(i);

//            ArticleDetailBean.ArticlenumEntity bean = list.get(i);
            String type = bean.getContentType();
            if ("2".equals(type)) {
                urls.add(bean.getURL());
                positons.put(i, urls.size() - 1);
            }
            if ("1".equals(bean.getContentType())) {//
                TextView tv = new TextView(this);
                int marginSpc = DensityUtils.dpToPx(ArticleDetailActivity.this, 16);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setMargins(marginSpc, marginSpc, marginSpc, 0);
                tv.setText(bean.getContent());
                tv.setLineSpacing(DensityUtils.dpToPx(ArticleDetailActivity.this, 8), 1);//设置行间距
//                int space = DensityUtils.spToPx(ArticleDetailActivity.this,15);
//                LogUtil.i(TAG,"字体大小是" + space);
                tv.setTextSize(18);//设置字体大小
                tv.setTextColor(getResources().getColor(R.color.item_forum_title));//设置颜色
                content.addView(tv, index++, p);

            } else {
                final ImageView img = new ImageView(this);
                int marginSpc = DensityUtils.dpToPx(ArticleDetailActivity.this, 16);
                LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                p.setMargins(marginSpc, marginSpc, marginSpc, 0);

//                img.setTag(position);
                final int position = i;
                String imgUrl = bean.getURL();
                if (!TextUtils.isEmpty(imgUrl)) {
                    Glide.with(this).load(imgUrl)
                            .error(R.drawable.default_img)
                            .signature(new IntegerVersionSignature(1)).into(img);
                }

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ArticleDetailActivity.this, ShowPhotoActivity.class);
                        intent.putExtra("list", (Serializable) urls);
                        intent.putExtra("position", positons.get(position));
                        startActivity(intent);
                    }
                });
//                position++;
                content.addView(img, index++, p);
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
    public void onClick(View v) {

    }
}

