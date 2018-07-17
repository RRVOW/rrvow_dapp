package com.rrvow.rrchain.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;

import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.common.event.AppExitEvent;
import com.rrvow.rrchain.common.event.BaseEvent;
import com.rrvow.rrchain.common.utils.PermissionUtils;
import com.rrvow.rrchain.common.widget.CommonDialogsInBase;

import org.greenrobot.eventbus.Subscribe;


/**
 * 申请权限
 *
 * @author lvpengcheng 2016/01/14
 */
public class RequestPermissionActivity extends FragmentActivity {

    private static final int REQUEST_PERMISSIONS = 101;
    private int mRequestAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getIntent().getExtras();
        if (null != data) {
            mRequestAction = data.getInt(PermissionUtils.EXTRA_PERMISSION_ACTION, -1);
            String[] permissions = PermissionUtils.getPermissionNeeded(this, mRequestAction);
            if (null != permissions) {
                if (permissions.length > 0) {
                    showPermissionsDialog(permissions);
                    return;
                } else {
                    //已授权 无需申请
                    setResult(RESULT_OK);
                }
            }
        }
        finish();
    }

    private void showPermissionsDialog(final String[] permissions) {
        OnBtnClickL clickListener = new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                dialog.dismiss();
                ActivityCompat.requestPermissions(RequestPermissionActivity.this, permissions,
                        REQUEST_PERMISSIONS);
            }
        };
        DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                finish();
            }
        };
        CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
        commonDialogsInBase.displayMsgWithOneBtnDialog(this, getString(R.string.request_permission),
                PermissionUtils.getPermissionsTips(this, mRequestAction),
                clickListener, cancelListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            showPermissionRefusedDialog();
                            return;
                        }
                    }
                    //授权通过
                    setResult(RESULT_OK);
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void showPermissionRefusedDialog() {
        CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
        commonDialogsInBase.displayTwoBtnDialog(this, "", null, getString(R.string.set_permission_action), getString(R.string.set_permission_tips), new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                finish();
            }
        }, new OnBtnClickL() {
            @Override
            public void onBtnClick(DialogInterface dialog) {
                Uri packageURI = Uri.parse("package:" + getPackageName());
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                startActivity(intent);
                setResult(RESULT_CANCELED);
                finish();
            }
        }, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @Subscribe
    public void onEvent(BaseEvent baseEvent) {
        if (baseEvent instanceof AppExitEvent) {
            finish();
        }
    }
}
