package com.rrvow.rrchain.common.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import cn.magicwindow.MLinkAPIFactory;
import cn.magicwindow.MWConfiguration;
import cn.magicwindow.MagicWindowSDK;
import cn.magicwindow.Session;
import cn.magicwindow.mlink.YYBCallback;

/**
 * Created by david on 2018/1/24.
 */

public class DeepLinkUtil {

    public final static int HANDLER_CHECKYYB_SUCCESS = 1;
    public final static int HANDLER_CHECKYYB_FAILED = 2;

    /**
     * 初始化
     *
     * @param ctx
     */
    public static void initLinkModel(final Context ctx) {
        MWConfiguration config = new MWConfiguration(ctx);
        config.setLogEnable(true);
        MagicWindowSDK.initSDK(config);
    }

    /**
     * 初始化session
     *
     * @param ctx
     */
    public static void initSession(Application app) {
        Session.setAutoSession(app);
    }

    /**
     * 通过注释方式注册
     *
     * @param ctx
     */
    public static void registerLinkModel(final Context ctx) {
        MLinkAPIFactory.createAPI(ctx).registerWithAnnotation(ctx);
    }

    /**
     * 获取参数做页面路由
     *
     * @param ctx
     * @param intent
     * @param handler
     */
    public static void routerLinkModel(final Context ctx, Intent intent, final Handler handler) {
        if (intent == null) {
            handler.sendEmptyMessageDelayed(HANDLER_CHECKYYB_FAILED, 1200);
            return;
        }

        if (intent.getData() != null) {
            MLinkAPIFactory.createAPI(ctx).router(ctx, intent.getData());
            handler.sendEmptyMessageDelayed(HANDLER_CHECKYYB_SUCCESS, 1200);
        } else {
            MLinkAPIFactory.createAPI(ctx).checkYYB(ctx, new YYBCallback() {
                @Override
                public void onFailed(Context context) {
                    handler.sendEmptyMessageDelayed(HANDLER_CHECKYYB_FAILED, 1200);
                }

                @Override
                public void onSuccess() {
                    handler.sendEmptyMessageDelayed(HANDLER_CHECKYYB_SUCCESS, 1200);
                }
            });
        }
    }
}
