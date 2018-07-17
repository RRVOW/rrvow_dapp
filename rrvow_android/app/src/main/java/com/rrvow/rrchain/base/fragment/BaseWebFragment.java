package com.rrvow.rrchain.base.fragment;

import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.view.BaseWebView;
import com.rrvow.rrchain.base.view.JSCallHandler;

/**
 * @author lpc
 */
public class BaseWebFragment extends BaseFragment {

    protected BaseWebView mWebView;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_web;
    }


    ////////////// Customize Method ///////////////
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        super.initView();
        mWebView = rootView.findViewById(R.id.webView);
        mWebView.addJavascriptInterface(new JSCallHandler(
                        getActivity(), mWebView, getBaseActionBar().getTvTitle()),
                JSCallHandler.CONTAINER_NAME);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.initWebViewClient(getActivity());
//        mWebView.loadUrl(URLConstant.getIndexUrl());
        mWebView.loadUrl("http://www.baidu.com");
    }

    public boolean onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }
}
