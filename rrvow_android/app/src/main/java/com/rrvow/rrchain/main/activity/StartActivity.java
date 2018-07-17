package com.rrvow.rrchain.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.BaseActivity;
import com.rrvow.rrchain.common.utils.DeepLinkUtil;
import com.rrvow.rrchain.common.widget.CommonStatusBar;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.rrvow.rrchain.main.service.CheckUpdateService;


/**
 * @author by lpc on 2018/1/12.
 */
public class StartActivity extends BaseActivity {

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case 0:
                case DeepLinkUtil.HANDLER_CHECKYYB_FAILED:
//                    forward();
                    checkUpdate();
                    if (AccountManager.getInstance().isFirstStart()) {
                        startLogin();
                    } else {
                        startMain();
                    }
                    break;
                case DeepLinkUtil.HANDLER_CHECKYYB_SUCCESS:
                    finish();
                    break;
            }

            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DeepLinkUtil.initLinkModel(this);
        DeepLinkUtil.registerLinkModel(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();
        setSwipeBackEnable(false);
        DeepLinkUtil.routerLinkModel(this, getIntent(), mHandler);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void setStatusBar() {
        CommonStatusBar.setStatusBarByTransparent(this, false);
    }

    @Override
    protected String getGATraceTitle() {
        return "开始页";
    }

    private void checkUpdate() {
        Intent intent = new Intent(StartActivity.this, CheckUpdateService.class);
        intent.setAction(CheckUpdateService.ACTION_CHECK_UPDATE);
        startService(intent);
    }

    private void forward() {
        String token = AccountManager.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
            startLogin();
        } else {
            startMain();
        }
    }

    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
