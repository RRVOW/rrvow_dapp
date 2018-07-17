package com.rrvow.rrchain.main.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.rrvow.rrchain.common.event.BaseEvent;
import com.rrvow.rrchain.common.event.CheckUpdateDismissEvent;
import com.rrvow.rrchain.main.fragment.UpdateFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * @author by lpc on 2016/8/8.
 */
public class CheckUpdateActivity extends FragmentActivity {

    private String mStrUrl;
    private String version_desc = "";
    private String isForceUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initData();
    }

    public void initData() {
        Bundle data = getIntent().getExtras();
        if (null != data) {
            mStrUrl = data.getString("download_url");
            version_desc = data.getString("version_desc");
            isForceUpdate = data.getString("isForceUpdate");
        }
        displayCheckUpdateDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void displayCheckUpdateDialog() {
        UpdateFragment updateFragment = new UpdateFragment();
        Bundle data = new Bundle();
        data.putString("url", mStrUrl);
        data.putString("content", version_desc);
        data.putString("isForceUpdate", isForceUpdate);
        updateFragment.setArguments(data);
        updateFragment.show(getSupportFragmentManager(), "");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (event instanceof CheckUpdateDismissEvent) {
            finish();
        }
    }
}
