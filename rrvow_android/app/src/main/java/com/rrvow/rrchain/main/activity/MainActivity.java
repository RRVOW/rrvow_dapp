package com.rrvow.rrchain.main.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.chinatelecom.dialoglibrary.widget.MaterialDialog;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.BaseActivity;
import com.rrvow.rrchain.common.event.BaseEvent;
import com.rrvow.rrchain.common.event.JumpMainEvent;
import com.rrvow.rrchain.common.event.LoginEvent;
import com.rrvow.rrchain.common.event.LogoutEvent;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.common.widget.CommonDialogsInBase;
import com.rrvow.rrchain.common.widget.CommonStatusBar;
import com.rrvow.rrchain.main.fragment.CrystalFragment;
import com.rrvow.rrchain.main.fragment.IndexFragment;
import com.rrvow.rrchain.main.fragment.SocialBusinessFragment;
import com.rrvow.rrchain.main.manager.AccountManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private List<ImageView> mIndexVList;

    private IndexFragment indexFragment;
    private CrystalFragment crystalFragment;
    private SocialBusinessFragment socialBusinessFragment;
//    private AgentFragment agentFragment;

    private MaterialDialog dialog;

    private long mExitTime;

    ///////////// Life Cycle /////////////
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 2) {
            if (crystalFragment != null && !crystalFragment.onBackPressed()) {
                quit();
            }
        }
//        else if (mViewPager.getCurrentItem() == 2) {
//            if (agentFragment != null && !agentFragment.onBackPressed()) {
//                quit();
//            }
//        }
        else {
            quit();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        overridePendingTransition(R.anim.page_in, R.anim.page_out);
        setSwipeBackEnable(false);
        initFragment();
        initAdapter();
    }

    @Override
    protected void setStatusBar() {
        CommonStatusBar.setStatusBarByTransparent(this, false);
    }

    @Override
    protected String getGATraceTitle() {
        switch (mViewPager.getCurrentItem()) {
            case 0:
                return "主页_水晶";
            case 1:
                return "主页_社会化电商";
            case 2:
                return "主页_我的钱包";
        }
        return "主页";
    }

    ///////////// Event Bus /////////////
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(BaseEvent event) {
        if (event instanceof JumpMainEvent) {
            JumpMainEvent jumpMainEvent = (JumpMainEvent) event;
            mViewPager.setCurrentItem(jumpMainEvent.getPage(), false);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (event instanceof LogoutEvent) {
            AccountManager.getInstance().logout(MainActivity.this);
            mViewPager.setCurrentItem(0, false);
            indexFragment.updateViewByLoginState();
        }
    }

    ///////////// Customize Method /////////////
    private void initFragment() {
        List<Fragment> fragments = new ArrayList<>();
        indexFragment = new IndexFragment();
        fragments.add(indexFragment);
        socialBusinessFragment = new SocialBusinessFragment();
        fragments.add(socialBusinessFragment);
        crystalFragment = new CrystalFragment();
        fragments.add(crystalFragment);
//        agentFragment = new AgentFragment();
//        fragments.add(agentFragment);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                fragments);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    private void initAdapter() {
        mViewPager.setOffscreenPageLimit(3);
        ImageView shopImgView = (ImageView) findViewById(R.id.shop_selected_imgview);
        shopImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0, false);
            }
        });

        ImageView orderImgView = (ImageView) findViewById(R.id.order_selected_imgview);
        orderImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIsLogin()) {
                    displayLoginDialog();
                    return;
                }
                mViewPager.setCurrentItem(1, false);
            }
        });

        ImageView msgImgView = (ImageView) findViewById(R.id.msg_selected_imgview);
        msgImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkIsLogin()) {
                    displayLoginDialog();
                    return;
                }
                mViewPager.setCurrentItem(2, false);
            }
        });

        mIndexVList = new ArrayList<>();
        mIndexVList.add(shopImgView);
        mIndexVList.add(orderImgView);
        mIndexVList.add(msgImgView);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                if (position < mIndexVList.size() - 1) {
                    mIndexVList.get(position).setAlpha(1f - positionOffset);
                    mIndexVList.get(position + 1).setAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                clearIndexVList();
                mIndexVList.get(position).setAlpha(1f);
                CommonStatusBar.setStatusBarByTransparent(MainActivity.this, position == 1);
                trace();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void clearIndexVList() {
        for (ImageView view : mIndexVList) {
            view.setAlpha(0f);
        }
    }

    public void displayLoginDialog() {
        if (dialog != null && dialog.isShowing()) {
            return;
        }
        CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
        dialog = commonDialogsInBase.displayTwoBtnDialog(this, null, "取消",
                "立即登录", "当前未登录账号，请登录",
                null, new OnBtnClickL() {
                    @Override
                    public void onBtnClick(DialogInterface dialog) {
                        startLoginActivity();
                        dialog.dismiss();
                    }
                });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean checkIsLogin() {
        if (!AccountManager.getInstance().isLogin()) {
            return false;
        }
        return true;
    }

    private void quit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            BaseToast.showShort("再按一次退出程序");
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        ViewPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            mFragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}

