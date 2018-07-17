package com.rrvow.rrchain.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rrvow.rrchain.R;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * yanweiqiang
 * 2018/2/9.
 */

public class RrdLoadFooter extends LinearLayout implements RefreshFooter, IRrdRefreshHeaderAndFooter {
    public static String REFRESH_FOOTER_PULLUP = "上拉加载";
    public static String REFRESH_FOOTER_RELEASE = "释放加载";
    public static String REFRESH_FOOTER_LOADING = "正在加载";
    public static String REFRESH_FOOTER_FINISH = "加载完成";
    public static String REFRESH_FOOTER_FAILED = "加载失败";
    public static String REFRESH_FOOTER_ALLLOADED = "全部加载完成";
    protected boolean mLoadmoreFinished = true;

    private final int REFRESHING_DEGREE = 330;
    private final int COMPLETE_DEGREE = 360;
    private final int FAIL_DEGREE = 330;

    CircularProgressDrawable progressDrawable;
    ImageView ivLogo;
    ImageView ivProgress;
    TextView tvText;

    private String noMoreText;

    public void setNoMoreText(String noMoreText) {
        this.noMoreText = noMoreText;
    }

    public RrdLoadFooter(Context context) {
        super(context);
        init();
    }

    public RrdLoadFooter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RrdLoadFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RrdLoadFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        setGravity(Gravity.CENTER);

        RelativeLayout relativeLayout = new RelativeLayout(getContext());

        float density = getResources().getDisplayMetrics().density;
        progressDrawable = new CircularProgressDrawable();
        progressDrawable.setProgressColor(Color.parseColor("#ff999999"));
        ivProgress = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        ivProgress.setImageDrawable(progressDrawable);
        relativeLayout.addView(ivProgress, params);

        ivLogo = new ImageView(getContext());
        RelativeLayout.LayoutParams logoParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        logoParams.addRule(CENTER_IN_PARENT);
        ivLogo.setImageResource(R.mipmap.refresh_logo_gray);
        relativeLayout.addView(ivLogo, logoParams);

        addView(relativeLayout);

        tvText = new TextView(getContext());
        tvText.setTextColor(Color.parseColor("#ff999999"));
        LayoutParams contentParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.leftMargin = (int) (10 * density);
        addView(tvText, contentParams);

        setPadding(0, (int) (10 * density), 0, (int) (10 * density));
    }

    @Override
    public void onPullingUp(float percent, int offset, int footerHeight, int extendHeight) {
        if (mLoadmoreFinished) {
            progressDrawable.setSweepDegree(COMPLETE_DEGREE);
            return;
        }

        percent = Math.min(1, percent);
        progressDrawable.setSweepDegree((int) (percent * REFRESHING_DEGREE));
    }

    @Override
    public void onPullReleasing(float percent, int offset, int footerHeight, int extendHeight) {
        if (mLoadmoreFinished) {
            progressDrawable.setSweepDegree(COMPLETE_DEGREE);
            return;
        }

        percent = Math.min(1, percent);
        progressDrawable.setSweepDegree((int) (percent * REFRESHING_DEGREE));
    }

    @Override
    public void onLoadmoreReleased(RefreshLayout layout, int footerHeight, int extendHeight) {
        if (mLoadmoreFinished) {
            progressDrawable.setSweepDegree(COMPLETE_DEGREE);
            return;
        }

        progressDrawable.startProgress();
    }

    @Override
    public boolean setLoadmoreFinished(boolean finished) {
        mLoadmoreFinished = finished;

        if (finished) {
            if (TextUtils.isEmpty(noMoreText)) {
                tvText.setText(REFRESH_FOOTER_ALLLOADED);
            } else {
                tvText.setText(noMoreText);
            }
        }

        return true;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int extendHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int extendHeight) {

    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            progressDrawable.setSweepDegree(COMPLETE_DEGREE);
            tvText.setText(REFRESH_FOOTER_FINISH);
        } else {
            progressDrawable.setSweepDegree(FAIL_DEGREE);
            tvText.setText(REFRESH_FOOTER_FAILED);
        }
        return 500;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        if (mLoadmoreFinished) {
            return;
        }

        switch (newState) {
            case None:
                break;
            case PullToUpLoad:
                tvText.setText(REFRESH_FOOTER_PULLUP);
                break;
            case Loading:
            case LoadReleased:
                tvText.setText(REFRESH_FOOTER_LOADING);
                break;
            case ReleaseToLoad:
                tvText.setText(REFRESH_FOOTER_RELEASE);
                break;
        }
    }

    @Override
    public void setLogoRes(int resId) {
        ivLogo.setImageResource(resId);
    }

    @Override
    public void setProgressColor(int color) {
        progressDrawable.setProgressColor(color);
    }

    @Override
    public void setTextColor(int color) {
        tvText.setTextColor(color);
    }
}
