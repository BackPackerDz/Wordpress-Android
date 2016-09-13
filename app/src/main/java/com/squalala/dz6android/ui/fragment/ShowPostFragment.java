package com.squalala.dz6android.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.squalala.dz6android.R;
import com.squalala.dz6android.common.BaseFragment;
import com.squalala.dz6android.greendao.Article;
import com.squalala.dz6android.ui.activity.ViewPagerActivity;
import com.squalala.dz6android.utils.DateUtils;
import com.squalala.dz6android.utils.HtmlUtils;
import com.squalala.dz6android.utils.IntentUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Back Packer
 * Date : 06/10/15
 */
public class ShowPostFragment extends BaseFragment {

    @Bind(R.id.webView)
    WebView webView;

    @Bind(R.id.title)
    TextView title;

    private String encoding = "utf-8";
    private String mime = "text/html";

    private String linkToShare;
    private boolean isNotification;

    private Long articleId;

    private Article article;

    private static String KEY_ID = "id";

    private ArrayList<String> urlImages;

    public static ShowPostFragment newInstance(long articleId) {

        ShowPostFragment fragment = new ShowPostFragment();
        Bundle args = new Bundle();

        args.putLong(KEY_ID, articleId);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.activity_show_post, container, false);

        articleId = getArguments().getLong(KEY_ID);

        article = articleDao.load(articleId);

        ButterKnife.bind(this, rootView);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);


        urlImages = HtmlUtils.getUrlImages(article.getContent());

        String htmlModified = HtmlUtils.addAttributesToImages(article.getContent());

    /*    System.out.println("============");
        System.out.println(htmlModified);
        System.out.println("============");*/
/*
        String htmlToAdd =
                "<center><b>"+ article.getTitle() +"</b></center><hr style=\"border-color:#D0D0D0\"></hr>\n" +
                        "<center style=\"color:#808080\">"+article.getAuthor()+ " - "+ DateUtils.getRelativeTime(article.getDate()) +"</center>";
*/
       //align="right"
        String htmlToAdd =
                "<p align=\"right\" style=\"color:#808080\">" + DateUtils.getRelativeTime(article.getDate()) +"</p><center><b>"+ article.getTitle() +"</b></center><hr style=\"border-color:#D0D0D0\"></hr>\n";



        String html = HtmlUtils.addHtml(htmlModified, htmlToAdd);


      //  System.out.println(html);

        webView.getSettings().setAppCacheEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

      //  webView.getSettings().setLoadWithOverviewMode(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowContentAccess(true);

        webView.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }
                return false;
            }

        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);
                System.out.println("hello " + url);

                if (url.startsWith("https://play.google.com/store/apps/details?id")) {
                    String packageId = url.replace("https://play.google.com/store/apps/details?id=", "")
                            .replace("&hl=fr", "");

                    Intent marketIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageId));
                    marketIntent2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(marketIntent2);
                }

                Uri uri = Uri.parse(url);
                if (uri.getHost().contains("youtube.com")) {
                    IntentUtils.viewYoutube(getContext(), url);
                    return true;
                }


                return true;
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                String javascript = "javascript:" +
                        "setTimeout(function() {" +
                        "   var iframes = document.getElementsByTagName('iframe');" +
                        "   for (var i = 0, l = iframes.length; i < l; i++) {" +
                        "       var iframe = iframes[i]," +
                        "       a = document.createElement('a');" +
                        "       a.setAttribute('href', iframe.src);" +
                        "       d = document.createElement('div');" +
                        "       d.style.width = iframe.offsetWidth + 'px';" +
                        "       d.style.height = iframe.offsetHeight + 'px';" +
                        "       d.style.top = iframe.offsetTop + 'px';" +
                        "       d.style.left = iframe.offsetLeft + 'px';" +
                        "       d.style.position = 'absolute';" +
                        "       d.style.opacity = '0';" +
                        "       d.style.filter = 'alpha(opacity=0)';" +
                        "       d.style.zIndex='" + Integer.MAX_VALUE + "';" +
                        "       d.style.background = 'black';" +
                        "       a.appendChild(d);" +
                        "       iframe.offsetParent.appendChild(a);" +
                        "   }" +
                        "}, 400);";
                view.loadUrl(javascript);

                super.onPageFinished(view, url);
            }
        });

        webView.addJavascriptInterface(new Object() {

            @JavascriptInterface           // For API 17+
            public void onClickImage(final String id) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //Code that interact with UI
                        System.out.println(id);

                        try {
                            Intent intent = new Intent(getActivity(), ViewPagerActivity.class);
                            intent.putStringArrayListExtra("urls", urlImages);
                            intent.putExtra("position", Integer.valueOf(id));
                            getActivity().startActivity(intent);
                            System.out.println(id);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }



        }, "Image");


        webView.loadDataWithBaseURL(null, html, mime, encoding, null);

        return rootView;
    }

    private void webViewGoBack(){
        webView.goBack();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };









}
