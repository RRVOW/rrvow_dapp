package com.rrvow.rrchain.common.widget;

import android.app.Activity;
import android.os.Build;
import android.view.View;

import com.rrvow.rrchain.common.utils.AppTools;
import com.jaeger.library.StatusBarUtil;

/**
 * @author by lpc on 2018/1/18.
 */
public final class CommonStatusBar {

    public static void setStatusBarByTransparent(Activity activity) {
        setStatusBarByTransparent(activity, true);
    }

    public static void setStatusBarByTransparent(Activity activity, boolean isDark) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, null);
            if (isDark) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View
                        .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } else {
            if (AppTools.isMiui()) {
                StatusBarUtil.setTranslucentForImageViewInFragment(activity, 0, null);
                AppTools.MIUISetStatusBarLightMode(activity, isDark);
            } else {
                StatusBarUtil.setTranslucentForImageViewInFragment(activity, 30, null);
            }
        }
    }


    /**
     * 设置带纯色titlebar的状态栏
     */
    public static void setStatusBarByColorActionBar(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StatusBarUtil.setColorForSwipeBack(activity, color, 0);
            activity.getWindow().getDecorView().setSystemUiVisibility(View
                    .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            if (AppTools.isMiui()) {
                StatusBarUtil.setColorForSwipeBack(activity, color, 0);
                AppTools.MIUISetStatusBarLightMode(activity, true);
            } else {
                StatusBarUtil.setColorForSwipeBack(activity, color, 30);
            }
        }
    }

}
