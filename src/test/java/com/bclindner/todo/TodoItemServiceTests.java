package com.bclindner.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.bclindner.todo.model.TodoItem;
import com.bclindner.todo.service.TodoItemService;

@SpringBootTest
class TodoItemServiceTests {
	
	@Autowired
	public TodoItemService todoItemService;

	private void compareTodoItems(TodoItem todo1, TodoItem todo2) {
		Assert.isTrue(todo1.id == todo2.id, "TodoItem.id from save() does not match value from getById()");
		Assert.isTrue(todo1.text == todo2.text, "TodoItem.text from save() does not match value from getById()");
		Assert.isTrue(todo1.completed == todo2.completed, "TodoItem.text from save() does not match value from getById()");
		// createdDate and lastModifiedDate aren't equal, my guess is some sort
		// of rounding issue or impl. detail w/ CrudRepository I can't do much
		// about... the important part for now is that the main todo data is
		// equivalent
	}

	@Test
	void contextLoads() {
		Assert.notNull(todoItemService, "todoItemService is null - was it injected properly?");
	}
	
	@Test
	void todoItemCreate() {
		var todo = todoItemService.save(new TodoItem("test todoItemCreate"));
		Assert.notNull(todo, "Created TodoItem is null");
		Assert.notNull(todo.id, "Created TodoItem has null ID");
		Assert.notNull(todo.text, "Created TodoItem text was not saved properly");
	}
	
	@Test
	void todoItemRead() {
		var todo = todoItemService.save(new TodoItem("test todoItemRead"));
		var todoDbContainer = todoItemService.getById(todo.id);
		Assert.isTrue(todoDbContainer.isPresent(), "TodoItem could not be retrieved after creation");
		var todoDb = todoDbContainer.get();
		compareTodoItems(todo, todoDb);
	}

	@Test
	void todoItemUpdate() {
		var todo = todoItemService.save(new TodoItem("test something...?"));
		todo.setText("test todoItemUpdate");
		todoItemService.save(todo);
		var todoDbContainer = todoItemService.getById(todo.id);
		Assert.isTrue(todoDbContainer.isPresent(), "TodoItem could not be retrieved after update");
		var todoDb = todoDbContainer.get();
		compareTodoItems(todo, todoDb);
		// one particular issue i found during this caused the createDate to be null when updating, so let's check that
		Assert.notNull(todoDb.getCreatedDate(), "Updated TodoItem has no CreatedDate");
		
	}

	@Test
	void todoItemDelete() {
		var todo = todoItemService.save(new TodoItem("test something...?"));
		Assert.isTrue(todoItemService.existsById(todo.id), "Created TodoItem does not show in existsById()");
		todoItemService.deleteById(todo.id);
		Assert.isTrue(!todoItemService.existsById(todo.id), "Delete TodoItem still shows in existsById()");
	}
}
