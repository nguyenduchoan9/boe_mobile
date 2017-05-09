package com.nux.dhoan9.firstmvvm.dependency.module;

import android.content.Context;

import com.nux.dhoan9.firstmvvm.data.repo.TodoRepo;
import com.nux.dhoan9.firstmvvm.dependency.scope.ActivityScope;
import com.nux.dhoan9.firstmvvm.dependency.scope.ForActivity;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;
import com.nux.dhoan9.firstmvvm.view.adapter.TodoListAdapter;
import com.nux.dhoan9.firstmvvm.view.diffCallBack.TodoDiffCallBack;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoListViewModel;
import com.nux.dhoan9.firstmvvm.viewmodel.TodoViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hoang on 12/04/2017.
 */

@Module
public class TodoModule {
    @Provides
    public TodoDiffCallBack provideTodoDiffCallBack() {
        return new TodoDiffCallBack();
    }

    @Provides
    public ListBinder<TodoViewModel> provideTodoListBinder(TodoDiffCallBack todoDiffCallBack) {
        return new ListBinder<>(todoDiffCallBack);
    }

    @Provides
    @ActivityScope
    public TodoListViewModel provideTodoListViewModel(ListBinder<TodoViewModel> todoListBinder, TodoRepo todoRepo){
        return new TodoListViewModel(todoListBinder, todoRepo);
    }

    @Provides
    @ActivityScope
    public TodoListAdapter provideTodoListAdapter(TodoListViewModel todoListViewModel, @ForActivity Context context){
        return new TodoListAdapter(todoListViewModel, context);
    }
}
