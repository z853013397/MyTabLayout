package com.zhu.xc.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements loginView, View.OnClickListener {
    private LoginPersenter loginPersenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loginPersenter =  new LoginPersenter(this);
    }
    private EditText userName;
    private EditText password;
    private Button login;

    private void initView() {
        userName = (EditText) this.findViewById(R.id.username);
        password = (EditText) this.findViewById(R.id.password);
        login = (Button) this.findViewById(R.id.login);
        login.setOnClickListener(this);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
//        object.put(new Object());
        array.put(new ImageView(this));
    }


    @Override
    public String getUsername() {
        return userName.getText().toString();
    }

    @Override
    public String getPassword() {
        return password.getText().toString();
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hidePregress() {

    }

    @Override
    public void onClick(View v) {
        loginPersenter.login();
    }
}
