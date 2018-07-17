package com.rrvow.rrchain.main.fragment;

import android.content.Intent;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.CommonWebActivity;
import com.rrvow.rrchain.base.activity.CommonWebShareActivity;
import com.rrvow.rrchain.base.fragment.BaseFragment;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.main.activity.TaskActivity;
import com.rrvow.rrchain.main.manager.AccountManager;

import butterknife.OnClick;

public class SocialBusinessFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_social_business;
    }

    @Override
    protected void initView() {
        super.initView();
        getBaseActionBar().customNavigationBarWithTitle("社会化电商");
    }

    @OnClick(R.id.ll_task)
    void toTask() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), TaskActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.ll_quotation)
    void toQuotation() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.RANKING_LIST_PAGE);
        startActivity(intent);
    }

    @OnClick(R.id.ll_invite)
    void toInvite() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), CommonWebShareActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.INVITE_PAGE
                + AccountManager.getInstance().getToken());
        startActivity(intent);
    }

    @OnClick(R.id.ll_strategy)
    void toStrategy() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        Intent intent = new Intent(getContext(), CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.CRYSTAL_STRATEGY);
        startActivity(intent);
    }
}
