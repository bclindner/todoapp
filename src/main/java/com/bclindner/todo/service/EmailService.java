package com.bclindner.todo.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bclindner.todo.model.TodoItem;

/**
 * Service for sending emails.
 * 
 * Currently only used for EmailJob, but should be extended if further email
 * send capabilities are required in the app.
 */
@Service
// Only initialize this if JavaMailSender is available for autowire
// (it won't be if the mail properties are not configured)
@ConditionalOnBean(JavaMailSender.class)
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private TodoItemService todoItemService;
    
    /**
     * Send the list of incomplete to-do items.
     */
    public void sendIncompleteTodoItems(String toEmail) throws MailException {
        // get the incomplete todoitems
        Iterable<TodoItem> todoItems = todoItemService.findIncomplete();
        // get all text from each into an ArrayList
        ArrayList<TodoItem> todoTexts = new ArrayList<TodoItem>();
        todoItems.forEach(todoTexts::add);
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toEmail);
        msg.setSubject("Incomplete To-Do Items");

        // if the list is empty, then send a simple "no to-do items" message
        if (todoTexts.isEmpty()) {
            msg.setText("No to-do items are incomplete - well done!");
        } else {
            // it would be best to have a proper Thymeleaf template or something
            // here to make this pretty but we're going for results here - let's
            // just build a string
            StringBuilder msgBuilder = new StringBuilder("You have incomplete to-do items:");
            for(TodoItem todoItem : todoItems) {
                msgBuilder.append("\n[ ] ");
                msgBuilder.append(todoItem.text);
            }
            msg.setText(msgBuilder.toString());
        }
        // send message
        mailSender.send(msg);
    }
    
}
