package com.rrvow.rrchain.main.view;

import com.rrvow.rrchain.base.view.IBaseView;

public interface TaskView extends IBaseView {

    void onRefreshSuccess(double amount, String date);

    void onRefreshStart();

    void onRefreshStop();

}
