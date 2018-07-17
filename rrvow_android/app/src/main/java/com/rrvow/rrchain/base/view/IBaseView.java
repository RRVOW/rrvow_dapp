package com.rrvow.rrchain.base.view;

import android.os.Handler;

/**
 * @author by lpc on 2017/8/28.
 */
public interface IBaseView {

    BaseActionBar getBaseActionBar();

    ProgressHUD getProgressHUD();

    ErrorHintView getErrorHintView();

    void showErrorHintView(int errorCode);

    void showErrorHintView(int resId, String msg);

    Handler getHandler();
}
