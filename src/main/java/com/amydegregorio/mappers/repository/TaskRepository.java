package com.amydegregorio.mappers.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.amydegregorio.mappers.domain.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
      
}
