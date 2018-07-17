package com.rrvow.rrchain.common.net;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rrvow.rrchain.common.constant.ErrorCodeConstant;
import com.rrvow.rrchain.common.event.LogoutEvent;
import com.rrvow.rrchain.common.net.callback.Callback;
import com.rrvow.rrchain.common.utils.ExceptionUtil;
import com.rrvow.rrchain.common.utils.LogUtils;
import com.rrvow.rrchain.common.utils.NetworkManager;
import com.rrvow.rrchain.common.utils.PreferencesUtils;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.main.App;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.RequestBody;


/**
 * @author lpc on 2017/05/03.
 */
public class HttpClient {

    private final String TAG = "HttpClient";

    public void sendGetRequest(String url, final Callback callback) {
        sendGetRequest(url, null, callback, true, App.IS_DEBUG ? true : false);
    }

    public void sendGetRequest(String url, Map<String, String> params, Callback callback) {
        sendGetRequest(url, params, callback, false, App.IS_DEBUG ? true : false);
    }

    public void sendGetRequest(String url, Map<String, String> params, boolean isNeedLogin,
                               Callback callback) {
        sendGetRequest(url, params, callback, isNeedLogin, App.IS_DEBUG ? true : false);
    }

    private void sendGetRequest(String url, Map<String, String> params, Callback callback,
                                boolean isNeedLogin, boolean showToast) {
        if (isNeedLogin) {
            if (null == params) {
                params = new HashMap<>();
            }
            params.put(PreferencesUtils.DATA_TOKEN_INDEX, AccountManager.getInstance().getToken());
        }
        LogUtils.d(TAG, "sendGetRequest: Url=" + url + "\nparams: " + params);
        if (callback != null) {
            callback.onStart();
        }

        if (NetworkManager.isConnectingToInternet(App.getContext())) {
            OkHttpUtils.get().url(url).params(params).build().execute(getCommonCallBack(callback,
                    showToast));
        } else {
            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_NO_NET, "暂无网络");
                callback.onStop();
            }
        }
    }

    public void sendPostRequest(String url, Map<String, String> params, final Callback callback) {
        sendPostRequest(url, params, callback, true, true);
    }

    public void sendPostRequest(String url, Map<String, String> params, final Callback callback,
                                boolean isNeedLogin) {
        sendPostRequest(url, params, callback, isNeedLogin, true);
    }

    public void sendPostRequest(String url, Map<String, String> params, final Callback callback,
                                boolean isNeedLogin, boolean showToast) {
        LogUtils.d(TAG, "sendPostRequest: Url=" + url + "\nparams: " + params);

        if (isNeedLogin) {
            if (null == params) {
                params = new HashMap<>();
            }
            params.put(PreferencesUtils.DATA_TOKEN_INDEX, AccountManager.getInstance().getToken());
        }

        if (callback != null) {
            callback.onStart();
        }

        if (NetworkManager.isConnectingToInternet(App.getContext())) {
            OkHttpUtils.post().url(url).params(params).build().execute(getCommonCallBack
                    (callback, showToast));
        } else {
            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_NO_NET, "暂无网络");
                callback.onStop();
            }
        }
    }

    public void sendPostJsonRequest(String url, Map<String, String> params, final Callback
            callback) {
        sendPostJsonRequest(url, params, callback, true, true);
    }

    public void sendPostJsonRequest(String url, Map<String, String> params, final Callback callback,
                                    boolean isNeedLogin, boolean showToast) {
        LogUtils.d(TAG, "sendPostRequest: Url=" + url + "\nparams: " + JSON.toJSONString(params));

        if (isNeedLogin) {
            if (null == params) {
                params = new HashMap<>();
            }
            params.put(PreferencesUtils.DATA_TOKEN_INDEX, AccountManager.getInstance().getToken());
        }

        if (callback != null) {
            callback.onStart();
        }

        if (NetworkManager.isConnectingToInternet(App.getContext())) {
            OkHttpUtils.postString().url(url).content(JSON.toJSONString(params)).build().execute
                    (getCommonCallBack(callback, showToast));
        } else {
            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_NO_NET, "暂无网络");
                callback.onStop();
            }
        }
    }

    public void sendPostJsonRequest(String url, Map<String, Object> params, final Callback
            callback, boolean showToast) {
        LogUtils.d(TAG, "sendPostRequest: Url=" + url + "\nparams: " + JSON.toJSONString(params));


        if (callback != null) {
            callback.onStart();
        }

        if (NetworkManager.isConnectingToInternet(App.getContext())) {
            OkHttpUtils.postString().url(url).content(JSON.toJSONString(params)).build().execute
                    (getCommonCallBack(callback, showToast));
        } else {
            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_NO_NET, "暂无网络");
                callback.onStop();
            }
        }
    }

    public void sendPutRequest(String url, RequestBody body, final Callback callback) {
        sendPutRequest(url, body, callback, true);
    }

    public void sendPutRequest(String url, RequestBody body, Callback callback, boolean showToast) {
        LogUtils.d(TAG, "sendPutRequest: Url=" + url + "\nparams: " + body);
        if (callback != null) {
            callback.onStart();
        }

        if (NetworkManager.isConnectingToInternet(App.getContext())) {
            OkHttpUtils.put().url(url).requestBody(body).build().execute(getCommonCallBack
                    (callback, showToast));
        } else {
            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_NO_NET, "暂无网络");
                callback.onStop();
            }
        }
    }

    public void sendDeleteRequest(String url, final Callback callback) {
        sendDeleteRequest(url, null, callback, true);
    }

    public void sendDeleteRequest(String url, RequestBody body, Callback callback) {
        sendDeleteRequest(url, body, callback, true);
    }

    public void sendDeleteRequest(String url, RequestBody body, Callback callback, boolean
            showToast) {
        LogUtils.d(TAG, "sendPutRequest: Url=" + url + "\nparams: " + body);
        if (callback != null) {
            callback.onStart();
        }

        if (NetworkManager.isConnectingToInternet(App.getContext())) {
            OkHttpUtils.delete().url(url).requestBody(body).build().execute(getCommonCallBack
                    (callback, showToast));
        } else {
            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_NO_NET, "暂无网络");
                callback.onStop();
            }
        }
    }

    private StringCallback getCommonCallBack(final Callback callback, final boolean showToast) {
        return new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                try {
                    LogUtils.d(TAG, "onError: " + e);
                    ExceptionUtil.showException(e);

                    if (callback != null) {
                        callback.onFail(ErrorCodeConstant.ERR_CODE_HTTP_RESPONSE_ERROR,
                                ErrorCodeConstant
                                        .MAP_ERROR_CODE
                                        .get(ErrorCodeConstant.ERR_CODE_HTTP_RESPONSE_ERROR));
                    }

                } catch (Exception me) {
                    ExceptionUtil.showException(me);
                    if (App.IS_DEBUG) {
                        BaseToast.showShort(me.getLocalizedMessage());
                    }
                } finally {
                    if (callback != null) {
                        try {
                            callback.onStop();
                        } catch (Exception e1) {
                            ExceptionUtil.showException(e1);
                        }
                    }
                }
            }

            @Override
            public void onResponse(String response, int id) {
                try {
                    Log.i(TAG, Thread.currentThread().getName());
                    handleResponse(response, callback, showToast);
                } catch (Exception e) {
                    ExceptionUtil.showException(e);
                    if (App.IS_DEBUG) {
                        BaseToast.showShort(e.getLocalizedMessage());
                    }
                } finally {
                    if (callback != null) {
                        try {
                            callback.onStop();
                        } catch (Exception e) {
                            ExceptionUtil.showException(e);
                        }
                    }
                }
            }
        };
    }

    /**
     * 解析接口返回
     *
     * @param response response
     * @param callback callback
     */
    private void handleResponse(String response, Callback callback, boolean showToast) {
        LogUtils.d(TAG, "onResponse: " + response);

        if (TextUtils.isEmpty(response)) {
            if (App.IS_DEBUG) {
                BaseToast.showShort("无法解析返回数据");
            }

            if (callback != null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_DATA_ILLEGAL, ErrorCodeConstant
                        .MAP_ERROR_CODE.get(ErrorCodeConstant.ERR_CODE_DATA_ILLEGAL));
            }

            return;
        }

        int code = ErrorCodeConstant.ERR_CODE_DATA_ILLEGAL;
        String message = ErrorCodeConstant.MAP_ERROR_CODE.get(ErrorCodeConstant
                .ERR_CODE_DATA_ILLEGAL);
        try {
            JSONObject jsonObject = JSON.parseObject(response);
            if (jsonObject == null) {
                callback.onFail(ErrorCodeConstant.ERR_CODE_DATA_ILLEGAL, "数据异常");
                return;
            }
            code = jsonObject.getIntValue("code");
            message = jsonObject.getString("msg");

            if (code == ErrorCodeConstant.ERR_CODE_USER_TOKEN_INVALID) {
                AccountManager.getInstance().clearToken();
                EventBus.getDefault().post(new LogoutEvent(message));
            }

            if (callback != null) {
                callback.onSuccess(jsonObject);
                return;
            }
        } catch (Exception e) {
            ExceptionUtil.showException(e);
            if (App.IS_DEBUG) {
                BaseToast.showShort(e.getLocalizedMessage());
            }
        }

        if (showToast) {
            BaseToast.showShort(message);
        }

        if (callback != null) {
            callback.onFail(ErrorCodeConstant.ERR_CODE_DATA_ILLEGAL, message);
        }
    }
}