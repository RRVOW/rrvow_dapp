package com.rrvow.rrchain.base.view;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rrvow.rrchain.base.activity.CommonWebActivity;
import com.rrvow.rrchain.base.activity.CommonWebShareActivity;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.event.JumpMainEvent;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.LogUtils;
import com.rrvow.rrchain.main.manager.AccountManager;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Pattern;

/**
 */
public class BaseWebViewClient extends WebViewClient {
    private static final String TAG = "BaseWebViewClient";

    private Activity mActivity;

    private boolean isAllowForward;

    public BaseWebViewClient(Activity activity, boolean isAllowForward) {
        super();
        this.mActivity = activity;
        this.isAllowForward = isAllowForward;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        LogUtils.d(TAG, "shouldOverrideUrlLoading start ...");
        return isBlockedURL(url);
    }

    @Override
    public void onPageFinished(final WebView view, String url) {
        LogUtils.d(TAG, "onPageFinished is : " + url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        LogUtils.d(TAG, "onPageStarted is : " + url);
        super.onPageStarted(view, url, favicon);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        LogUtils.d(TAG, "onReceivedError is start ...");
        super.onReceivedError(view, request, error);

        if (error != null) {
            LogUtils.e(TAG, "onReceivedError is : " + error.toString());
        }

        if (error.getErrorCode() == ERROR_CONNECT || error.getErrorCode() == ERROR_TIMEOUT ||
                error.getErrorCode() == ERROR_HOST_LOOKUP || error.getErrorCode() ==
                ERROR_PROXY_AUTHENTICATION || error.getErrorCode() == ERROR_FILE_NOT_FOUND) {
            view.loadUrl("file:///android_asset/html/error.html");
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String
            failingUrl) {
        LogUtils.d(TAG, "onReceivedError is start : " + failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);

        LogUtils.e(TAG, String.format("onReceivedError errorCode[%d] description[%s]", errorCode,
                description));

        if (errorCode == ERROR_CONNECT || errorCode == ERROR_TIMEOUT || errorCode ==
                ERROR_HOST_LOOKUP || errorCode == ERROR_PROXY_AUTHENTICATION || errorCode ==
                ERROR_FILE_NOT_FOUND) {
            view.loadUrl("file:///android_asset/html/error.html");
        }
    }

    private boolean isBlockedURL(String url) {
        LogUtils.d(TAG, "isSupportedURL is : " + url);

        if (TextUtils.isEmpty(url)) {
            return false;
        }

        url = url.trim();

        // 打电话
        if (url.startsWith("tel")) {
            if (this.mActivity == null) {
                LogUtils.e(TAG, "未绑定activity，拨打电话失败。");
                return true;
            }

            if (AppTools.checkPermission(this.mActivity, Manifest.permission.CALL_PHONE, 0)) {
                AppTools.callPhone(this.mActivity, url, null);
            }

            return true;
        }

        // 发短信
        if (url.startsWith("sms")) {
            AppTools.sendSMS(this.mActivity, url);
            return true;
        }

        // 发送邮件
        if (url.startsWith("mail")) {
            if (this.mActivity == null) {
                LogUtils.e(TAG, "未绑定activity，发送邮件失败。");
                return true;
            }

            AppTools.sendMail(this.mActivity, url);

            return true;
        }

        if (isAllowForward) {
            return where2forward(url);
        }

        return false;
    }

    public boolean isAllowForward() {
        return isAllowForward;
    }

    public void setAllowForward(boolean allowForward) {
        isAllowForward = allowForward;
    }

    public boolean where2forward(String url) {
        boolean result = false;

        int index = url.indexOf("&redirect_uri");
        if (index > 0) {
            url = url.substring(0, index);
        }

//        if (isForward2GoodsDetail(url)) {
////            Intent intent = new Intent(mActivity, GoodsDetailsActivity.class);
////            intent.putExtra(GoodsDetailsActivity.PARAM_URL, url);
////            intent.putExtra(GoodsDetailsActivity.PARAM_TITLE, "");
////
////            mActivity.startActivity(intent);
////            result = true;
////        }

        if (isForward2MicroPage(url) ||
                isForward2Group(url) ||
                isForward2Spread(url) ||
                isForward2Partner(url) ||
                isForward2Team(url) ||
                isForward2CreditMall(url) ||
                isForward2CreditExchange(url) ||
                isForward2Pin(url) ||
                isForward2PinDetail(url) ||
                isForward2Cash(url) ||
                isForward2RedEnvelope(url) ||
                isForward2CrystalDetail(url)) {

            Intent intent = new Intent(mActivity, CommonWebActivity.class);
            intent.putExtra(CommonWebActivity.PARAM_URL, url);
            intent.putExtra(CommonWebActivity.PARAM_TITLE, "");

            // 支持分享的页面
            if (isForward2PinDetail(url) ||
                    isForward2RedEnvelope(url)) {
                intent.putExtra(CommonWebActivity.PARAM_SUPPORT_SHARE, true);
            }

            mActivity.startActivity(intent);
            result = true;
        }

        if (isForward2UserInfo(url)) {
            EventBus.getDefault().post(new JumpMainEvent(3));
            mActivity.finish();
            result = true;
        }

        if (isSharePage(url)) {
            Intent intent = new Intent(mActivity, CommonWebShareActivity.class);
            intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.INVITE_PAGE + AccountManager
                    .getInstance().getToken());

            mActivity.startActivity(intent);
            result = true;
        }

        if (isForward2Home(url)) {
            //EventBus.getDefault().post(new JumpMainEvent(0));
            //mActivity.finish();
            //result = true;
        }

//        if (isForward2OrderList(url)) {
//            Intent intent = new Intent(mActivity, OrderActivity.class);
//            mActivity.startActivity(intent);
//            result = true;
//        }
//
//        if (isForward2Guide(url)) {
//            Intent intent = new Intent(mActivity, TweeterCenterActivity.class);
//            mActivity.startActivity(intent);
//            result = true;
//        }

        LogUtils.d(TAG, "where2forward result:" + result);
        return result;
    }

    /**
     * 商品详情
     *
     * @param url
     * @return
     */
    private boolean isForward2GoodsDetail(String url) {
        String pattern = ".*/goods/[0-9]*.*";
        return Pattern.matches(pattern, url);
    }

    /**
     * 水晶总数
     */
    private boolean isForward2CrystalDetail(String url) {
        return url.contains("crystalNum.html");
    }

    /**
     * 个人中心
     *
     * @param url
     * @return
     */
    private boolean isForward2UserInfo(String url) {
        String pattern = ".*/user";
        Pattern.compile(pattern);

        return Pattern.matches(pattern, url);
    }

    /**
     * 订单列表
     *
     * @param url
     * @return
     */
    private boolean isForward2OrderList(String url) {
        String pattern = ".*/order";
        return Pattern.matches(pattern, url);
    }

    /**
     * 微页面
     *
     * @param url
     * @return
     */
    private boolean isForward2MicroPage(String url) {
        String pattern = ".*/feature/[0-9]*.*";
        return Pattern.matches(pattern, url);
    }

    /**
     * 商品分组页
     *
     * @param url
     * @return
     */
    private boolean isForward2Group(String url) {
        String pattern = ".*/tag/[0-9]*.*";
        return Pattern.matches(pattern, url);
    }

    /**
     * 推客中心
     *
     * @param url
     * @return
     */
    private boolean isForward2Guide(String url) {
        String pattern = ".*/guide";
        return Pattern.matches(pattern, url);
    }

    /**
     * 推广页面
     *
     * @param url
     * @return
     */
    private boolean isForward2Spread(String url) {
        String pattern = ".*/guider/spread";
        return Pattern.matches(pattern, url);
    }

    /**
     * 合伙人中心
     *
     * @param url
     * @return
     */
    private boolean isForward2Partner(String url) {
        String pattern = ".*/partner";
        return Pattern.matches(pattern, url);
    }

    /**
     * 战队中心
     *
     * @param url
     * @return
     */
    private boolean isForward2Team(String url) {
        String pattern = ".*/team";
        return Pattern.matches(pattern, url);
    }

    /**
     * 积分商城
     *
     * @param url
     * @return
     */
    private boolean isForward2CreditMall(String url) {
        String pattern = ".*/credit/mall";
        return Pattern.matches(pattern, url);
    }

    /**
     * 积分卡兑换
     *
     * @param url
     * @return
     */
    private boolean isForward2CreditExchange(String url) {
        String pattern = ".*/credit/exchange";
        return Pattern.matches(pattern, url);
    }

    /**
     * 拼团
     *
     * @param url
     * @return
     */
    private boolean isForward2Pin(String url) {
        String pattern = ".*/pin";
        return Pattern.matches(pattern, url);
    }

    /**
     * 拼团详情
     *
     * @param url
     * @return
     */
    private boolean isForward2PinDetail(String url) {
        String pattern = ".*/pin/detail/[0-9]*";
        return Pattern.matches(pattern, url);
    }

    /**
     * 阶梯购
     *
     * @param url
     * @return
     */
    private boolean isForward2Cash(String url) {
        String pattern = ".*/cash/[0-9]*";
        return Pattern.matches(pattern, url);
    }

    /**
     * 红包抵用券
     *
     * @param url
     * @return
     */
    private boolean isForward2RedEnvelope(String url) {
        String pattern = ".*/red-envelope/[0-9]*";
        return Pattern.matches(pattern, url);
    }

    /**
     * 邀请页面
     *
     * @param url
     * @return
     */
    private boolean isSharePage(String url) {
        return url.contains("invitCode.html");
    }

    /**
     * 首页
     *
     * @param url
     * @return
     */
    private boolean isForward2Home(String url) {

        String pattern = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+" +
                "([A-Za-z0-9-~\\\\/])+$";
        if (Pattern.matches(pattern, url)) {
            String urlHost = Uri.parse(url).getHost();
            String curHost = Uri.parse(URLConstant.getWebUrl()).getHost();
            if (curHost != null && curHost.equals(urlHost)) {
                return true;
            }
        }

        return false;
    }


    public static void main(String args[]) {
        String url1 = "http://shop13299363.weiba896.com/goods/666209449";

        String pattern = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+" +
                "([A-Za-z0-9-~\\\\/])+$";
        if (Pattern.matches(pattern, url1)) {
            String urlHost = Uri.parse(url1).getHost();
            String curHost = Uri.parse(URLConstant.getWebUrl()).getHost();
            if (curHost.equals(urlHost)) {
                System.out.println("isForward2GoodsDetail");
            }
        }

    }
}
