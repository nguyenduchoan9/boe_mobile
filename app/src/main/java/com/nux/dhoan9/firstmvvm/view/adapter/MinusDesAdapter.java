package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.view.fragment.MinusDesDialog;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 06/08/2017.
 */

public class MinusDesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    int DES = 0;
    int BLANK_DES = 1;
    List<MinusDesDialog.MinusModel> data;
    Context mContext;
    LayoutInflater layoutInflater;
    int posDes = -1;

    public MinusDesAdapter(Context context) {
        data = new ArrayList<>();
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getSelectedPos() {
        return posDes;
    }

    public void setData(List<MinusDesDialog.MinusModel> models) {
        data = models;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if (DES == viewType) {
            v = layoutInflater.inflate(R.layout.layout_des_item, parent, false);
            return new DesViewHolder(v);
        }
        v = layoutInflater.inflate(R.layout.layout_blank_des_item, parent, false);
        return new BlankDesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        MinusDesDialog.MinusModel model = getItemModel(position);
        if (DES == viewType) {
            if (holder instanceof DesViewHolder) {
                ((DesViewHolder) holder).tvDes.setText(model.description);
                if (model.pos != posDes) {
                    unsetSelected(((DesViewHolder) holder).rlContainer, ((DesViewHolder) holder).tvDes);
                } else {
                    setSelected(((DesViewHolder) holder).rlContainer, ((DesViewHolder) holder).tvDes);
                }
                ((DesViewHolder) holder).itemView.setOnClickListener(v -> {
                    posDes = model.pos;
                    notifyDataSetChanged();
                });
            }
        } else {
            if (holder instanceof BlankDesViewHolder) {
                if (model.pos != posDes) {
                    unsetSelected(((BlankDesViewHolder) holder).rlContainer, ((BlankDesViewHolder) holder).tvDes);
                } else {
                    setSelected(((BlankDesViewHolder) holder).rlContainer, ((BlankDesViewHolder) holder).tvDes);
                }
                ((BlankDesViewHolder) holder).itemView.setOnClickListener(v -> {
                    posDes = model.pos;
                    notifyDataSetChanged();
                });
            }
        }
    }

    private void setSelected(RelativeLayout rl, TextView des) {
        rl.setBackgroundColor(selectedColorContainerDes());
        des.setTextColor(selectedColorTextDes());
    }

    private void unsetSelected(RelativeLayout rl, TextView des) {
        rl.setBackgroundColor(unselectedColorContainerDes());
        des.setTextColor(unselectColorTextDes());
    }

    private int selectedColorTextDes() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    private int selectedColorContainerDes() {
        return ContextCompat.getColor(mContext, R.color.register);
    }

    private int unselectColorTextDes() {
        return ContextCompat.getColor(mContext, R.color.primaryText);
    }

    private int unselectedColorContainerDes() {
        return ContextCompat.getColor(mContext, R.color.white);
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).description.length() > 0) {
            return DES;
        }
        return BLANK_DES;
    }

    private MinusDesDialog.MinusModel getItemModel(int pos) {
        return data.get(pos);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class DesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.rlContainer)
        RelativeLayout rlContainer;

        public DesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class BlankDesViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDes)
        TextView tvDes;
        @BindView(R.id.rlContainer)
        RelativeLayout rlContainer;

        public BlankDesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
