package com.rrvow.rrchain.main.manager;

import android.content.Context;
import android.content.Intent;

import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.CacheUtil;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.main.activity.LoginActivity;

/**
 * @author by lpc on 2017/8/31.
 */
public class AccountManager {

    private static AccountManager mInstance;

    public static AccountManager getInstance() {
        if (mInstance == null) {
            mInstance = new AccountManager();
        }
        return mInstance;
    }

    private AccountManager() {
    }

    public void logout(Context ctx) {
        if (AppTools.isInLoginPage(ctx)) {
            return;
        }

        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(ctx, LoginActivity.class);
        ctx.startActivity(intent);

        //清楚登录状态
        clearToken();
    }

    public String getToken() {
        return CacheUtil.getCache("token", "");
    }

    public void saveToken(String token) {
        CacheUtil.putCache("token", token);
    }

    public void clearToken() {
        saveToken("");
    }

    public boolean isLogin() {
        return StringUtil.isNotEmpty(getToken());
    }

    public void saveAccount(String account) {
        CacheUtil.putCache("account", account);
    }

    public String getAccount() {
        return CacheUtil.getCache("account", "");
    }

    public void savePhone(String phone) {
        CacheUtil.putCache("phone", phone);
    }

    public String getPhone() {
        return CacheUtil.getCache("phone", "");
    }

    public boolean isFirstStart() {
        return CacheUtil.getCache("isFirst", true);
    }

    public void clearFirstStart() {
        CacheUtil.putCache("isFirst", false);
    }

}
