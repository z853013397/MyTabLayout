package com.zeyuan.kyq.page.sideeffect;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BasePage;
import com.zeyuan.kyq.bean.MainPageInfoBean;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DensityUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.view.ForumDetailActivity;
import com.zeyuan.kyq.widget.CircleImageView;

/**
 * Created by Administrator on 2015/12/7.
 */
public class MainCasePage extends BasePage implements View.OnClickListener {
    public interface OnMoreClickListener {
        void onClickListener();
    }

private OnMoreClickListener onMoreClickListener;


    public void setOnMoreClickListener(OnMoreClickListener onMoreClickListener) {
        this.onMoreClickListener = onMoreClickListener;
    }

    private static final String TAG = "MainCasePage";
    CircleImageView avatar;
    private TextView host_name;
    private TextView host_circle;
    private TextView forum_title;
    private MainPageInfoBean.SimilarcaseNumEntity entity;

    public MainCasePage(Context context, MainPageInfoBean.SimilarcaseNumEntity entity) {
        super(context);
        this.entity = entity;
        initData();

    }

    @Override
    public View getView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.simalar_case_item, null);
    }

    private TextView moreButton;

    @Override
    public void initView(View rootView) {
        moreButton = (TextView) findViewById(R.id.more_info);
        avatar = (CircleImageView) findViewById(R.id.avatar);
        host_name = (TextView) findViewById(R.id.forum_host_name);
//        host_circle = (TextView) findViewById(R.id.host_circle);
        forum_title = (TextView) findViewById(R.id.forum_title);
        rootView.setOnClickListener(this);
        moreButton.setOnClickListener(this);
    }

    private final static String msg = "靶向阶段【胃癌有哪些综合的治疗方法】jakldsfj klajfd lsajlfjalsdjfl";

    @Override
    public void initData() {
//        Glide.with(context).load(entity.getImageUrl()).into(avatar);
        Glide.with(context).load(entity.getImageUrl())
                .signature(new IntegerVersionSignature(1))
                .error(R.mipmap.default_avatar)//这个是加载失败的
                .into(avatar);
        forum_title.setText(entity.getTitle());
        host_name.setText(entity.getAuthor());
    }

    public String getIndex() {
        return entity.getIndex();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_info: {
                LogUtil.i(TAG, "more click");

                if (onMoreClickListener != null) {
                    onMoreClickListener.onClickListener();
                }
                return;
            }
        }
        Intent intent = new Intent(context, ForumDetailActivity.class);
        intent.putExtra(Contants.index, entity.getIndex());
        context.startActivity(intent);
    }
}
