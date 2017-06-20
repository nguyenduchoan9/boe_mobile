package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.nux.dhoan9.firstmvvm.databinding.LayoutItemHistoryBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.HistoryListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.HistoryViewModel;
import javax.inject.Inject;

/**
 * Created by hoang on 13/06/2017.
 */
@ActivityScope
public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final LayoutInflater layoutInflater;
    HistoryListViewModel viewModel;

    @Inject
    public HistoryAdapter(@ForActivity Context context, HistoryListViewModel viewModel) {
        this.layoutInflater = LayoutInflater.from(context);
        this.viewModel = viewModel;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutItemHistoryBinding binding =
                LayoutItemHistoryBinding.inflate(layoutInflater, parent, false);
        return new HistoryViewHolder(binding);
    }

    private HistoryViewModel getItemPos(int pos) {
        return viewModel.getCartItems().get(pos);
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        HistoryViewModel viewModel = getItemPos(position);
        holder.binding.setVm(viewModel);
    }

    @Override
    public int getItemCount() {
        return viewModel.getCartItems().size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        LayoutItemHistoryBinding binding;

        public HistoryViewHolder(LayoutItemHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setupListener();
        }

        private void setupListener() {
            itemView.setOnClickListener(v -> {
                HistoryViewModel viewModel = getItemPos(getAdapterPosition());
                listener.onItemClick(viewModel.listOrderId);
            });
        }
    }

    public interface HistoryAdapterListener {
        void onItemClick(String orderListId);
    }

    private HistoryAdapterListener listener;

    public void setLinstener(HistoryAdapterListener listener) {
        this.listener = listener;
    }
}
