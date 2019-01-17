package com.amydegregorio.mappers.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import com.amydegregorio.mappers.util.Priorities;
import com.amydegregorio.mappers.util.Statuses;
import com.googlecode.jmapper.annotations.JMap;
import com.googlecode.jmapper.annotations.JMapConversion;

public class TaskDto {
   @JMap
   private Long id;
   @NotNull
   @JMap
   private String description;
   @JMap
   private LocalDate startDate;
   @JMap
   private LocalDate completionDate;
   @JMap
   private String priority;
   @JMap
   private String status;
   
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
   
   public String getPriority() {
      return priority;
   }
   
   public void setPriority(String priority) {
      this.priority = priority;
   }
   
   public String getStatus() {
      return status;
   }
   
   public void setStatus(String status) {
      this.status = status;
   }

   @Override
   public String toString() {
      return "TaskDto [id=" + id + ", description=" + description + ", startDate=" + startDate + ", completionDate="
               + completionDate + ", priority=" + priority + ", status=" + status + "]";
   }
   
   @JMapConversion(from = {"priority"}, to = {"priority"}) 
   public String convertPriority(Priorities priority){
      switch(priority) {
         case HIGH:
            return "HIGH";
         case MEDIUM:
            return "MEDIUM";
         case LOW:
            return "LOW";
         case URGENT:
            return "URGENT";
         default:
            return "";
      }
   }
   
   @JMapConversion(from = {"status"}, to = {"status"})
   public String convertStatus(Statuses status) {
      switch(status) {
         case NOT_STARTED:
            return "NOT_STARTED";
         case IN_PROGRESS:
            return "IN_PROGRESS";
         case COMPLETE:
            return "COMPLETE";
         default:
            return "";
      }
   }
}
