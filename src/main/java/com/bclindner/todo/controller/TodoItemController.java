package com.bclindner.todo.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bclindner.todo.exception.BadResourceException;
import com.bclindner.todo.exception.ResourceExistsException;
import com.bclindner.todo.exception.ResourceNotFoundException;
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
     * Create a TodoItem.
     * @param todoItem
     * @return The created TodoItem.
     */
    @PostMapping("/")
    public ResponseEntity<TodoItem> createTodo(@RequestBody TodoItem todoItem) throws ResourceExistsException {
        // ensure the todo item does not have a set ID
        //
        // this is technically OK in REST conventions, but it's generally
        // production practice in my experience to perform updates on the
        // resource's link directly via PUT, rather than via POST - doing
        // updates here could cause unexpected side effects as a result
        if (todoItem.getId() != null) {
            // if it does, this is not a creation, it is an update - throw an
            // exception
            throw new ResourceExistsException();
        }
        // save the todo item in the DB
        TodoItem newTodoItem = service.save(todoItem);
        // REST convention: take the new item's ID and generate a 201 response
        // with a Location header pointing to it
        // see also: https://stackoverflow.com/a/52024684
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(newTodoItem.getId())
            .toUri();
        return ResponseEntity
            .created(location)
            // adding the TodoItem as a body is largely optional - the Location
            // header is enough to make this a REST-compliant HTTP 201 response.
            // we do it here for convenience, and because we already have the
            // object anyways
            .body(newTodoItem);
    }

    /**
     * Returns all TodoItems.
     * 
     * At larger scales, it would be important to both paginate this and
     * implement more advanced querying, but we are skipping that for now.
     *
     * @return To-do item list.
     */
    @GetMapping("/")
    public Iterable<TodoItem> getAllTodos() {
        return service.findAll();
    }
    
    /**
     * Get a TodoItem by its ID.
     * @param id ID of the TodoItem.
     * @return TodoItem (if it exists).
     */
    @GetMapping("/{id}")
    public TodoItem getTodoById(@PathVariable Long id) {
        Optional<TodoItem> todoItem = service.getById(id);
        // if we got nothing, throw an exception
        if(!todoItem.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return todoItem.get();
    }

    /**
     * Update a to-do item by its ID.
     * @param id ID of the TodoItem.
     * @param todoItem Item to update.
     * @return Updated TodoItem.
     */
    @PutMapping("/{id}")
    public TodoItem updateTodo(@PathVariable Long id, @RequestBody TodoItem todoItem) {
        // ensure the todoItem ID matches the path
        if (id != todoItem.id) {
            throw new BadResourceException(
                String.format("Cannot update resource with ID {} with object using ID {}", id, todoItem.id)
            );
        }
        // ensure the ID exists
        if(!service.existsById(id)) {
            // if doesn't, this would be a creation - throw an exception
            throw new ResourceExistsException();
        }
        // run the update, return the updated TodoItem
        TodoItem newTodoItem = service.save(todoItem);
        return newTodoItem;
    }
    
    /**
     * Delete a to-do item with a given ID.
     * @param id ID of the TodoItem.
     */
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable Long id) {
        service.deleteById(id);
    }
    
}