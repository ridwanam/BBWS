package com.k.bbws;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SearchFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Button button;
    TextView textView;
    EditText etCari;
    Animation btnAnim,textAnim,webAnim;
    Context context;
    int anim = 0;

    final Handler handler = new Handler();
    SwipeRefreshLayout swipeRefreshLayout;
    WebView webView;
    WebSettings webSettings;
    String URL = "https://www.bbwscilicis.com/?s=";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        button = view.findViewById(R.id.btnCari);
        etCari = view.findViewById(R.id.editText);
        textView = view.findViewById(R.id.txtJudul);
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
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // Menampilkan loading ketika webview proses load halaman
                swipeRefreshLayout.setRefreshing(true);
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etCari.onEditorAction(EditorInfo.IME_ACTION_DONE);
                if (anim == 0) {
                anim++;
                //load animation
                btnAnim = AnimationUtils.loadAnimation(getContext(),R.anim.totop);
                textAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);
                webAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fadein);
                //run animation
                etCari.startAnimation(textAnim);
                textView.startAnimation(textAnim);
                //run animation button
                button.startAnimation(btnAnim);
                //delayed program for wait animation finished
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 0.7s = 700ms
                        // Do logic
                        String getCari = etCari.getText().toString();
                        button.setY(100);
                        button.setText("Cari Kembali");
                        //set layout
////                        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
////                                ConstraintLayout.LayoutParams.WRAP_CONTENT,
////                                ConstraintLayout.LayoutParams.WRAP_CONTENT
////                        );
////                        params.setMargins(10,250,10,10);
//
//                        swipeRefreshLayout.setLayoutParams(params);

                        webView.setVisibility(View.VISIBLE);
                        webView.startAnimation(webAnim);
                        webView.loadUrl(URL+getCari);
                        etCari.getText().clear();
                    }
                }, 700);

            }else if (anim == 1) {
                anim--;
                btnAnim = AnimationUtils.loadAnimation(getContext(),R.anim.tonormalpositition);
                textAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fadein);
                webAnim = AnimationUtils.loadAnimation(getContext(),R.anim.fadeout);
                //run animation
                etCari.startAnimation(textAnim);
                textView.startAnimation(textAnim);
                button.startAnimation(btnAnim);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 0.7s = 700ms

                        button.setY(645);
                        button.setText("Cari");
                        webView.startAnimation(webAnim);
                        webView.setVisibility(View.GONE);
                    }
                }, 700);
            }
            }
        });

        return view;
    }

    private void webViewGoBack(){
        webView.goBack();
    }
    public void onRefresh() {
        webView.reload();
    }
}
