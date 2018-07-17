package com.rrvow.rrchain.main.view;

import com.rrvow.rrchain.base.view.IBaseView;
import com.rrvow.rrchain.main.model.CrystalBean;

import java.util.List;

/**
 * @author lpc
 */
public interface MainView extends IBaseView {

    void onGetCrystalList(List<CrystalBean> crystalList, List<CrystalBean> contributionList);

    void onGetCrystalNum(String crystalNum, String contributionNum);

    void onGetCrystalSeed(List<CrystalBean> seedBeanList, String growing_crystals, String
            growing_crystals_id, String refreshTime);

    void onGetIndexBanner(String growup, String transcation, String invite, String singin);

    void onGetIndexBannerFail();

    void onReqStop();
}
