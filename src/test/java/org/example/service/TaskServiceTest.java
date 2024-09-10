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
        // Initializes mocks annotated with @Mock before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddTask() {
        // Arrange: Create a task and set up the mock to return it when saved
        Task task = new Task(null, "Title", "Description", LocalDate.now(), "Category");
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // Act: Save the task using the service
        Task savedTask = taskService.addTask(task);

        // Assert: Verify the saved task's title and that save was called once
        assertEquals(task.getTitle(), savedTask.getTitle());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void testDeleteTask() {
        // Act: Delete a task by ID
        taskService.deleteTask(1L);

        // Assert: Verify that deleteById was called once with the correct ID
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testGetTasks() {
        // Arrange: Create a list of tasks and set up the mock to return it
        List<Task> tasks = List.of(new Task(1L, "Title", "Description", LocalDate.now(), "Category"));
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act: Retrieve tasks using the service
        List<Task> result = taskService.getTasks();

        // Assert: Verify the number of tasks retrieved and that findAll was called once
        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateTask_TaskNotFound() {
        // Arrange: Set up the mock to return empty when a task ID is not found
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert: Expect a RuntimeException when updating a non-existent task
        Exception exception = assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(1L, new Task(1L, "Title", "Description", LocalDate.now(), "Category"));
        });

        // Verify the exception message
        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    public void testMarkAsCompleted_ExistingTaskId_MarksTaskAsCompleted() {
        // Arrange: Create a task, set it to PENDING, and set up the mock to return it
        Task existingTask = new Task(1L, "Title", "Description", LocalDate.now(), "Category");
        existingTask.setStatus(Task.TaskStatus.PENDING);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // Act: Mark the task as completed using the service
        Task result = taskService.markAsCompleted(1L);

        // Assert: Verify the task's status is COMPLETED and that findById and save were called once
        assertEquals(Task.TaskStatus.COMPLETED, result.getStatus());
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }
}