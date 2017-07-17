package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.nux.dhoan9.firstmvvm.R;
import com.nux.dhoan9.firstmvvm.model.DishSugesstion;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hoang on 16/07/2017.
 */

public class DishSugesstionAdapter
        extends RecyclerView.Adapter<DishSugesstionAdapter.SuggestionViewHolder>
        implements Filterable {
    List<DishSugesstion> originalDishes;
    List<DishSugesstion> dishesFilter;
    LayoutInflater layoutInflater;

    public DishSugesstionAdapter(Context context) {
        originalDishes = new ArrayList<>();
        dishesFilter = new ArrayList<>();
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<DishSugesstion> data) {
        originalDishes = data;
        dishesFilter = data;
        notifyDataSetChanged();
    }

    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.layout_item_dish_suggestion, parent, false);
        return new SuggestionViewHolder(v);
    }

    private DishSugesstion getByPos(int pos) {
        return dishesFilter.get(pos);
    }

    @Override
    public void onBindViewHolder(SuggestionViewHolder holder, int position) {
        DishSugesstion dishSugesstion = getByPos(position);
        holder.tvDishName.setText(dishSugesstion.getDishName());
        if (null != listener) {
            holder.itemView.setOnClickListener(v -> {
                listener.onItemClick(dishSugesstion.getId(), dishSugesstion.getDishName());
            });
        }
    }

    @Override
    public int getItemCount() {
        return dishesFilter.size();
    }

    private DishSuggestionFilter filter;

    @Override
    public Filter getFilter() {
        if (null == filter) {
            return new DishSuggestionFilter();
        }
        return filter;
    }

    private class DishSuggestionFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence term) {
            FilterResults filterResults = new FilterResults();
            if (null == term || term.length() == 0) {
                filterResults.values = originalDishes;
                filterResults.count = originalDishes.size();
            } else {
                List<DishSugesstion> filterDish = new ArrayList<>();
                for (DishSugesstion dish : originalDishes) {
                    String lowerTerm = Normalizer.normalize(term.toString().toLowerCase(), Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    String lowerName = Normalizer.normalize(dish.getDishName().toLowerCase(), Normalizer.Form.NFD)
                            .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
                    Log.d("Hoang", "term: " + lowerName + " - name: " + lowerName);
                    if (lowerName.contains(lowerTerm)) {
                        filterDish.add(dish);
                    }
                }
                filterResults.values = filterDish;
                filterResults.count = filterDish.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dishesFilter = (ArrayList<DishSugesstion>) results.values;
            if (null != listener) {
                if (dishesFilter.size() == 0) {
                    listener.onFilterNoting();
                } else {
                    listener.onFilterSuccess();
                }
            }
            notifyDataSetChanged();
        }
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDishName)
        TextView tvDishName;

        public SuggestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface FilterResultListener {
        void onFilterSuccess();

        void onFilterNoting();

        void onItemClick(int dishId, String dishName);
    }

    private FilterResultListener listener;

    public void setListener(FilterResultListener listener) {
        this.listener = listener;
    }
}
