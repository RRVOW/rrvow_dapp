package com.rrvow.rrchain.common.widget;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;

import com.rrvow.rrchain.R;


/**
 * @author by lpc on 2018/1/31.
 */
public class RecyclerViewDivider extends DividerItemDecoration {
    /**
     * @param context Current context, it will be used to access resources.
     */
    public RecyclerViewDivider(Context context) {
        super(context, VERTICAL);
        setDrawable(context.getResources().getDrawable(R.drawable.list_divider));
    }
}
