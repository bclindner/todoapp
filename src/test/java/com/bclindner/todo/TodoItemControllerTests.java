package com.bclindner.todo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

/**
 * General MockMvc integration tests for the API.
 * 
 * Not actually a part of the deliverable, these are just to make sure the APIs
 * are up to my personal spec.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TodoItemControllerTests {
    @Autowired
    private MockMvc mvc;
    
    @Test
    public void testGet() throws Exception {
        mvc.perform(
            get("/todo-item/")
            .contentType("application/json"))
            .andExpect(status().isOk());
        // apparently, for security reasons, Spring 6 deprecated the trailing
        // slash ignore as it caused issues w/ Spring Security edgecases that
        // led to vulns, so that part is tested here
        mvc.perform(
            post("/todo-item/")
            .contentType("application/json"))
            .andExpect(status().is(400));

    }
}
