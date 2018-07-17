package com.rrvow.rrchain.main.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.BaseActivity;
import com.rrvow.rrchain.common.event.LoginEvent;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.common.widget.CommonStatusBar;
import com.rrvow.rrchain.common.widget.ProgressButtonLayout;
import com.rrvow.rrchain.common.widget.blurlayout.BlurLayout;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.rrvow.rrchain.main.presenter.LoginPresenter;
import com.rrvow.rrchain.main.view.LoginView;

import org.greenrobot.eventbus.EventBus;

import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity<LoginView, LoginPresenter> implements LoginView {

    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.et_code)
    EditText etCode;

    @BindView(R.id.tv_send_code)
    TextView tvSendCode;

    @BindView(R.id.et_invite_code)
    EditText etInviteCode;

    @BindView(R.id.ll_invite_code)
    LinearLayout llInviteCode;

    @BindView(R.id.bl_input)
    BlurLayout blInput;

    @BindView(R.id.btn_login)
    Button btnLogin;

    @BindView(R.id.txt_back)
    TextView txtBack;

    @BindView(R.id.pbl_login)
    ProgressButtonLayout progressButton;

    private int secondCount = 60;

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        super.initView();
        setSwipeBackEnable(false);

        if (AccountManager.getInstance().isFirstStart()) {
            txtBack.setVisibility(View.INVISIBLE);
        } else {
            txtBack.setVisibility(View.VISIBLE);
        }

        etCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        && llInviteCode.getVisibility() != View.VISIBLE) {
                    loginByAccount();
                    AppTools.hideSoftInput(LoginActivity.this);
                    return true;
                }
                return false;
            }
        });

        etInviteCode.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE ||
                        (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    loginByAccount();
                    AppTools.hideSoftInput(LoginActivity.this);
                    return true;
                }
                return false;
            }
        });

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 11) {
                    tvSendCode.setEnabled(true);
                } else {
                    tvSendCode.setEnabled(false);
                }
                checkInputComplete();
            }
        });

        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputComplete();
            }
        });

        etInviteCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkInputComplete();
            }
        });

        String phone = AccountManager.getInstance().getPhone();
        if (StringUtil.isNotEmpty(phone)) {
            etPhone.setText(phone);
            etPhone.setSelection(phone.length());
        }
    }

    @OnClick(R.id.btn_login)
    void login() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        loginByAccount();
    }

    private void loginByAccount() {
        String account = etPhone.getText().toString();
        String smsCode = etCode.getText().toString();
        String inviteCode = etInviteCode.getText().toString();

        if (account.length() < 11) {
            BaseToast.showShort("请输入有效的手机号");
            return;
        }

        if (StringUtil.isEmpty(smsCode)) {
            BaseToast.showShort("验证码不能为空");
            return;
        }

        if (llInviteCode.getVisibility() == View.VISIBLE
                && StringUtil.isEmpty(inviteCode)) {
            BaseToast.showShort("邀请码不能为空");
            return;
        }

        mPresenter.login(account, smsCode, inviteCode);
        progressButton.showProgress();
    }

    @OnClick(R.id.tv_send_code)
    void getSmsCode() {
        String account = etPhone.getText().toString();
        if (account.length() < 11) {
            BaseToast.showShort("请输入合法的手机号");
            return;
        }
        mPresenter.getSmsCode(account);
    }

    @Override
    public void onLoginSuccess() {
        AccountManager.getInstance().clearFirstStart();
        EventBus.getDefault().post(new LoginEvent());
        progressButton.showCover(getWindow().peekDecorView(), new ProgressButtonLayout
                .CoverCallback() {
            @Override
            public void onCovered() {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }

    @Override
    public void onGetCodeSuccess(boolean needInviteCode) {
        if (needInviteCode) {
            llInviteCode.setVisibility(View.VISIBLE);
            etCode.setImeActionLabel("下一步", EditorInfo.IME_ACTION_NEXT);
            etCode.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        } else {
            llInviteCode.setVisibility(View.GONE);
            etCode.setImeActionLabel("登录", EditorInfo.IME_ACTION_DONE);
            etCode.setImeOptions(EditorInfo.IME_ACTION_DONE);
        }

        startCountDown();
        etCode.requestFocus();
        AppTools.showSoftInput(etCode);
    }

    @Override
    public void onReqStop() {
        progressButton.hideProgress();
    }

    /**
     * 倒计时
     */
    private void startCountDown() {
        tvSendCode.setClickable(false);
        getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (tvSendCode == null) {
                    return;
                }

                secondCount--;
                tvSendCode.setText(MessageFormat.format("{0}秒后重发", secondCount));
                if (secondCount == 0) {
                    if (etPhone.length() > 0) {
                        tvSendCode.setEnabled(true);
                    } else {
                        tvSendCode.setEnabled(false);
                    }
                    tvSendCode.setText("重新发送");
                    tvSendCode.setClickable(true);
                    secondCount = 60;
                } else {
                    getHandler().postDelayed(this, 1000);
                }
            }
        });
    }

    private void checkInputComplete() {
        String account = etPhone.getText().toString();
        String smsCode = etCode.getText().toString();
        String inviteCode = etInviteCode.getText().toString();

        if (llInviteCode.getVisibility() == View.VISIBLE) {
            btnLogin.setEnabled(account.length() >= 11 && smsCode.length() > 0
                    && inviteCode.length() > 0);
        } else {
            btnLogin.setEnabled(account.length() >= 11 && smsCode.length() > 0);
        }
    }

    @Override
    protected void setStatusBar() {
        CommonStatusBar.setStatusBarByTransparent(this, false);
    }

    @Override
    protected String getGATraceTitle() {
        return "登录";
    }

    @OnClick(R.id.txt_back)
    void back() {
        finish();
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
    }
}
