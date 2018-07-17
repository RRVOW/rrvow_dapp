package com.rrvow.rrchain.common.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.rrvow.rrchain.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class SmartRefreshLayoutWrapper extends SmartRefreshLayout {
    public SmartRefreshLayoutWrapper(Context context) {
        super(context);
    }

    public SmartRefreshLayoutWrapper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartRefreshLayoutWrapper(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IRrdRefreshHeaderAndFooter getRrdRefreshHeader() {
        return (IRrdRefreshHeaderAndFooter) getRefreshHeader();
    }

    public IRrdRefreshHeaderAndFooter getRrdRefreshFooter() {
        return (IRrdRefreshHeaderAndFooter) getRefreshFooter();
    }

    public void useCustomerStyle(int resID) {
        useRefreshStyle(resID, R.mipmap.refresh_logo_white, getResources().getColor(R.color.white), getResources().getColor(R.color.white));
    }

    public void useGrayStyle() {
        useRefreshStyle(R.color.transparent, R.mipmap.refresh_logo_gray, getResources().getColor(R.color.light_gray), getResources().getColor(R.color.light_gray));
    }

    public void useRefreshStyle(final int bgResId, final int logoResId, final int progressColor, final int textColor) {
        post(new Runnable() {
            @Override
            public void run() {
                IRrdRefreshHeaderAndFooter headerAndFooter = null;

                if (getRefreshHeader() instanceof IRrdRefreshHeaderAndFooter) {
                    headerAndFooter = (IRrdRefreshHeaderAndFooter) getRefreshHeader();
                } else if (getRefreshFooter() instanceof IRrdRefreshHeaderAndFooter) {
                    headerAndFooter = (IRrdRefreshHeaderAndFooter) getRefreshFooter();
                }

                if (headerAndFooter != null) {
                    headerAndFooter.setBackgroundResource(bgResId);
                    headerAndFooter.setLogoRes(logoResId);
                    headerAndFooter.setProgressColor(progressColor);
                    headerAndFooter.setTextColor(textColor);
                }
            }
        });
    }
}
