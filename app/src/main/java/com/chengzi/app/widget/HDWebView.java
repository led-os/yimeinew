package com.chengzi.app.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.blankj.utilcode.util.FileIOUtils;
import com.chengzi.app.ui.common.activity.PictureSwitcherActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HDWebView extends WebView {

    private ArrayList<String> imgs;
    private boolean isDetached;
    private OnHierarchyChangeListener listener = new OnHierarchyChangeListener() {
        @Override
        public void onChildViewAdded(View parent, View child) {

        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (child == HDWebView.this) {
                ((ViewGroup) parent).setOnHierarchyChangeListener(null);
                stopLoading();
                getSettings().setJavaScriptEnabled(false);
                clearHistory();
                clearView();
                clearMatches();
                removeAllViews();


                destroy();

            }
        }
    };

    public HDWebView(Context context) {
        super(context);
    }

    public HDWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HDWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public HDWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public static String fixImage(String htmltext, List<String> list) {
        if (TextUtils.isEmpty(htmltext)) {
            return "";
        }
        Document doc = Jsoup.parse(htmltext);

        Elements body = doc.getElementsByTag("body");
        body.attr("style","margin:0;padding:0");  //  取出webview 默认的内边框


        doc.append("<script language=\"javascript\">\n" +
                "function imgUrl(id){\n" +
                "android.scanPictures(id);\n" +
                "}\n" +
                "</script>");

        Elements elements = doc.getElementsByTag("img");
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.attr("width", "100%").attr("height", "auto");
            element.attr("style", "CURSOR: hand")
                    .attr("id", String.valueOf(i))
                    .attr("onClick", "imgUrl(id)");

            list.add(element.attr("src"));
        }

        return doc.toString();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isDetached = false;
        post(() -> {
            if (getParent() != null) {
                ((ViewGroup) getParent()).setOnHierarchyChangeListener(listener);
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDetached = true;
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null && !isDetached) {
            isDetached = false;
            parent.removeView(this);
        }
    }

    public void setContent(String content) {
        int i = content == null ? 0 : content.hashCode();
        File cache = new File(getContext().getExternalCacheDir(), i + "text.html");

        imgs = new ArrayList<>();
        String fixImage = fixImage(content, imgs);

        FileIOUtils.writeFileFromString(cache, fixImage);

        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(this, "android");
        loadUrl("file://" + cache.getPath());
    }

    @JavascriptInterface
    public void scanPictures(String id) {
        PictureSwitcherActivity.start(getContext(),imgs,Integer.valueOf(id));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (scrollable || (imgs != null && imgs.size() > 0)) {
            return super.onTouchEvent(event);
        } else {
            return false;
        }
    }

    private boolean scrollable;
    public void scrollable(boolean scrollable){
        this.scrollable = scrollable;
    }
}
