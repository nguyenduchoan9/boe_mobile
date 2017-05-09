package com.nux.dhoan9.firstmvvm.utils.support;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 12/04/2017.
 */

public abstract class DiffCallBack<E> extends DiffUtil.Callback {
    protected List<E> oldList = new ArrayList<E>();
    protected List<E> newList = new ArrayList<E>();

    void setData(List<E> oldList, List<E> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }
}
