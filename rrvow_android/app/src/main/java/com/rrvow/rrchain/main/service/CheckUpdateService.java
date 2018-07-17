package com.rrvow.rrchain.main.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.rrvow.rrchain.common.constant.ErrorCodeConstant;
import com.rrvow.rrchain.common.net.callback.Callback;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.main.App;
import com.rrvow.rrchain.main.activity.CheckUpdateActivity;
import com.rrvow.rrchain.main.model.MainBiz;

/**
 * @author by lpc on 2017/4/12.
 */
public class CheckUpdateService extends IntentService {

    public static final String ACTION_CHECK_UPDATE = "action_check_update";
    private boolean flag;

    public CheckUpdateService() {
        super("");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (null != intent) {
            if (ACTION_CHECK_UPDATE.equals(intent.getAction())) {
                flag = intent.getBooleanExtra("flag", false);
                checkUpdate();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    /**
     * 检查更新
     */
    private void checkUpdate() {
        MainBiz mainBiz = new MainBiz();
        mainBiz.getAppVersion(new Callback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(JSONObject response) {
                handleResult(response);
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
            }

            @Override
            public void onStop() {
            }
        });
    }

    private void handleResult(JSONObject response) {
        String code = "";
        code = response.getString("code");
        if (ErrorCodeConstant.SUCCESS.equals(code)) {
            JSONObject data = response.getJSONObject("data");
            String version_num = data.getString("versionCode");
            String version_name = data.getString("versionName");
            String version_desc = data.getString("releaseNote");
            String download = data.getString("downloadURL");
            String isForceUpdate = data.getString("isForcedUpdate");
            if (Integer.parseInt(version_num) > AppTools.getVersionCode(App.getContext())) {
                displayUpdateTips(download, version_desc, isForceUpdate);
            } else {
                if (flag) {
                    BaseToast.showShort("已经是最新版本");
                }
            }
        }
    }

    private void displayUpdateTips(String download_url, String version_desc, String isForceUpdate) {
        Intent intent = new Intent(CheckUpdateService.this, CheckUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("download_url", download_url);
        intent.putExtra("version_desc", version_desc);
        intent.putExtra("isForceUpdate", isForceUpdate);
        startActivity(intent);
    }
}
