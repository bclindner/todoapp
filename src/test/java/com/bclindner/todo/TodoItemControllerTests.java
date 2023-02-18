package com.bclindner.todo;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.bclindner.todo.model.TodoItem;
import com.bclindner.todo.service.TodoItemService;

/**
 * General MockMvc integration tests for the API.
 * 
 * Not actually a part of the deliverable, these are just to make sure the APIs
 * are up to my personal spec.
 */
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class TodoItemControllerTests {

    // Request tester
    @Autowired
    private MockMvc mvc;
    
    // Service (for adding/testing data)
    @Autowired
    private TodoItemService todoItemService;
    
    /**
     * Test the empty GET request.
     * @throws Exception
     */
    @Test
    public void testGetEmpty() throws Exception {
        mvc.perform(
            get("/todo-item/")
            .contentType("application/json"))
            .andExpect(status().isNoContent());
    }
    
    /**
     * Test that the /todo-item/ endpoint returns 404 properly if no trailing
     * slash is specified.
     *
     * I don't like this behavior personally - the trailing slash should be
     * neither here nor there when requesting a REST collection - but
     * apparently, for security reasons, Spring 6 deprecated the trailing slash
     * ignore as a default behavior as it caused issues w/ Spring Security
     * edgecases that led to vulns:
     *
     * https://github.com/spring-projects/spring-framework/issues/28552
     * 
     * In order to solve this problem, a custom PathPatternParser configuration
     * would need to be set up, but given the security reasons for this change,
     * I'm not sure that's a good idea.
     *
     * As such, we will abide by this recommendation (begrudgingly) and test it
     * here.
     * @throws Exception
     */
    @Test
    public void testTrailingSlashRequired() throws Exception {
        mvc.perform(
            get("/todo-item")
            .contentType("application/json"))
            .andExpect(status().is(404));
    }
    
    /**
     * Test creation of a TodoItem resource.
     * @throws Exception
     */
    @Test
    @DirtiesContext
    public void testCreate() throws Exception {
        mvc.perform(
            post("/todo-item/")
            .contentType("application/json")
            .accept("application/json")
            .content("{\"text\": \"To-do item\"}")
        )
        .andExpect(status().isCreated())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.text").value("To-do item"))
        .andExpect(jsonPath("$.completed").value(false))
        .andExpect(header().string("Location", Matchers.containsString("/1"))); // may want to match this better later
        // check service
        assertNotNull(todoItemService.getById(1L).get(), "TodoItem doesn't exist in service");
    }

    /**
     * Test access to a TodoItem resource.
     * @throws Exception
     */
    @Test
    @DirtiesContext
    public void testGet() throws Exception {
        // add to service
        TodoItem todoItem = todoItemService.save(new TodoItem("testGet"));
        mvc.perform(
            get("/todo-item/")
            .accept("application/json")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(todoItem.getId()));
    }
    
    
    
    /**
     * Test update of a TodoItem resource.
     * @throws Exception
     */
    @Test
    @DirtiesContext
    public void testUpdate() throws Exception {
        // add to service
        TodoItem todoItem = todoItemService.save(new TodoItem("testUpdate"));
        String todoIdString = todoItem.getId().toString();
        mvc.perform(
            put("/todo-item/" + todoIdString)
            .contentType("application/json")
            .accept("application/json")
            .content("{\"id\": " + todoIdString + ", \"text\": \"To-do item!\", \"completed\": true, \"createdDate\": null, \"lastModifiedDate\": null}")
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(todoItem.getId()))
        .andExpect(jsonPath("$.text").value("To-do item!"))
        .andExpect(jsonPath("$.completed").value(true));
        // check the service
        Optional<TodoItem> retrievedTodoItem = todoItemService.getById(todoItem.getId());
        assertTrue(retrievedTodoItem.isPresent(), "TodoItem doesn't exist in service");
        TodoItem retrievedTodoItemUnwrapped = retrievedTodoItem.get();
        assertEquals("To-do item!", retrievedTodoItemUnwrapped.getText(), "TodoItem was not properly updated");
    }
    
    /**
     * Test deletion of a TodoItem resource.
     * @throws Exception
     */
    @Test
    @DirtiesContext
    public void testDelete() throws Exception {
        // add to service
        TodoItem todoItem = todoItemService.save(new TodoItem("testDelete"));
        // run DELETE
        mvc.perform(
            delete("/todo-item/1")
        )
        .andExpect(status().isNoContent());
        // check the service
        Optional<TodoItem> retrievedTodoItem = todoItemService.getById(todoItem.getId());
        assertFalse(retrievedTodoItem.isPresent(), "TodoItem still exists in service after deletion");
    }
    
}
