package com.rrvow.rrchain.base.activity;

import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.common.utils.ImageUtil;
import com.rrvow.rrchain.common.widget.BaseToast;
import com.rrvow.rrchain.common.widget.CustomShareBoard;

import butterknife.OnClick;
import cn.sharesdk.framework.Platform;

/**
 * @author lpc
 */
public class CommonWebShareActivity extends CommonWebActivity {

    private CustomShareBoard customShareBoard;

    @Override
    protected void initView() {
        super.initView();
        getBaseActionBar().customNavigationBarWithRightImgBtn("",
                getResources().getDrawable(R.mipmap.ic_share));
        getBaseActionBar().getIvRightIcon().setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.right_icon)
    void rightClick() {
        getBaseActionBar().customNavigationBarWithRightImgBtn("邀请码",
                getResources().getDrawable(R.mipmap.ic_share));
        Bitmap bitmap = ImageUtil.printImage(mWebView);
        if (bitmap != null) {
            if (customShareBoard == null) {
                customShareBoard = new CustomShareBoard(CommonWebShareActivity.this);
            }
            customShareBoard.setBitmap(bitmap);
            customShareBoard.setShareType(Platform.SHARE_IMAGE);
            customShareBoard.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        } else {
            BaseToast.showShort("分享数据未准备好");
        }
    }


    @Override
    protected void onLoadProgress(int progress) {
        super.onLoadProgress(progress);
        if (progress == 100) {
            getBaseActionBar().getIvRightIcon().setVisibility(View.VISIBLE);
        }
    }
}
