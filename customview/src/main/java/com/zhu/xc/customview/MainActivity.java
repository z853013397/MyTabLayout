package com.zhu.xc.customview;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private GridView gridview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview);

//准备要添加的数据条目
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < 11; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.mipmap.ic_launcher);
            item.put("textItem", "text" + i);
            items.add(item);
        }
//实例化一个适配器
        SimpleAdapter adapter = new SimpleAdapter(this, items, R.layout.grid_item, new String[]{"imageItem", "textItem"}, new int[]{R.id.image_item, R.id.text_item});
        //获得GridView实例
        gridview = (GridView)findViewById(R.id.mygridview);
        //gridview.setNumColumns(3);//可以在xml中设置
        //gridview.setGravity(Gravity.CENTER);//同上
//将GridView和数据适配器关联
        gridview.setAdapter(adapter);

        gridview.setOnItemClickListener(this);
//        gridview.setStretchMode(GridView.STRETCH_SPACING);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(view.getTranslationX() != 500 ) {

            ObjectAnimator.ofFloat(view, "translationX", 20, 500).start();
        }else {
            ObjectAnimator.ofFloat(view, "translationX", 500, 20).start();
        }

    }
}

