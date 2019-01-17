package com.amydegregorio.mappers.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.amydegregorio.mappers.util.Priorities;
import com.amydegregorio.mappers.util.Statuses;
import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;

@Entity
public class Task {
   
   @Id
   @GeneratedValue(strategy=GenerationType.AUTO)
   @JMap
   private Long id;
   @JMap
   private String description;
   @JMap
   private LocalDate startDate;
   @JMap
   private LocalDate completionDate;
   @JMap
   private Priorities priority;
   @JMap
   private Statuses status;
   
   public Long getId() {
      return id;
   }
   
   public void setId(Long id) {
      this.id = id;
   }
   
   public String getDescription() {
      return description;
   }
   
   public void setDescription(String description) {
      this.description = description;
   }
   
   public LocalDate getStartDate() {
      return startDate;
   }
   
   public void setStartDate(LocalDate startDate) {
      this.startDate = startDate;
   }
   
   public LocalDate getCompletionDate() {
      return completionDate;
   }
   
   public void setCompletionDate(LocalDate completionDate) {
      this.completionDate = completionDate;
   }
   
   public Priorities getPriority() {
      return priority;
   }
   
   public void setPriority(Priorities priority) {
      this.priority = priority;
   }
   
   public Statuses getStatus() {
      return status;
   }
   
   public void setStatus(Statuses status) {
      this.status = status;
   }

   @Override
   public String toString() {
      return "Task [id=" + id + ", description=" + description + ", startDate=" + startDate + ", completionDate="
               + completionDate + ", priority=" + priority + ", status=" + status + "]";
   }
   
   @JMapConversion(from = {"priority"}, to = {"priority"}) 
   public Priorities convertPriority(String priority){
      switch(priority) {
         case "HIGH":
            return Priorities.HIGH;
         case "MEDIUM":
            return Priorities.MEDIUM;
         case "LOW":
            return Priorities.LOW;
         case "URGENT":
            return Priorities.URGENT;
         default:
            return null;
      }
   }
   
   @JMapConversion(from = {"status"}, to = {"status"})
   public Statuses convertStatus(String status) {
      switch(status) {
         case "NOT_STARTED":
            return Statuses.NOT_STARTED;
         case "IN_PROGRESS":
            return Statuses.IN_PROGRESS;
         case "COMPLETE":
            return Statuses.COMPLETE;
         default:
            return null;
      }
   }

}
