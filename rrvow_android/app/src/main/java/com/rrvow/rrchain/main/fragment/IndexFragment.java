package com.rrvow.rrchain.main.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.chinatelecom.dialoglibrary.listener.OnBtnClickL;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.base.activity.CommonWebActivity;
import com.rrvow.rrchain.base.activity.CommonWebShareActivity;
import com.rrvow.rrchain.base.fragment.BaseFragment;
import com.rrvow.rrchain.common.constant.URLConstant;
import com.rrvow.rrchain.common.event.BaseEvent;
import com.rrvow.rrchain.common.event.LoginEvent;
import com.rrvow.rrchain.common.utils.AppTools;
import com.rrvow.rrchain.common.utils.DateUtil;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.common.widget.CommonDialogsInBase;
import com.rrvow.rrchain.common.widget.PagerSlidingTabStrip;
import com.rrvow.rrchain.common.widget.WrapperViewPager;
import com.rrvow.rrchain.main.activity.LoginActivity;
import com.rrvow.rrchain.main.adapter.CrystalListAdapter;
import com.rrvow.rrchain.main.manager.AccountManager;
import com.rrvow.rrchain.main.model.CrystalBean;
import com.rrvow.rrchain.main.presenter.MainPresenter;
import com.rrvow.rrchain.main.view.MainView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.impl.ScrollBoundaryDeciderAdapter;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class IndexFragment extends BaseFragment<MainView, MainPresenter> implements MainView {

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    @BindView(R.id.view_pager)
    WrapperViewPager viewPager;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.img_big_crystal)
    ImageView imgBigCrystal;

    @BindView(R.id.img_crystal1)
    ImageView imgCrystal1;

    @BindView(R.id.img_crystal2)
    ImageView imgCrystal2;

    @BindView(R.id.img_crystal3)
    ImageView imgCrystal3;

    @BindView(R.id.img_crystal4)
    ImageView imgCrystal4;

    @BindView(R.id.img_crystal5)
    ImageView imgCrystal5;

    @BindView(R.id.img_crystal6)
    ImageView imgCrystal6;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    @BindView(R.id.txt_big_crystal)
    TextView txtBigCrystal;

    @BindView(R.id.txt_crystal1)
    TextView txtCrystal1;

    @BindView(R.id.txt_crystal2)
    TextView txtCrystal2;

    @BindView(R.id.txt_crystal3)
    TextView txtCrystal3;

    @BindView(R.id.txt_crystal4)
    TextView txtCrystal4;

    @BindView(R.id.txt_crystal5)
    TextView txtCrystal5;

    @BindView(R.id.txt_crystal6)
    TextView txtCrystal6;

    @BindView(R.id.txt_crystal_num)
    TextView txtCrystalNum;

    @BindView(R.id.txt_contribution_num)
    TextView txtContributionNum;

    @BindView(R.id.txt_time)
    TextView txtRefreshTime;

    @BindView(R.id.vf_msg)
    ViewFlipper vfMsg;

    private TextView txtCrystalEmpty, txtContributionEmpty;

    private RecyclerView mCrystalList;
    private RecyclerView mContributionList;
    private IndexPagerAdapter mIndexPagerAdapter;
    private CrystalListAdapter crystalListAdapter;
    private CrystalListAdapter contributionAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_index;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    @Override
    protected void initView() {
        super.initView();
        initListView();
        startCrystalAnim();
        refreshLayout.setScrollBoundaryDecider(new ScrollBoundaryDeciderAdapter() {
            @Override
            public boolean canLoadmore(View content) {
                return false;
            }
        });
//        refreshLayout.useCustomerStyle(R.color.index_refrsh_color);
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPresenter.getCrystalData();
                vfMsg.stopFlipping();
                mPresenter.getIndexBannerData();
            }
        });
        mPresenter.getCrystalData();
        mPresenter.getIndexBannerData();
        updateViewByLoginState();
        forward();
    }

    public void updateViewByLoginState() {
        if (getActivity() == null || getActivity().isFinishing() || isDetached()) {
            return;
        }

        if (checkIsLogin()) {
            txtBigCrystal.setText("水晶生成中");
        } else {
            setLogoutView();
        }
    }

    private void forward() {
        if (!checkIsLogin()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.txt_crystal_sta)
    void toCrystalStrategy() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }
        if (!checkIsLogin()) {
            displayLoginDialog();
            return;
        }
        Intent intent = new Intent(getContext(), CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.CRYSTAL_STRATEGY);
        startActivity(intent);
    }

    private void initListView() {
        List<View> views = new ArrayList<>();
        View crystalView = getLayoutInflater().inflate(R.layout.view_crystal_raking, null, false);
        mCrystalList = crystalView.findViewById(R.id.list);
        TextView txtLabel = crystalView.findViewById(R.id.txt_label);
        txtCrystalEmpty = crystalView.findViewById(R.id.txt_empty);
        txtLabel.setText("水晶");
        views.add(crystalView);
        View contributionView = getLayoutInflater().inflate(R.layout.view_crystal_raking, null,
                false);
        mContributionList = contributionView.findViewById(R.id.list);
        txtContributionEmpty = contributionView.findViewById(R.id.txt_empty);
        txtContributionEmpty.setText("暂无贡献力排行");
        views.add(contributionView);
        mIndexPagerAdapter = new IndexPagerAdapter(views);
        viewPager.setAdapter(mIndexPagerAdapter);
        viewPager.setOffscreenPageLimit(2);

        tabStrip.setViewPager(viewPager);

        crystalListAdapter = new CrystalListAdapter();
        contributionAdapter = new CrystalListAdapter();
        mCrystalList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContributionList.setLayoutManager(new LinearLayoutManager(getContext()));
        mCrystalList.setAdapter(crystalListAdapter);
        mContributionList.setAdapter(contributionAdapter);

        viewPager.setObjectForPosition(crystalView, 0);
        viewPager.setObjectForPosition(contributionView, 1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.resetHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        viewPager.resetHeight(0);
    }

    private void startCrystalAnim() {
        startAnimation(imgBigCrystal, txtBigCrystal, 5, 800);
        startAnimation(imgCrystal1, txtCrystal1, 3, 500);
        startAnimation(imgCrystal2, txtCrystal2, 3, 450);
        startAnimation(imgCrystal3, txtCrystal3, 3, 480);
        startAnimation(imgCrystal4, txtCrystal4, 3, 600);
        startAnimation(imgCrystal5, txtCrystal5, 3, 550);
        startAnimation(imgCrystal6, txtCrystal6, 3, 520);
    }

    @OnClick({R.id.img_crystal1, R.id.img_crystal2, R.id.img_crystal3,
            R.id.img_crystal4, R.id.img_crystal5, R.id.img_crystal6,
            R.id.img_big_crystal})
    void onCrystalClick(View view) {
        if (!checkIsLogin()) {
            displayLoginDialog();
            return;
        }
        switch (view.getId()) {
            case R.id.img_crystal1:
                startCrystalDismissAnim(imgCrystal1, txtCrystal1);
                imgCrystal1.setEnabled(false);
                pickCrystal(imgCrystal1);
                playCrystalSound();
                break;
            case R.id.img_crystal2:
                startCrystalDismissAnim(imgCrystal2, txtCrystal2);
                imgCrystal2.setEnabled(false);
                pickCrystal(imgCrystal2);
                playCrystalSound();
                break;
            case R.id.img_crystal3:
                startCrystalDismissAnim(imgCrystal3, txtCrystal3);
                imgCrystal3.setEnabled(false);
                pickCrystal(imgCrystal3);
                playCrystalSound();
                break;
            case R.id.img_crystal4:
                startCrystalDismissAnim(imgCrystal4, txtCrystal4);
                imgCrystal4.setEnabled(false);
                pickCrystal(imgCrystal4);
                playCrystalSound();
                break;
            case R.id.img_crystal5:
                startCrystalDismissAnim(imgCrystal5, txtCrystal5);
                imgCrystal5.setEnabled(false);
                pickCrystal(imgCrystal5);
                playCrystalSound();
                break;
            case R.id.img_crystal6:
                startCrystalDismissAnim(imgCrystal6, txtCrystal6);
                imgCrystal6.setEnabled(false);
                pickCrystal(imgCrystal6);
                playCrystalSound();
                break;
        }
    }

    @OnClick(R.id.txt_ranking_list)
    void toRankingList() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }

        Intent intent = new Intent(getContext(), CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.RANKING_LIST_PAGE);
        startActivity(intent);
    }

    @OnClick(R.id.txt_invite)
    void toInvite() {
        if (AppTools.isFastDoubleClick()) {
            return;
        }

        Intent intent = new Intent(getContext(), CommonWebShareActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.INVITE_PAGE
                + AccountManager.getInstance().getToken());
        startActivity(intent);
    }

    public boolean checkIsLogin() {
        if (!AccountManager.getInstance().isLogin()) {
            return false;
        }
        return true;
    }

    public void displayLoginDialog() {
        CommonDialogsInBase commonDialogsInBase = new CommonDialogsInBase();
        commonDialogsInBase.displayTwoBtnDialog(getActivity(), null, "取消", "立即登录", "当前未登录账号，请登录",
                null, new OnBtnClickL() {
                    @Override
                    public void onBtnClick(DialogInterface dialog) {
                        startLoginActivity();
                        dialog.dismiss();
                    }
                });
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    private void pickCrystal(ImageView imageView) {
        if (imageView.getTag() != null && imageView.getTag() instanceof String) {
            mPresenter.pickCrystal((String) imageView.getTag());
        }
    }

    private void startCrystalDismissAnim(final ImageView imageView, final TextView textView) {
        AnimatorSet animationSet = new AnimatorSet();
        animationSet.playTogether(ObjectAnimator.ofFloat(imageView, "alpha", 1, 0),
                ObjectAnimator.ofFloat(imageView, "translationY", imageView.getHeight() / 4,
                        -imageView.getHeight()));
        animationSet.setDuration(500);
        animationSet.addListener(new EmptyAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.setVisibility(View.INVISIBLE);
            }
        });
        animationSet.start();

        AnimatorSet animationSet1 = new AnimatorSet();
        animationSet1.playTogether(ObjectAnimator.ofFloat(textView, "alpha", 1, 0),
                ObjectAnimator.ofFloat(textView, "translationY", imageView.getHeight() / 4,
                        -imageView.getHeight()));
        animationSet1.setDuration(500);
        animationSet1.addListener(new EmptyAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setVisibility(View.INVISIBLE);
            }
        });

        animationSet1.start();
    }

    private void playCrystalSound() {
        if (getActivity() == null) {

            return;
        }
        MediaPlayer mMediaPlayer = buildMediaPlayer(getActivity());

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(BaseEvent event) {
        if (event instanceof LoginEvent) {
            if (mPresenter != null) {
                mPresenter.getIndexBannerData();
                mPresenter.getCrystalData();
            }
            updateViewByLoginState();
        }
    }

    private MediaPlayer buildMediaPlayer(Context activity) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            AssetFileDescriptor file = activity.getResources().openRawResourceFd(R.raw.crystal);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file
                        .getLength());
            } finally {
                file.close();
            }
            mediaPlayer.setVolume(0.2f, 0.2f);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException ioe) {
            mediaPlayer.release();
            return null;
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mPresenter != null) {
                mPresenter.getCrystalData();
                mPresenter.getIndexBannerData();
                updateViewByLoginState();
            }

            scrollToTop();
        }
    }

    private void scrollToTop() {
        if (scrollView != null) {
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    if (scrollView != null) {
                        scrollView.scrollTo(0, 0);
                    }
                }
            });
        }
    }

    private void startAnimation(ImageView imageView, TextView txtView, int distance, int time) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "translationY",
                distance, 0);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.setDuration(time);
        objectAnimator.start();

        ObjectAnimator objectAnimator1 = objectAnimator.clone();
        objectAnimator1.setTarget(txtView);
        objectAnimator1.start();
    }

    private void startFadeInAnimation(final ImageView imageView, final TextView txtView) {
        if (imageView.getVisibility() == View.VISIBLE) {
            return;
        }

        imageView.setVisibility(View.VISIBLE);
        imageView.setEnabled(true);
        txtView.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0, 1);
        objectAnimator.setDuration(1000);
        objectAnimator.start();
        objectAnimator.addListener(new EmptyAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setEnabled(true);
            }
        });

        ObjectAnimator objectAnimator1 = objectAnimator.clone();
        objectAnimator1.setTarget(txtView);
        objectAnimator1.start();
        objectAnimator1.addListener(new EmptyAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation, boolean isReverse) {
                txtView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        scrollToTop();
    }

    @Override
    public void onGetCrystalList(List<CrystalBean> crystalList, List<CrystalBean>
            contributionList) {
        if (crystalList == null || crystalList.isEmpty()) {
            txtCrystalEmpty.setVisibility(View.VISIBLE);
        } else {
            txtCrystalEmpty.setVisibility(View.GONE);
        }

        if (contributionList == null || contributionList.isEmpty()) {
            txtContributionEmpty.setVisibility(View.VISIBLE);
        } else {
            txtCrystalEmpty.setVisibility(View.GONE);
        }
        crystalListAdapter.setData(crystalList);
        contributionAdapter.setData(contributionList);
    }

    @Override
    public void onGetCrystalNum(String crystalNum, String contributionNum) {
        if (StringUtil.isNotEmpty(crystalNum)) {
            txtCrystalNum.setText(StringUtil.formatDNum(crystalNum));
        } else {
            txtCrystalNum.setText("--");
        }

        if (StringUtil.isNotEmpty(contributionNum)) {
            txtContributionNum.setText(contributionNum);
        } else {
            txtContributionNum.setText("--");
        }
    }

    @Override
    public void onGetCrystalSeed(List<CrystalBean> seedBeanList, String growing_crystals,
                                 String growing_crystals_id, String refreshTime) {
        txtRefreshTime.setText("更新于" + DateUtil.millisToStr(StringUtil.parseLong(refreshTime)));
        if (StringUtil.parseDouble(growing_crystals) > 0) {
            startFadeInAnimation(imgCrystal5, txtCrystal5);
            txtCrystal5.setText(growing_crystals);
            imgCrystal5.setTag(growing_crystals_id);
        }
        if (seedBeanList != null && !seedBeanList.isEmpty()) {
            displaySeedList(seedBeanList);
        }
    }

    @Override
    public void onGetIndexBanner(String growup, String transcation, String invite, String singin) {
        vfMsg.stopFlipping();
        if (vfMsg.getChildCount() > 0) {
            for (int i = 0; i < vfMsg.getChildCount(); i++) {
                View view = vfMsg.getChildAt(i);
                view.clearAnimation();
            }
            vfMsg.removeAllViews();
        }
        vfMsg.setVisibility(View.VISIBLE);
        String ban1 = "水晶明细:    进货获取+" + transcation + "    自然生长+" + growup;
        View view = LayoutInflater.from(getContext()).inflate(R.layout
                .item_vf, null, false);
        TextView textView1 = view.findViewById(R.id.tv);
        textView1.setText(ban1);
        vfMsg.addView(view);

        String ban2 = "贡献力明细:    邀请好友+" + invite + "    每日登录+" + singin;
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout
                .item_vf, null, false);
        TextView textView2 = view1.findViewById(R.id.tv);
        textView2.setText(ban2);
        vfMsg.addView(view1);

        vfMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppTools.isFastDoubleClick()) {
                    return;
                }

                if (vfMsg.getDisplayedChild() == 0) {
                    toCrystalNumPage();
                } else {
                    toContributionPage();
                }
            }
        });
        vfMsg.startFlipping();
    }

    private void toCrystalNumPage() {
        Intent intent = new Intent(getContext(), CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.CRYSTAL_DETAIL + AccountManager
                .getInstance().getToken());
        startActivity(intent);
    }

    public void toContributionPage() {
        Intent intent = new Intent(getContext(), CommonWebActivity.class);
        intent.putExtra(CommonWebActivity.PARAM_URL, URLConstant.CONTRIBUTION_DETAIL +
                AccountManager.getInstance().getToken());
        startActivity(intent);
    }

    @Override
    public void onGetIndexBannerFail() {
        vfMsg.setVisibility(View.GONE);
    }

    private boolean needRefreshSeed() {
        return imgCrystal1.getVisibility() != View.VISIBLE
                && imgCrystal2.getVisibility() != View.VISIBLE
                && imgCrystal3.getVisibility() != View.VISIBLE
                && imgCrystal4.getVisibility() != View.VISIBLE
                && imgCrystal5.getVisibility() != View.VISIBLE
                && imgCrystal6.getVisibility() != View.VISIBLE;
    }

    private void displaySeedList(List<CrystalBean> seedBeanList) {
        if (!needRefreshSeed()) {
            return;
        }
        int seedNum = seedBeanList.size();
        switch (seedNum) {
            case 1:
                startFadeInAnimation(imgCrystal1, txtCrystal1);
                break;
            case 2:
                startFadeInAnimation(imgCrystal1, txtCrystal1);
                startFadeInAnimation(imgCrystal4, txtCrystal4);
                break;
            case 3:
                startFadeInAnimation(imgCrystal1, txtCrystal1);
                startFadeInAnimation(imgCrystal4, txtCrystal4);
                startFadeInAnimation(imgCrystal3, txtCrystal3);
                break;
            case 4:
                startFadeInAnimation(imgCrystal1, txtCrystal1);
                startFadeInAnimation(imgCrystal4, txtCrystal4);
                startFadeInAnimation(imgCrystal3, txtCrystal3);
                startFadeInAnimation(imgCrystal2, txtCrystal2);
                break;
            case 5:
            default:
                startFadeInAnimation(imgCrystal1, txtCrystal1);
                startFadeInAnimation(imgCrystal4, txtCrystal4);
                startFadeInAnimation(imgCrystal3, txtCrystal3);
                startFadeInAnimation(imgCrystal2, txtCrystal2);
                startFadeInAnimation(imgCrystal6, txtCrystal6);
                break;
        }

        setCrystalTag(seedBeanList);
    }

    private void setLogoutView() {
        txtBigCrystal.setText("登录获取水晶");
        txtRefreshTime.setText("----.--.--:--");
        txtCrystalNum.setText("");
        txtContributionNum.setText("");
        imgCrystal1.setVisibility(View.INVISIBLE);
        txtCrystal1.setVisibility(View.INVISIBLE);
        imgCrystal2.setVisibility(View.INVISIBLE);
        txtCrystal2.setVisibility(View.INVISIBLE);
        imgCrystal3.setVisibility(View.INVISIBLE);
        txtCrystal3.setVisibility(View.INVISIBLE);
        imgCrystal4.setVisibility(View.INVISIBLE);
        txtCrystal4.setVisibility(View.INVISIBLE);
        imgCrystal5.setVisibility(View.INVISIBLE);
        txtCrystal5.setVisibility(View.INVISIBLE);
        imgCrystal6.setVisibility(View.INVISIBLE);
        txtCrystal6.setVisibility(View.INVISIBLE);
        vfMsg.stopFlipping();
        vfMsg.setVisibility(View.GONE);
    }

    private void setCrystalTag(List<CrystalBean> seedBeanList) {
        for (int i = 0; i < seedBeanList.size(); i++) {
            CrystalBean crystalBean = seedBeanList.get(i);
            if (crystalBean != null) {
                String value = crystalBean.getValue();
                if (i == 0) {
                    imgCrystal1.setTag(crystalBean.getId());
                    if (StringUtil.isNotEmpty(value)) {
                        txtCrystal1.setText(value);
                    } else {
                        txtCrystal1.setText("");
                    }
                }
                if (i == 1) {
                    imgCrystal4.setTag(crystalBean.getId());
                    if (StringUtil.isNotEmpty(value)) {
                        txtCrystal4.setText(value);
                    } else {
                        txtCrystal4.setText("");
                    }
                }
                if (i == 2) {
                    imgCrystal3.setTag(crystalBean.getId());
                    if (StringUtil.isNotEmpty(value)) {
                        txtCrystal3.setText(value);
                    } else {
                        txtCrystal3.setText("");
                    }
                }
                if (i == 3) {
                    imgCrystal2.setTag(crystalBean.getId());
                    if (StringUtil.isNotEmpty(value)) {
                        txtCrystal2.setText(value);
                    } else {
                        txtCrystal2.setText("");
                    }
                }
                if (i == 4) {
                    imgCrystal6.setTag(crystalBean.getId());
                    if (StringUtil.isNotEmpty(value)) {
                        txtCrystal6.setText(value);
                    } else {
                        txtCrystal6.setText("");
                    }
                }
            }
        }
    }

    @Override
    public void onReqStop() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
        }
    }

    public class IndexPagerAdapter extends PagerAdapter {
        private List<View> views;

        public IndexPagerAdapter(List<View> views) {
            super();
            this.views = views;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            String[] titles = {"水晶排行", "贡献力排行"};
            return titles[position];
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    static class EmptyAnimatorListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {
        }

        @Override
        public void onAnimationEnd(Animator animation) {
        }

        @Override
        public void onAnimationCancel(Animator animation) {
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }
    }
}
