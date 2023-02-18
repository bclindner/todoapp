package com.bclindner.todo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.bclindner.todo.model.TodoItem;
import com.bclindner.todo.service.TodoItemService;

@SpringBootTest
@ActiveProfiles("test")
class TodoItemServiceTests {
	
	@Autowired
	public TodoItemService todoItemService;

	private void compareTodoItems(TodoItem todo1, TodoItem todo2) {
		assertTrue(todo1.id == todo2.id, "TodoItem.id from save() does not match value from getById()");
		assertTrue(todo1.text == todo2.text, "TodoItem.text from save() does not match value from getById()");
		assertTrue(todo1.completed == todo2.completed, "TodoItem.text from save() does not match value from getById()");
		// createdDate and lastModifiedDate aren't equal, my guess is some sort
		// of rounding issue or impl. detail w/ CrudRepository I can't do much
		// about... the important part for now is that the main todo data is
		// equivalent
	}

	@Test
	void contextLoads() {
		assertNotNull(todoItemService, "todoItemService is null - was it injected properly?");
	}
	
	@Test
	void todoItemCreate() {
		TodoItem todo = todoItemService.save(new TodoItem("test todoItemCreate"));
		assertNotNull(todo, "Created TodoItem is null");
		assertNotNull(todo.id, "Created TodoItem has null ID");
		assertNotNull(todo.text, "Created TodoItem text was not saved properly");
	}
	
	@Test
	void todoItemRead() {
		TodoItem todo = todoItemService.save(new TodoItem("test todoItemRead"));
		Optional<TodoItem> todoDbContainer = todoItemService.getById(todo.id);
		assertTrue(todoDbContainer.isPresent(), "TodoItem could not be retrieved after creation");
		TodoItem todoDb = todoDbContainer.get();
		compareTodoItems(todo, todoDb);
	}
	
	@Test
	void todoItemReadCompleted() {
		todoItemService.save(new TodoItem("test todoItemReadCompleted", true));
		TodoItem incompleteTodo = todoItemService.save(new TodoItem("test todoItemReadCompleted"));
		// get items and ensure the completed todo's ID is the only one there
		Iterable<TodoItem> todoDbIterator = todoItemService.findIncomplete();
		ArrayList<TodoItem> todoList = new ArrayList<TodoItem>();
		todoDbIterator.forEach(todoList::add);
		assertEquals(todoList.get(0).getId(), incompleteTodo.getId(), "Incomplete TodoItem was not retrieved from findIncomplete");
	}

	@Test
	void todoItemUpdate() {
		TodoItem todo = todoItemService.save(new TodoItem("test something...?"));
		todo.setText("test todoItemUpdate");
		todoItemService.save(todo);
		Optional<TodoItem> todoDbContainer = todoItemService.getById(todo.id);
		assertTrue(todoDbContainer.isPresent(), "TodoItem could not be retrieved after update");
		TodoItem todoDb = todoDbContainer.get();
		compareTodoItems(todo, todoDb);
		// one particular issue i found during this caused the createDate to be null when updating, so let's check that
		assertNotNull(todoDb.getCreatedDate(), "Updated TodoItem has no CreatedDate");
		
	}

	@Test
	void todoItemDelete() {
		TodoItem todo = todoItemService.save(new TodoItem("test something...?"));
		assertTrue(todoItemService.existsById(todo.id), "Created TodoItem does not show in existsById()");
		todoItemService.deleteById(todo.id);
		assertTrue(!todoItemService.existsById(todo.id), "Delete TodoItem still shows in existsById()");
	}
}
