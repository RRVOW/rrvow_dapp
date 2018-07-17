package com.rrvow.rrchain.main;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.rrvow.rrchain.common.manager.DWCrashHandler;
import com.rrvow.rrchain.common.widget.RrdLoadFooter;
import com.rrvow.rrchain.common.widget.RrdRefreshHeader;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreater;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreater;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.squareup.leakcanary.LeakCanary;


/**
 * @author by lpc on 2017/8/28.
 */
public class App extends Application {

    public static final boolean IS_TEST = false;
    public static final boolean IS_DEBUG = false;

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 内存泄漏检查
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);

        Fresco.initialize(this, ImagePipelineConfig.newBuilder(mContext).setDownsampleEnabled
                (true).build());

        // Crash Handler
        if (!IS_DEBUG) {
            DWCrashHandler.getInstance().init(getApplicationContext());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreater(new DefaultRefreshHeaderCreater() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new RrdRefreshHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于
                // %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreater(new DefaultRefreshFooterCreater() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new RrdLoadFooter(context);
            }
        });
    }
}
