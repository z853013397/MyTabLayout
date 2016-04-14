package com.zeyuan.kyq.view;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.adapter.GridAdapter;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.PostForumBean;
import com.zeyuan.kyq.presenter.PostForumPresenter;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.DecryptUtils;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.FlowLayout;
import com.zeyuan.kyq.widget.MyGridView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 发布帖子
 */
public class ReleaseForumActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, ViewDataListener, AdapterView.OnItemLongClickListener {
    private static final String TAG = "ReleaseForumActivity";
    private static final int REQUEST_PICK = 0;
    private Button releaseForum;
    private MyGridView gridView;//show the photo which is choosed
    private List<String> selectedPicture;//containts  photo which is choosed path
    private GridAdapter adapter;
    private List<String> followCircles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_forum);
        initTitlebar(getString(R.string.release_forum));
        selectedPicture = new ArrayList<>();
        followCircles = new ArrayList<>();
        initFollowCircles();
        initView();
    }

    private void initFollowCircles() {
        circleIds = new ArrayList<>();
        List<String> followCircle = UserinfoData.getFocusCircle(this);
        if (followCircle != null && followCircle.size() > 0) {
            followCircles.addAll(followCircle);
        }
    }

    private EditText title;
    private EditText content;
    /**
     * 点击展开收起 关注的圈子
     */
    private LinearLayout ll;

    private ImageView delete_bt_content;//输入帖子标题右边的删除按钮
    private FlowLayout mFlowLayout;
    private ImageView img;
    private TextView tv_title;//头部的文本
    private TextView deleteAll;//头部的文本
    private Button chooseCirclesConfrim;

    private void initView() {
        deleteAll = (TextView) findViewById(R.id.delte_all);
        chooseCirclesConfrim = (Button) findViewById(R.id.confirm);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getString(R.string.release_pos, ""));
        img = (ImageView) findViewById(R.id.img);
        img.setRotation(90);
        mFlowLayout = (FlowLayout) findViewById(R.id.fl);
        initFlowLayout();
        ll = (LinearLayout) findViewById(R.id.ll);
        delete_bt_content = (ImageView) findViewById(R.id.delete_bt_content);
        gridView = (MyGridView) findViewById(R.id.show_pic);
        releaseForum = (Button) findViewById(R.id.release_forum);
        content = (EditText) findViewById(R.id.content);
        title = (EditText) findViewById(R.id.titles);
        ImageView back = (ImageView) findViewById(R.id.btn_back);
        releaseForum.setOnClickListener(this);
        delete_bt_content.setOnClickListener(this);
        back.setOnClickListener(this);
        adapter = new GridAdapter(this, selectedPicture);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        gridView.setOnItemLongClickListener(this);
        ll.setOnClickListener(this);
        deleteAll.setOnClickListener(this);
        chooseCirclesConfrim.setOnClickListener(this);
    }

    //    private int positon = -1;
    private List<String> circleIds;

    private void initFlowLayout() {
        for (int i = 0, j = followCircles.size(); i < j; i++) {
//            final int num = i;
            final String item = followCircles.get(i);
//            }
            final CheckBox checkBox = (CheckBox) LayoutInflater.from(this).inflate(R.layout.checkbox, null);
            final String showDate = MapDataUtils.getCircleValues(item);
            checkBox.setText(showDate);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {//隐藏关注的圈子 并在头部显示选择的圈子 还要设置 发布的圈子的id
                        circleIds.add(item);
//                        String str = getString(R.string.release_pos, getShowCircleIds());
//                        tv_title.setText(str);
//                        CircleID = item;
                        openImg();
//                        if (-1 != positon) {//第一次选中
//                            ((CheckBox) mFlowLayout.getChildAt(positon)).setChecked(false);
//                        }
//                        positon = num;
//                        mFlowLayout.setVisibility(View.GONE);
                    } else {
                        circleIds.remove(item);

                    }
//                    String str = getString(R.string.release_pos, getShowCircleIds());
//                    tv_title.setText(str);
                }
            });
            mFlowLayout.addView(checkBox);
        }

    }

    private String getCircleIds() {
        return MapDataUtils.listToString(circleIds);
    }

    private String getShowCircleIds() {
        return MapDataUtils.getMapValuesStr(GlobalData.circleValues, getCircleIds());
    }


    private void openImg() {
        ObjectAnimator.ofFloat(img, "rotation", 90, 0).setDuration(200).start();
    }

    private void closeImg() {
        ObjectAnimator.ofFloat(img, "rotation", 0, 90).setDuration(200).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
//                startActivity(new Intent(this, MainActivity.class));
                break;
            }
            case R.id.release_forum: {
                getString();
                break;
            }
            case R.id.delete_bt_content: {
                title.setText("");
                break;
            }
            case R.id.delte_all: {
                content.setText("");
                break;
            }
            case R.id.ll: {
                setFLChanged();
                break;
            }
            case R.id.confirm: {
                setFLChanged();
                String str = getString(R.string.release_pos, getShowCircleIds());
                tv_title.setText(str);
                break;
            }
        }
    }

    private void setFLChanged() {
        if (View.VISIBLE == mFlowLayout.getVisibility()) {
            mFlowLayout.setVisibility(View.GONE);
            chooseCirclesConfrim.setVisibility(View.GONE);
            openImg();
        } else {
            mFlowLayout.setVisibility(View.VISIBLE);
            chooseCirclesConfrim.setVisibility(View.VISIBLE);
            closeImg();
        }
    }

    /**
     * 发布帖子
     */
    private void postForum() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("正在发布帖子");
        mProgressDialog.show();
        if (selectedPicture != null && selectedPicture.size() > 0) {//说明是有图片的

            new Thread(new Runnable() {
                @Override
                public void run() {
                    uploadPhoto();
                }
            }).start();
        } else {
            new PostForumPresenter(this).getData();
        }
    }

    private List<String> urls;//这个里面装着上传到cdn的url
    //    private int index = 0; //这个表示长传成功图片的数量
    private ProgressDialog mProgressDialog;

    private void uploadPhoto() {

        urls = new ArrayList<>();
        final int index = selectedPicture.size();
        for (int i = 0; i < selectedPicture.size(); i++) {
            final CDNHelper get = new CDNHelper(this);
            try {
                String imageName = getImgName(this, false);
                get.uploadFile(compressFileName(selectedPicture.get(i)), imageName, new SaveCallback() {
                    @Override
                    public void onProgress(String s, int i, int i1) {
//                        LogUtil.i(TAG, "onProgress:" + s);
                    }

                    @Override
                    public void onFailure(String s, OSSException e) {
                        LogUtil.i(TAG, "onFailure:" + e);
//                        index++;
                    }

                    @Override
                    public void onSuccess(String s) {
                        LogUtil.i(TAG, "上传成功onSuccess:" + s);
                        urls.add(get.getResourseURL());
                        if (urls.size() == index) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    new PostForumPresenter(ReleaseForumActivity.this).getData();
                                }
                            });
                        }
                        LogUtil.i(TAG, "大图的URL:" + urls.toString());
                    }
                });


                final CDNHelper gets = new CDNHelper(this);
                gets.uploadFile(thumbnailBitmap(selectedPicture.get(i), 100, 100), insertThumb(imageName), new SaveCallback() {
                    @Override
                    public void onSuccess(String s) {
                        LogUtil.i(TAG, "上传小图成功onSuccess:" + s);
//                        String url = gets.getResourseURL();
//                        LogUtil.i(TAG, "小图的URL:" + url.toString());

                    }

                    @Override
                    public void onProgress(String s, int i, int i1) {

                    }

                    @Override
                    public void onFailure(String s, OSSException e) {

                    }
                });

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    private String thumbnailBitmap(String fileName, int width, int heigth) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);//可能内存溢出
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, heigth);
        return BitmapToFile(bitmap);
    }

    private void getString() {
        Title = title.getText().toString().trim();
        if (TextUtils.isEmpty(Title)) {
            showToast("请输入标题");
            return;
        }
        Content = content.getText().toString().trim();
        if (TextUtils.isEmpty(Content)) {
            showToast("请输入内容");
            return;
        }
        if (Content.length() < 10) {
            showToast("内容需大于10个字");
            return;
        }

        if (TextUtils.isEmpty(CircleID)) {
            showToast("请选择要发布的圈子");
            return;
        }
        postForum();
    }

    protected void initTitlebar(String title) {
        ImageView back = (ImageView) findViewById(R.id.title1).findViewById(R.id.btn_back);
        back.setOnClickListener(new ClickBack());
        TextView title_tv = (TextView) findViewById(R.id.title1).findViewById(R.id.title);
        title_tv.setText(title);
    }


    private String Tag = "7001";//症状的id
    private String Title;//
    private String Content;//

    private String CircleID;//


    private String URLNum;//表示多少张图片的url
    private String URL;//这个是图片的url 如果一张就是url0 二张 就是url1

    //    private String InfoName = "dafdafdsa"+ new Random().nextInt(100);
    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        if (tag == 0) {
            map.put(Contants.InfoID, UserinfoData.getInfoID(this));
            map.put(Contants.Tag, Tag);
            map.put(Contants.InfoName, UserinfoData.getInfoname(this));//这个是用来判断先产生的用户
            map.put(Contants.Title, Title);
            String jiami = DecryptUtils.encode(Content);
            map.put(Contants.Content, jiami);
            map.put(Contants.CircleID, getCircleIds());
            if (urls != null && urls.size() > 0) {
                map.put(Contants.URLNum, urls.size() + "");
                for (int i = 0; i < urls.size(); i++) {
                    map.put(Contants.URL + i, urls.get(i) + "");
                }
            }
        }
        return map;
    }

    @Override
    public void toActivity(Object t, int position) {
        if (position == 0) {
            PostForumBean postForumBean = (PostForumBean) t;
            LogUtil.i(TAG, t.toString());
            if (Contants.OK_DATA.equals(postForumBean.getIResult())) {
                showToast("发布成功");
                finish();
            }
        }
    }

    @Override
    public void showLoading(int tag) {

    }

    @Override
    public void hideLoading(int tag) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int tag) {

    }

//


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        if (position == selectedPicture.size()) {
            int index = adapter.getCount();
            if (index >= 10) {
                showToast("最多上传9张图片");
                return;
            }
            Intent intent = new Intent(this, SelectPictureActivity.class);
            index = 10 - index;
            intent.putExtra(SelectPictureActivity.INTENT_MAX_NUM, index);
            startActivityForResult(
                    intent, REQUEST_PICK);
        } else {
            startActivity(new Intent(this, ShowPhotoActivity.class).putExtra("list", (Serializable) selectedPicture).putExtra("position", position));

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            List list = (ArrayList<String>) data
                    .getSerializableExtra(SelectPictureActivity.INTENT_SELECTED_PICTURE);
            LogUtil.i(TAG, "选择的图片uri是：" + list.toString());
            selectedPicture
                    .addAll(list);
            adapter.updateDate(selectedPicture);
//            gridView.setAdapter(new GridAdapter(selectedPicture));
        }
    }

    /**
     * 压缩图片
     *
     * @param fileName
     * @return
     */
    private String compressFileName(String fileName) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);
        return BitmapToFile(bitmap);
    }

    @NonNull
    private String BitmapToFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

        while (baos.toByteArray().length > 200) {
            options -= 10;
            if (options <= 80) {
                break;//跳出循环
            }
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        FileOutputStream fos = null;
        tempFile = new File(Environment.getExternalStorageDirectory(),
                getImgName(ReleaseForumActivity.this, false));
        if (!tempFile.exists()) {
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.i(TAG, "file path : " + tempFile.getPath());
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tempFile.getPath();
    }

    private File tempFile;

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + (new Random().nextInt()) + ".jpg";
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tempFile != null) {
            tempFile.delete();//删除掉 文件
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG, "onPause");
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (position == adapter.getCount() - 1) {
            return true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseForumActivity.this);
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectedPicture.remove(position);
                adapter.updateDate(selectedPicture);
            }
        });
        builder.create().show();
        return true;
    }

    public static File scal(File outputFile) {
//        File outputFile = new File(path);
        long fileSize = outputFile.length();
        final long fileMaxSize = 200 * 1024;
        if (fileSize >= fileMaxSize) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(outputFile.getPath(), options);
            int height = options.outHeight;
            int width = options.outWidth;

            double scale = Math.sqrt((float) fileSize / fileMaxSize);
            options.outHeight = (int) (height / scale);
            options.outWidth = (int) (width / scale);
            options.inSampleSize = (int) (scale + 0.5);
            options.inJustDecodeBounds = false;

            Bitmap bitmap = BitmapFactory.decodeFile(outputFile.getPath(), options);
//            outputFile = new File(PhotoUtil.createImageFile().getPath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outputFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
                fos.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
//            Log.d(, sss ok  + outputFile.length());
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            } else {
                File tempFile = outputFile;
//                outputFile = new File(PhotoUtil.createImageFile().getPath());
//                PhotoUtil.copyFileUsingFileChannels(tempFile, outputFile);
            }

        }
        return outputFile;

    }
}
