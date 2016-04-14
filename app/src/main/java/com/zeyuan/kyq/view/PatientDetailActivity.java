package com.zeyuan.kyq.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.app.GlobalData;
import com.zeyuan.kyq.bean.BaseBean;
import com.zeyuan.kyq.bean.PatientDetailBean;
import com.zeyuan.kyq.bean.TNMObj;
import com.zeyuan.kyq.fragment.dialog.CancerTypeDialog;
import com.zeyuan.kyq.fragment.dialog.CityDialog;
import com.zeyuan.kyq.fragment.dialog.DialogFragmentListener;
import com.zeyuan.kyq.fragment.dialog.DigitDialog;
import com.zeyuan.kyq.fragment.dialog.GeneDialog;
import com.zeyuan.kyq.fragment.dialog.ListDialog;
import com.zeyuan.kyq.presenter.PatientDetailPresenter;
import com.zeyuan.kyq.presenter.UpdatePatientDetailPresenter;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.CDNHelper;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.LogUtil;
import com.zeyuan.kyq.utils.DataUtils;
import com.zeyuan.kyq.utils.MapDataUtils;
import com.zeyuan.kyq.utils.NetNumber;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 患者详情
 */
public class PatientDetailActivity extends BaseActivity implements View.OnClickListener, ViewDataListener, DialogFragmentListener, OnDismissListener {
//    private static final int AVATAR_CHANGE = 1 << 0;//头像修改
//    private static final int NAME_CHANGE = 1 << 1;//姓名
//    private static final int SEX_CHANGE = 1 << 2;//性别
//    private static final int AGE_CHANGE = 1 << 3;//age
//    private static final int LOCATION_CHANGE = 1 << 4;//location
//    private static final int CANCER_TYPE_CHANGE = 1 << 5;//cancer_type
//    private static final int CANCER_POS = 1 << 6;//转移
//
//    private static final int GENE_CHANGE = 1 << 7;//gene
//    private static final int DISCOVER_TIME_CHANGE = 1 << 8;//discoverTime
//    private static final int TNM_CHANGE = 1 << 9;//period type he id 分期
////    private static final int NUM_CHANGE = 1 << 10;//TNM数字分期

    private static final int AVATAR_SIZE = 500;//上传头像的最大 内存  单位kb

    private static final String TAG = "PatientDetailActivity";
    private static final String DIGIT_TYPE = "1";
    private static final String DIGIT_TNM_TYPE = "2";
    private static final String NO_DATA = "0";

    private CircleImageView CircleImageView_avatar;//头像
    private TextView mTextView_name;//名字
    private TextView mTextView_sex;//性别
    private TextView mTextView_age;//年龄
    private TextView mTextView_location;//所在地
    private TextView mTextView_cancer_type;//癌症种类

    private TextView mTextView_transfer_pos;//转移情况
    private TextView mTextView_gene;//基因类型
    private TextView mTextView_period_case;//突变类型

    private TextView cancer_time;//抗癌开始时间


    private LinearLayout ll;//这个是第三的三个展开项
    private TextView period_start;//原发肿瘤情况 T
    private TextView linba;//区域淋巴结情况 N
    private TextView far_trsnsfo_case;//远处转移情况 M


    private Button save;//修改信息之后的保存按钮 此处可以精简代码

    /**
     * 拍照用到的
     */
    public static final int PHOTOHRAPH = 1;// 拍照
    public static final int PHOTOZOOM = 2; // 缩放
    public static final int PHOTORESOULT = 3;// 结果
    public static final int NONE = 0;
    public static final String IMAGE_UNSPECIFIED = "image/*";

    private ImageView back;

    private AlertDialog dialog;

//    private String cancerId;//把从网络数据中获取的cancerid 设置成成员变量是因为在选择分期的dialog中要用到


    /**
     * 网络访问层
     */
    private void bindView(PatientDetailBean patientDetailBean) {
        String name = patientDetailBean.getInfoName();
        setTextString(mTextView_name, name);//设置名字
//        if (!TextUtils.isEmpty(name)) {
//            UserinfoData.saveInfoname(this, name);
//        }
        String headImgUrl = patientDetailBean.getHeadimgurl();
        if (!TextUtils.isEmpty(headImgUrl)) {
//            UserinfoData.saveAvatarUrl(this, headImgUrl);
            Glide.with(PatientDetailActivity.this).load(headImgUrl).signature(new IntegerVersionSignature(1))
                    .error(R.mipmap.default_avatar)//这个是加载失败的
                            // .placeholder() 这个是在加载等待中
                    .into(CircleImageView_avatar);
        }
        /**
         * 1是男 2是女
         */
        mTextView_sex.setText("1".equals(patientDetailBean.getSex()) ? getString(R.string.man) : getString(R.string.women));//设置性别

        String age = patientDetailBean.getAge();
        setTextString(mTextView_age, age);//设置年龄

        String location = patientDetailBean.getCity();
        setTextString(mTextView_location, MapDataUtils.getCityValue(location));//设置城市

        String data = String.valueOf(patientDetailBean.getDiscoverTime());
        setTextString(cancer_time, DataUtils.getDate(data));//设置抗癌开始时间

        CancerID = String.valueOf(patientDetailBean.getCancerID());
        setTextString(mTextView_cancer_type, GlobalData.cancerValues.get(CancerID));//设置癌症种类

        showGene(patientDetailBean.getGene());

        showTransData(patientDetailBean.getTransferPos());//设置突变情况

        String periodID = MapDataUtils.getDigitValues(patientDetailBean.getPeriodID());//设置突变情况
        if (TextUtils.isEmpty(periodID) || NO_DATA.equals(periodID)) {
            mTextView_period_case.setText(R.string.no_data);
        }
        setTextString(mTextView_period_case, periodID);//设置转移

        setProgressGone();//
    }

    private ScrollView sv;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initView();
        initData();
        setListener();
    }


    private void initData() {
        new PatientDetailPresenter(this).getData();
    }

    private void setListener() {
        back.setOnClickListener(this);//返回的点击
        CircleImageView_avatar.setOnClickListener(this);//头像的点击
        cancer_time.setOnClickListener(this);//抗癌开始日的点击
        mTextView_age.setOnClickListener(this);//年龄的点击
        mTextView_period_case.setOnClickListener(this);
        mTextView_sex.setOnClickListener(this);//性别的点击
        mTextView_name.setOnClickListener(this);//名字的点击
        mTextView_location.setOnClickListener(this);//位置的点击
        mTextView_cancer_type.setOnClickListener(this);//癌症的点击
        save.setOnClickListener(this);//保存的点击
        mTextView_transfer_pos.setOnClickListener(this);//脑转
        mTextView_gene.setOnClickListener(this);//基因
        far_trsnsfo_case.setOnClickListener(this);
        linba.setOnClickListener(this);
        period_start.setOnClickListener(this);
    }

    private void initView() {
        save = (Button) findViewById(R.id.save);
        sv = (ScrollView) findViewById(R.id.sv);
        ll = (LinearLayout) findViewById(R.id.ll);
        back = (ImageView) findViewById(R.id.btn_back);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.patient_detail));
        CircleImageView_avatar = (CircleImageView) findViewById(R.id.avatar);
        mTextView_name = (TextView) findViewById(R.id.name);
        mTextView_sex = (TextView) findViewById(R.id.sex);
        cancer_time = (TextView) findViewById(R.id.cancer_time);
        mTextView_age = (TextView) findViewById(R.id.age);
        mTextView_location = (TextView) findViewById(R.id.location);
        mTextView_cancer_type = (TextView) findViewById(R.id.cancer_type);
        mTextView_transfer_pos = (TextView) findViewById(R.id.transfer_pos);
        mTextView_gene = (TextView) findViewById(R.id.gene);
        mTextView_period_case = (TextView) findViewById(R.id.period_case);
        far_trsnsfo_case = (TextView) findViewById(R.id.far_trsnsfo_case);
        linba = (TextView) findViewById(R.id.linba);
        period_start = (TextView) findViewById(R.id.period_start);
//        findViewById(R.id.contents).setVisibility(View.VISIBLE);
    }

    private boolean flags = true;//ture表示显示数字分期 false 表示 打开tnm分期

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
                break;
            }
            case R.id.avatar: {
                takePhoto();
                break;
            }
            case R.id.name: {
                showInputName();
                break;
            }
            case R.id.sex: {
                showSelectSex();
                break;
            }
            case R.id.age: {
                showInputAge();
                break;
            }
            case R.id.cancer_type: {
                showCancerType();
                break;
            }
            case R.id.transfer_pos: {//显示转移
                createTransDialog();
                break;
            }
            case R.id.gene: {//基因
                if (TextUtils.isEmpty(CancerID)) {
                    Toast.makeText(PatientDetailActivity.this, "请选择癌肿", Toast.LENGTH_SHORT).show();
                    return;
                }
                showGeneDialog();
                break;
            }
            case R.id.period_case: {//请选择分期
                if (TextUtils.isEmpty(CancerID)) {
                    Toast.makeText(PatientDetailActivity.this, "请选择癌肿", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (flags) {
                    showDigitDataDialog();
                } else {
                    openTNM();
                }

                break;
            }
            case R.id.cancer_time: {
                showTime();
                break;
            }
            case R.id.location: {
                CityDialog dialog = new CityDialog();
                dialog.setOnOnCitySelListener(this);
                dialog.show(getFragmentManager(), Contants.CITY_DIALOG);

                break;
            }
            case R.id.save: {
                /**
                 * 当更换过头像 逻辑是先上传到cdn 在 上传到服务器
                 */
                if (tempFile == null) {
                    updatePaitentDetail();
                } else {
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
                    updateAvatar(tempFile);
//                        }
//                    }).start();
                }
                break;
            }
            case R.id.far_trsnsfo_case: {//远处转移情况  M
                showDigitMDialog();
                break;
            }
            case R.id.period_start: {//原发肿瘤情况  T
                showDigitTDialog();
                break;
            }
            case R.id.linba: {//区域淋巴结情况 N
                showDigitNDialog();
                break;
            }
        }
    }

    private void showTime() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DATE);


        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;//特么我也不知道 为什么要加1 才正确
                StringBuilder builder = new StringBuilder().append(year).append("-")
                        .append((monthOfYear) < 10 ? "0" + (monthOfYear) : (monthOfYear))
                        .append("-")
                        .append((dayOfMonth < 10) ? "0" + (dayOfMonth) : (dayOfMonth));
                cancer_time.setText(builder);
                DiscoverTime = DataUtils.showTimeToLoadTime(builder.toString());
                save.setVisibility(View.VISIBLE);
            }
        }, year, month, day).show();
    }

    private List<String> chooseGeneList;

    private void showGeneDialog() {
        List<String> data = GlobalData.gene.get(CancerID);
        if (data == null || data.isEmpty()) {
            Toast.makeText(this, R.string.cancer_has_no_gene,Toast.LENGTH_LONG).show();
            return;
        }
        GeneDialog dialog;
        if (chooseGeneList != null && chooseGeneList.size() > 0) {
            dialog = new GeneDialog(data, chooseGeneList, 0);
        } else {
            dialog = new GeneDialog(data, null, 0);
        }
        dialog.setTitle("请选择突变情况");
        dialog.setDialogFragmentListener(this);
        dialog.show(getFragmentManager(), Contants.GENE_DIALOG);
    }


    private boolean flag = true;//控制原始肿瘤情况显示

    private void showTNM() {
        if (flag) {
            openTNM();
            flag = false;
        } else {
            closeTNM();
            flag = true;
        }
    }

    private void closeTNM() {
        ll.setVisibility(View.GONE);
    }

    private void openTNM() {
        ll.setVisibility(View.VISIBLE);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                sv.fullScroll(ScrollView.FOCUS_DOWN);//scrollview移动到底部
            }
        });
    }


    private void showDigitTDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_T, CancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_T_DIALOG);
    }

//    private static final String digit_n = "digit_n";

    private void showDigitNDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_N, CancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_N_DIALOG);
    }

    /**
     * M分期
     */
//    private static final String digit_m = "digit_m";
    private void showDigitMDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT_M, CancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_M_DIALOG);
    }

//    private static final String digit = "digit";

    /**
     * 这个是显示数字分期
     */
    private void showDigitDataDialog() {
        DigitDialog dialog = DigitDialog.newInstance(DigitDialog.DIGIT, CancerID);
        dialog.setListener(this);
        dialog.show(getFragmentManager(), Contants.DIGIT_DIALOG);
    }

    private void showCancerType() {
        CancerTypeDialog dialog = new CancerTypeDialog();
        dialog.setOnCancerTyperListener(this);
        dialog.show(getFragmentManager(), Contants.CANCER_DIALOG);
    }

    private void closeKeyboard() {
        //关闭软键盘
        if (etName != null) {
            imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
        }
        //恢复位置
//        mAlertViewExt.setMarginBottom(0);
    }

    /**
     * 显示输入的年龄
     */
    private void showInputAge() {
//        List<String> list = new ArrayList<>();
//        for (int i = 1; i < 121; i++) {
//            list.add(String.valueOf(i));
//        }
//
//        ListDialog dialog = new ListDialog(list, 3, 0);
//        dialog.setTitles("请选择年龄");
//        dialog.setListener(new DialogFragmentListener() {
//            @Override
//            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
//                setChageAge(data);
//            }
//        });
//        dialog.show(getFragmentManager(), "dfl");

        DigitDialog dialog1 = DigitDialog.newInstance(DigitDialog.AGE, null);
        dialog1.setListener(new DialogFragmentListener() {

            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                setChageAge(data);
            }
        });

        dialog1.show(getFragmentManager(), "age");


    }

    private void setChageAge(String inputName) {
        mTextView_age.setText(inputName);
        Age = inputName;
        setViewVisible();
    }

    private boolean cancerChanged = false;
    /**
     * 后台请求数据更新
     */
    private PatientDetailBean updatePatientbean;

    private void updatePaitentDetail() {
        updatePatientbean = new PatientDetailBean();
        {
            updatePatientbean.setGene(Gene);
            updatePatientbean.setAge(Age);
            if (cancerChanged) {
                updatePatientbean.setCancerID(CancerID);
            }
            updatePatientbean.setCity(City);
            updatePatientbean.setDiscoverTime(DiscoverTime);
            updatePatientbean.setHeadImgUrl(Headimgurl);
            updatePatientbean.setPeriodID(PeriodID);
            updatePatientbean.setSex(Sex);
            updatePatientbean.setInfoName(InfoName);
            updatePatientbean.setTransferPos(TransferPos);
            updatePatientbean.setProvince(ProvinceID);
            updatePatientbean.setPeriodType(PeriodType);
        }
        new UpdatePatientDetailPresenter(updatePatientbean, this).getData();//不持有线程引用
    }

    private EditText etName;

    /**
     * 修改名字 弹出对话框
     */
    private void showInputName() {
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.alertext_form, null);
        etName = (EditText) extView.findViewById(R.id.etName);
        new AlertView(null, "请完善你的个人资料！", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                LogUtil.i(TAG, "个人资料填的是哪个位置：" + position);
                if (position == 0) {
                    String name = etName.getText().toString().trim();
                    if (!TextUtils.isEmpty(name)) {
                        setChangeName(name);
                    }
                }
                closeKeyboard();
            }
        }).addExtView(extView).setOnDismissListener(this).setCancelable(true).show();

    }

    private void setChangeName(String inputName) {
        mTextView_name.setText(inputName);
        InfoName = inputName;
        setViewVisible();
    }

    /**
     * 修改setbit的值，并显示保存按钮
     *
     * @param
     */
    private void setViewVisible() {
        save.setVisibility(View.VISIBLE);
    }

    /**
     * 弹出选择性别的选择框
     */
    private void showSelectSex() {
        new AlertView("选择性别", null, "取消", null,
                new String[]{"男", "女"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        setSexMan();
                        break;
                    case 1:
                        setSexWomen();
                        break;
                }
            }
        }).setOnDismissListener(this).setCancelable(true).show();
    }

    private void setSexWomen() {
        mTextView_sex.setText(R.string.women);
        Sex = "2";
        setViewVisible();
    }

    private void setSexMan() {
        mTextView_sex.setText(R.string.man);
        Sex = "1";
        setViewVisible();
    }

    /**
     * 弹出底下的dialog
     */
    public void takePhoto() {
        new AlertView("上传头像方式", null, "取消", null,
                new String[]{"拍照", "从相册中选择"},
                this, AlertView.Style.ActionSheet, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                switch (position) {
                    case 0:
                        fromTP();
                        break;
                    case 1:
                        fromPic();
                        break;
                }
            }
        }).setCancelable(true).show();


    }


    /**
     * 从拍照中获取图片
     */
    private void fromTP() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getFilesDir(), "temp.jpg")));
        startActivityForResult(intent, PHOTOHRAPH);
    }

    /**
     * 从相册中获取图片
     */
    private void fromPic() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            startActivityForResult(intent, PHOTOZOOM);
        } else {
            Toast.makeText(PatientDetailActivity.this, "无法连接到相册", Toast.LENGTH_SHORT).show();
        }
    }

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("return-data", true);
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        if (componentName != null) {
            startActivityForResult(intent, PHOTORESOULT);
        } else {
            Toast.makeText(PatientDetailActivity.this, "无法连接到系统裁剪功能", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == NONE) {
            Toast.makeText(this, R.string.choose_no_photo, Toast.LENGTH_SHORT).show();
            return;
        }
        // 拍照
        if (requestCode == PHOTOHRAPH) {
            Uri uri = data.getData();
            //设置文件保存路径这里放在跟目录下
            Bitmap cameraPhoto = data.getExtras().getParcelable("data");//从流中得到图片
            FileOutputStream foss = null;
            tempFile = new File(Environment.getExternalStorageDirectory(),
                    getPhotoFileName());
            try {
                foss = new FileOutputStream(tempFile);
                cameraPhoto.compress(Bitmap.CompressFormat.JPEG, 100, foss);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (foss != null) {
                    try {
                        foss.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            startPhotoZoom(Uri.fromFile(tempFile));
        }

        if (data == null)
            return;

        // 读取相册缩放图片
        if (requestCode == PHOTOZOOM) {
            startPhotoZoom(data.getData());
        }
        // 处理结果
        if (requestCode == PHOTORESOULT) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap photo = extras.getParcelable("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int options = 100;
                photo.compress(Bitmap.CompressFormat.JPEG, options, baos);
//                while (baos.toByteArray().length > AVATAR_SIZE) {
//                    options -= 10;
//                    if (options <= 0) {
//                        break;//跳出循环
//                    }
//                    baos.reset();
//                    photo.compress(Bitmap.CompressFormat.JPEG, options, baos);
//                }
                FileOutputStream fos = null;
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        getPhotoFileName());
                try {
                    fos = new FileOutputStream(tempFile);
//                    photo.compress(Bitmap.CompressFormat.JPEG, 80, fots);

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
                CircleImageView_avatar.setImageBitmap(photo);
                setViewVisible();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File tempFile;//这个文件 看名字

    private void updateAvatar(File photo) {
        final CDNHelper getDemo = new CDNHelper(this);
        final CDNHelper littleDemo = new CDNHelper(this);
        try {
            String imageName = getImgName(PatientDetailActivity.this, true);
            getDemo.uploadFile(photo.getPath(), imageName, new SaveCallback() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.i(TAG, "上传头像成功.名字是：" + s);
                    Headimgurl = getDemo.getResourseURL();
                    LogUtil.i(TAG, "上传头像成功.rul是：" + Headimgurl);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updatePaitentDetail();
                        }
                    });
                    /**
                     * 也可以这么写
                     */
//                    new Handler(Looper.getMainLooper()).post(new Runnable() {
//                        @Override
//                        public void run() {
//                            updatePaitentDetail();
//                        }
//                    });
                }

                @Override
                public void onProgress(String s, int i, int i1) {
                    LogUtil.i(TAG, "onProgress:" + i);
                }

                @Override
                public void onFailure(String s, OSSException e) {
                    LogUtil.i(TAG, "onFailure:" + s);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PatientDetailActivity.this, "上传头像失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            littleDemo.uploadFile(thumbnailBitmap(photo.getPath(), 100, 100).getPath(), insertThumb(imageName), new SaveCallback() {
                @Override
                public void onSuccess(String s) {
                    LogUtil.i(TAG, "头像小图的url是：" + s);
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
            Toast.makeText(PatientDetailActivity.this, R.string.fail_file, Toast.LENGTH_SHORT).show();
        }

    }


    public String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 生成100 * 100像素的缩率图
     * @param fileName
     * @param width
     * @param heigth
     * @return
     */
    private File thumbnailBitmap(String fileName, int width, int heigth) {
        Bitmap bitmap = BitmapFactory.decodeFile(fileName);//可能内存溢出
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, heigth);
        return compressBitmap(bitmap);
    }


    private File compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//        while (baos.toByteArray().length > 300) {
//            baos.reset();
//            options -= 10;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
//        }
//        mImageView_build_head.setImageBitmap(bitmap);
        FileOutputStream fos = null;
        tempFile = new File(Environment.getExternalStorageDirectory(),
                getPhotoFileName());
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
        return tempFile;
    }

    /**
     * 读取图片的旋转角度
     *
     * @param path 图片的路径
     * @return
     */
    public int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * @param bitmap  图片
     * @param degress 旋转的角度
     * @return 旋转后图片
     */
    public Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    private String InfoName;
    private String Headimgurl;
    private String CancerID;
    private String TransferPos;
    private String Gene;
    private String DiscoverTime;
    private String Sex;//0是nv 1是男
    private String Age;
    private String City;
    private String ProvinceID;
    private String digitT;
    private String digitN;
    private String digitM;
    private String Digit;
    private String PeriodType;//1 是数字分期 2 是tnm分期
    private String PeriodID;

    private int setBit = 0;//这个表示那个数据项目修改了。便于使后台少做一次查询

    @Override
    public Map getParamInfo(int tag) {
        Map<String, String> map = new HashMap<>();
        map.put(Contants.InfoID, UserinfoData.getInfoID(this));
//        if (tag == 1) {//这个是更新患者详情
//            if (!TextUtils.isEmpty(InfoName)) {
//                map.put(Contants.InfoName, InfoName);
//                setBit += NAME_CHANGE;
//            }
//            if (!TextUtils.isEmpty(CancerID)) {//修改癌种后，提交未知的分期和基因突变的信息（0）
//                setBit += CANCER_TYPE_CHANGE;
//                map.put(Contants.CancerID, CancerID);
//                Gene = "0";
//                PeriodID = "0";
//                PeriodType = "";
//            }
//            if (!TextUtils.isEmpty(Headimgurl)) {
//                setBit += AVATAR_CHANGE;
//                LogUtil.i(TAG, Headimgurl + "图片为空么");
//                map.put(Contants.Headimgurl, Headimgurl);
//            }
//
//            if (!TextUtils.isEmpty(TransferPos)) {
//                setBit += CANCER_POS;
//                map.put(Contants.TransferPos, TransferPos);
//            }
//            if (!TextUtils.isEmpty(Gene)) {
//                setBit += GENE_CHANGE;
//                map.put(Contants.Gene, Gene);
//
//            }
//            if (!TextUtils.isEmpty(DiscoverTime)) {
//                setBit += DISCOVER_TIME_CHANGE;
//                map.put(Contants.DiscoverTime, DiscoverTime);
//            }
//            if (!TextUtils.isEmpty(Sex)) {
//                setBit += SEX_CHANGE;
//                map.put(Contants.Sex, Sex);
//            }
//
//            if (!TextUtils.isEmpty(Age)) {
//                setBit += AGE_CHANGE;
//                map.put(Contants.Age, Age);
//            }
//
//            if (!TextUtils.isEmpty(City)) {
//                setBit += LOCATION_CHANGE;
//                UserinfoData.saveCityID(this, City);
//                map.put(Contants.City, City);
//                map.put(Contants.ProvinceID, ProvinceID);
//            }
//
//            if (!TextUtils.isEmpty(PeriodID) && !TextUtils.isEmpty(PeriodType)) {
//                setBit += TNM_CHANGE;
//                map.put(Contants.PeriodID, PeriodID);
//                map.put(Contants.PeriodType, PeriodType);
//            }
//            map.put(Contants.SetBit, String.valueOf(setBit));//这个用来标记 是哪些项目修改了
//        }
        return map;
    }

    private boolean isChangeCancerID(String cancerid) {
        if (CancerID.equals(cancerid)) {
            return false;
        }
        return true;
    }

    @Override
    public void toActivity(Object t, int positon) {
        if (positon == NetNumber.PATIENT_DETAIL) {
            PatientDetailBean patientDetailBean = (PatientDetailBean) t;
            LogUtil.i(TAG, "网络数据患者详情：" + patientDetailBean.toString());
            UserinfoData.saveUserData(this, patientDetailBean);
            bindView(patientDetailBean);
        }
        if (positon == NetNumber.UPDATE_PATIENT) {//这个是更新患者详情后返回的参数
            BaseBean beans = (BaseBean) t;
            LogUtil.i(TAG, t.toString());
            LogUtil.i(TAG, "网络数据2更新患者详情：" + beans.toString());
            if (Contants.RESULT.equals(beans.iResult)) {
                UserinfoData.saveUserData(this, updatePatientbean);
            }
//            if (!TextUtils.isEmpty(Headimgurl)) {
//                UserinfoData.saveAvatarUrl(this, Headimgurl);
//            }
//            if (!TextUtils.isEmpty(ProvinceID)) {
//                UserinfoData.saveProvinceID(this, ProvinceID);
//            }
//
//            if (!TextUtils.isEmpty(City)) {
//                UserinfoData.saveCityID(this, City);
//            }
//
//            if (!TextUtils.isEmpty(PeriodID)) {
//                UserinfoData.savePeriodID(this, PeriodID);
//            }
//            if (!TextUtils.isEmpty(CancerID)) {
//                UserinfoData.saveCancerID(this, CancerID);
//            }
//            if (!TextUtils.isEmpty(DiscoverTime)) {
//                UserinfoData.saveDiscoverTime(this, DiscoverTime);
//            }


            Toast.makeText(PatientDetailActivity.this, R.string.save_success, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private ProgressDialog mProgressDialog;

    @Override
    public void showLoading(int tag) {
        if (tag == 1) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("正在保存");
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading(int tag) {
        if (tag == 1) {
            mProgressDialog.dismiss();
//            Toast.makeText(PatientDetailActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showError(int tag) {
        LogUtil.i(TAG, "error");
    }


    private String tTemp = "";
    private String nTemp = "";
    private String mTemp = "";

    @Override
    public void getDataFromDialog(DialogFragment dialog, String data, int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment cancerType = fragmentManager.findFragmentByTag(Contants.CANCER_DIALOG);
        Fragment citydialog = fragmentManager.findFragmentByTag(Contants.CITY_DIALOG);

        Fragment digitDataDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_DIALOG);
        Fragment digitTDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_T_DIALOG);
        Fragment digitNDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_N_DIALOG);
        Fragment digitMDialog = fragmentManager.findFragmentByTag(Contants.DIGIT_M_DIALOG);
        Fragment geneDialog = fragmentManager.findFragmentByTag(Contants.GENE_DIALOG);
        if (dialog == geneDialog) {
//            Gene = data;//设置突变基因的id
            setGeneChange(data);
            if (TextUtils.isEmpty(data)) {
                mTextView_gene.setText("");
                chooseGeneList.clear();
                return;
            }
            showGene(data);
        }


        if (dialog == cancerType) {
            if (isChangeCancerID(data)) {
                CancerID = data;
                mTextView_cancer_type.setText(GlobalData.cancerValues.get(data));
                setViewVisible();
                cancerChanged = true;
            }

        }

        if (dialog == citydialog) {
            City = data;
            setViewVisible();
            ProvinceID = String.valueOf(position);
            mTextView_location.setText(MapDataUtils.getCityValue(data));
        }

        if (dialog == digitDataDialog) {//数字分期
            LogUtil.i(TAG, data);
            if (DigitDialog.SWITCH.equals(data)) {
                mTextView_period_case.setText("未知");
                openTNM();
                flags = false;
                return;
            }
            PeriodType = DIGIT_TYPE;
            PeriodID = data;
//            UserinfoData.savePeriodID(this, data);
            String temp = GlobalData.DigitValues.get(data);
            Digit = temp;
            mTextView_period_case.setText(temp);// 要现实保存按钮
            setViewVisible();
        }

        if (dialog == digitTDialog) {
            LogUtil.i(TAG, "digitTDialog:" + data);
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
            digitT = data;
//            String temp = ZYApplication.DigitValues.get(data);
            period_start.setText(getShow(data));
            tTemp = data;
            chooseTNMFinish();
        }

        if (dialog == digitNDialog) {
            LogUtil.i(TAG, "digitNDialog:" + data);
            PeriodType = "2";

            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
            digitN = data;
            linba.setText(getShow(data));
            nTemp = data;
            chooseTNMFinish();

        }

        if (dialog == digitMDialog) {
            LogUtil.i(TAG, "digitMDialog:" + data);
            PeriodType = "2";
            if (DigitDialog.SWITCH.equals(data)) {
                closeTNM();
                flags = true;
                return;
            }
            digitM = data;
            far_trsnsfo_case.setText(getShow(data));
            mTemp = data;
            chooseTNMFinish();
        }
    }

    private void showGene(String data) {
        if (TextUtils.isEmpty(data) || NO_DATA.equals(data)) {
            mTextView_gene.setText(R.string.no_data);
            return;
        }
        String[] temp = data.split(",");
        if (chooseGeneList == null) {
            chooseGeneList = new ArrayList<>();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length; i++) {
            chooseGeneList.add(temp[i]);
            if (i != 0) {
                sb.append(",");
            }
            sb.append(GlobalData.geneValues.get(temp[i]));
        }
        mTextView_gene.setText(sb);
    }

    private void setGeneChange(String data) {
        Gene = data;
        setViewVisible();
    }

    private void chooseTNMFinish() {
        if (!TextUtils.isEmpty(CancerID) && !TextUtils.isEmpty(tTemp) && !TextUtils.isEmpty(nTemp) && !TextUtils.isEmpty(mTemp)) {
            List<TNMObj> list = GlobalData.tnmObjs;
            int size = list.size();
            TNMObj tnmTmp = null;
            for (int i = 0; i < size; i++) {
                tnmTmp = list.get(i);
                if ((tnmTmp.getCancerId().equals(CancerID)) &&
                        (tnmTmp.getTid().equals("0") || tnmTmp.getTid().equals(tTemp))

                        && (tnmTmp.getNid().equals("0") || tnmTmp.getNid().equals(nTemp))

                        && (tnmTmp.getMid().equals("0") || tnmTmp.getMid().equals(mTemp))) {
                    String digitID = tnmTmp.getDigitId();
                    String showdigitId = GlobalData.DigitValues.get(digitID);
                    LogUtil.i(TAG, "digitid是：" + digitID + "showdigit是" + showdigitId);
                    mTextView_period_case.setText(showdigitId + "(" + getShow(tTemp) + getShow(nTemp) + getShow(mTemp) + ")");
//                    UserinfoData.savePeriodID(this, digitID);
                    PeriodID = digitID;
                    PeriodType = DIGIT_TNM_TYPE;
                    setViewVisible();
                    break;
                }
                PeriodID = "0";
                PeriodType = DIGIT_TNM_TYPE;
                mTextView_period_case.setText("未知" + "(" + getShow(tTemp) + getShow(nTemp) + getShow(mTemp) + ")");
                setViewVisible();
            }
        } else {
            mTextView_period_case.setText("未知" + "(" + getShow(tTemp) + getShow(nTemp) + getShow(mTemp) + ")");
//            mTextView_period_case.setText("未知");
        }
    }

//    public String getCancerId() {
//        return cancerId;
//    }

    /**
     * 根据选中的id 来获得显示的tnm分期
     * d @param data
     *
     * @return
     */
    private String getShow(String data) {
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        String id = GlobalData.DigitValues.get(data);
        StringBuffer temp = new StringBuffer(id);
        int index = id.indexOf(" ");
        StringBuffer sb = temp.delete(index, temp.length());
        return sb.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tempFile != null) {
            tempFile.delete();
        }
    }


    private void createTransDialog() {
        List<String> transPosList = new ArrayList<>();
        Set<String> set = GlobalData.transferpos.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            transPosList.add(key);
        }
        GeneDialog dialog;
        if (chooseTransData != null && chooseTransData.size() > 0) {
            dialog = new GeneDialog(transPosList, chooseTransData, Contants.diolog_type);
        } else {
            dialog = new GeneDialog(transPosList, null, Contants.diolog_type);
        }
        dialog.setTitle("请选择转移情况");
        dialog.setDialogFragmentListener(new DialogFragmentListener() {
            @Override
            public void getDataFromDialog(DialogFragment dialog, String data, int position) {
                TransferPos = data;
                setViewVisible();
                showTransData(data);
            }
        });
        dialog.show(getFragmentManager(), "transpos");
    }

    private List<String> chooseTransData;

    public void showTransData(String data) {
        if (TextUtils.isEmpty(data) || NO_DATA.equals(data)) {
            mTextView_transfer_pos.setText(R.string.no_data);
            return;
        }
        if (chooseTransData == null) {
            chooseTransData = new ArrayList<>();
        }
        String[] strings = data.split(",");
        StringBuilder sb = new StringBuilder();
        if (strings.length > 0) {
            for (String str : strings) {
                chooseTransData.add(str);
                if (sb.length() == 0) {
                    sb.append(GlobalData.transferpos.get(str));
                } else {
                    sb.append("," + GlobalData.transferpos.get(str));
                }
            }
        }
        mTextView_transfer_pos.setText(sb);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(Object o) {
        closeKeyboard();
    }
}
