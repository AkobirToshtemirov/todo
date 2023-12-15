package com.epam.todo;

import com.epam.todo.exception.TodoItemNotFoundException;
import com.epam.todo.model.TodoItem;
import com.epam.todo.repository.TodoItemRepository;
import com.epam.todo.service.TodoItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodoItemServiceTest {

    @Mock
    private TodoItemRepository todoItemRepository;

    @InjectMocks
    private TodoItemService todoItemService;

    @Test
    void testGetAllTodoItems() {
        List<TodoItem> mockTodoItems = new ArrayList<>();
        mockTodoItems.add(new TodoItem(1L, "Task 1", "Description 1"));
        mockTodoItems.add(new TodoItem(2L, "Task 2", "Description 2"));

        when(todoItemRepository.findAll()).thenReturn(mockTodoItems);

        List<TodoItem> result = todoItemService.getAllTodoItems();

        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
        assertEquals("Description 2", result.get(1).getDescription());
    }

    @Test
    void testGetTodoItemById_ExistingId() {
        TodoItem mockTodoItem = new TodoItem(1L, "Task 1", "Description 1");
        when(todoItemRepository.findById(1L)).thenReturn(Optional.of(mockTodoItem));

        TodoItem result = todoItemService.getTodoItemById(1L);

        assertEquals(mockTodoItem, result);
    }

    @Test
    void testGetTodoItemById_NonExistingId() {
        when(todoItemRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(TodoItemNotFoundException.class, () -> todoItemService.getTodoItemById(100L));
    }

    @Test
    void testCreateTodoItem() {
        TodoItem todoItemToCreate = new TodoItem();
        todoItemToCreate.setTitle("New Task");
        todoItemToCreate.setDescription("New Description");

        when(todoItemRepository.save(any(TodoItem.class))).thenReturn(todoItemToCreate);

        TodoItem createdTodoItem = todoItemService.createTodoItem(todoItemToCreate);

        assertEquals("New Task", createdTodoItem.getTitle());
        assertEquals("New Description", createdTodoItem.getDescription());
    }

    @Test
    void testUpdateTodoItem_NonExistingId() {
        when(todoItemRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(TodoItemNotFoundException.class, () -> todoItemService.updateTodoItem(100L, new TodoItem()));
    }

    @Test
    void testDeleteTodoItem_ExistingId() {
        doNothing().when(todoItemRepository).deleteById(1L);

        todoItemService.deleteTodoItem(1L);

        verify(todoItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTodoItem_NonExistingId() {
        doThrow(TodoItemNotFoundException.class).when(todoItemRepository).deleteById(100L);

        assertThrows(TodoItemNotFoundException.class, () -> todoItemService.deleteTodoItem(100L));
    }

}
