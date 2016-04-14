package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;

/**
 * Created by Administrator on 2015/8/24.
 * 登录界面
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
   private Button login;
//    private AlertDialog dialog;
    private Button regiter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        setListener();
    }

    private void setListener() {
        login.setOnClickListener(this);
        regiter.setOnClickListener(this);
        initTitlebar("登录");
    }

    private void initView() {
        login = (Button) findViewById(R.id.btn_login);
        regiter = (Button) findViewById(R.id.btn_register);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this,MainActivity.class));
                finish();
                break;

            case R.id.btn_register:
                startActivity(new Intent(this,PatientInfoActivity.class));
                break;
        }
    }

    public void guide(View v) {
        startActivity(new Intent(this,GuideActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
