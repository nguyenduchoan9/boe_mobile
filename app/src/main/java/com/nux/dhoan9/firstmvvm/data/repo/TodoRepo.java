package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.model.Todo;

import java.util.List;

/**
 * Created by hoang on 12/04/2017.
 */

public interface TodoRepo {
    List<Todo> getTodos();
    void updateTodo(Todo todo);
    void deleteTodo(Todo todo);
    Todo createTodo(String title, String dueDate);
}
