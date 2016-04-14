package com.zeyuan.kyq.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseActivity;
import com.zeyuan.kyq.utils.Contants;
import com.zeyuan.kyq.utils.IntegerVersionSignature;

public class ShowImageActivity extends BaseActivity {
	public static final String ARGS_BITMAP = "args_bitmap";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ggz_activity_showimage);

		ImageView activity_showimage_image = (ImageView) findViewById(R.id.activity_showimage_image);
		Intent intent = getIntent();
		if (intent != null) {
//			byte[] bis = intent.getByteArrayExtra(ARGS_BITMAP);
//			Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
			String imageurl = intent.getStringExtra(Contants.Avatar);
//			activity_showimage_image.setImageBitmap(bitmap);
			Glide.with(this).load(imageurl).signature(new IntegerVersionSignature(1)).into(activity_showimage_image);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		finish();
		return super.onTouchEvent(event);
	}
}
