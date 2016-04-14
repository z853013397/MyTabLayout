package com.zeyuan.kyq.fragment.main;

import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.zeyuan.kyq.R;
import com.zeyuan.kyq.app.BaseFragment;
import com.zeyuan.kyq.utils.IntegerVersionSignature;
import com.zeyuan.kyq.utils.UserinfoData;
import com.zeyuan.kyq.widget.CircleImageView;

/**
 * Created by Administrator on 2016/1/12.
 */
public class TabFragment extends BaseFragment {
    protected CircleImageView avatar;
    protected void showAvatar() {
        String imageUrl = UserinfoData.getAvatarUrl(context);
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl).signature(new IntegerVersionSignature(1)).error(R.mipmap.default_avatar).into(avatar);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (avatar != null) {
            showAvatar();
        }
    }

    public void scrollTop() {

    }

}
