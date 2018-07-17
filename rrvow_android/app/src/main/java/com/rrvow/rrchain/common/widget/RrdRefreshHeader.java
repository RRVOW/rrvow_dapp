package com.rrvow.rrchain.common.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rrvow.rrchain.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;

import static android.widget.RelativeLayout.CENTER_IN_PARENT;

/**
 * yanweiqiang
 * 2018/2/9.
 */

public class RrdRefreshHeader extends LinearLayout implements RefreshHeader, IRrdRefreshHeaderAndFooter {
    public static String REFRESH_HEADER_PULLDOWN = "下拉刷新";
    public static String REFRESH_HEADER_REFRESHING = "正在刷新";
    public static String REFRESH_HEADER_RELEASE = "释放刷新";
    public static String REFRESH_HEADER_FINISH = "刷新完成";
    public static String REFRESH_HEADER_FAILED = "刷新失败";

    private final int REFRESHING_DEGREE = 330;
    private final int COMPLETE_DEGREE = 360;
    private final int FAIL_DEGREE = 330;


    CircularProgressDrawable progressDrawable;
    ImageView ivLogo;
    TextView tvText;

    public RrdRefreshHeader(Context context) {
        super(context);
        init();
    }

    public RrdRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RrdRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RrdRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        setGravity(Gravity.CENTER);

        RelativeLayout relativeLayout = new RelativeLayout(getContext());

        float density = getResources().getDisplayMetrics().density;
        progressDrawable = new CircularProgressDrawable();
        progressDrawable.setProgressColor(Color.parseColor("#ff999999"));
        ImageView imageView = new ImageView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.addRule(CENTER_IN_PARENT);
        imageView.setImageDrawable(progressDrawable);
        relativeLayout.addView(imageView, params);

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

        //setPadding(0, (int) (10 * density), 0, (int) (10 * density));
    }

    @Override
    public void onPullingDown(float percent, int offset, int headerHeight, int extendHeight) {
        percent = Math.min(1, percent);
        progressDrawable.setSweepDegree((int) (percent * REFRESHING_DEGREE));
    }

    @Override
    public void onReleasing(float percent, int offset, int headerHeight, int extendHeight) {
        percent = Math.min(1, percent);
        progressDrawable.setSweepDegree((int) (percent * REFRESHING_DEGREE));
    }

    @Override
    public void onRefreshReleased(RefreshLayout layout, int headerHeight, int extendHeight) {
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Scale;
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
        progressDrawable.startProgress();
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        progressDrawable.stopProgress();

        if (success) {
            progressDrawable.setSweepDegree(COMPLETE_DEGREE);
            tvText.setText(REFRESH_HEADER_FINISH);
        } else {
            progressDrawable.setSweepDegree(FAIL_DEGREE);
            tvText.setText(REFRESH_HEADER_FAILED);
        }

        return 500;
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                tvText.setText(REFRESH_HEADER_PULLDOWN);
                break;
            case ReleaseToRefresh:
                tvText.setText(REFRESH_HEADER_RELEASE);
                break;
            case Refreshing:
                tvText.setText(REFRESH_HEADER_REFRESHING);
                break;
            case RefreshFinish:
                tvText.setText(REFRESH_HEADER_FINISH);
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
