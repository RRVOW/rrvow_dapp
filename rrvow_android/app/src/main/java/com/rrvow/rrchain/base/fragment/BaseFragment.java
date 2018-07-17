package com.rrvow.rrchain.base.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.presenter.BasePresenter;
import com.rrvow.rrchain.base.view.BaseActionBar;
import com.rrvow.rrchain.base.view.ErrorHintView;
import com.rrvow.rrchain.base.view.IBaseView;
import com.rrvow.rrchain.base.view.ProgressHUD;
import com.rrvow.rrchain.common.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<V extends IBaseView, T extends BasePresenter<V>> extends
        DialogFragment implements IBaseView {

    private BaseActionBar mBaseActionBar;
    private ErrorHintView mErrorHintView;
    protected ProgressHUD mProgressHUD;
    private Handler mHandler = new Handler();

    protected T mPresenter;

    protected View rootView;
    private Unbinder unbinder;

    protected String mPageTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = createPresenter();

        if (mPresenter != null) {
            //noinspection unchecked
            mPresenter.attachView((V) this);
        }

        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getBaseActionBar() != null && getBaseActionBar().getTvTitle() != null) {
            mPageTitle = getBaseActionBar().getTvTitle().getText().toString();
        }

        if (TextUtils.isEmpty(mPageTitle)) {
            mPageTitle = this.getClass().getSimpleName();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }

        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getLayoutId() != 0) {
            rootView = inflater.inflate(getLayoutId(), null, false);
            unbinder = ButterKnife.bind(this, rootView);
        }
        return rootView == null ? super.onCreateView(inflater, container, savedInstanceState) :
                rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBaseActionBar = new BaseActionBar(this);
        mErrorHintView = new ErrorHintView(getContext(), mHandler, R.mipmap.ic_default_error,
                getString(R.string.default_hint_error));
        mProgressHUD = new ProgressHUD(getContext());
        initView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    protected int getLayoutId() {
        return 0;
    }

    protected T createPresenter() {
        return null;
    }

    protected void initView() {
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
        if (getView() != null) {
            mErrorHintView.showErrorHintView(getView(), errorCode);
        }
    }

    @Override
    public void showErrorHintView(int resId, String msg) {
        if (getView() != null) {
            mErrorHintView.showErrorHintView(getView(), resId, msg);
        }
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {

    }
}
