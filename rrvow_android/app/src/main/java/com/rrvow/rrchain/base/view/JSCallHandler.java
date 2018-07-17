package com.rrvow.rrchain.base.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.TextView;

import com.rrvow.rrchain.common.event.JumpMainEvent;
import com.rrvow.rrchain.common.event.ShareInfoEvent;
import com.rrvow.rrchain.common.manager.DWAnalyticsManager;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.LogUtils;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.main.App;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 2016/11/11.
 */
public class JSCallHandler {
    public static final String CONTAINER_NAME = "WebviewContainer";

    private Context mContext;
    private WebView mWebView;
    private TextView tv_title;

    public JSCallHandler(Context mContext, WebView webView, TextView tv_title) {
        this.mContext = mContext;
        this.mWebView = webView;
        this.tv_title = tv_title;
    }

    /**
     * 解析encode的url为map格式，第一个参数固定为调用方法名，后面为携带参数
     *
     * @param protocolStr
     * @return
     */
    public static Map<String, String> decodeUrl(String protocolStr) {
        Log.i("temp", protocolStr);

        if (TextUtils.isEmpty(protocolStr)) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        if (protocolStr.contains("?")) {
            String url1 = protocolStr.substring(0, protocolStr.indexOf("?"));
            String url2 = protocolStr.substring(protocolStr.indexOf("?") + 1, protocolStr.length());
            if (!TextUtils.isEmpty(url1)) {
                map.put("functionName", URLDecoder.decode(url1));
            }

            if (!TextUtils.isEmpty(url2)) {
                String[] entrys = url2.split("&");
                if (null != entrys && 0 != entrys.length) {
                    for (String entry : entrys) {
                        String key = entry.substring(0, entry.indexOf("="));
                        String value = entry.substring(entry.indexOf("=") + 1, entry.length());
                        map.put(key, URLDecoder.decode(value));
                    }
                }
            }
        }

        return map;
    }

    @JavascriptInterface
    public void callAppFunc(String protocolStr) {
        LogUtils.d("webview protocol[" + protocolStr + "]");
        if (AppTools.isFastDoubleClick()) {
            return;
        }

        // 根据规范解析协议方法
        Map<String, String> map = decodeUrl(protocolStr);
        String functionName = "";
        String callbackFunctionStr = "";
        if (null != map && 0 != map.size()) {
            functionName = map.get("functionName");
            callbackFunctionStr = map.get("callbackFunction");
        }

        // 合法分发到不同的业务方法
        if (!TextUtils.isEmpty(functionName)) {
            DWAnalyticsManager.getInstance(App.getContext()).traceWithCategoryAndAction("交互协议",
                    functionName);

            if ("rrvowpay_openDetailUrl".equals(functionName)) {
                openDetailUrl(map);
            } else if ("rrvowpay_closePage".equals(functionName)) {
                closePage();
            } else if ("rrvowpay_openUrlByOsBrowser".equals(functionName)) {
                openUrlByOsBrowser(map);
            } else if ("rrvowpay_getAppVersion".equals(functionName)) {
                getAppVersion(callbackFunctionStr);
            } else if ("rrvowpay_getMobileDeviceInfo".equals(functionName)) {
                getMobileDeviceInfo(callbackFunctionStr, map);
            } else if ("rrvowpay_chgPageTitle".equals(functionName)) {
                chgPageTitle(callbackFunctionStr, map);
            } else if ("rrvowpay_share_openMenu".equals(functionName)) {
                share_openMenu(callbackFunctionStr, map);
            } else if ("rrvowpay_popAppHint".equals(functionName)) {
                popAppHint(map);
            } else if ("rrvowpay_openPage".equals(functionName)) {
                openPage(map);
            } else if ("rrvowpay_getUserId".equals(functionName)) {
                getUserId(callbackFunctionStr);
            } else if ("rrvowpay_getRole".equals(functionName)) {
                getRole(callbackFunctionStr);
            } else if ("rrvowpay_pay".equals(functionName)) {
                sendPayRequest(callbackFunctionStr, map);
            } else if ("rrvowpay_shareData".equals(functionName)) {
                onShareData(map);
            }
        }
    }

    /**
     * 触发获取当前页面分享信息的JS协议回调
     */
    public void triggerGetShareDataFromH5() {
        final String callBackStr = "javascript:getShareInfo()";
        loadCallBackUrl(callBackStr);
    }


    /////////////// Customize Method /////////////////
    private void onShareData(Map<String, String> map) {
        String iconURL = map.get("iconURL");
        String detailURL = map.get("detailURL");
        String title = map.get("title");
        String des = map.get("description");

        try {
            iconURL = URLDecoder.decode(iconURL, "UTF-8");
            detailURL = URLDecoder.decode(detailURL, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ShareInfoEvent shareInfo = new ShareInfoEvent();
        shareInfo.setTitle(title);
        shareInfo.setDescription(des);
        shareInfo.setIconURL(iconURL);
        shareInfo.setDetailURL(detailURL);
        EventBus.getDefault().post(shareInfo);
    }

    private void getRole(String callbackFunctionStr) {
        int role = getRoleIndex();
        String retError = "1000";
        String errorDesc = "操作成功";
        if (role == -1) {
            retError = "1001";
            errorDesc = "role为空";
        }
        loadCallBackUrl("javascript:" + callbackFunctionStr + "('" + retError + "','" + errorDesc
                + "','" + role + "')");
    }

    private int getRoleIndex() {
        return -1;
    }

    /**
     * 打开任意原生界面
     *
     * @param map
     */
    private void openPage(Map<String, String> map) {
        String tabIndex = map.get("tabIndex");
        String pageName = map.get("pageName");
        Intent intent;
        if ("home".equals(pageName)) {
            EventBus.getDefault().post(new JumpMainEvent(StringUtil.parseInt(tabIndex)));
        } else if ("login".equals(pageName)) {
//            AccountManager.getInstance().logout(App.getContext());
        } else if (!TextUtils.isEmpty(pageName)) {
            try {
                Class<?> forName = Class.forName(pageName);
                intent = new Intent(mContext, forName);
                mContext.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用系统浏览器打开一个url
     *
     * @param map
     */
    private void openUrlByOsBrowser(Map<String, String> map) {
        String url = map.get("encodeUrl");
        String load_url = "";
        if (!TextUtils.isEmpty(url)) {
            try {
                load_url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(load_url);
        intent.setData(content_url);
        mContext.startActivity(intent);
    }

    /**
     * 弹出app原生提示
     *
     * @param map
     */
    private void popAppHint(Map<String, String> map) {
        String msg = map.get("msg");
        if (!TextUtils.isEmpty(msg)) {
            BaseToast.showShort(mContext, msg);
        }
    }

    /**
     * 分享
     *
     * @param callbackFunctionStr
     * @param map
     */
    private void share_openMenu(String callbackFunctionStr,
                                Map<String, String> map) {
//        String iconURL = URLDecoder.decode(map.get("iconURL"));
//        String detailURL = URLDecoder.decode(map.get("detailURL"));
//        String title = map.get("title");
//        String description = map.get("description");
//
//        String retError = "1000";
//        String errorDesc = "操作成功";
//        ShareParams sp = new ShareParams();
//        sp.setShareType(Platform.SHARE_WEBPAGE);
//        if (!TextUtils.isEmpty(title)) {
//            sp.setTitle(title);
//        } else {
//            sp.setTitle("来自返享app");
//            retError = "1004";
//            errorDesc = "title为空";
//        }
//        if (!TextUtils.isEmpty(description)) {
//            sp.setText(description);
//        } else {
//            sp.setText("来自返享app");
//            retError = "1005";
//            errorDesc = "description为空";
//        }
//        if (!TextUtils.isEmpty(detailURL)) {
//            sp.setUrl(detailURL);
//        } else {
//            sp.setUrl("http://www.rrvow.com");
//            retError = "1006";
//            errorDesc = "detailURL为空";
//        }
//        if (!TextUtils.isEmpty(iconURL)) {
//            sp.setImageUrl(iconURL);
//        } else {
//            sp.setImageData(BitmapFactory.decodeResource(mContext.getResources(),
//                    R.drawable.logo));
//            retError = "1007";
//            errorDesc = "iconURL为空";
//        }
//        //朋友圈分享出去
//        Platform platform = ShareSDK.getPlatform(mContext, WechatMoments.NAME);
//        platform.share(sp);
//        mWebView.loadUrl("javascript:"+ callbackFunctionStr + "(" + retError + "," + errorDesc
// + ")");
    }

    /**
     * 修改webView的title
     *
     * @param callbackFunctionStr
     * @param map
     */
    private void chgPageTitle(String callbackFunctionStr,
                              Map<String, String> map) {
        final String title = map.get("title");
        if (tv_title != null && !TextUtils.isEmpty(title)) {
            tv_title.post(new Runnable() {
                @Override
                public void run() {
                    tv_title.setText(title);
                }
            });
        }
    }

    /**
     * 打开新的WebView原生页面加载URL
     *
     * @param map
     */
    private void openDetailUrl(Map<String, String> map) {
//        String url = map.get("encodeUrl");
//        String pageTitle = map.get("pageTitle");
//        String isFrom = map.get("isFrom");
//        String encodeUrl = URLDecoder.decode(url);
//        Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
//        intent.putExtra(CommonWebActivity.PARAM_URL, encodeUrl);
//        intent.putExtra(CommonWebActivity.PARAM_TITLE, pageTitle);
//        try {
//            intent.putExtra(CommonWebActivity.PARAM_IS_FROM, Integer.parseInt(isFrom));
//        } catch (Exception ignore) {
//        }
//        mContext.startActivity(intent);
    }

    /**
     * 获取user_id
     *
     * @param callbackFunctionStr
     */
    private void getUserId(String callbackFunctionStr) {
        final String callBackStr = "javascript:" + callbackFunctionStr + "()";
        loadCallBackUrl(callBackStr);
    }

    /**
     * 关闭当前页面
     */
    private void closePage() {
        ((Activity) mContext).finish();
    }

    /**
     * 获取app版本
     *
     * @return
     */
    private void getAppVersion(final String callbackFunctionStr) {
        String retError = "1000";
        String errorDesc = "操作成功";
        String versionName = null;
        PackageInfo packageInfo = AppTools.getPackageInfo();
        if (packageInfo != null) {
            versionName = packageInfo.versionName;
        }

        if (TextUtils.isEmpty(versionName)) {
            retError = "1003";
            errorDesc = "versionName为空";
        }
        final String callBackStr = "javascript:" + callbackFunctionStr + "('" + retError + "','"
                + errorDesc + "','" + versionName + "')";
        loadCallBackUrl(callBackStr);
    }

    private void loadCallBackUrl(final String url) {
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(url);
            }
        });
    }

    /**
     * 获取手机相关信息
     *
     * @param callbackFunctionStr
     */
    private void getMobileDeviceInfo(String callbackFunctionStr, Map<String, String> map) {
        String imei = "";
        String imsi = "";
        try {
            imei = AppTools.getIMEI(mContext);
            imsi = AppTools.getIMSI(mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String networkType = AppTools.getNetworkType(mContext);
        Map<String, Integer> display = AppTools.getDisplayResolution(mContext);
        String brand = AppTools.getPhoneBrand();
        String model = AppTools.getPhoneModel();
        String osVersion = AppTools.getOSVersion();
        PackageInfo packageInfo = AppTools.getPackageInfo();
        String appVersionName = null;
        if (packageInfo != null) {
            appVersionName = packageInfo.versionName;
        }

        //缺少操作系统名称
        JSONObject json = new JSONObject();
        try {
            json.put("IMEI", imei);
            json.put("NetworkType", networkType);
            json.put("IMSI", imsi);
            json.put("DisplayResolution", display.get("height") + "×" + display.get("width"));
            json.put("Brand", brand);
            json.put("Model", model);
            json.put("OsVersion", osVersion);
            json.put("AppVersion", appVersionName);
        } catch (Exception e) {
            LogUtils.e(e.getLocalizedMessage());
        }

        loadCallBackUrl("javascript:" + callbackFunctionStr + "('" + json.toString() + "')");
    }

    private void sendPayRequest(String callbackFunctionStr, Map<String, String> map) {
    }

}
