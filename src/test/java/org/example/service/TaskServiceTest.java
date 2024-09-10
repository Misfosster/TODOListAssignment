package org.example.service;

import org.example.model.Task;
import org.example.model.Task.TaskStatus;
import org.example.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTask() {
        Task task = new Task(null, "Title", "Description", LocalDate.now(), "Category");
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.addTask(task);

        assertEquals(task.getTitle(), savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testAddTask_ValidTask_ReturnsAddedTask() {
        Task task = new Task(null, "Title", "Description", LocalDate.now(), "Category");
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.addTask(task);

        assertEquals(task.getTitle(), savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteTask() {
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteTask_ExistingTaskId_DeletesTask() {
        taskService.deleteTask(1L);
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetTasks() {
        List<Task> tasks = List.of(new Task(1L, "Title", "Description", LocalDate.now(), "Category"));
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getTasks();

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testGetTasks_ReturnsAllTasks() {
        List<Task> tasks = List.of(
            new Task(1L, "Title 1", "Description 1", LocalDate.now(), "Category 1"),
            new Task(2L, "Title 2", "Description 2", LocalDate.now(), "Category 2")
        );
        when(taskRepository.findAll()).thenReturn(tasks);

        List<Task> result = taskService.getTasks();

        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateTask_TaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, new Task(1L, "Title", "Description", LocalDate.now(), "Category"));
        });

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    public void testUpdateTask_ExistingTask_ReturnsUpdatedTask() {
        Task existingTask = new Task(1L, "Title", "Description", LocalDate.now(), "Category");
        Task updatedTask = new Task(1L, "Updated Title", "Updated Description", LocalDate.now(), "Updated Category");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals(updatedTask.getTitle(), result.getTitle());
        assertEquals(updatedTask.getDescription(), result.getDescription());
        assertEquals(updatedTask.getCategory(), result.getCategory());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    public void testMarkAsCompleted_ExistingTaskId_MarksTaskAsCompleted() {
        // Given
        Task existingTask = new Task(1L, "Title", "Description", LocalDate.now(), "Category");
        existingTask.setStatus(Task.TaskStatus.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Task result = taskService.markAsCompleted(1L);

        // Then
        assertEquals(Task.TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }
}