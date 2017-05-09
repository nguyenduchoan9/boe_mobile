package com.nux.dhoan9.firstmvvm.viewmodel;

import android.support.annotation.NonNull;

import com.nux.dhoan9.firstmvvm.data.repo.TodoRepo;
import com.nux.dhoan9.firstmvvm.model.Todo;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;

/**
 * Created by hoang on 12/04/2017.
 */

public class TodoListViewModel {
    private final ListBinder<TodoViewModel> todoListBinder;
    private final TodoRepo todoRepo;
    private final List<TodoViewModel> todos = new ArrayList<>();
    private PublishSubject<Integer> scrollTo = PublishSubject.create();

    public TodoListViewModel(@NonNull ListBinder<TodoViewModel> todoListBinder,
                             @NonNull TodoRepo todoRepo) {
        this.todoListBinder = todoListBinder;
        this.todoRepo = todoRepo;
    }

    public Observable<Integer> scrollTo() {
        return scrollTo.observeOn(AndroidSchedulers.mainThread());
    }

    public ListBinder<TodoViewModel> getTodoListBinder() {
        return todoListBinder;
    }

    public List<TodoViewModel> getTodos() {
        return todos;
    }

    public void initialize() {
        todos.addAll(todoViewModels(todoRepo.getTodos()));
        todoListBinder.notifyDataChange(todos);
    }

    public void setCompleted(int pos, boolean completed) {
        TodoViewModel viewModel = todos.get(pos);
        if (viewModel.completed != completed) {
            viewModel = viewModel.setCompleted(completed);
            todoRepo.updateTodo(viewModel.toModel());
            todos.set(pos, viewModel);
            todoListBinder.notifyDataChange(todos);
        }
    }

    public void create(String title, String dueDate) {
        Todo todo = todoRepo.createTodo(title, dueDate);
        TodoViewModel viewModel = new TodoViewModel(todo);
        todos.add(0, viewModel);
        todoListBinder.notifyDataChange(todos);
        scrollTo.onNext(0);
    }

    public void deleteTodo(int pos) {
        TodoViewModel viewModel = todos.get(pos);
        todoRepo.deleteTodo(viewModel.toModel());
        todos.remove(pos);
        todoListBinder.notifyDataChange(todos);
    }

    private List<TodoViewModel> todoViewModels(List<Todo> todos) {
        List<TodoViewModel> viewModels = new ArrayList<>();
        for (Todo todo : todos) {
            viewModels.add(new TodoViewModel(todo));
        }

        return viewModels;
    }
}
