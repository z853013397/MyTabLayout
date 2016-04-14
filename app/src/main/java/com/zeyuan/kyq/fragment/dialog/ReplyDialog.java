package com.zeyuan.kyq.fragment.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zeyuan.kyq.R;

/**
 * User: san(853013397@qq.com)
 * Date: 2015-12-14
 * Time: 11:01
 * FIXME
 */


public class ReplyDialog extends DialogFragment {
    private View rootView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.dialog);
        dialog.setCancelable(false);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_replay, null);
        rootView.requestFocus();
        initView();
        initData();
        dialog.setContentView(rootView);
        Window window = dialog.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.mystyle);

        EditText reply_content = (EditText) rootView.findViewById(R.id.reply_content);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(reply_content, 0); //显示软键盘
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软键盘
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView() {

    }

    private void initData() {
    }
}
