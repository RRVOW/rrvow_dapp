package com.rrvow.rrchain.base.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.presenter.BasePresenter;
import com.rrvow.rrchain.base.view.BaseActionBar;
import com.rrvow.rrchain.base.view.ErrorHintView;
import com.rrvow.rrchain.base.view.IBaseView;
import com.rrvow.rrchain.base.view.ProgressHUD;
import com.rrvow.rrchain.common.event.AppExitEvent;
import com.rrvow.rrchain.common.event.BaseEvent;
import com.rrvow.rrchain.common.manager.DWAnalyticsManager;
import com.rrvow.rrchain.common.widget.CommonStatusBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * @author lpc 16/6/30.
 */
@SuppressLint("Registered")
public abstract class BaseActivity<V extends IBaseView, T extends BasePresenter<V>> extends
        SwipeBackActivity implements IBaseView {

    private BaseActionBar mBaseActionBar;
    protected ProgressHUD mProgressHUD;
    private ErrorHintView mErrorHintView;
    private Handler mHandler = new Handler();

    private Unbinder unbinder;
    protected T mPresenter;

    protected String mPageTitle;
    protected boolean isWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (getLayoutId() != 0) {
            setContentView(getLayoutId());
        }
        getSwipeBackLayout().setEnableGesture(true);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this);

        if (mPresenter != null) {
            //noinspection unchecked
            mPresenter.attachView((V) this);
        }

        getParameters();
        initData();
        initView();


        // fabric
        DWAnalyticsManager.getInstance(getApplicationContext()).crashlytics(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        trace();
    }

    protected final void trace() {
        String traceTitle = getGATraceTitle();
        if (TextUtils.isEmpty(traceTitle)) {
            traceTitle = this.getClass().getSimpleName();
        }
        DWAnalyticsManager.getInstance(this).traceWithPageTitle(traceTitle);
    }

    protected int getLayoutId() {
        return 0;
    }

    protected T createPresenter() {
        return null;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);

        if (unbinder != null) {
            unbinder.unbind();
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }

        super.onDestroy();
    }

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        mBaseActionBar = new BaseActionBar(this);
        mErrorHintView = new ErrorHintView(this, mHandler, R.mipmap.ic_default_error, getString(R
                .string.default_hint_error));
        mProgressHUD = new ProgressHUD(this);

        if (!isWeb) {
            setStatusBar();
        }
    }

    protected void setStatusBar() {
        CommonStatusBar.setStatusBarByColorActionBar(this, Color.WHITE);
    }

    /**
     * google分析 页面跟踪标题自定义
     */
    protected abstract String getGATraceTitle();

    /**
     * 传递的参数处理
     */
    protected void getParameters() {
    }

    @Override
    public BaseActionBar getBaseActionBar() {
        return mBaseActionBar;
    }

    @Override
    public ProgressHUD getProgressHUD() {
        return mProgressHUD;
    }

    @Override
    public ErrorHintView getErrorHintView() {
        return mErrorHintView;
    }

    @Override
    public void showErrorHintView(int errorCode) {
        mErrorHintView.showErrorHintView(errorCode);
    }

    @Override
    public void showErrorHintView(int resId, String msg) {
        mErrorHintView.showErrorHintView(resId, msg);
    }

    @Override
    public Handler getHandler() {
        return this.mHandler;
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof AppExitEvent) {
            finish();
        }
    }
}
