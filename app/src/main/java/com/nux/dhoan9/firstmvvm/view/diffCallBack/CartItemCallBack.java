package com.nux.dhoan9.firstmvvm.view.diffCallBack;

import com.nux.dhoan9.firstmvvm.utils.support.DiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.CartItemViewModel;
/**
 * Created by hoang on 12/05/2017.
 */

public class CartItemCallBack extends DiffCallBack<CartItemViewModel> {
    @Override
    public boolean areItemsTheSame(int oldPosition, int newPosition) {
        return oldList.get(oldPosition).id == newList.get(newPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CartItemViewModel oldCartItem = oldList.get(oldItemPosition);
        CartItemViewModel newCartItem = newList.get(newItemPosition);
        return oldCartItem.id == newCartItem.id
                && oldCartItem.name.equals(newCartItem.name)
                && oldCartItem.price == newCartItem.price
                && oldCartItem.image == newCartItem.image
                && oldCartItem.quantity == newCartItem.quantity;
    }
}
