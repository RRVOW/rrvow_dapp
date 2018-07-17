package com.rrvow.rrchain.main.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.fragment.BaseFragment;
import com.rrvow.rrchain.common.event.AppExitEvent;
import com.rrvow.rrchain.common.event.CheckUpdateDismissEvent;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.common.widget.BaseToast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author lpc
 */
public class UpdateFragment extends BaseFragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    private String icForceUpdate;

    public UpdateFragment() {
        super();
        setStyle(STYLE_NO_FRAME, R.style.dialog_theme);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_update;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String content = getArguments().getString("content");
            tvContent.setText(content);
        }

        icForceUpdate = getArguments().getString("isForceUpdate");

        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                EventBus.getDefault().post(new CheckUpdateDismissEvent());
            }
        });

        getDialog().setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if ("1".equals(icForceUpdate)) {
                    EventBus.getDefault().post(new AppExitEvent());
                }
                dialog.dismiss();
                EventBus.getDefault().post(new CheckUpdateDismissEvent());
            }
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if ("1".equals(icForceUpdate)) {
                    EventBus.getDefault().post(new AppExitEvent());
                }
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    EventBus.getDefault().post(new CheckUpdateDismissEvent());
                }
                return false;
            }
        });
    }

    @OnClick(R.id.tv_get_it)
    void onViewClicked() {
        toSystemBrowser();
        dismissAllowingStateLoss();
        EventBus.getDefault().post(new CheckUpdateDismissEvent());

        if ("1".equals(icForceUpdate)) {
            EventBus.getDefault().post(new AppExitEvent());
        }
    }

    private void toSystemBrowser() {
        if (getArguments() != null) {
            String url = getArguments().getString("url");
            if (StringUtil.isEmpty(url)) {
                url = "http://www.baidu.com";
            }
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        EventBus.getDefault().post(new CheckUpdateDismissEvent());
    }
}
