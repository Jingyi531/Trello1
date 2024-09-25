package com0.trellobackend.service;

import com0.trellobackend.dto.WorkspaceDTO;
import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.UserRepository;
import com0.trellobackend.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

@Service
public class WorkspaceService {
    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    UserRepository userRepository;

    public Workspace findWorkSpacebyID(int id){
        if(workspaceRepository.findById(id).isPresent()){
            return workspaceRepository.findById(id).get();
        }
        return null;
    }


    public Workspace createWorkspace(Workspace workspace){
        return workspaceRepository.save(workspace);
    }

    /**
     * returns true if workspace is found
     */
    public Workspace deleteWorkspace(int id){
        if(workspaceRepository.findById(id).isPresent()){
            Workspace workspace = workspaceRepository.findById(id).get();
            for (User user : workspace.getUsers()) {
                user.getWorkSpaces().remove(workspace);
                userRepository.save(user);
            }
            workspaceRepository.delete(workspace);
            return workspace;
        }
        return null;
    }


    public Workspace renameWorkspace(int id, String newName) {
        Workspace savedWorkspace = findWorkSpacebyID(id);
        if(savedWorkspace == null){
            return null;
        }
        savedWorkspace.setName(newName);
        workspaceRepository.save(savedWorkspace);
        return savedWorkspace;
    }

    public Workspace updateDescription(int id, String newDescription) {
        Workspace savedWorkspace = findWorkSpacebyID(id);
        if(savedWorkspace == null){
            return null;
        }
        savedWorkspace.setDescription(newDescription);
        workspaceRepository.save(savedWorkspace);
        return savedWorkspace;
    }
}
