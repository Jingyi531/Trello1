package com0.trellobackend.dto;

import com0.trellobackend.model.Task;
import com0.trellobackend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private int taskId;

    private String name;
    private LocalDate dueDate;
    private String status;

    private List<String> users;
    public TaskDTO(Task task){
        this.taskId = task.getTaskId();
        this.name = task.getName();
        this.dueDate = task.getDueDate();
        this.status = task.getStatus();

        this.users = new ArrayList<>();

        // add user email
        this.users = new LinkedList<>();
        List<User> userList = task.getUsers();

        if(userList != null){
            for (User user : userList) {
                this.users.add(user.getEmail());
            }
        }
    }

}
