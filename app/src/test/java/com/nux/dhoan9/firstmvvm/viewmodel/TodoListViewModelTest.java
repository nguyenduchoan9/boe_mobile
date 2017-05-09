package com.nux.dhoan9.firstmvvm.viewmodel;

import com.nux.dhoan9.firstmvvm.data.repo.TodoRepo;
import com.nux.dhoan9.firstmvvm.model.Todo;
import com.nux.dhoan9.firstmvvm.utils.support.ListBinder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

//import io.reactivex.observers.TestObserver;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by hoang on 12/04/2017.
 */
public class TodoListViewModelTest {
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private TodoListViewModel todoListViewModel;
//    private TestObserver<Integer> testScrollTo = TestObserver.create();

    @Mock
    ListBinder<TodoViewModel> listBinder;
    @Mock
    TodoRepo todoRepo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        todoRepo = new TodoRepoImpl();
        todoListViewModel = new TodoListViewModel(listBinder, todoRepo);
        when(todoRepo.getTodos()).thenReturn(mockTodos());
    }

    private List<Todo> mockTodos() {
        List<Todo> todos = new ArrayList<>();
        todos.add(new Todo(1, "title 1", "date 1", true));
        todos.add(new Todo(2, "title 2", "date 2", false));
        todos.add(new Todo(3, "title 3", "date 3", false));
        todos.add(new Todo(4, "title 4", "date 4", true));

        return todos;
    }

    @Test
    public void testInitialize() throws Exception {
        todoListViewModel.initialize();
        assertEquals(1, todoListViewModel.getTodos().get(0).id);
        assertEquals(2, todoListViewModel.getTodos().get(1).id);
        assertEquals(3, todoListViewModel.getTodos().get(2).id);
        assertEquals(4, todoListViewModel.getTodos().get(3).id);

        verify(listBinder).notifyDataChange(todoListViewModel.getTodos());
    }

    @Test
    public void testCreateTodo() throws Exception {
        when(todoRepo.createTodo(TITLE, DATE)).thenReturn(new Todo(100, TITLE, DATE, false));
        todoListViewModel.initialize();
        todoListViewModel.create(TITLE, DATE);
        assertEquals(TITLE, todoListViewModel.getTodos().get(0).title);
        assertEquals(DATE, todoListViewModel.getTodos().get(0).dueDate);
        assertEquals(100, todoListViewModel.getTodos().get(0).id);
        assertFalse(todoListViewModel.getTodos().get(0).completed);
        verify(listBinder, times(2)).notifyDataChange(todoListViewModel.getTodos());
    }

    @Test
    public void testCreateTodoScrollToTop() throws Exception {
//        todoListViewModel.scrollTo().subscribe(testScrollTo);
        when(todoRepo.createTodo(TITLE, DATE)).thenReturn(new Todo(100, TITLE, DATE, false));
        todoListViewModel.initialize();
        todoListViewModel.create(TITLE, DATE);
//        testScrollTo.assertValue(0);
        verify(listBinder, times(2)).notifyDataChange(todoListViewModel.getTodos());
    }

    @Test
    public void testSetCompletedViewModel() throws Exception {
        todoListViewModel.initialize();
        todoListViewModel.setCompleted(1, true);

        assertTrue(todoListViewModel.getTodos().get(1).completed);
        assertEquals(2, todoListViewModel.getTodos().get(1).id);

        verify(listBinder, times(2)).notifyDataChange(todoListViewModel.getTodos());
    }

    @Test
    public void testSetSameCompletedViewModel() throws Exception {
        todoListViewModel.initialize();
        todoListViewModel.setCompleted(1, false);
        assertFalse(todoListViewModel.getTodos().get(1).completed);
        verify(listBinder, times(1)).notifyDataChange(todoListViewModel.getTodos());
    }

    @Test
    public void testSetCompletedModel() throws Exception {
        todoListViewModel.initialize();
        todoListViewModel.setCompleted(1, true);
        ArgumentCaptor<Todo> argument = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepo).updateTodo(argument.capture());
        assertTrue(argument.getValue().completed);
        assertEquals(2, argument.getValue().id);
    }

    @Test
    public void testSetSameCompletedModel() throws Exception {
        todoListViewModel.initialize();
        todoListViewModel.setCompleted(1, false);
        verify(todoRepo, never()).updateTodo(any(Todo.class));
    }

    @Test
    public void testDeleteViewModel() throws Exception {
        todoListViewModel.initialize();
        todoListViewModel.deleteTodo(0);
        assertEquals(3, todoListViewModel.getTodos().size());
        assertEquals(2, todoListViewModel.getTodos().get(0).id);
        assertEquals(3, todoListViewModel.getTodos().get(1).id);
        assertEquals(4, todoListViewModel.getTodos().get(2).id);
        verify(listBinder, times(2)).notifyDataChange(todoListViewModel.getTodos());
    }

    @Test
    public void testDeleteModel() throws Exception {
        todoListViewModel.initialize();
        todoListViewModel.deleteTodo(0);
        ArgumentCaptor<Todo> argument = ArgumentCaptor.forClass(Todo.class);
        verify(todoRepo).deleteTodo(argument.capture());
        assertEquals(1, argument.getValue().id);
    }

    @After
    public void tearDown() throws Exception {
        todoListViewModel = null;
    }
}