package com0.trellobackend.service;


import com0.trellobackend.model.Board;

import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.UserRepository;
import com0.trellobackend.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    WorkspaceService workspaceService;

    /*ToDO:
    change how we add boards
    */
    public User createUser(User user){

        Workspace newWorkspace = new Workspace("New Workspace", "Add a description");

        user.getWorkSpaces().add(newWorkspace);
        return userRepository.save(user);
    }

    public User loginUser(User user) {
        User user2 = userRepository.findByEmail(user.getEmail());
        if(user2 !=null){
            if(user2.getPassword().equals(user.getPassword())) {
                return user2;
            }
        }
        return null;
    }


    public List<User> fetchAllUsers(){
        return userRepository.findAll();
    }

    /**
     * @param id userID
     * @return a User if user exits, null if not
     */
    public User getUserByID(int id){

        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return user.get();
        }
        return null;
    }

    // Workspace

    public List<Workspace> getWorkspacesByUserId(int id){

        User savedUser = getUserByID(id);
        if(savedUser == null){
            return null;
        }

        return savedUser.getWorkSpaces();
    }

    public Workspace addWorkspaceToUserById(Workspace workspace, int userId) {
        User user = getUserByID(userId);
        if(user == null){
            return null;
        }
        Workspace savedWorkspace = workspaceRepository.save(workspace);
        user.getWorkSpaces().add(savedWorkspace);
        userRepository.save(user);

        savedWorkspace.getUsers().add(user);
        workspaceRepository.save(savedWorkspace);
        return savedWorkspace;

    }

    public Workspace assignWorkspaceByEmail(String email, int workspaceId) {
        User user = userRepository.findByEmail(email);
        if(user == null){
            return null;
        }

        Workspace workspace = workspaceService.findWorkSpacebyID(workspaceId);
        if(workspace == null){
            return null;
        }

        if(user.getWorkSpaces().contains(workspace)){
            return null;
        }

        user.getWorkSpaces().add(workspace);
        userRepository.save(user);
        return workspace;
    }



    public boolean resetPassword(User user) {
        User userFromDB = userRepository.findByEmail(user.getEmail());

        // get user by email
        if(userFromDB == null){
            return false;
        }

        //check security answer
        if(!userFromDB.getSecurityAnswer().equals(user.getSecurityAnswer())){
            return false;
        }

        // set password
        userFromDB.setPassword(user.getPassword());
        userRepository.save(userFromDB);
        return true;
    }



}

