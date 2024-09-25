package com0.trellobackend.service;

import com0.trellobackend.model.Board;
import com0.trellobackend.model.Task;
import com0.trellobackend.model.User;
import com0.trellobackend.repository.BoardRepository;
import com0.trellobackend.repository.TaskRepository;
import com0.trellobackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    public Task createTask(Task task, int boardId){
        if(task.getName() == null){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Task has no name");
        }
        Optional<Board> board = boardRepository.findById(boardId);
        if(!board.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"BoardId dose not exist");
        }
        if(task.getDueDate().isBefore(LocalDate.now())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Invalid due date");
        }
        task.setBoard(board.get());
        return taskRepository.save(task);
    }

    public List<Task> getTasks(int boardId) {
        if(!boardRepository.findById(boardId).isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid boardId");
        }
        return taskRepository.findByBoardId(boardId);
    }

    public Task updateStatus(int taskId, String status) {
        String[] arr = {"todo", "doing", "done"};
        List<String> list = Arrays.asList(arr);
        if (!list.contains(status)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid status");
        }
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(!optionalTask.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid taskId");
        }
        Task task = optionalTask.get();
        task.setStatus(status);
        return taskRepository.save(task);
    }

    public Task assignUserToTask(int taskId, String email) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User does not exist");
        }
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if(!taskOptional.isPresent() ){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Task does not exist");
        }
        Task task = taskOptional.get();

        if(task.getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User is already assigned");
        }

        if(!task.getBoard().getWorkspace().getUsers().contains(user)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid user");
        }
        task.addUser(user);
        return taskRepository.save(task);
    }

    public Task updateDueDate(int taskId, LocalDate newDate) {
        if(newDate.isBefore(LocalDate.now())){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Task date is before today");
        }

        Optional<Task> taskOptional= taskRepository.findById(taskId);
        if(!taskOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"Task id does not exist");
        }
        Task task = taskOptional.get();
        task.setDueDate(newDate);
        return taskRepository.save(task);

    }
}
