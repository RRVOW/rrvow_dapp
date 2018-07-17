package com.rrvow.rrchain.main.view;

import com.rrvow.rrchain.base.view.IBaseView;

public interface LoginView extends IBaseView {

    public void onLoginSuccess();

    public void onGetCodeSuccess(boolean needInviteCode);

    void onReqStop();
}
