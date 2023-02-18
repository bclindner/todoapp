package com.bclindner.todo.model;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    /**
     * Database generated ID.
     * Managed by Spring Data.
     */
    @Id
    @GeneratedValue
    public Long id; 
    public Long getId() {
        return id;
    }
    /**
     * Text content of the to-do item.
     * In a more complicated model, this would have a "description" field to go
     * along with it.
     */
    public String title;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Whether or not this TodoItem has been marked as completed.
     */
    public Boolean completed;
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
    public String createdDate;
    public String getCreatedDate() {
        return createdDate;
    }
    /**
     * Last modified date.
     * Managed by Spring Data.
     */
    @LastModifiedDate
    public String lastModifiedDate;
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }
    /**
     * Create a basic TodoItem with no ID in an incomplete state.
     * This is helpful for creation in a TodoItemRepository.
     * @param title Title of the TodoItem.
     */
    public TodoItem(String title) {
        this.title = title;
    }
    /**
     * Auto-generated hashCode function.
     * Re-generate if the model is changed.
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
        result = prime * result + ((completed == null) ? 0 : completed.hashCode());
        result = prime * result + ((createdDate == null) ? 0 : createdDate.hashCode());
        result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
        return result;
    }
    /**
     * Auto-generated equality function.
     * Re-generate if the model is changed.
     */
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
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
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
