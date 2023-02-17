package com.bclindner.todo.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bclindner.todo.model.TodoItem;
import com.bclindner.todo.service.TodoItemService;

/**
 * REST controller for TodoItems.
 */
@RestController
@RequestMapping("/todo-item")
public class TodoItemController {
    
    @Autowired
    private TodoItemService service;

    /**
     * Returns all TodoItems.
     * 
     * At larger scales, it would be important to both paginate this and
     * implement more advanced querying, but we are skipping that for now
     * @return To-do item list.
     */
    @GetMapping("")
    public Iterable<TodoItem> getAllTodos() {
        return service.findAll();
    }

    @PostMapping("")
    public TodoItem createTodo(TodoItem todoItem) {
        return service.save(todoItem);
    }
    
    @GetMapping("/{id}")
    public Optional<TodoItem> getTodoById(@PathVariable Long id) {
        return service.getById(id);
    }
}
