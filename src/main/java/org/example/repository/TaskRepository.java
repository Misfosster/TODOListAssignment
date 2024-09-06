package org.example.repository;

import org.example.model.Task;
import org.example.repository.TaskRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}