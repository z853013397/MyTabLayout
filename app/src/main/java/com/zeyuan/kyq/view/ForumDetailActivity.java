package com.zeyuan.kyq.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.ImgGridAdapter;
import com.zeyuan.kyq.adapter.ReplyForumItemAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.FollowBean;
import com.zeyuan.kyq.bean.ForumDetailBean;
import com.zeyuan.kyq.bean.ReplyForum;
import com.zeyuan.kyq.bean.ReplyListBean;
import com.zeyuan.kyq.presenter.FlwForumPresenter;
import com.zeyuan.kyq.presenter.FollowCirclePresenter;
import com.zeyuan.kyq.presenter.ForumDetailPresenter;
import com.zeyuan.kyq.presenter.GetReplyListPresenter;
import com.zeyuan.kyq.presenter.ReplyForumPresent;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;
import com.zeyuan.kyq.widget.MyGridView;
import com.zeyuan.kyq.widget.MyLayout;
import com.zeyuan.kyq.widget.MyListView;
import com.zeyuan.kyq.widget.MyScrollView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 帖子详情
 * 没有做取消收藏和取消关注
 * 思路：在获取后台数据的同时修改状态 在赋值是否关注和收藏
 */
public class ForumDetailActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, ViewDataListener, MyLayout.OnSoftKeyboardListener, AdapterView.OnItemClickListener, ReplyForumItemAdapter.ShowInput {
    private static final String TAG = "ForumDetailActivity";
    //    private String ownerID;//楼主的id
    private TextView mTextView_forum_title;//帖子的标题
    private TextView mTextView_forum_content;//帖子的内容
    //    private Button mImageView_patient_detail;//患者病历
    private MyListView mListView_listview;//回复的listview
    private LinearLayout mLinearLayout_layout_bottom;//最底部的layout/**/
    private EditText mEditText_edit_txt;//回复输入的editext
    //    private ImageButton mImageButton_forum_share;//分享的按钮
    private CheckBox mCheckBox_connection_forum;//收藏的按钮
    private ImageButton mImageButton_message;//定位的图标 上面有显示数字
    private TextView mTextView_message_number;//上面显示的数字
    private ReplyForumItemAdapter adapter;//回复列表的itemadapter
//    private TextView circleTitle;//圈子的标题 如：深圳圈
//    private CircleImageView avatar;//圈子的头像
//    private TextView topic_number;//话题数量   话题 2920
//    private TextView mTextView_number;//圈子标题中显示的人数  人数 2920
//    private CheckBox mCheckBox_isfollow;//是否关注

    /**
     * 这些是楼主的信息
     */
    private CircleImageView host_avatar;//发布帖子人的头像
    private TextView mTextView_release_forum_name;//发布帖子的名字
    private TextView mTextView_release_forum_time;//发布帖子的时间
    private TextView mTextView_reply_textview;//最右边的回复
//    private ReplyForumPresent relpyForumPresent = new ReplyForumPresent(this);//回复帖子
//    private int screenHeight;

    private MyLayout myLayout;//用来监听软键盘的弹出 和收起
    private List<ReplyListBean.ReplyNumEntity> replyListData;//回复列表的list
    //    private InputMethodManager manager;
    private List<String> imageUrls;//楼主发布图片的urls
    private ImgGridAdapter imgAdapter;
    private String hostImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_detail);
        replyListData = new ArrayList<>();
        imageUrls = new ArrayList<>();
        initTitlebar(getString(R.string.forum_detail));
        index = getIntent().getStringExtra(Contants.index);
        if (TextUtils.isEmpty(index)) {
            throw new RuntimeException("ForumDetailActivity error entrance!");
        }
//        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        initView();
        getForumData();
        setListener();
    }

    private void getForumData() {
        new ForumDetailPresenter(this).getData();//帖子详情
    }


    private void getReplyList() {
        new GetReplyListPresenter(this).getData();//这个是回复列表

    }

    private void setListener() {
//        mImageView_patient_detail.setOnClickListener(this);
//        mImageButton_forum_share.setOnClickListener(this);
        host_avatar.setOnClickListener(this);
        mCheckBox_connection_forum.setOnCheckedChangeListener(this);
        mImageButton_message.setOnClickListener(this);
//        mCheckBox_isfollow.setOnCheckedChangeListener(this);
        myLayout.setOnSoftKeyboardListener(this);
        send_message.setOnClickListener(this);
//        mScrollView.setOnClickListener(this);
//        mScrollView.setOnTouchListener(this);
    }


    private String getEditText() {
        return mEditText_edit_txt.getText().toString().trim();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.patient_detail: {//患者病历的点击
//                ObjectAnimator.ofFloat(mEditText_edit_txt, "width", mEditText_edit_txt.getWidth(), 300).start();
//
//                break;
//            }
//            case R.id.sv: {
//                LogUtil.i(TAG, "收起输入法？？？！！！");
//                toggleSoftInput();
//                break;
//            }
//            case R.id.circle_head: {//点击去某个圈子
//                Intent intent = new Intent(this, CircleActivity.class);
////                intent.putExtra(Contants.index, index);
//                startActivity(intent);
//                break;
//            }
            case R.id.message: {//定位的点击，上面有个显示数字的
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mScrollView.scrollTo(0, scroll_content.getHeight());
                    }
                });
                break;
            }
//            case R.id.connection_forum: {//收藏帖子的点击
//
//                break;
//            }
            case R.id.send_message: {//右下角回复的点击
                getString();
                break;
            }
            case R.id.host_avatar: {
//                host_avatar
                toShowImage();
                break;
            }
            default:
                throw new RuntimeException("数据出错");
        }
    }

    private void FlwForum() {
        new FlwForumPresenter(this).getData();
    }

    private void toShowImage() {
        if (TextUtils.isEmpty(hostImageUrl)) {
            return;
        }
        Intent intent = new Intent(this, ShowImageActivity.class);
        intent.putExtra(Contants.Avatar, hostImageUrl);
        startActivity(intent);
    }

    private void getString() {
        content = getEditText();
        if (TextUtils.isEmpty(content)) {
            showToast(getString(R.string.empty_reply));
            return;
        }
        new ReplyForumPresent(this).getData();
    }


    private void setTextView(TextView textview, String number) {
        textview.setText(number);
    }

    private MyScrollView mScrollView;
    private LinearLayout scroll_content;//这个是点击左下角的图标 需要移动的位置
    private TextView send_message;
    private MyGridView mMyGridView;
    private TextView huifu_num;

    private void initView() {
        mMyGridView = (MyGridView) findViewById(R.id.gridview);
        huifu_num = (TextView) findViewById(R.id.huifu_num);

        imgAdapter = new ImgGridAdapter(this, imageUrls);
        mMyGridView.setAdapter(imgAdapter);
        send_message = (TextView) findViewById(R.id.send_message);
        myLayout = (MyLayout) findViewById(R.id.whole_content);
        scroll_content = (LinearLayout) findViewById(R.id.scroll_content);
        mScrollView = (MyScrollView) findViewById(R.id.sv);
        mTextView_forum_title = (TextView) findViewById(R.id.forum_title);
        mTextView_forum_content = (TextView) findViewById(R.id.forum_content);
//        mImageView_patient_detail = (Button) findViewById(R.id.patient_detail);
        mListView_listview = (MyListView) findViewById(R.id.listview);
        mLinearLayout_layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        mEditText_edit_txt = (EditText) findViewById(R.id.edit_txt);
//        mImageButton_forum_share = (ImageButton) findViewById(R.id.forum_share);
        mCheckBox_connection_forum = (CheckBox) findViewById(R.id.connection_forum);
        mImageButton_message = (ImageButton) findViewById(R.id.message);
        mTextView_message_number = (TextView) findViewById(R.id.message_number);
//        View circle_head = findViewById(R.id.circle_head);
//        circle_head.setOnClickListener(this);
//        avatar = (CircleImageView) circle_head.findViewById(R.id.avatar);
//        circleTitle = (TextView) circle_head.findViewById(R.id.title);
//        topic_number = (TextView) circle_head.findViewById(R.id.topic_number);
//        mTextView_number = (TextView) circle_head.findViewById(R.id.number);
//        mCheckBox_isfollow = (CheckBox) circle_head.findViewById(R.id.isfollow);

        View host_member = findViewById(R.id.host_member);//这个是显示帖子主人的头像
        host_avatar = (CircleImageView) host_member.findViewById(R.id.host_avatar);
        mTextView_release_forum_name = (TextView) host_member.findViewById(R.id.release_forum_name);
        mTextView_release_forum_time = (TextView) host_member.findViewById(R.id.release_forum_time);
        mTextView_reply_textview = (TextView) host_member.findViewById(R.id.reply_textview);
        mTextView_reply_textview.setVisibility(View.GONE);
        adapter = new ReplyForumItemAdapter(this, replyListData);
        adapter.setShowInput(this);
        mListView_listview.setAdapter(adapter);

        mMyGridView.setOnItemClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.connection_forum: {//收藏的点击
                if (isChecked) {
                    isFlw = "0";
                } else {
                    isFlw = "1";
                }
                FlwForum();
                break;
            }

//            case R.id.isfollow: {//这个是关注圈子的点击
//                if (isChecked) {
//                    followOrcancel = "1";//关注为1
//                } else {
//                    followOrcancel = "2";
//                }
//                folowCircle();
//                break;
//            }
        }

    }

    private void folowCircle() {
        new FollowCirclePresenter(this).getData();
    }

    private String index;//帖子的id
    private String content;//回复帖子的内容
    private String page = "1";
    /**
     * 下面2个参数是评论列表中的某个人所需要带上的参数
     */
    private String toinfoid;//平论人的id
    private String touser;//评论这个人的参数

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
        if (tag == NetNumber.FORUM_DETAIL) {//这个是获取帖子详情
            map.put(Contants.index, index);
        }
        if (tag == NetNumber.REPLY_FORUM) {//这个是回复帖子
            map.put(Contants.index, index);//帖子的id
            map.put(Contants.fromuser, UserinfoData.getInfoname(this));
            String decodeContetn = DecryptUtils.encode(content);
            map.put(Contants.Content, decodeContetn.trim());
            if (!TextUtils.isEmpty(toinfoid)) {
                map.put(Contants.toinfoid, toinfoid);
            }
            if (!TextUtils.isEmpty(touser)) {
                map.put(Contants.touser, touser);
            }
        }


        if (tag == NetNumber.REPLY_LIST) {//getReplyList
            map.put(Contants.index, index);
            map.put(Contants.page, page);
        }
        if (tag == NetNumber.FOLLOW_CIRCLE) {
            map.put(Contants.CircleID, circleId);//圈子的id
            map.put(Contants.followOrcancel, followOrcancel);//1关注 2.取消
            String type = "";
            if (Integer.valueOf(circleId) < 10000) {
                type = type1;
            } else {
                type = type2;
            }
            map.put(Contants.type, type);//1.同城圈 2.抗癌圈  circle < 10000则是同城圈 >= 10000则是抗癌圈
        }

        if (tag == NetNumber.FLW_FORUM) {//这个是收藏帖子的数据
            map.put(Contants.strTitle, title);
            map.put(Contants.index, index);
            map.put(Contants.isCancel, isFlw);

        }


        return map;
    }

    private static final String type1 = "1";
    private static final String type2 = "2";
    private String isFlw = "1";//取消收藏 1;   收藏 0
    private String circleId;//圈子的id
    private String followOrcancel;//1关注 2.取消
    private String title;
    private String ownerID;
    private String author;

    @Override
    public void toActivity(Object t, int position) {
        if (position == NetNumber.FORUM_DETAIL) {//这个是获取帖子详情
            ForumDetailBean forumDetail = (ForumDetailBean) t;
            LogUtil.i(TAG, "帖子详情" + forumDetail.toString());
            if (Contants.OK_DATA.equals(forumDetail.getIResult())) {
                ownerID = forumDetail.getOwnerID();
                author = forumDetail.getAuthor();
                title = forumDetail.getTitle();
                bindToUI(forumDetail);
                circleId = forumDetail.getCircleId();
                touser = author;
                toinfoid = ownerID;
                getReplyList();
            }
        }
        if (position == NetNumber.REPLY_FORUM) {//这个是回复帖子
            ReplyForum replyForum = (ReplyForum) t;
            LogUtil.i(TAG, replyForum.toString());
            if (Contants.OK_DATA.equals(replyForum.getIResult())) {
                showToast("回复成功");//收起输入法 刷新帖子
                toggleSoftInput();
                getForumData();
                clearReplyData();
            }

        }
        if (position == NetNumber.REPLY_LIST) {//这个是getreplylist返回的数据
            ReplyListBean bean = (ReplyListBean) t;
            if (!Contants.OK_DATA.equals(bean.getIResult())) {
                return;
            }
            List<ReplyListBean.ReplyNumEntity> list = bean.getReplyNum();
            if (list != null && list.size() > 0) {
                for (ReplyListBean.ReplyNumEntity entity : list) {
                    if (author.equals(entity.getToUser())) {
                        entity.setIsHost(true);
                    }
                }
                replyListData.clear();
                replyListData.addAll(list);
                adapter.update(replyListData);
                mTextView_forum_title.setFocusable(true);
                mTextView_forum_title.setFocusableInTouchMode(true);
                mTextView_forum_title.requestFocus();
            }
        }

        if (position == NetNumber.FOLLOW_CIRCLE) {//这个是是否关注
            LogUtil.i(TAG, t.toString());
            FollowBean bean = (FollowBean) t;
            if (Contants.OK_DATA.equals(bean.getIResult())) {
                if ("1".equals(followOrcancel)) {
                    Toast.makeText(this, R.string.follow_success, Toast.LENGTH_SHORT).show();
                } else {
                    showToast("取消关注成功");
                }

            } else {
//            Toast.makeText(context, "关注失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (position == NetNumber.FLW_FORUM) {//这个是收藏帖子的数据
            LogUtil.i(TAG, t.toString());
            BaseBean bean = (BaseBean) t;
            if (bean.iResult.equals(Contants.RESULT)) {
                if ("1".equals(isFlw)) {
                    showToast("取消收藏成功");
                } else {
                    showToast(getString(R.string.flw_success));
                }
            }
        }
    }

    private void toggleSoftInput() {
        LogUtil.i(TAG, "收起输入法？？！！");
        View v = getCurrentFocus();
        hideKeyboard(v.getWindowToken());
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void bindToUI(ForumDetailBean forumDetail) {
        setReplyNum(forumDetail);

        setAvatar(forumDetail);

        String title = forumDetail.getTitle();
        if (!TextUtils.isEmpty(title)) {
            setTextString(mTextView_forum_title, title);
        }
        mTextView_release_forum_name.setText(forumDetail.getAuthor());
//        ownerID = forumDetail.getOwnerID();
        String postTime = String.valueOf(forumDetail.getPostTime());
        setTextString(mTextView_release_forum_time, getDateFromData(postTime));
//        if (!TextUtils.isEmpty(postTime)) {
//            mTextView_release_forum_time.setText(getDateFromData(postTime));
//        }
        mTextView_forum_content.setText(forumDetail.getContent());
        List<String> list = forumDetail.getImgUrlNum();
        if (list != null && list.size() > 0) {
            imageUrls.addAll(list);
            imgAdapter.update(imageUrls);
        }

        setProgressGone();
    }

    private void setAvatar(ForumDetailBean forumDetail) {
        String host_avatar_url = forumDetail.getHeadimgurl();
        if (!TextUtils.isEmpty(host_avatar_url)) {
            hostImageUrl = host_avatar_url;
            Glide.with(this).load(host_avatar_url).signature(new IntegerVersionSignature(1)).into(host_avatar);
        }
    }

    private void setReplyNum(ForumDetailBean forumDetail) {
        String replyNum = forumDetail.getReplyNum();
        String replyNumTxt = getString(R.string.the_all_huifu, replyNum);//这样子啊
        huifu_num.setText(replyNumTxt);
        mTextView_message_number.setText(replyNum);
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

    private String getDateFromData(String date) {
        date = date.concat("000");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        long dates = Long.parseLong(date);
        return format.format(new Date(dates));
    }

    private boolean flag = false;// 这个控制隐藏键盘的时候 回调只被调用一次

    @Override
    public void onShown() {
        LogUtil.i(TAG, "input on show");
        flag = true;
//        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtils.dpToPx(this, 90));
//        p.setMargins(DensityUtils.dpToPx(this, 16), DensityUtils.dpToPx(this, 8), 0, DensityUtils.dpToPx(this, 16));
//        mEditText_edit_txt.setLayoutParams(p);
//        mEditText_edit_txt.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
//        mEditText_edit_txt.getLayoutParams().height = DensityUtils.dpToPx(this, 90);
//        mEditText_edit_txt.requestLayout();
        if (!TextUtils.isEmpty(content)) {
            mEditText_edit_txt.setText(content);
            mEditText_edit_txt.setSelection(content.length());
        }

        setSendMesVis();
    }

    private void setSendMesVis() {
        mImageButton_message.setVisibility(View.GONE);
        mCheckBox_connection_forum.setVisibility(View.GONE);
        mTextView_message_number.setVisibility(View.GONE);
        send_message.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHidden() {
        LogUtil.i(TAG, "input on hidden");
        if (flag) {
            hideInput();
        }
    }

    private void hideInput() {
//        mEditText_edit_txt.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
//        mEditText_edit_txt.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
//        mEditText_edit_txt.requestLayout();
        mImageButton_message.setVisibility(View.VISIBLE);
        mCheckBox_connection_forum.setVisibility(View.VISIBLE);
        mTextView_message_number.setVisibility(View.VISIBLE);
        send_message.setVisibility(View.GONE);
        flag = false;
        clearReplyData();
    }

    /**
     * 隐藏输入法后 清空 回复某人的数据
     */
    private void clearReplyData() {
        mEditText_edit_txt.setText("");
        touser = author;
        toinfoid = ownerID;
        mEditText_edit_txt.setHint("我想说");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mMyGridView) {//帖子中 群主发的图片的点击事件
            Intent intent = new Intent(ForumDetailActivity.this, ShowPhotoActivity.class);
            intent.putExtra("list", (Serializable) imageUrls);
            intent.putExtra("position", position);
            startActivity(intent);
        }

    }

    /**
     * 回复列表中的某项被点击了
     *
     * @param position
     */
    @Override
    public void showInput(int position) {
        LogUtil.i(TAG, "某个回复列表项目被点击了，position is ：" + position);
        ReplyListBean.ReplyNumEntity entity = adapter.getItem(position);
        mEditText_edit_txt.setHint("回复@" + entity.getFromUser() + ":");
        showSoftInput();
        toinfoid = entity.getUserId();
        touser = entity.getFromUser();
    }

    private void showSoftInput() {
        mEditText_edit_txt.setFocusableInTouchMode(true);
        mEditText_edit_txt.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) mEditText_edit_txt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(mEditText_edit_txt, 0);
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        LogUtil.i(TAG, "onrouch");
//        toggleSoftInput();
//        return false;
//    }

//    /**
//     * 这个是一个view动画
//     */
//    private static class ViewWrapper {
//        private View mView;
//
//        public ViewWrapper(View view) {
//            mView = view;
//        }
//
//        public int getWidth() {
//            return mView.getLayoutParams().width;
//        }
//
//        public void setWidth(int width) {
//            mView.getLayoutParams().width = width;
//            mView.requestLayout();
//        }
//    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
//                hideKeyboard(v.getWindowToken());
//                hideInput();
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null) {
//            int[] l = {0, 0};
            int[] m = {0, 0};
//            v.getLocationInWindow(l);
//            int left = l[0],
//                    top = l[1],
//                    bottom = top + v.getHeight(),
//                    right = left + v.getWidth();
            mLinearLayout_layout_bottom.getLocationInWindow(m);

            int layoutTop = m[1];
            LogUtil.i(TAG, "layoutTop:" + layoutTop);
            LogUtil.i(TAG, "event.getY():" + event.getY());

//        if (event.getX() > left && event.getX() < right
//                && event.getY() > top && event.getY() < bottom) {
            if (layoutTop < event.getY()) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点


        return false;
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            content = getEditText();
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}