package com.rrvow.rrchain.common.widget;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.main.App;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 *
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

    private static final int REQUEST_STORAGE_PERMISSION = 1001;

    private Activity mActivity;
    private Button but;
    private ClipboardManager cmb;
    private String image = "", title = "", intro = "", url = "";
    private String descOfQRCode;
    private ShareCallback shareCallback;
    private Bitmap bitmap;

    private int shareType = Platform.SHARE_WEBPAGE;

    public void setShareCallback(ShareCallback shareCallback) {
        this.shareCallback = shareCallback;
    }

    public onRequestPermissionListener getListener() {
        return mListener;
    }

    public void setListener(onRequestPermissionListener mListener) {
        this.mListener = mListener;
    }

    private onRequestPermissionListener mListener;

    public interface onRequestPermissionListener {
        void onShareToQQ();

        void onShareToQZone();
    }


    public CustomShareBoard(Activity activity) {
        super(activity);
        this.mActivity = activity;
        initView(activity);
    }

    public void setSharecontent(String url, String image, String title, String intro) {
        this.image = image;
        this.title = title;
        this.intro = intro;
        this.url = url;

        // 设置默认分享链接
        if (TextUtils.isEmpty(this.url)) {
            this.url = URLConstant.getIndexUrl();
        }
    }

    private String imageOfWechat = "", titleOfWechat = "", introOfWechat = "", urlOfWechat = "";

    public void setShareContentOfWeChat(String urlOfWechat, String imageOfWechat, String
            titleOfWechat, String introOfWechat) {
        this.urlOfWechat = urlOfWechat;
        this.imageOfWechat = imageOfWechat;
        this.titleOfWechat = titleOfWechat;
        this.introOfWechat = introOfWechat;
    }

    private String imageOfWeChatCycle = "", titleOfWeChatCycle = "", introOfWeChatCycle = "",
            urlOfWeChatCycle = "";

    public void setShareContentOfWeChatCycle(String urlOfWeChatCycle, String imageOfWeChatCycle,
                                             String titleOfWeChatCycle, String introOfWeChatCycle) {
        this.urlOfWeChatCycle = urlOfWeChatCycle;
        this.imageOfWeChatCycle = imageOfWeChatCycle;
        this.titleOfWeChatCycle = titleOfWeChatCycle;
        this.introOfWeChatCycle = introOfWeChatCycle;
    }

    private void initView(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.app_share, null);
        rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
        rootView.findViewById(R.id.wechat).setOnClickListener(this);
        rootView.findViewById(R.id.qq).setOnClickListener(this);
        rootView.findViewById(R.id.sms).setOnClickListener(this);
        rootView.findViewById(R.id.com).setOnClickListener(this);
        rootView.findViewById(R.id.qzone).setOnClickListener(this);
        rootView.findViewById(R.id.QuickResponseCode).setOnClickListener(this);
        setContentView(rootView);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        setBackgroundDrawable(dw);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setTouchable(true);
        rootView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = v.findViewById(R.id.share_rl).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
        but = (Button) rootView.findViewById(R.id.end_share);
        but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(intro)) {
            intro = title;
        }
        switch (v.getId()) {
            case R.id.wechat_circle:
                if (isWechatAvilible(mActivity)) {
                    showShare(WechatMoments.NAME);
                } else {
                    BaseToast.showShort(AppTools.getString(R.string.no_wx));
                }
                break;
            case R.id.wechat:
                if (isWechatAvilible(mActivity)) {
                    showShare(Wechat.NAME);
                } else {
                    BaseToast.showShort(AppTools.getString(R.string.no_wx));
                }
                break;
            case R.id.qq:
                if (mListener != null) {
                    mListener.onShareToQQ();
                }
                break;
            case R.id.sms:
                break;
            case R.id.com:
                copy(url);
                showDialog(v);
                break;
            case R.id.qzone:
                if (mListener != null) {
                    mListener.onShareToQZone();
                }
                break;
            case R.id.QuickResponseCode:
                break;
            default:
                break;
        }

        dismiss();
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static boolean isWechatAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showShare(String platformName) {
        if (shareType == Platform.SHARE_WEBPAGE) {
            shareWebPage(platformName);
        } else if (shareType == Platform.SHARE_IMAGE) {
            shareImage(bitmap, platformName);
        }
    }

    private void shareImage(Bitmap bitmap, String platformName) {
        final Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_IMAGE);
        shareParams.setImageData(bitmap);
        Platform platform = ShareSDK.getPlatform(platformName);
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                BaseToast.showShort("分享失败");
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                BaseToast.showShort("分享成功");
                if (shareCallback != null) {
                    shareCallback.onSuccess();
                }
            }

            public void onCancel(Platform arg0, int arg1) {
                BaseToast.showShort("分享取消");
            }
        });
        platform.share(shareParams);
    }

    private void shareWebPage(String platformName) {
        final Platform.ShareParams shareParams = new Platform.ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle(title);
        shareParams.setText(intro);
        shareParams.setImageUrl(image);
        shareParams.setUrl(url);
        Platform platform = ShareSDK.getPlatform(platformName);
        platform.setPlatformActionListener(new PlatformActionListener() {
            public void onError(Platform arg0, int arg1, Throwable arg2) {
                BaseToast.showShort("分享失败");
            }

            public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
                BaseToast.showShort("分享成功");
                if (shareCallback != null) {
                    shareCallback.onSuccess();
                }
            }

            public void onCancel(Platform arg0, int arg1) {
                BaseToast.showShort("分享取消");
            }
        });
        platform.share(shareParams);
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public void copy(String content) {
        // 得到剪贴板管理器
        cmb = (ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());

    }

    public void showDialog(View view) {
        if (!"".equals(cmb.getText())) {
            BaseToast.showShort("复制成功");
        } else {
            BaseToast.showShort("复制失败");
        }
    }


    public String getDescOfQRCode() {
        return descOfQRCode;
    }

    public void setDescOfQRCode(String descOfQRCode) {
        this.descOfQRCode = descOfQRCode;
    }

    public interface ShareCallback {
        void onSuccess();
    }
}
