package com.k.bbws;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class PengaduanPublik extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@org.jetbrains.annotations.NotNull Message message) {
            switch (message.what) {
                case 1:{
                    webViewGoBack();
                }break;
            }
        }
    };

    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    WebSettings webSettings;

    String URL = "https://docs.google.com/forms/d/e/1FAIpQLSeeLX89vC0rLZ5j3qctmWi2FpKlTQsDW-gssVpDNYHCJa0Urw/viewform?embedded=true";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kritik_saran, container, false);

        //set web view
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        webView = view.findViewById(R.id.webView);
        swipeRefreshLayout.setOnRefreshListener(this);
        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // to enable JavaScript
        webSettings.getUseWideViewPort();

        webView.setOnKeyListener(new View.OnKeyListener(){

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }

                return false;
            }

        });


        webView.setWebViewClient(new WebViewClient(){
            // Ketika webview error atau selesai load page loading akan dismiss

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        webView.loadUrl(URL);
        return view;
    }
    private void webViewGoBack(){
        webView.goBack();
    }
    public void onRefresh() {
        webView.reload();
    }
}