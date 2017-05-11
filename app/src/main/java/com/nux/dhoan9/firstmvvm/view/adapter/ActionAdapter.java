package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.ActionItemLayoutBinding;
import com.nux.dhoan9.firstmvvm.model.ActionMenuHorizontal;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.CartActivity;
import com.nux.dhoan9.firstmvvm.view.activity.HistoryActivity;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hoang on 10/05/2017.
 */

public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {
    private final Context mContext;
    private final LayoutInflater inflater;
    private List<ActionMenuHorizontal> actionMenuHorizontals;

    public ActionAdapter(Context mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
        actionMenuHorizontals = new ArrayList<>();
    }

    @Override
    public ActionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ActionItemLayoutBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.action_item_layout, parent, false);
        return new ActionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ActionViewHolder holder, int position) {
        ActionMenuHorizontal actionMenuHorizontal = actionMenuHorizontals.get(position);
        holder.binding.setActionTitle(actionMenuHorizontal.getTitle());
        if (Constant.MENU_TYPE_CART == actionMenuHorizontal.getType()) {
            mContext.startActivity(CartActivity.newInstance(mContext));
        } else if (Constant.MENU_TYPE_HISTORY == actionMenuHorizontal.getType()) {
            mContext.startActivity(HistoryActivity.newInstance(mContext));
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return actionMenuHorizontals.size();
    }

    public class ActionViewHolder extends RecyclerView.ViewHolder {
        final ActionItemLayoutBinding binding;

        public ActionViewHolder(ActionItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            initView();
        }

        private void initView() {
        }
    }

    public void initialize(List<ActionMenuHorizontal> list) {
        actionMenuHorizontals.addAll(list);
        notifyDataSetChanged();
    }
}
