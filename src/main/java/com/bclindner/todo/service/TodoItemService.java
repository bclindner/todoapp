package com.bclindner.todo.service;

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
     * This can be used for both creation and updates.
     * @param todoItem
     * @return The saved to-do item.
     */
    public TodoItem save(TodoItem todoItem) {
        return repository.save(todoItem);
    }
    
    /**
     * Check to see if a particular ID is already mapped to a TodoItem.
     * @param id
     * @return true if the TodoItem exists, false otherwise.
     */
    public boolean existsById(Long id) {
        return repository.existsById(id);
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
     * Get TodoItems that are not marked complete.
     * @return
     */
    public Iterable<TodoItem> findIncomplete() {
        return repository.findByCompletedFalse();
    }
    
    /**
     * Return all to-do list items in the database.
     * @return An iterator with all to-do items.
     */
    public Iterable<TodoItem> findAll() {
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
