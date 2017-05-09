package com.nux.dhoan9.firstmvvm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nux.dhoan9.firstmvvm.databinding.ItemTodoBinding;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hoang on 12/04/2017.
 */
@ActivityScope
public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private final TodoListViewModel viewModel;
    private final LayoutInflater layoutInflater;
    @Inject
    public TodoListAdapter(TodoListViewModel viewModel, @ForActivity Context context) {
        this.viewModel = viewModel;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemTodoBinding itemTodoBinding = ItemTodoBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(itemTodoBinding);
    }

    private List<TodoViewModel> getTodos() {
        return viewModel.getTodos();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TodoViewModel todo = getTodos().get(position);
        holder.itemTodoBinding.setTodo(todo);
        holder.itemTodoBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return getTodos().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ItemTodoBinding itemTodoBinding;

        public ViewHolder(ItemTodoBinding itemTodoBinding) {
            super(itemTodoBinding.getRoot());
            this.itemTodoBinding = itemTodoBinding;
            setUpListeners();
        }

        private void setUpListeners() {
            itemTodoBinding.cbCompleted.setOnCheckedChangeListener((btnView, isChecked) ->
                    viewModel.setCompleted(getAdapterPosition(), isChecked));
            itemTodoBinding.tvRemove.setOnClickListener(v ->
                    viewModel.deleteTodo(getAdapterPosition()));
        }
    }
}
