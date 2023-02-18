package com.bclindner.todo.repository;

import org.springframework.data.repository.CrudRepository;

import com.bclindner.todo.model.TodoItem;

/**
 * CrudRepository for TodoItems.
 */
public interface TodoItemRepository extends CrudRepository<TodoItem, Long> {
    /**
     * CrudRepository-handled method for getting TodoItems where completed = false.
     *
     * This could probably also be done via `@Query("SELECT * from todoitem WHERE completed = false")`.
     * @return
     */
    public Iterable<TodoItem> findByCompletedFalse();
}
