package com.nux.dhoan9.firstmvvm.viewmodel;

import android.content.res.Resources;
import android.view.View;

import com.nux.dhoan9.firstmvvm.model.Todo;
import com.nux.dhoan9.firstmvvm.utils.ThreadScheduler;

/**
 * Created by hoang on 12/04/2017.
 */

public class TodoViewModel {
    public final long id;
    public final String title;
    public final String dueDate;
    public final boolean completed;

    public TodoViewModel(Todo todo) {
        this.id = todo.id;
        this.title = todo.title;
        this.dueDate = todo.dueDate;
        this.completed = todo.completed;
    }

    private TodoViewModel(TodoViewModel todoViewModel, boolean completed) {
        this.id = todoViewModel.id;
        this.title = todoViewModel.title;
        this.dueDate = todoViewModel.dueDate;
        this.completed = completed;
    }

    public TodoViewModel setCompleted(boolean completed) {
        return new TodoViewModel(this, completed);
    }

    public int removeVisibility() {
        return completed ? View.VISIBLE : View.GONE;
    }

    public Todo toModel() {
        return new Todo(id, title, dueDate, completed);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TodoViewModel) {
            TodoViewModel other = (TodoViewModel) obj;
            return id == other.id;
        }
        return super.equals(obj);
    }
}
