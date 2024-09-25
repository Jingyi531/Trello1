package com0.trellobackend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.UserRepository;
import com0.trellobackend.repository.WorkspaceRepository;
import com0.trellobackend.service.WorkspaceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorkspaceServiceTest {
    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkspaceService workspaceService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindWorkSpacebyID_ExistingWorkspaceId() {
        // Arrange
        int workspaceId = 1;
        int wantedNumberOfInvocations = 2;
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(workspaceId);

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));

        // Act
        Workspace result = workspaceService.findWorkSpacebyID(workspaceId);

        // Assert
        assertEquals(workspace, result);
        //is this needed?
        verify(workspaceRepository,Mockito.times(wantedNumberOfInvocations)).findById(workspaceId);
    }

    @Test
    public void testFindWorkSpacebyID_NonExistingWorkspaceId() {
        // Arrange
        int workspaceId = 1;

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.ofNullable(null));

        // Act
        Workspace result = workspaceService.findWorkSpacebyID(workspaceId);

        // Assert
        assertNull(result);
        verify(workspaceRepository, Mockito.times(1)).findById(workspaceId);
    }

    @Test
    public void testCreateWorkspace_ValidInput() {
        // Arrange
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(1);
        workspace.setName("Test Workspace");

        when(workspaceRepository.save(workspace)).thenReturn(workspace);

        // Act
        Workspace result = workspaceService.createWorkspace(workspace);

        // Assert
        assertEquals(workspace, result);
        verify(workspaceRepository).save(workspace);
    }

    @Test
    public void testDeleteWorkspace_ExistingWorkspaceId() {
        // Arrange
        int workspaceId = 1;
        int setUserID = 0;
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(workspaceId);

        User user1 = new User();
        user1.setUserID(++setUserID);
        user1.getWorkSpaces().add(workspace);

        User user2 = new User();
        user2.setUserID(++setUserID);
        user2.getWorkSpaces().add(workspace);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        workspace.setUsers(users);

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));

        // Act
        Workspace result = workspaceService.deleteWorkspace(workspaceId);

        // Assert
        assertEquals(workspace, result);

        // Saved n times
        for (User user : users) {
            verify(userRepository).save(user);
        }
        verify(workspaceRepository).delete(workspace);
    }

    @Test
    public void testDeleteWorkspace_NonExistingWorkspaceId() {
        // Arrange
        int workspaceId = 1;
        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.ofNullable(null));

        // Act
        Workspace result = workspaceService.deleteWorkspace(workspaceId);

        // Assert
        Assertions.assertNull(result);
        verify(workspaceRepository, Mockito.never()).delete(any(Workspace.class));
    }

    @Test
    public void testRenameWorkspace_ExistingWorkspace() {
        // Arrange
        int workspaceId = 1;
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(workspaceId);

        String newName = "New Name";

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));
        when(workspaceRepository.save(workspace)).thenReturn(workspace);

        // Act
        Workspace result = workspaceService.renameWorkspace(workspaceId, newName);

        // Assert
        assertEquals(newName, result.getName());
        verify(workspaceRepository).save(workspace);
    }

    @Test
    public void testRenameWorkspace_NonExistingWorkspace() {
        // Arrange
        int workspaceId = 1;
        String newName = "New Name";

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.ofNullable(null));

        // Act
        Workspace result = workspaceService.renameWorkspace(workspaceId, newName);

        // Assert
        Assertions.assertNull(result);
        verify(workspaceRepository, Mockito.never()).save(Mockito.any(Workspace.class));
    }

   @Test
   public void testUpdateDescription_ExistingWorkspace(){
       // Arrange
       int workspaceId = 1;
       Workspace workspace = new Workspace();
       workspace.setWorkspace_id(workspaceId);

       String newDescription = "New Description";

       when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.of(workspace));
       when(workspaceRepository.save(workspace)).thenReturn(workspace);

       // Act
       Workspace result = workspaceService.updateDescription(workspaceId, newDescription);

       // Assert
       assertEquals(newDescription, result.getDescription());
       verify(workspaceRepository).save(workspace);
   }



    @Test
    public void testUpdateDescription_NonExistingWorkspace(){
        // Arrange
        int workspaceId = 1;
        String newDescription = "New Description";

        when(workspaceRepository.findById(workspaceId)).thenReturn(Optional.ofNullable(null));


        // Act
        Workspace result = workspaceService.updateDescription(workspaceId, newDescription);

        // Assert
        assertNull(result);
        verify(workspaceRepository, never()).save(any(Workspace.class));
    }

}