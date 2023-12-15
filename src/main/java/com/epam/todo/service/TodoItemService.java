package com.epam.todo.service;

import com.epam.todo.exception.TodoItemNotFoundException;
import com.epam.todo.model.TodoItem;
import com.epam.todo.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoItemService {

    private final TodoItemRepository todoItemRepository;

    @Autowired
    public TodoItemService(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    public List<TodoItem> getAllTodoItems() {
        return todoItemRepository.findAll();
    }

    public TodoItem getTodoItemById(Long id) {
        Optional<TodoItem> todoItemOptional = todoItemRepository.findById(id);
        return todoItemOptional.orElseThrow(() -> new TodoItemNotFoundException("TodoItem not found with id: " + id));
    }

    public TodoItem createTodoItem(TodoItem todoItem) {
        return todoItemRepository.save(todoItem);
    }

    public TodoItem updateTodoItem(Long id, TodoItem updatedTodoItem) {
        TodoItem existingTodoItem = todoItemRepository.findById(id)
                .orElseThrow(() -> new TodoItemNotFoundException("TodoItem not found with id: " + id));

        existingTodoItem.setTitle(updatedTodoItem.getTitle());
        existingTodoItem.setDescription(updatedTodoItem.getDescription());

        return todoItemRepository.save(existingTodoItem);
    }

    public void deleteTodoItem(Long id) {
        todoItemRepository.deleteById(id);
    }
}
