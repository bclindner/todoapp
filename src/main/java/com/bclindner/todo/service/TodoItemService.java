package com.bclindner.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bclindner.todo.model.TodoItem;
import com.bclindner.todo.repository.TodoItemRepository;

@Service
public class TodoItemService {
    @Autowired
    private TodoItemRepository repository;
    
    /**
     * Save a TodoItem to the database.
     * @param todoItem
     * @return The saved to-do item.
     */
    public TodoItem save(TodoItem todoItem) {
        return repository.save(todoItem);
    }
    
    /**
     * Get a TodoItem by its ID.
     * @param id ID of the TodoItem.
     * @return The to-do item in question, or `null`.
     */
    public Optional<TodoItem> getById(Long id) {
        return repository.findById(id);
    }
    
    /**
     * Return all to-do list items in the database.
     * @return A list of to-do items.
     */
    public List<TodoItem> findAll() {
        return repository.findAll();
    }
    
    /**
     * Delete a TodoItem.
     * @param id ID of the TodoItem.
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
