package com.rrvow.rrchain.base.model;

import com.rrvow.rrchain.common.net.HttpClient;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.main.App;
import com.rrvow.rrchain.main.manager.AccountManager;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;

// 这是我的提交测试
/**
 * @author by lpc on 2017/8/28.
 */
public class BaseBiz {

    protected HttpClient mHttpClient;

    public BaseBiz() {
        mHttpClient = new HttpClient();
    }

    protected Map<String, String> getCommonParams() {
        Map<String, String> commonParams = new HashMap<>();
        commonParams.put("token", AccountManager.getInstance().getToken());
        commonParams.put("device_id", AppTools.getDeviceId());
        commonParams.put("device_type", "2");
        commonParams.put("version", AppTools.getVersionName(App.getContext()));
        return commonParams;
    }

    protected FormBody.Builder getCommonRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("token", AccountManager.getInstance().getToken());
        builder.add("device_id", AppTools.getDeviceId());
        builder.add("device_type", "2");
        builder.add("version", AppTools.getVersionName(App.getContext()));
        return builder;
    }
}
