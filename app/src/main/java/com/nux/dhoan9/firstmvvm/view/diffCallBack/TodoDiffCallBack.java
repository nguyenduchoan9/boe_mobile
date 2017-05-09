package com.nux.dhoan9.firstmvvm.view.diffCallBack;

import com.nux.dhoan9.firstmvvm.utils.support.DiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoViewModel;

/**
 * Created by hoang on 12/04/2017.
 */

public class TodoDiffCallBack extends DiffCallBack<TodoViewModel> {

    public TodoDiffCallBack() {
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).id == newList.get(newItemPosition).id;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).title.equals(newList.get(newItemPosition).title)
                && oldList.get(oldItemPosition).dueDate.equals(newList.get(newItemPosition).dueDate)
                && oldList.get(oldItemPosition).completed == newList.get(newItemPosition).completed;
    }
}
