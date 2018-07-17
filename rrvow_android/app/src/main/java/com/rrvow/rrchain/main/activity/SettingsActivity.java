package com.rrvow.rrchain.main.activity;

import android.content.DialogInterface;
import android.widget.Button;
import android.widget.TextView;

import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.BaseActivity;
import com.rrvow.rrchain.common.event.LogoutEvent;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.widget.CommonDialogsInBase;
import com.rrvow.rrchain.main.manager.AccountManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.txt_account)
    TextView txtAccount;

    @BindView(R.id.txt_version)
    TextView txtVersion;

    @BindView(R.id.btn_logout)
    Button btnLogout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    protected void initView() {
        super.initView();
        getBaseActionBar().customNavigationBarWithBackBtn("设置");
        txtVersion.setText(AppTools.getVersionName(this));
        txtAccount.setText(AccountManager.getInstance().getAccount());
    }

    @Override
    protected String getGATraceTitle() {
        return "设置";
    }

    @OnClick(R.id.btn_logout)
    public void onViewClicked() {
        CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
        commonDialogsInBase.displayTwoBtnDialog(this, null, "取消",
                "退出", "您确定要退出吗？", null, new OnBtnClickL() {
                    @Override
                    public void onBtnClick(DialogInterface dialog) {
                        dialog.dismiss();
                        EventBus.getDefault().post(new LogoutEvent(""));
                        finish();
                    }
                });
    }

}
