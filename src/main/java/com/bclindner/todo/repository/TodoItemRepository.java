package com.bclindner.todo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bclindner.todo.model.TodoItem;

public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
    TodoItem save(TodoItem todoItem);
    TodoItem findById(TodoItem item);
    List<TodoItem> findAll();
    void deleteById(TodoItem item);
}
