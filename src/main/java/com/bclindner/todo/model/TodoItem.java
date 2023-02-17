package com.bclindner.todo.model;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

/**
 * Entity class for TodoItems.
 * 
 * Primitive - a full application might have description, start/due/complete
 * dates, and other fields to bring it in line with other to-do item standards,
 * i.e. iCalendar's VTODO spec.
 */
@Entity
public class TodoItem {
    @Id
    @GeneratedValue
    public Long id; 
    public String title;
    public Boolean completed;
    @CreatedDate
    public String createdDate;
}
