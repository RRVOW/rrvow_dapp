package com.rrvow.rrchain.common.manager;

import android.content.Context;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.common.utils.LogUtils;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.main.App;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

/**
 * Created by david on 2018/1/30.
 */
public class DWAnalyticsManager {
    private final static String TAG = DWAnalyticsManager.class.getSimpleName();
    private static DWAnalyticsManager instance;

    public static DWAnalyticsManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new DWAnalyticsManager(ctx);
        }

        return instance;
    }

    public DWAnalyticsManager(Context ctx) {
        this.mContext = ctx;
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this.mContext);
        analytics.dispatchLocalHits();
        analytics.setLocalDispatchPeriod(30);
        //analytics.setDryRun(BuildConfig.DEBUG);// 如果是DEBUG状态不上传统计数据
        //To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        mTracker = analytics.newTracker("UA-67242875-1");
        mTracker.enableExceptionReporting(true);
//        mTracker.enableAutoActivityTracking(true); // 设为true后traceWithPageTitle将失效
//        mTracker.set("&cd1", "官网");
    }

    private Tracker mTracker;

    private Context mContext;

    public void traceWithPageTitle(String pageTitle) {
        LogUtils.i(TAG, "Setting screen name: " + pageTitle);
        if (mTracker == null) {
            return;
        }

        if (StringUtil.isEmpty(pageTitle)) {
            LogUtils.w(TAG, "pageTitle is null.");
            return;
        }

        mTracker.setScreenName("Android_" + pageTitle);

        mTracker.send(new HitBuilders.ScreenViewBuilder().build());

        mTracker.setScreenName(null);
    }

    public void traceWithCategoryAndAction(String category, String action) {
        LogUtils.d(TAG, "trace category : " + category + " action : " + action);
        if (mTracker == null) {
            return;
        }

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .build());
    }

    /////////////// Fabric Crashlytics ////////////////
    public void crashlytics(Context ctx) {
        Fabric crashlyticsKit = new Fabric.Builder(ctx)
                .kits(new Crashlytics()).debuggable(App.IS_DEBUG).build();

        Fabric.with(crashlyticsKit);
    }
}

