package com0.trellobackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int userID;

    @Column(unique = true)
    String email;

    String password;

    int securityQuestion;

    String securityAnswer;




    @ManyToMany(targetEntity = Workspace.class, fetch = FetchType.LAZY,  cascade = CascadeType.ALL)
    @JoinTable(
            name="Workspace_User_Link",
            joinColumns = @JoinColumn( name="user_id", referencedColumnName="user_id"),
            inverseJoinColumns = @JoinColumn(name="workspace_id", referencedColumnName=" workspace_id"))
    public List<Workspace> workSpaces;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    public List<Task> tasks;

    // default constructor
    public User(){ this.workSpaces = new ArrayList<>();}


    // constructor
    public User( String email, String password, int security_question, String security_answer) {
        this.email = email;
        this.password = password;
        this.securityQuestion = security_question;
        this.securityAnswer = security_answer;
        this.workSpaces = new ArrayList<Workspace>();

    }

}
