package com.rrvow.rrchain.main.activity;

import android.content.Intent;
import android.view.View;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.CommonWebActivity;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.utils.AppTools;

public class ApplyAgentActivity extends CommonWebActivity {

    @Override
    protected void initView() {
        super.initView();
        getBaseActionBar().getIvRightIcon().setImageResource(R.mipmap.ic_strategy);
        getBaseActionBar().getIvRightIcon().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppTools.isFastDoubleClick()) {
                    return;
                }
                Intent intent = new Intent(ApplyAgentActivity.this, CommonWebActivity.class);
                intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.APPLY_STRATEGY);
                startActivity(intent);
            }
        });
    }
}
