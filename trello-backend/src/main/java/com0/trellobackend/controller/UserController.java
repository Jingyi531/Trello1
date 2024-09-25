package com0.trellobackend.controller;

import com0.trellobackend.dto.UserDTO;
import com0.trellobackend.dto.WorkspaceDTO;

import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    @Autowired
    UserService userService;

    /**
     * Create a user with user information
     * @return true if succeed
     */
    @PostMapping("/create")
    public UserDTO createUser(@RequestBody User user){
    User newUser = userService.createUser(user);
    if(newUser == null) return null;
    return new UserDTO(newUser);
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody User user){
      User loggedInUser = userService.loginUser(user);
      if(loggedInUser == null) return null;
      return new UserDTO(loggedInUser);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody User user) {
        boolean check = userService.resetPassword(user);
        String resetFailedMessage = "Reset Failed. Make sure the email, password, security answers are correct.";
        String resetSuccessfulMessage = "Password reset successful.";
        if(check == true) {
            return ResponseEntity.ok(resetSuccessfulMessage);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resetFailedMessage);
    }

    /**
     * @return all user information
     */
    @GetMapping("/fetch")
    public List<UserDTO> fetchAllUsers(){
       List<User> fetchedUserList = userService.fetchAllUsers();

       List<UserDTO> newUserList = new LinkedList();
        for (User user: fetchedUserList) {
            newUserList.add(new UserDTO(user));
        }
        return newUserList;
    }

    @GetMapping("/get/{id}")
    public UserDTO getUserByID(@PathVariable("id") int id){
        User user = userService.getUserByID(id);
        if(user == null){
            return null;
        }
        return new UserDTO(user);
    }





    // Workspace

    @PutMapping("/assignWorkspace/{workspaceId}")
    public WorkspaceDTO assignWorkspaceByEmail(@PathVariable int workspaceId, @RequestParam String email)
    {
        Workspace assignedWorkspace = userService.assignWorkspaceByEmail(email,workspaceId);
        if(assignedWorkspace== null){
            return null;
        }
        return new WorkspaceDTO(assignedWorkspace);
    }





    /**
     * @param id userID
     * @return all workspaces
     */
    @GetMapping("/getWorkspaces/{id}")
    public List<WorkspaceDTO> getWorkspacesByUserId(@PathVariable int id)
    {
        List<Workspace> fetchedWorkspaceList = userService.getWorkspacesByUserId(id);
        if(fetchedWorkspaceList == null){
            return null;
        }
        List<WorkspaceDTO> newWorkspaceList = new LinkedList();
        for (Workspace workspace:  fetchedWorkspaceList) {
            newWorkspaceList.add(new WorkspaceDTO(workspace));
        }
        return newWorkspaceList;
    }


    /**
     * @param id userID
     * @param workspace Workspace
     */
    @PostMapping("/addWorkspace/{id}")
    public WorkspaceDTO addWorkspaceToUserById(@RequestBody Workspace workspace, @PathVariable int id){
        Workspace newWorkspace = userService.addWorkspaceToUserById(workspace, id);
        if(newWorkspace == null){
            return null;
        }
        return new WorkspaceDTO(newWorkspace);
    }



}
