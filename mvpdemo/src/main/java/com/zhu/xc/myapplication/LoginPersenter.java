package com.zhu.xc.myapplication;

/**
 * Created by xc on 2016/5/9.
 */
public class   LoginPersenter {
    private  LoginBiz biz;

    private loginView view;

    public LoginPersenter(loginView view) {
        this.view = view;
        biz = new LoginBizImp();
    }

    public  void login() {

        view.showProgress();
        biz.login(view.getUsername(),view.getPassword());


        view.hidePregress();


    }


}
