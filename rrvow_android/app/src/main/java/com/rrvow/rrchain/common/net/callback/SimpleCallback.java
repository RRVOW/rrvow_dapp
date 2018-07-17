package com.rrvow.rrchain.common.net.callback;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.rrvow.rrchain.base.view.ErrorHintView;
import com.rrvow.rrchain.base.view.IBaseView;
import com.rrvow.rrchain.base.view.ProgressHUD;
import com.rrvow.rrchain.common.widget.BaseToast;

/**
 * Author rrvow_android.
 * Date 2017/5/11.
 */

public abstract class SimpleCallback implements Callback {
    private IBaseView iBaseView;
    private ProgressHUD progressHUD;
    private ErrorHintView errorHintView;

    private boolean autoShowErrorHint;
    private boolean autoShowProgressHUD;

    public SimpleCallback setAutoShowErrorHint(boolean autoShowErrorHint) {
        this.autoShowErrorHint = autoShowErrorHint;
        return this;
    }

    public SimpleCallback setAutoShowProgressHUD(boolean autoShowProgressHUD) {
        this.autoShowProgressHUD = autoShowProgressHUD;
        return this;
    }

    public SimpleCallback(IBaseView iBaseView) {
        super();
        if (iBaseView == null) {
            return;
        }

        this.iBaseView = iBaseView;
        this.progressHUD = iBaseView.getProgressHUD();
        this.errorHintView = iBaseView.getErrorHintView();
        this.autoShowErrorHint = false;
        this.autoShowProgressHUD = false;
    }

    @Override
    public void onStart() {
        showProgressHUD();

        if (iBaseView != null) {
            onStartEnsureView();
        }
    }

    protected void onStartEnsureView() {

    }

    @Override
    public void onSuccess(JSONObject response) {
        hideErrorHintView();

        if (iBaseView != null) {
            onSuccessEnsureView(response);
        }
    }

    protected void onSuccessEnsureView(JSONObject response) {

    }

    @Override
    public void onFail(int errorCode, String errorMessage) {
        showErrorHintView(errorCode);

        if (!TextUtils.isEmpty(errorMessage)) {
            BaseToast.showShort(errorMessage);
        }

        if (iBaseView != null) {
            onFailEnsureView(errorCode, errorMessage);
        }
    }

    protected void onFailEnsureView(int errorCode, String errorMessage) {

    }

    @Override
    public void onStop() {

        dismissProgressHUD();

        if (iBaseView != null) {
            onStopEnsureView();
        }
    }

    public void onStopEnsureView() {

    }

    private void showProgressHUD() {
        if (autoShowProgressHUD && progressHUD != null && !progressHUD.isShowing()) {
            progressHUD.showLoadingProgressHUD();
        }
    }

    private void dismissProgressHUD() {
        if (autoShowProgressHUD && progressHUD != null && progressHUD.isShowing()) {
            progressHUD.dismiss();
        }
    }

    private void showErrorHintView(int errorCode) {
        if (autoShowErrorHint && errorHintView != null) {
            iBaseView.showErrorHintView(errorCode);
        }
    }

    private void hideErrorHintView() {
        if (autoShowErrorHint && errorHintView != null) {
            errorHintView.hiddenErrorHintView();
        }
    }
}