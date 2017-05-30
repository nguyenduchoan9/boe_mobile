package com.nux.dhoan9.firstmvvm.view.diffCallBack;

import com.nux.dhoan9.firstmvvm.utils.support.DiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.MenuCategoriesViewModel;

/**
 * Created by hoang on 30/05/2017.
 */

public class MenuDiffCallback extends DiffCallBack<MenuCategoriesViewModel>{

    public MenuDiffCallback(){

    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getCategory().getId()
                == newList.get(newItemPosition).getCategory().getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getCategory().getId()
                == newList.get(newItemPosition).getCategory().getId()
                && oldList.get(oldItemPosition).getCategory().getName()
                .equals(newList.get(newItemPosition).getCategory().getName());
    }

}
