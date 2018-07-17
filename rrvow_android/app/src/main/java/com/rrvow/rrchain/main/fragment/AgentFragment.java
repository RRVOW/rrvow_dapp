package com.rrvow.rrchain.main.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.CommonWebActivity;
import com.rrvow.rrchain.base.fragment.BaseWebFragment;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.main.activity.ApplyAgentActivity;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;

/**
 * @author lpc
 */
public class AgentFragment extends BaseWebFragment {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Override
    protected void initView() {
        super.initView();
        getBaseActionBar().customNavigationBarWithTitle("我的代理");
        mWebView.loadUrl(URLConstant.MY_AGENT_PAGE
                + AccountManager.getInstance().getToken());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return where2forward(url);
            }
        });

        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mWebView.loadUrl(URLConstant.MY_AGENT_PAGE
                        + AccountManager.getInstance().getToken());
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AgentFragment.this.refreshLayout.finishRefresh();
                    }
                }, 1000);
            }
        });

        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
            @Override
            public boolean canLoadmore(View content) {
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean where2forward(String url) {
        boolean result = false;
        if (AppTools.isFastDoubleClick()) {
            return true;
        }

        if (url.contains("applyAgent.html")) {
            Intent intent = new Intent(getActivity(), ApplyAgentActivity.class);
            intent.putExtra(CommonWebActivity.PARAM_URL, url);
            startActivity(intent);
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
        return !url.contains("myAgent.html");
    }
}
