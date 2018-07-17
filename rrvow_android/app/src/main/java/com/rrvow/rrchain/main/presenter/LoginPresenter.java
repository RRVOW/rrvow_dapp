package com.rrvow.rrchain.main.presenter;

import com.alibaba.fastjson.JSONObject;
import com.rrvow.rrchain.base.presenter.BasePresenter;
import com.rrvow.rrchain.common.constant.ErrorCodeConstant;
import com.rrvow.rrchain.common.net.callback.Callback;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.rrvow.rrchain.main.model.MainBiz;
import com.rrvow.rrchain.main.view.LoginView;

public class LoginPresenter extends BasePresenter<LoginView> {

    private MainBiz mainBiz;

    public LoginPresenter() {
        mainBiz = new MainBiz();
    }

    public void login(final String account, String smsCode, String inviteCode) {
        mainBiz.login(account, smsCode, inviteCode, new Callback() {
            @Override
            public void onStart() {
//                if (getView() != null) {
//                    getView().getProgressHUD().showLoadingProgressHUD();
//                }
            }

            @Override
            public void onSuccess(JSONObject response) {
                String code = response.getString("code");
                if (ErrorCodeConstant.SUCCESS.equals(code)) {
                    JSONObject data = response.getJSONObject("data");
                    String token = data.getString("token");
                    AccountManager.getInstance().saveToken(token);
                    AccountManager.getInstance().saveAccount(account);
                    AccountManager.getInstance().savePhone(account);
                    getView().onLoginSuccess();
                } else {
                    BaseToast.showShort(response.getString("msg"));
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                BaseToast.showShort(errorMessage);
            }

            @Override
            public void onStop() {
                if (getView() != null) {
                    getView().onReqStop();
                }
            }
        });
    }

    public void getSmsCode(String account) {
        mainBiz.getSmsCode(account, new Callback() {
            @Override
            public void onStart() {
                if (getView() != null) {
                    getView().getProgressHUD().showLoadingProgressHUD();
                }
            }

            @Override
            public void onSuccess(JSONObject response) {
                String code = response.getString("code");
                if (ErrorCodeConstant.SUCCESS.equals(code)) {
                    JSONObject data = response.getJSONObject("data");
                    String status = data.getString("status");
                    if (getView() != null) {
                        getView().onGetCodeSuccess("1".equals(status));
                    }
                } else {
                    BaseToast.showShort(response.getString("msg"));
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                BaseToast.showShort(errorMessage);
            }

            @Override
            public void onStop() {
                if (getView() != null) {
                    getView().getProgressHUD().dismiss();
                }
            }
        });
    }

}
