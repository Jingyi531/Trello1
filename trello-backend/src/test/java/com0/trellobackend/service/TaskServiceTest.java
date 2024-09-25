package com0.trellobackend.service;

import com0.trellobackend.model.*;
import com0.trellobackend.repository.*;


import com0.trellobackend.model.Board;
import com0.trellobackend.model.Task;
import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.BoardRepository;
import com0.trellobackend.repository.TaskRepository;
import com0.trellobackend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTask_validTask(){
        // Arrange
        int boardId = 1;
        Board board = new Board();
        board.setId(boardId);

        Task task = new Task("A TasK");
        task.setTaskId(1);
        task.setDueDate(LocalDate.now());

        when(taskRepository.save(task)).thenReturn(task);
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // Act
        Task result = taskService.createTask(task, boardId);

        // Assertion
        assertEquals(result, task);
        verify(taskRepository).save(task);

    }


    @Test
    public void testCreateTask_NonExistingBoardId(){
        //Arrange
        int boardId = 1;

        Task task=new Task("ATask");
        task.setTaskId(1);
        task.setDueDate(LocalDate.now());
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(null));

        //Act
        ResponseStatusException exception=assertThrows(
                ResponseStatusException.class,
                ()->{taskService.createTask(task,boardId);}
        );

        // Assertion
        assertEquals("BoardId dose not exist",exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));
    }


    @Test
    public void testCreateTask_WithoutTaskName(){
        //Arrange
        int boardId = 1;
        Board board = new Board();
        board.setId(boardId);

        Task task=new Task();
        task.setTaskId(1);
        task.setDueDate(LocalDate.now());
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        //Act
        ResponseStatusException exception=assertThrows(
                ResponseStatusException.class,
                ()->{taskService.createTask(task,boardId);}
        );

        // Assertion
        assertEquals("Task has no name", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));
    }




    @Test
    public void testCreateTask_InvalidDueDate(){
        // Arrange
        int boardId = 1;
        Board board = new Board();
        board.setId(boardId);

        Task task=new Task("A Task");
        task.setTaskId(1);
        task.setDueDate(LocalDate.now().minusDays(1L));
        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        //Act
        ResponseStatusException exception=assertThrows(
                ResponseStatusException.class,
                ()->{taskService.createTask(task,boardId);}
        );

        // Assertion
        assertEquals("Invalid due date", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void testGetTask_ValidBoardId(){
        // Arrange
        int boardId = 1;
        Board board = new Board();
        board.setId(boardId);

        Task task1=new Task("A Task");
        task1.setTaskId(1);
        task1.setDueDate(LocalDate.now());

        Task task2=new Task("A Task");
        task2.setTaskId(1);
        task2.setDueDate(LocalDate.now());

        List<Task> tasks = new ArrayList<>();
        tasks.add(task1);
        tasks.add(task2);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(taskRepository.findByBoardId(boardId)).thenReturn(tasks);

        //Act
        List<Task> result = taskService.getTasks(boardId);

        // Assertion
        assertEquals(tasks, result);

    }

    @Test
    public void testGetTask_InValidBoardId(){
        // Arrange
        int boardId = 1;
        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(null));

        //Act
        ResponseStatusException exception=assertThrows(
                ResponseStatusException.class,
                ()->{taskService.getTasks(boardId);}
        );

        // Assertion
        assertEquals("Invalid boardId", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void testUpdateStatus_existingTaskId(){
        // Arrange
        String newStatus = "doing";
        int taskId = 1;
        Task task = new Task("A Task");
        task.setTaskId(taskId);
        task.setStatus("todo");
        task.setDueDate(LocalDate.now().minusDays(1L));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        //Act
        Task result = taskService.updateStatus(taskId, newStatus);
        // Assertion
        assertEquals("doing", task.getStatus());
        verify(taskRepository).save(task);

    }

    @Test
    public void testUpdateStatus_validWithDone(){
        // Arrange
        String newStatus = "done";
        int taskId = 1;
        Task task = new Task("A Task");
        task.setTaskId(taskId);
        task.setStatus("todo");
        task.setDueDate(LocalDate.now().minusDays(1L));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        //Act
        Task result = taskService.updateStatus(taskId, newStatus);
        // Assertion
        assertEquals("done", task.getStatus());
        verify(taskRepository).save(task);

    }

    @Test
    public void testUpdateStatus_nonExistingTaskId(){
        // Arrange
        String newStatus = "doing";
        int taskId = 1;

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(null));


        //Act
        ResponseStatusException exception=assertThrows(
                ResponseStatusException.class,
                ()->{taskService.updateStatus(taskId, newStatus);}
        );

        // Assertion
        assertEquals("Invalid taskId", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }

    @Test
    public void testUpdateStatus_invalidStatus(){
        // Arrange
        String misspellingStatus = "A%a";
        int taskId = 1;
        Task task = new Task("A Task");
        task.setTaskId(taskId);
        task.setStatus("todo");
        task.setDueDate(LocalDate.now().minusDays(1L));

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));


        //Act
        ResponseStatusException exception=assertThrows(
                ResponseStatusException.class,
                ()->{taskService.updateStatus(taskId, misspellingStatus);}
        );

        // Assertion
        assertEquals("Invalid status", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void assignUserToTask_validCase(){
        // Arrange
        int taskId = 1;
        String email = "testA@example.com";

        Task task = new Task("A Task");
        task.setTaskId(taskId);
        task.setDueDate(LocalDate.now().minusDays(1L));

        Board board = new Board();
        task.setBoard(board);

        Workspace workspace = new Workspace();
        board.setWorkspace(workspace);

        User user = new User();
        user.setEmail(email);
        List<User> users = new ArrayList<>();
        users.add(user);
        workspace.setUsers(users);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        //Act
        Task result = taskService.assignUserToTask(taskId, email);

        // Assertion
        assertEquals(task, result);
        assertTrue(result.getUsers().contains(user));
        verify(taskRepository).save(result);

    }



    @Test
    public void testAssignUserToTask_NonExistingUser(){
        // Arrange
        int taskId = 1;
        String email = "testA@example.com";

        Task task = new Task("A Task");
        task.setTaskId(taskId);

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()->{taskService.assignUserToTask(taskId, email);}
        );


        // Assertion
        assertEquals("User does not exist", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void testAssignUserToTask_NonExistingTask(){
        // Arrange
        int taskId = 1;
        int userId = 2;
        String email = "testA@example.com";

        User user = new User();
        user.setUserID(userId);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(null));

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()->{taskService.assignUserToTask(taskId, email);}
        );


        // Assertion
        assertEquals("Task does not exist", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void testAssignUserToTask_UserNotInWorkspace(){
        // Arrange
        int taskId = 1;
        String email = "testA@example.com";

        Task task = new Task("A Task");
        task.setTaskId(taskId);

        Board board = new Board();
        task.setBoard(board);

        Workspace workspace = new Workspace();
        board.setWorkspace(workspace);

        User user = new User();
        user.setEmail(email);

        workspace.setUsers(new ArrayList<>());

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()->{taskService.assignUserToTask(taskId, email);}
        );


        // Assertion
        assertEquals("Invalid user", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void testAssignUserToTask_UserAlreadyAssigned(){
        // Arrange
        int taskId = 1;
        String email = "testA@example.com";

        Task task = new Task("A Task");
        task.setTaskId(taskId);

        Board board = new Board();
        task.setBoard(board);

        Workspace workspace = new Workspace();
        board.setWorkspace(workspace);

        User user = new User();
        user.setEmail(email);
        List<User> users = new ArrayList<>();
        users.add(user);
        workspace.setUsers(users);

        task.setUsers(users);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()->{taskService.assignUserToTask(taskId, email);}
        );


        // Assertion
        assertEquals("User is already assigned", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }


    @Test
    public void testUpdateDueDate_ValidCase(){
        // Arrange
        int taskId = 1;
        LocalDate newDate = LocalDate.now().plusDays(1);

        Task task = new Task("A Task");
        task.setTaskId(taskId);
        task.setDueDate(LocalDate.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        Task result = taskService.updateDueDate(taskId, newDate);

        assertEquals(newDate, result.getDueDate());
        verify(taskRepository).save(task);
    }


    @Test
    public void testUpdateDueDate_InvalidDueDate(){
        // Arrange
        int taskId = 1;
        LocalDate newDate = LocalDate.now().minusDays(1);

        Task task = new Task("A Task");
        task.setTaskId(taskId);
        task.setDueDate(LocalDate.now());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()->{taskService.updateDueDate(taskId, newDate);;}
        );

        // Assertion
        assertEquals("Task date is before today", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }

    @Test
    public void testUpdateDueDate_NonExistingTaskId(){
        // Arrange
        int taskId = 1;
        LocalDate newDate = LocalDate.now();

        when(taskRepository.findById(taskId)).thenReturn(Optional.ofNullable(null));
        // Act
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                ()->{taskService.updateDueDate(taskId, newDate);;}
        );

        // Assertion
        assertEquals("Task id does not exist", exception.getReason());
        verify(taskRepository, never()).save(any(Task.class));

    }

}
