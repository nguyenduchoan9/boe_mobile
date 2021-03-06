package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.databinding.MenuHorizontalItemBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.Constant;
import com.nux.dhoan9.firstmvvm.view.activity.DishesByCategoryActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCateListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCategoriesViewModel;

import javax.inject.Inject;

/**
 * Created by hoang on 09/05/2017.
 */
public class MenuCategoryListAdapter extends RecyclerView.Adapter<MenuCategoryListAdapter.MenuCategoryViewHolder> {
    private final MenuCateListViewModel menuCateListViewModel;
    private final LayoutInflater inflater;
    private Context mContext;
    private String keySearch = "";

    public MenuCategoryListAdapter(MenuCateListViewModel menuCateListViewModel, Context context) {
        this.menuCateListViewModel = menuCateListViewModel;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MenuCategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MenuHorizontalItemBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.menu_horizontal_item, parent, false);
        return new MenuCategoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MenuCategoryViewHolder holder, int position) {
        Log.e("category", String.valueOf(position));
        holder.bindingData(menuCateListViewModel.getPosition(position));
    }

    @Override
    public int getItemCount() {
        if(null == menuCateListViewModel){
            return 0;
        }
        return menuCateListViewModel.getSize();
    }

    public class MenuCategoryViewHolder extends RecyclerView.ViewHolder {
        final MenuHorizontalItemBinding binding;
        RecyclerView rvDishes;

        public MenuCategoryViewHolder(MenuHorizontalItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindingData(MenuCategoriesViewModel viewModel) {
            initView(viewModel);
        }

        private void initView(MenuCategoriesViewModel viewModel) {
            binding.tvMore.setOnClickListener(v -> {
                Intent i = DishesByCategoryActivity.newInstance(mContext);
                i.putExtra(Constant.KEY_ID_CATEGORY, viewModel.category.getId());
                i.putExtra(Constant.KEY_SEARCH, keySearch);
                mContext.startActivity(i);
            });
            LinearLayoutManager manager =
                    new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            rvDishes = binding.rvDishes;

            DishListAdapter adapter = new DishListAdapter(viewModel.dishViewModels, mContext);
            adapter.setListener(new DishListAdapter.OrderHandleListener() {
                @Override
                public void onMaxOrderClick() {

                }

                @Override
                public void onOrderClick(boolean isMax, int dishId) {
                    if (null != mListener) mListener.onOrderClick(isMax, dishId);
                }
            });
            rvDishes.setAdapter(adapter);
            rvDishes.setLayoutManager(manager);

            binding.setViewModel(viewModel);
            binding.setListBinder(menuCateListViewModel);
        }
    }

    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    public String getKeySearch() {
        return keySearch;
    }

    public interface OrderHandleListener {
        void onOrderClick(boolean isMax, int dishId);
    }

    private OrderHandleListener mListener;

    public void setListener(OrderHandleListener mListener) {
        this.mListener = mListener;
    }
}
