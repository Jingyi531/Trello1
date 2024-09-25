package com0.trellobackend.controller;

import com0.trellobackend.dto.TaskDTO;
import com0.trellobackend.model.Task;
import com0.trellobackend.service.TaskService;
import org.springframework.beans.factory.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*", allowedHeaders = "*")

public class TaskController {
    @Autowired
    private TaskService taskService;

    // Create a new task
    @PostMapping("/create/{id}")
    public TaskDTO createTask(@RequestBody Task task, @PathVariable int id) {
        task.setDueDate(LocalDate.now().plusDays(1));
        task.setStatus("todo");
        Task createdTask = taskService.createTask(task, id);
        return new TaskDTO(task);
    }

    // Get all tasks for a board
    @GetMapping("/get/{boardId}")
    public List<TaskDTO> getTasksByrBoardId(@PathVariable int boardId) {
        List<Task> tasks = taskService.getTasks(boardId);
        List<TaskDTO> taskDTOs = new LinkedList<>();
        for (Task task: tasks) {
            taskDTOs.add(new TaskDTO(task));
        }
        return taskDTOs;
    }

    // Update task state
    @PutMapping("updateState/{taskId}")
    public TaskDTO updateTaskState(@PathVariable int taskId, @RequestParam String status) {
        Task updatedTask = taskService.updateStatus(taskId, status);
        return new TaskDTO(updatedTask);
    }

    // Assign user to task
    @PutMapping("assignUser/{taskId}")
    public TaskDTO assignUserToTask(@PathVariable int taskId, @RequestParam String email) {
        Task updatedTask = taskService.assignUserToTask(taskId, email);
        return new TaskDTO(updatedTask);
    }

    // Assign user to task
    @PutMapping("updateDueDate/{taskId}")
    public TaskDTO updateDueDate(@PathVariable int taskId, @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate newDate) {
        Task updatedTask = taskService.updateDueDate(taskId, newDate);
        return new TaskDTO(updatedTask);
    }

}
