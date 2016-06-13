package com.zhu.xc.myapplication;

/**
 * Created by xc on 2016/5/9.
 */
public class LoginBizImp implements LoginBiz{
    @Override
    public void login(String username, String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
