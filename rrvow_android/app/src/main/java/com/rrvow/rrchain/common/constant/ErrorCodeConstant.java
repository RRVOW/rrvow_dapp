package com.rrvow.rrchain.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by daiwei on 16/7/25.
 */
public class ErrorCodeConstant {

    public static final int ERR_CODE_SUCCESS = 1;

    public static final String SUCCESS = "1";

    public static final int ERR_CODE_USER_TOKEN_INVALID = 2;

    public static final int ERR_CODE_NO_DATA = -14002110;

    public static final int ERR_CODE_NO_PERMISSION = 1001;

    public static final int ERR_CODE_NO_NET = -14002111;

    public static final int ERR_CODE_HTTP_SOCKET_FAIL = -14002112;

    public static final int ERR_CODE_HTTP_CODE_ERROR = -14002113;

    public static final int ERR_CODE_HTTP_RESPONSE_ERROR = -14002114;

    public static final int ERR_CODE_HTTP_EXCEPTION = -14002115;

    public static final int ERR_CODE_HTTP_RESPONSE_ERROR_WIFI = -14002116;

    public static final int ERR_CODE_HTTP_AIRPLANE_MODE = -14002117;

    public static final int ERR_CODE_CUSTOM = -14002118;

    public static final int ERR_CODE_DATA_ILLEGAL = -100003;

    public static final Map<Integer, String> MAP_ERROR_CODE = new HashMap<Integer, String>(0);

    static {
        MAP_ERROR_CODE.put(ERR_CODE_SUCCESS, "成功");
        MAP_ERROR_CODE.put(ERR_CODE_NO_DATA, "没有数据");
        MAP_ERROR_CODE.put(ERR_CODE_NO_NET, "手机没有联网");
        MAP_ERROR_CODE.put(ERR_CODE_HTTP_SOCKET_FAIL, "网络连接异常，请检测您的网络是否连接正常");      // "连接不上后台服务"
        MAP_ERROR_CODE.put(ERR_CODE_HTTP_CODE_ERROR, "网络连接异常，请检测您的网络是否连接正常");        //
        // "后台服务HTTP响应异常"
        MAP_ERROR_CODE.put(ERR_CODE_HTTP_RESPONSE_ERROR, "网络连接异常，请检测您的网络是否连接正常");        //
        // "后台服务响应数据异常" http CODE != 200
        MAP_ERROR_CODE.put(ERR_CODE_HTTP_EXCEPTION, "网络连接异常，请检测您的网络是否连接正常");        // "HTTP解析异常"
        MAP_ERROR_CODE.put(ERR_CODE_HTTP_RESPONSE_ERROR_WIFI, "wifi连接异常,请检测你的网络");
        MAP_ERROR_CODE.put(ERR_CODE_HTTP_AIRPLANE_MODE, "您设置了飞行模式");
    }
}
