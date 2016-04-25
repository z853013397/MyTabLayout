package com.zhu.xc.jsdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showWebView();
    }

    private WebView mWebView;
    private void showWebView() {
//        mWebView = new WebView(this);
        mWebView = (WebView) this.findViewById(R.id.webview);
//        setContentView(mWebView);
//        mWebView.requestFocus();
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
//                super.onProgressChanged(view, newProgress);
                MainActivity.this.setTitle("Loading...");
                MainActivity.this.setProgress(newProgress);
                if(newProgress >= 80) {
                    MainActivity.this.setTitle("js test...");
                }
            }

        });
        mWebView.setOnKeyListener(new View.OnKeyListener(){

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });
//        mWebView.setonkey
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(false);
        webSettings.setSupportZoom(false);
        webSettings.setDefaultTextEncodingName("UTF-8");
        mWebView.addJavascriptInterface(this,"jsObj");
        mWebView.loadUrl("http://192.168.56.1:8080/examples/jsandroid.html");
    }
//    @JavascriptInterface
//    private Object getHtmlObject(){
//        Object insertObj = new Object(){
            @android.webkit.JavascriptInterface
            public String HtmlcallJava(){
                return "Html call Java";
            }
            @android.webkit.JavascriptInterface
            public String HtmlcallJava2(final String param){
                return "Html call Java : " + param;
            }





            /**
             * java 调用 js 中的方法
             */
            @android.webkit.JavascriptInterface
            public void JavacallHtml(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript: showFromHtml()");
                        Toast.makeText(MainActivity.this, "clickBtn", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            /**
             * java 调用 js 中的方法
             */
            @android.webkit.JavascriptInterface
            public void JavacallHtml2(){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript: showFromHtml2('IT-homer blog')");
                        Toast.makeText(MainActivity.this, "clickBtn2", Toast.LENGTH_SHORT).show();
                    }
                });
            }
//        };
//
//        return insertObj;
//    }
//
    public void onClick(View v) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl("javascript: showFromHtml2('IT-homer blog')");
                Toast.makeText(MainActivity.this, "clickBtn2", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
