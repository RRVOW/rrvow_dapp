package com.rrvow.rrchain.common.utils;

import android.text.TextUtils;
import com.rrvow.rrchain.common.constant.URLConstant;

public class UrlMatchUtils {

    public static String getTraceTitleByUrl(String url) {
        if (TextUtils.isEmpty(url))
            return null;

        if (url.contains(URLConstant.CRYSTAL_STRATEGY))
            return "水晶攻略";
        if (url.contains(URLConstant.INVITE_PAGE))
            return "邀请好友";
        if (url.contains(URLConstant.CRYSTAL_DETAIL))
            return "我的水晶数";
        if (url.contains(URLConstant.CONTRIBUTION_DETAIL))
            return "我的贡献力";
        if (url.contains(URLConstant.RANKING_LIST_PAGE))
            return "虚拟市值排行";
        if (url.contains("crystalTask")) // 我的钱包->更多
            return "水晶任务";

        return null;
    }


}
