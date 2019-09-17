package com.chengzi.app.utils;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class JavaMyWebViewClient extends WebViewClient {
    @Override
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);
        // html加载完成之后，添加监听图片的点击js函数
        addImageClickListner(webView);
        imgReset(webView);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    // 注入js函数监听
    private void addImageClickListner(WebView webView) {
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "    objs[i].onclick=function()  " +
                "    {  "
                + "        window.imagelistner.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }

    /**
     * 循环遍历标签中的图片
     * js 语法
     */
    private void imgReset(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%';   " +
                "}" +
                "})()");
    }
}