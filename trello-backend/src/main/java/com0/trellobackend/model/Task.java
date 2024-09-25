package com0.trellobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "task")
public class Task {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private int taskId;

    private String name;
    private LocalDate dueDate;
    private String status; // Can be "todo" or "doing" or "done"


    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinTable(
            name="Task_User_Link",
            joinColumns = @JoinColumn(name="task_id", referencedColumnName=" task_id"),
            inverseJoinColumns = @JoinColumn( name="user_id", referencedColumnName="user_id"))
    private List<User> users;

    @JsonIgnore
    @JoinColumn(name = "board_id", referencedColumnName = "id")
    @ManyToOne(targetEntity = Board.class)
    private Board board;



    // Constructor
    public Task(){
    }

    public Task(String name){
        this.name = name;
        this.users = new ArrayList();
    }

    public void addUser(User user) {
        this.users.add(user);
    }
}
