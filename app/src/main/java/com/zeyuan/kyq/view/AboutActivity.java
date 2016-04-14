package com.zeyuan.kyq.view;

import android.os.Bundle;
import android.widget.TextView;

import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.DataUtils;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initTitlebar(getString(R.string.about));
        TextView versionName = (TextView) findViewById(R.id.version_name);
        String name = DataUtils.getVersionName(this);
        versionName.setText(getString(R.string.version_name) + name);
    }

}
