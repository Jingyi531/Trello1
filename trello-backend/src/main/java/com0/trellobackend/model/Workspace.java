package com0.trellobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Workspace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(value = "workspace_id")
    private int workspace_id;

    @JsonProperty(value = "name")
    private String name;

    @JsonProperty(value = "description")
    private String description;

    @OneToMany(targetEntity = Board.class, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id")
    public List<Board> boards;

    @ManyToMany(mappedBy = "workSpaces")
    public List<User> users;

    public Workspace(){
        this.users = new ArrayList<>();
        this.boards = new ArrayList<>();
    }

    public Workspace(String name, String description){
        this.name = name;
        this.boards = new ArrayList<>();
        this.users = new ArrayList<>();
        this.description = description;
    }
}
