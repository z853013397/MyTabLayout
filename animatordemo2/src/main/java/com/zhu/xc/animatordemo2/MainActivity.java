package com.zhu.xc.animatordemo2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
    }

    public void onClick(View v) {
        AnimatorHelper helper = new AnimatorHelper(img);
        helper.setAnimatorParams(dp2Px(30),dp2Px(100),500,0).startAnimator();

        Log.i(TAG, "view start translation " + img.getTranslationX());
    }


    private int dp2Px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public void onClicks(View v) {
        AnimatorHelper helper = new AnimatorHelper(img);
        helper.setAnimatorParams(dp2Px(60),dp2Px(100),500,0).setEndAnimator();
        Log.i(TAG, "view end translation " + img.getTranslationX());
    }


}
