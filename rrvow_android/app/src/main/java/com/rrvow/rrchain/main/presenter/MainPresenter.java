package com.rrvow.rrchain.main.presenter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rrvow.rrchain.base.presenter.BasePresenter;
import com.rrvow.rrchain.common.net.callback.Callback;
import com.rrvow.rrchain.common.utils.StringUtil;
import com.rrvow.rrchain.main.model.CrystalBean;
import com.rrvow.rrchain.main.model.MainBiz;
import com.rrvow.rrchain.main.view.MainView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lpc
 */
public class MainPresenter extends BasePresenter<MainView> {

    private MainBiz mainBiz;

    public MainPresenter() {
        mainBiz = new MainBiz();
    }

    public void getCrystalData() {
        mainBiz.getCrystalData(new Callback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(JSONObject response) {
                String code = response.getString("code");
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    String crystalListStr = data.getString("crystals_rank");
                    String contributionListStr = data.getString("contribution_rank");
                    List<CrystalBean> crystalBeanList = JSON.parseArray(crystalListStr,
                            CrystalBean.class);
                    List<CrystalBean> contributionList = JSON.parseArray(contributionListStr,
                            CrystalBean.class);
                    if (getView() != null) {
                        getView().onGetCrystalList(crystalBeanList, contributionList);
                    }

                    String crystals_cnt = data.getString("crystals_cnt");
                    String contribution = data.getString("contribution");

                    if (getView() != null) {
                        getView().onGetCrystalNum(crystals_cnt, contribution);
                    }

                    String crystals_seed = data.getString("crystals_seed");
                    String updated_at = data.getString("updated_at");
                    String growing_crystals = data.getString("growing_crystals");
                    String growing_crystals_id = data.getString("growing_crystals_id");
                    List<CrystalBean> seedList = JSON.parseArray(crystals_seed,
                            CrystalBean.class);
                    if (getView() != null) {
                        getView().onGetCrystalSeed(seedList, growing_crystals,
                                growing_crystals_id, updated_at);
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {

            }

            @Override
            public void onStop() {
                if (getView() != null) {
                    getView().onReqStop();
                }
            }
        });
    }

    /**
     * 拾取水晶
     */
    public void pickCrystal(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        mainBiz.pickCrystal(ids, new Callback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(JSONObject response) {
                getCrystalData();
                getIndexBannerData();
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
            }

            @Override
            public void onStop() {
            }
        });
    }

    public void getIndexBannerData() {
        mainBiz.getIndexBannerData(new Callback() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(JSONObject response) {
                String code = response.getString("code");
                JSONObject data = response.getJSONObject("data");
                if (data != null) {
                    String growup = data.getString("grownup");
                    String transcation = data.getString("tanscation");
                    String invite = data.getString("invite");
                    String singin = data.getString("signin");
                    if (getView() != null) {
                        getView().onGetIndexBanner(growup, transcation, invite
                                , singin);
                    }
                } else {
                    if (getView() != null) {
                        getView().onGetIndexBannerFail();
                    }
                }
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
            }

            @Override
            public void onStop() {
            }
        });
    }
}
