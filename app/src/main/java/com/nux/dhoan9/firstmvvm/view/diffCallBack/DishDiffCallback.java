package com.nux.dhoan9.firstmvvm.view.diffCallBack;

import com.nux.dhoan9.firstmvvm.utils.support.DiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.DishViewModel;
/**
 * Created by hoang on 08/05/2017.
 */

public class DishDiffCallback extends DiffCallBack<DishViewModel> {
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        DishViewModel oldObject = oldList.get(oldItemPosition);
        DishViewModel newObject = newList.get(newItemPosition);
        return oldObject.id == newObject.id
                && oldObject.name.equals(newObject.name)
                && oldObject.price == newObject.price
                && oldObject.image == newObject.image;
    }
}
