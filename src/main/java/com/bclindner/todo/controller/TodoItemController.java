package com.bclindner.todo.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bclindner.todo.exception.BadResourceException;
import com.bclindner.todo.exception.ResourceExistsException;
import com.bclindner.todo.exception.ResourceNotFoundException;
import com.bclindner.todo.model.TodoItem;
import com.bclindner.todo.service.TodoItemService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * REST controller for TodoItems.
 */
@RestController
@RequestMapping("/todo-item")
@Tag(name="TodoItem API", description = "API for to-do list items.")
@SecurityRequirement(name = "OAuth2")
public class TodoItemController {
    
    @Autowired
    private TodoItemService service;

    @PostMapping()
    @Operation(
        summary = "Create TodoItem",
        description = "Create a to-do list item. Note that this will not perform updates - the API will return 400 if an ID is specified.",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "TodoItem creation was successful",
                headers = {
                    @Header(
                        name = "Location",
                        schema = @Schema(type = "string"),
                        description = "Link to the created TodoItem"
                    )
                }
            ),
            @ApiResponse(
                responseCode = "400",
                description = "TodoItem is not correctly formatted"
            )
        }
    )
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

    @GetMapping()
    @Operation(
        summary = "List TodoItems",
        description = "List all available TodoItems.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "204",
                description = "No TodoItems to show"
            )
        }
    )
    public ResponseEntity<Collection<TodoItem>> getAllTodos() {
        Iterable<TodoItem> todos = service.findAll();
        // convert to a collection so we can see the length
        ArrayList<TodoItem> todoList = new ArrayList<TodoItem>();
        todos.forEach(todoList::add);
        // REST convention: if the list is empty, return 204 instead of 200,
        // just as extra confirmation that yes, there are no TodoItems
        if (todoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(todoList);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get TodoItem by ID",
        description = "Get a TodoItem by its ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "No TodoItem with the specified ID exists"
            )
        }
    )
    public TodoItem getTodoById(@PathVariable Long id) throws ResourceNotFoundException {
        Optional<TodoItem> todoItem = service.getById(id);
        // if we got nothing, throw an exception
        if(!todoItem.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return todoItem.get();
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update TodoItem",
        description = "Update a TodoItem by its ID.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Success"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "TodoItem is malformatted"
            )
        }
    )
    public TodoItem updateTodo(@PathVariable Long id, @RequestBody TodoItem todoItem) throws BadResourceException, ResourceExistsException{
        // ensure the todoItem ID matches the path
        if (id != todoItem.id) {
            throw new BadResourceException();
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
    @Operation(
        summary = "Delete TodoItem",
        description = "Delete a TodoItem by its ID.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Deletion successful"
            )
        }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodo(@PathVariable Long id) {
        service.deleteById(id);
    }
    
}