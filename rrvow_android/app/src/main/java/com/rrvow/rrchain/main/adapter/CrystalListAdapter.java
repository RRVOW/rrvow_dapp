package com.rrvow.rrchain.main.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rrvow.rrchain.R;
import com.rrvow.rrchain.main.model.CrystalBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author by lpc on 2017/9/6.
 */
public class CrystalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CrystalBean> mDatas;

    public CrystalListAdapter() {
        mDatas = new ArrayList<>();
    }

    public void setData(List<CrystalBean> funcList) {
        mDatas.clear();
        if (funcList != null) {
            mDatas.addAll(funcList);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        View itemView = mInflater.inflate(R.layout.item_crystal_list, parent, false);
        return new CrystalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            bindItemView(holder, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindItemView(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CrystalViewHolder) {
            CrystalViewHolder crystalViewHolder = (CrystalViewHolder) holder;
            CrystalBean crystalBean = mDatas.get(position);
            if (crystalBean != null) {
                String name = crystalBean.getName();
                if (name.length() == 11) {
                    name = name.substring(0, 3) + "****" + name.substring(name.length() - 4, name
                            .length());
                }
                crystalViewHolder.txtUserName.setText(name);
                crystalViewHolder.txtContribution.setText(crystalBean.getValue());
                crystalViewHolder.txtRanking.setText(String.valueOf(position + 1));
                if (getResId(position) != -1) {
                    crystalViewHolder.imgGold.setImageResource(getResId(position));
                    crystalViewHolder.imgGold.setVisibility(View.VISIBLE);
                    crystalViewHolder.txtRanking.setVisibility(View.GONE);
                } else {
                    crystalViewHolder.imgGold.setVisibility(View.GONE);
                    crystalViewHolder.txtRanking.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private int getResId(int position) {
        int resId = -1;
        switch (position) {
            case 0:
                resId = R.mipmap.ic_gold;
                break;
            case 1:
                resId = R.mipmap.ic_silver;
                break;
            case 2:
                resId = R.mipmap.ic_copper;
                break;
        }
        return resId;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class CrystalViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_gold)
        ImageView imgGold;

        @BindView(R.id.txt_user_name)
        TextView txtUserName;

        @BindView(R.id.txt_contribution)
        TextView txtContribution;

        @BindView(R.id.txt_ranking)
        TextView txtRanking;

        CrystalViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
