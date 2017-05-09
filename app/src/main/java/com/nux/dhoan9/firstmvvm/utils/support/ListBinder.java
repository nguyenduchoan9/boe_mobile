package com.nux.dhoan9.firstmvvm.utils.support;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

import static rx.android.MainThreadSubscription.verifyMainThread;


/**
 * Created by hoang on 12/04/2017.
 */

public class ListBinder<E> {
    private final DiffCallBack<E> diffCallBack;
    private List<E> current = new ArrayList<E>();
    private OnDataChangeListener listener;

    public ListBinder(DiffCallBack<E> diffCallBack) {
        this.diffCallBack = diffCallBack;
    }

    public interface OnDataChangeListener{
        void onChange(DiffUtil.DiffResult diffResult);
    }

    public void setListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

    public void notifyDataChange(List<E> data){
        verifyMainThread();
        DiffUtil.DiffResult diffResult = calculateDiff(data);
        if(null != listener){
            listener.onChange(diffResult);
        }
    }

    private DiffUtil.DiffResult calculateDiff(List<E> data){
        List<E> newList = new ArrayList<E>(data);
        diffCallBack.setData(current, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallBack);
        current = newList;
        return diffResult;
    }
}
