package com0.trellobackend.dto;

import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class WorkspaceDTO {
    private int workspace_id;
    private String name;
    private String description;
    private List<Integer> users;

    public WorkspaceDTO() {
    }

    public WorkspaceDTO(Workspace workspace) {
        if(workspace == null){
            return;
        }
        this.workspace_id = workspace.getWorkspace_id();
        this.name = workspace.getName();
        this.description = workspace.getDescription();

        // add userId
        this.users = new LinkedList<>();
        List<User> userList = workspace.getUsers();

        if(userList != null){
            for (User user : userList) {
                this.users.add(user.getUserID());
            }
        }

    }

}