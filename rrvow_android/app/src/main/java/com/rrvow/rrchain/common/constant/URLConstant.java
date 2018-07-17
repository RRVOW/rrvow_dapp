package com.rrvow.rrchain.common.constant;

import com.rrvow.rrchain.main.App;

/**
 * @author by lpc.
 */
public final class URLConstant {

    // http://yan66665.gitee.io/rrvowpay/JSBridge.html 协议调试
    private static final String BASE_URL = "https://rrenlian.wxrrd.com/";
    private static final String PIC_URL = "https://ms.wrcdn.com/";


    //测试环境
//    private static final String BASE_TEST_URL = "http://121.43.173.22:9524/";
    private static final String BASE_TEST_URL = "http://39.107.107.204:8080/";
    private static final String WEB_URL = "";
    public static final String CONFIRM_ORDER_URL = "http://trade.weiba896.com/order/confirm";
    //确认订单页面

    public static String getIndexUrl() {
        return "";
    }

    public static String getBaseUrl() {
        if (App.IS_TEST) {
            return BASE_TEST_URL;
        }
        return BASE_URL;
    }

    public static String getWebUrl() {
        return WEB_URL;
    }


    public static final String GET_SMS_CODE = getBaseUrl() + "login";

    public static final String LOGIN = getBaseUrl() + "login/is_identify";

    public static final String GET_APP_VERSION = getBaseUrl() + "is_update";

    public static final String MAIN_INDEX = getBaseUrl() + "show/main_index";

    public static final String PICK_CRYSTAL = getBaseUrl() + "pick_crystals";

    public static final String MY_CRYSTAL_PAGE = getBaseUrl() + "myCrystal.html?token=";

    public static final String MY_AGENT_PAGE = getBaseUrl() + "myAgent.html?token=";

    public static final String CRYSTAL_STRATEGY = getBaseUrl() + "crystalStrategy.html";

    public static final String APPLY_STRATEGY = getBaseUrl() + "applyStrategy.html";

    public static final String INVITE_PAGE = getBaseUrl() + "invitCode.html?&token=";

    public static final String INDEX_BANNER = getBaseUrl() + "data/getDataToday";

    public static final String CRYSTAL_DETAIL = getBaseUrl() + "crystalNum.html?&token=";

    public static final String CONTRIBUTION_DETAIL = getBaseUrl() + "contribution.html?&token=";

    public static final String AMOUNT_SUM = getBaseUrl() + "data/getAmountSum";

    public static final String RANKING_LIST_PAGE = getBaseUrl() + "list" +
            ".html?nsukey=knXPyai34RS0HTlmEiRKafNdAZoM19FsmWYTXm9ZDU5AHduB6Mxyovj5w2" +
            "%2B2YEjvPsi6aigI%2BPmMsZW6ygKoW2TkPa6" +
            "%2Fj5qsGbBqU3SUbT4eOsw4OoBB0GjbdzuKotiPhA45F3C3z23CpdH5447lTYiqmHqGitGu95lEkkvPD2lNG4GHSwz2jWxBopnKQ%2BDFDqxHw1KaXpYSEg52i6oO%2BQ%3D%3D";
}
