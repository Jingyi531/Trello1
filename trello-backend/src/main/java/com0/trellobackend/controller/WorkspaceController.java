package com0.trellobackend.controller;

import com0.trellobackend.dto.WorkspaceDTO;
import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspace")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    @PostMapping("/saveWorkspace")
    public Workspace createWorkspace(@RequestBody Workspace workspace){
        return workspaceService.createWorkspace(workspace);
    }

    /**
     * returns true if workspace is found
     */
    @DeleteMapping("/deleteWorkspace/{id}")
    public WorkspaceDTO deleteWorkspaceId(@PathVariable int id){
        Workspace deletedWorkspace = workspaceService.deleteWorkspace(id);
        if(deletedWorkspace == null) return null;
        return new WorkspaceDTO(deletedWorkspace);
    }

    @PutMapping("/rename/{id}")
    public WorkspaceDTO renameWorkspace(@PathVariable int id, @RequestParam String newName){
        Workspace savedWorkspace = workspaceService.renameWorkspace(id, newName);
        if(savedWorkspace == null) return null;
        return new WorkspaceDTO(savedWorkspace);
    }

    @PutMapping("/updateDescription/{id}")
    public WorkspaceDTO updateDescription(@PathVariable int id, @RequestParam String newDescription){
        Workspace savedWorkspace = workspaceService.updateDescription(id, newDescription);
        if(savedWorkspace == null) return null;
        return new WorkspaceDTO(savedWorkspace);
    }




}
