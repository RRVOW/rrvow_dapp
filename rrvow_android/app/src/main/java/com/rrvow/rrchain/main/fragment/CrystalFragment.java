package com.rrvow.rrchain.main.fragment;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.CommonWebActivity;
import com.rrvow.rrchain.base.activity.CommonWebShareActivity;
import com.rrvow.rrchain.base.fragment.BaseFragment;
import com.rrvow.rrchain.base.view.BaseWebView;
import com.rrvow.rrchain.base.view.JSCallHandler;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.event.BaseEvent;
import com.rrvow.rrchain.common.event.LoginEvent;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.widget.CommonDialogsInBase;
import com.rrvow.rrchain.main.activity.LoginActivity;
import com.rrvow.rrchain.main.activity.SettingsActivity;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lpc
 */
public class CrystalFragment extends BaseFragment {

    @BindView(R.id.webview)
    BaseWebView mWebView;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.top_bar)
    LinearLayout mTopBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_crystal;
    }

    @OnClick(R.id.right_icon)
    void toSettings() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        if (AccountManager.getInstance().isLogin()) {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        } else {
            displayLoginDialog();
        }
    }

    @Override
    protected void initView() {
        super.initView();
        mWebView.addJavascriptInterface(new JSCallHandler(
                        getActivity(), mWebView, getBaseActionBar().getTvTitle()),
                JSCallHandler.CONTAINER_NAME);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return where2forward(url);
            }

            @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (error.getErrorCode() == ERROR_CONNECT || error.getErrorCode() ==
                        ERROR_TIMEOUT || error.getErrorCode() == ERROR_HOST_LOOKUP || error
                        .getErrorCode() == ERROR_PROXY_AUTHENTICATION || error.getErrorCode() ==
                        ERROR_FILE_NOT_FOUND) {
                    view.loadUrl("file:///android_asset/html/error.html");
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String
                    failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                if (errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT || errorCode ==
                        ERROR_HOST_LOOKUP || errorCode == ERROR_PROXY_AUTHENTICATION || errorCode
                        == ERROR_FILE_NOT_FOUND) {
                    view.loadUrl("file:///android_asset/html/error.html");
                }
            }
        });

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.loadUrl(URLConstant.MY_CRYSTAL_PAGE
                + AccountManager.getInstance().getToken());

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                mWebView.loadUrl(URLConstant.MY_CRYSTAL_PAGE
                        + AccountManager.getInstance().getToken());
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (CrystalFragment.this.refreshLayout != null) {
                            CrystalFragment.this.refreshLayout.finishRefresh();
                        }
                    }
                }, 1000);
            }
        });

//        refreshLayout.useCustomerStyle(R.color.index_refrsh_color);
        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
            @Override
            public boolean canLoadmore(View content) {
                return false;
            }
        });
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnMultiPurposeListener(new OnMultiPurposeListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
            }

            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int
                    headerHeight, int extendHeight) {
                if (mTopBar != null) {
                    mTopBar.setAlpha(1 - percent);
                }
            }

            @Override
            public void onHeaderReleased(RefreshHeader header, int headerHeight, int
                    maxDragHeight) {
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int
                    headerHeight, int extendHeight) {
                if (mTopBar != null) {
                    mTopBar.setAlpha(1 - percent);
                }
            }

            @Override
            public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int
                    maxDragHeight) {
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
            }

            @Override
            public void onFooterPulling(RefreshFooter footer, float percent, int offset, int
                    footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterReleased(RefreshFooter footer, int footerHeight, int
                    maxDragHeight) {
            }

            @Override
            public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int
                    footerHeight, int extendHeight) {

            }

            @Override
            public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int
                    maxDragHeight) {
            }

            @Override
            public void onFooterFinish(RefreshFooter footer, boolean success) {
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            }

            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull
                    RefreshState oldState, @NonNull RefreshState newState) {
            }
        });
    }

    public void displayLoginDialog() {
        CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
        commonDialogsInBase.displayTwoBtnDialog(getActivity(), null, "取消", "立即登录", "当前未登录账号，请登录",
                null, new OnBtnClickL() {
                    @Override
                    public void onBtnClick(DialogInterface dialog) {
                        startLoginActivity();
                        dialog.dismiss();
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (event instanceof LoginEvent) {
            if (mWebView != null) {
                mWebView.loadUrl(URLConstant.MY_CRYSTAL_PAGE
                        + AccountManager.getInstance().getToken());
            }
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public boolean where2forward(String url) {
        boolean result = false;

        if (!AccountManager.getInstance().isLogin()) {
            displayLoginDialog();
            return true;
        }

        if (AppTools.isFastDoubleClick()) {
            return true;
        }

        if (isSharePage(url)) {
            Intent intent = new Intent(getActivity(), CommonWebShareActivity.class);
            intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.INVITE_PAGE + AccountManager
                    .getInstance().getToken());
            if (getActivity() != null) {
                startActivity(intent);
            }
            return true;
        }

        if (isForward2CrystalDetail(url)) {
            Intent intent = new Intent(getActivity(), CommonWebActivity.class);
            intent.putExtra(CommonWebActivity.PARAM_URL, url);
            intent.putExtra(CommonWebActivity.PARAM_TITLE, "");
            if (getActivity() != null) {
                startActivity(intent);
            }
            result = true;
        }

        return result;
    }

    /**
     * 水晶总数
     */
    private boolean isForward2CrystalDetail(String url) {
        return !url.contains("myCrystal.html");
    }

    private boolean isSharePage(String url) {
        return url.contains("invitCode.html");
    }
}
