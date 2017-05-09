package com.nux.dhoan9.firstmvvm.data.repo;

import com.nux.dhoan9.firstmvvm.model.Todo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoang on 12/04/2017.
 */

public class TodoRepoImpl implements TodoRepo {

    private List<Todo> todos = new ArrayList<>();

    public TodoRepoImpl() {
        for (int i = 0; i < 40; i++) {
            todos.add(new Todo(i, "Task" + (i + 1), "0" + (i + 1) + "/12/2016", i % 4 == 0));
        }
    }

    @Override
    public List<Todo> getTodos() {
        return todos;
    }

    @Override
    public void updateTodo(Todo todo) {
        int index = indexOf(todo);
        if (-1 != index) {
            todos.set(index, todo);
        }
    }

    @Override
    public void deleteTodo(Todo todo) {
        todos.remove(todo);
    }

    @Override
    public Todo createTodo(String title, String dueDate) {
        Todo todo = new Todo(nextId(), title, dueDate, false);
        todos.add(0, todo);
        return null;
    }

    private int indexOf(Todo todo) {
        return todos.indexOf(todo);
    }

    private long nextId() {
        return todos.get(todos.size() - 1).id + 1;
    }
}
