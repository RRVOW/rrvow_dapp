package com.rrvow.rrchain.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.chinatelecom.dialoglibrary.listener.OnOperItemClickL;
import com.chinatelecom.dialoglibrary.widget.ActionSheetDialog;
import com.rrvow.rrchain.R;
import com.rrvow.rrchain.common.widget.BaseDialog;


/**
 * @author by shy on 2017/9/8 0008.
 */
public final class DialogUtil {

    public static BaseDialog createDialog(Context context, String title, String content, String[]
            btns, BaseDialog.OnClickListener... onClickListeners) {
        BaseDialog dialog = new BaseDialog(context, title, content, btns, onClickListeners);
        WindowManager wm = ((Activity) context).getWindowManager();
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        Point outSize = new Point();
        d.getSize(outSize);
        lp.width = (int) (outSize.x * 0.7);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static BaseDialog createDialogWithNoBtn(Context context, String title, String content) {
        BaseDialog dialog = new BaseDialog(context, title, content, null, new BaseDialog
                .OnClickListener[]{});
        WindowManager wm = ((Activity) context).getWindowManager();
        Display d = wm.getDefaultDisplay();
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.width = (int) (d.getWidth() * 0.7);
        dialog.show();
        return dialog;
    }

    public static BaseDialog createInputDialog(Context context, String title, String content,
                                               String inputHint, String[] btns,
                                               BaseDialog.OnClickListener... onClickListeners) {
        BaseDialog dialog = createDialog(context, title, content, btns, onClickListeners);
        dialog.setInputVisibility(View.VISIBLE);
        dialog.setInputHint(inputHint);
        return dialog;
    }

    public static void showActionSheetDialog(Context context, final String[] stringItems, final
    OnOperItemClickL callback) {
        final ActionSheetDialog dialog = new ActionSheetDialog(context, stringItems, null);
        dialog.itemTextColor(context.getResources().getColor(R.color.light_black))
                .setItemCancelTextSize_SP(16)
                .itemTextSize(18)
                .isTitleShow(false).show();
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();

                callback.onOperItemClick(parent, view, position, id);
            }
        });
    }


}
