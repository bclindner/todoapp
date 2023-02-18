package com.bclindner.todo.model;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
@EntityListeners(AuditingEntityListener.class)
public class TodoItem {
    /**
     * Default constructor for the TodoItem.
     */
    public TodoItem() {
    }
    /**
     * Create a basic TodoItem with no ID in an incomplete state.
     * This is helpful for creation in a TodoItemRepository.
     * @param text Text of the TodoItem.
     */
    public TodoItem(String text) {
        this.text = text;
    }
    /**
     * Database generated ID.
     * Managed by Spring Data.
     */
    @Id
    @GeneratedValue
    @Schema(example="1", description = "Database generated ID.")
    public Long id; 
    public Long getId() {
        return id;
    }
    /**
     * Text content of the to-do item.
     */
    @Column(length=256,nullable=false)
    @Schema(example="To-do item", description = "Text content of the to-do item.")
    public String text;
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    /**
     * Whether or not this TodoItem has been marked as completed.
     */
    @Schema(example="false", description = "Whether or not this TodoItem has been marked as completed.")
    public Boolean completed = false;
    public Boolean getCompleted() {
        return completed;
    }
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
    /**
     * Creation date.
     * Managed by Spring Data.
     */
    @CreatedDate
    // prevent this from being updated:
    // https://github.com/spring-projects/spring-data-rest/issues/1565
    @Column(updatable=false)
    @Schema(example="2023-02-18T17:37:30.282Z", description = "Date this TodoItem was created.")
    private Instant createdDate;
    public Instant getCreatedDate() {
        return createdDate;
    }
    /**
     * Last modified date.
     * Managed by Spring Data.
     */
    @LastModifiedDate
    @Schema(example="2023-02-18T17:37:30.282Z", description = "Date this TodoItem was last changed.")
    public Instant lastModifiedDate;
    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((completed == null) ? 0 : completed.hashCode());
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TodoItem other = (TodoItem) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        if (completed == null) {
            if (other.completed != null)
                return false;
        } else if (!completed.equals(other.completed))
            return false;
        if (createdDate == null) {
            if (other.createdDate != null)
                return false;
        } else if (!createdDate.equals(other.createdDate))
            return false;
        if (lastModifiedDate == null) {
            if (other.lastModifiedDate != null)
                return false;
        } else if (!lastModifiedDate.equals(other.lastModifiedDate))
            return false;
        return true;
    }
}
