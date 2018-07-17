package com.rrvow.rrchain.main.activity;

import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.BaseActivity;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.main.presenter.TaskPresenter;
import com.rrvow.rrchain.main.view.TaskView;

import butterknife.BindView;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity<TaskView, TaskPresenter> implements TaskView {

    @BindView(R.id.pb)
    ProgressBar progressBar;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_diff)
    TextView tvDiff;
    @BindView(R.id.ll_txt)
    LinearLayout llTxt;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.tv_update_time)
    TextView tvUpdateTime;
    @BindView(R.id.loading)
    ProgressBar loading;

    final static int LEVEL_FIRST = 1000;
    final static int LEVEL_SECOND = 5000;
    final static int LEVEL_THIRD = 10000;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task;
    }

    @Override
    protected TaskPresenter createPresenter() {
        return new TaskPresenter();
    }

    @Override
    protected void initView() {
        super.initView();
        getBaseActionBar().customNavigationBarWithBackBtn("任务激励");
        mPresenter.refresh();
    }

    @Override
    protected String getGATraceTitle() {
        return "任务激励";
    }

    /**
     * 将销售金额转化成进度
     * @param value
     * @return
     */
    private int getProgress(double value) {
        if (value < 0) {
            return 0;
        }

        int progress = 0;
        if(value <= LEVEL_FIRST) { // 30
            progress = (int)((value / LEVEL_FIRST) * 30);
        } else if (value <= LEVEL_SECOND) { // 60
            progress = (int)(((value-LEVEL_FIRST) / (LEVEL_SECOND-LEVEL_FIRST)) * 30) + 30;
        } else if (value <= LEVEL_THIRD) { // 60
            progress = (int)(((value-LEVEL_SECOND) / (LEVEL_THIRD-LEVEL_SECOND)) * 30) + 60;
        }
        return progress;
    }

    /**
     * 开始动画
     * @param endValue
     */
    private void startAni(int endValue) {
        final ValueAnimator anim = ValueAnimator.ofInt(0, endValue);
        anim.setDuration(1000);
        anim.setStartDelay(0);
        anim.setRepeatCount(0);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (Integer) animation.getAnimatedValue();
                if (progressBar!=null) {
                    progressBar.setProgress(progress);
                } else {
                    anim.cancel();
                }
            }
            });
        anim.start();
    }

    @OnClick(R.id.btn_refresh)
    public void click() {
        if (AppTools.isFastDoubleClick() || loading.getVisibility() == View.VISIBLE) {
            return;
        }
        mPresenter.refresh();
    }

    @Override
    public void onRefreshSuccess(double amount, String date) {
        mProgressHUD.cancel();
        if (amount>=0 && amount<LEVEL_FIRST ) {
            String amountStr = StringUtil.formatAmount(amount);
            String diffStr = StringUtil.formatAmount((LEVEL_FIRST-amount));
            String percentStr = "50%";
            int progress = getProgress(amount);
            updateUI(amountStr, diffStr, percentStr, View.VISIBLE, date, progress);
        }
        else if (amount<LEVEL_SECOND) {
            String amountStr = StringUtil.formatAmount(amount);
            String diffStr = StringUtil.formatAmount((LEVEL_SECOND-amount));
            String percentStr = "20%";
            int progress = getProgress(amount);
            updateUI(amountStr, diffStr, percentStr, View.VISIBLE, date, progress);
        }
        else if (amount<LEVEL_THIRD) {
            String amountStr = StringUtil.formatAmount(amount);
            String diffStr = StringUtil.formatAmount((LEVEL_THIRD-amount));
            String percentStr = "10%";
            int progress = getProgress(amount);
            updateUI(amountStr, diffStr, percentStr, View.VISIBLE, date, progress);
        } else {
            String amountStr = StringUtil.formatAmount(amount);
            int progress = 100;
            updateUI(amountStr, "", "", View.GONE, date, progress);
        }
    }

    @Override
    public void onRefreshStart() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRefreshStop() {
        loading.setVisibility(View.GONE);
    }

    /**
     * 更新UI
     * @param amount
     * @param diff
     * @param percent
     * @param visibility
     * @param updateTime
     * @param progress
     */
    private void updateUI(String amount, String diff, String percent, int visibility, String updateTime, int progress) {
        tvAmount.setText("¥"+amount);
        tvDiff.setText(diff);
        tvPercent.setText(percent);
        llTxt.setVisibility(visibility);
        tvUpdateTime.setText("数据更新于"+updateTime);
        startAni(progress);
    }
}