package com.rrvow.rrchain.main.presenter;

import com.alibaba.fastjson.JSONObject;
import com.rrvow.rrchain.base.presenter.BasePresenter;
import com.rrvow.rrchain.common.constant.ErrorCodeConstant;
import com.rrvow.rrchain.common.net.callback.Callback;
import com.rrvow.rrchain.common.utils.LogUtils;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.main.model.MainBiz;
import com.rrvow.rrchain.main.view.TaskView;

public class TaskPresenter extends BasePresenter<TaskView> {

    private MainBiz mainBiz;

    public TaskPresenter() {
        mainBiz = new MainBiz();
    }

    public void refresh() {
        mainBiz.getAmountSum(new Callback() {
            @Override
            public void onStart() {
                if (getView() != null) {
                    getView().onRefreshStart();
                }
            }

            @Override
            public void onSuccess(JSONObject response) {
                String code = response.getString("code");
                if (ErrorCodeConstant.SUCCESS.equals(code)) {
                    JSONObject data = response.getJSONObject("data");
                    double amount = data.getDouble("sum");
                    String updateTime = data.getString("update_time");
                    getView().onRefreshSuccess(amount, updateTime);
                } else {
                    BaseToast.showShort(response.getString("msg"));
                }
                LogUtils.d("getAmountSum", response.toString());
            }

            @Override
            public void onFail(int errorCode, String errorMessage) {
                BaseToast.showShort(errorMessage);
                if (getView() != null) {
                    getView().onRefreshStop();
                }
            }

            @Override
            public void onStop() {
                if (getView() != null) {
                    getView().onRefreshStop();
                }
            }
        });
    }
}
