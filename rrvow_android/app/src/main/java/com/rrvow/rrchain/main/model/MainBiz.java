package com.rrvow.rrchain.main.model;

import com.alibaba.fastjson.JSON;
import com.rrvow.rrchain.base.model.BaseBiz;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.net.callback.Callback;

import java.util.List;
import java.util.Map;

public class MainBiz extends BaseBiz {

    public void login(String account, String smsCode, String inviteCode, Callback callback) {
        Map<String, String> params = getCommonParams();
        params.put("phone_num", account);
        params.put("message", smsCode);
        params.put("code", inviteCode);
        mHttpClient.sendPostRequest(URLConstant.LOGIN, params, callback);
    }

    public void getSmsCode(String account, Callback callback) {
        Map<String, String> params = getCommonParams();
        params.put("phone_num", account);
        mHttpClient.sendPostRequest(URLConstant.GET_SMS_CODE, params, callback);
    }

    public void getAppVersion(Callback callback) {
        Map<String, String> params = getCommonParams();
        params.put("platform_type", "1");
        mHttpClient.sendPostRequest(URLConstant.GET_APP_VERSION, params, callback);
    }

    public void getCrystalData(Callback callback) {
        mHttpClient.sendPostRequest(URLConstant.MAIN_INDEX, getCommonParams(), callback);
    }

    public void pickCrystal(List<String> ids, Callback callback) {
        Map<String, String> params = getCommonParams();
        params.put("crystals_seed_id", JSON.toJSONString(ids));
        mHttpClient.sendPostRequest(URLConstant.PICK_CRYSTAL, params, callback, true);
    }

    public void getAmountSum(Callback callback) {
        mHttpClient.sendPostRequest(URLConstant.AMOUNT_SUM, getCommonParams(), callback);
    }

    public void getIndexBannerData(Callback callback) {
        mHttpClient.sendPostRequest(URLConstant.INDEX_BANNER, getCommonParams(), callback);
    }
}
