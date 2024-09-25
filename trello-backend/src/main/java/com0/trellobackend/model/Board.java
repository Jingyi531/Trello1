package com0.trellobackend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @JoinColumn(name = "workspace_id", referencedColumnName = "workspace_id")
    @ManyToOne(targetEntity = Workspace.class)
    private Workspace workspace;

    @OneToMany(targetEntity = Task.class, mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> taskList;

    public Board( String title) {
        this.id = id;
        this.name = title;
    }

    public Board(){

    }

}
