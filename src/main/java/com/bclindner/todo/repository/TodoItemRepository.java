package com.bclindner.todo.repository;

import org.springframework.data.repository.CrudRepository;

import com.bclindner.todo.model.TodoItem;

/**
 * CrudRepository for TodoItems.
 */
public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
}
