package com0.trellobackend.service;

import com0.trellobackend.model.User;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.UserRepository;
import com0.trellobackend.repository.WorkspaceRepository;
import com0.trellobackend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceService workspaceService;

    @InjectMocks
    @Spy
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateUser_NewUser() {
        // Arrange
        User user = new User();

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.createUser(user);

        // Assert user
        assertNotNull(result);
        assertEquals(result, user,"Mismatch" );

        verify(userRepository).save(user);

        // Assert workspace
        assertNotNull(result.getWorkSpaces());
        assertEquals(1, result.getWorkSpaces().size());
        Workspace workspace = result.getWorkSpaces().get(0);
        assertEquals("New Workspace",workspace.getName());
        assertEquals("Add a description",workspace.getDescription());
    }


    @Test
    public void testLoginUser_ValidCredentials() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        User result = userService.loginUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    public void testLoginUser_InvalidUser() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        // Act
        User result = userService.loginUser(user);

        // Assert
        assertNull(result);

    }

    @Test
    public void testLoginUser_InvalidPassword() {
        // Arrange
        String email = "test@example.com";
        String password = "IncorrectPassword";
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        User userInDB = new User();
        userInDB.setEmail(email);
        userInDB.setPassword("CorrectPassword");


        when(userRepository.findByEmail(email)).thenReturn(userInDB);

        // Act
        User result = userService.loginUser(user);

        // Assert
        assertNull(result);

    }



    /* FetchAllUse */
    @Test
    public void testFetchAllUsers_EmptyArray() {
        // Arrange
        List<User> userList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.fetchAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository).findAll();
    }


    @Test
    public void testFetchAllUsers_NotEmptyArray() {
        // Arrange
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        when(userRepository.findAll()).thenReturn(userList);

        // Act
        List<User> result = userService.fetchAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(userList.size(), result.size());
        assertEquals(userList, result);

    }



    /* GetUserByID */
    @Test
    public void testGetUserByID_ValidUser() {
        int userId = 1;
        User user = new User();
        user.setUserID(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = userService.getUserByID(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserID(), "userid is not equal");
    }

    // return null
    @Test
    public void testGetUserByID_InvalidUser() {
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(null));

        User result = userService.getUserByID(userId);

        assertNull(result);
    }




    /* GetWorkspacesByUserId */
    @Test
    public void testGetWorkspacesByUserId_ValidUser() {
        // Arrange
        int userId = 1;
        int setWorkspaceID = 0;
        User user = new User();
        user.setUserID(userId);

        Workspace workspace1 = new Workspace();
        workspace1.setWorkspace_id(++setWorkspaceID);

        Workspace workspace2 = new Workspace();
        workspace2.setWorkspace_id(++setWorkspaceID);

        List<Workspace> workspaces = new ArrayList<>();
        workspaces.add(workspace1);
        workspaces.add(workspace2);

        user.setWorkSpaces(workspaces);
        when(userService.getUserByID(userId)).thenReturn(user);

        // Act
        List<Workspace> result = userService.getWorkspacesByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(user.getWorkSpaces().size(), result.size());

        assertEquals(workspaces, result);
    }

    @Test
    public void testGetWorkspacesByUserId_InvalidUser() {
        // Arrange
        int userId = 1;

        when(userService.getUserByID(userId)).thenReturn(null);

        // Act
        List<Workspace> result = userService.getWorkspacesByUserId(userId);

        // Assert
        assertNull(result);
    }



    /* AddWorkspaceToUserById */
    @Test
    public void testAddWorkspaceToUserById_ValidWorkspace() {
        // Arrange
        int userId = 1;
        int wantedNumberOfInvocations = 2;
        int setWorkSpaceID = 0;
        User user = new User();
        user.setUserID(userId);
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(++setWorkSpaceID);
        when(userService.getUserByID(userId)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(workspaceRepository.save(workspace)).thenReturn(workspace);

        // Act
        Workspace result = userService.addWorkspaceToUserById(workspace, userId);

        // Assert
        assertNotNull(result);
        assertEquals(workspace.getWorkspace_id(), result.getWorkspace_id());
        assertTrue(user.getWorkSpaces().contains(workspace));

        verify(userRepository).save(user);
        verify(workspaceRepository,times(wantedNumberOfInvocations)).save(workspace);
    }



    @Test
    public void testAddWorkspaceToUserById_InvalidUser() {
        // Arrange
        int userId = 1;
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(1);
        when(userService.getUserByID(userId)).thenReturn(null);

        Workspace result = userService.addWorkspaceToUserById(workspace, userId);

        assertNull(result);
    }


    /* AssignWorkspaceByEmail */
    @Test
    void assignWorkspaceByEmail_ValidEmailAndWorkspace_ReturnsWorkspace() {
        // Arrange
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        int workspaceId = 1;
        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(workspaceId);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(workspaceService.findWorkSpacebyID(workspaceId)).thenReturn(workspace);


        // Act
        Workspace result = userService.assignWorkspaceByEmail(email, workspaceId);

        // Assert
        assertEquals(workspace, result);
        assertTrue(user.getWorkSpaces().contains(workspace));
        verify(userRepository).save(user);
    }

    @Test
    void assignWorkspaceByEmail_InvalidEmail() {
        // Arrange
        String email = "test@example.com";
        int workspaceId = 1;

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        Workspace result = userService.assignWorkspaceByEmail(email, workspaceId);

        // Assert
        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void assignWorkspaceByEmail_InvalidWorkspace() {
        // Arrange
        String email = "test@example.com";
        int workspaceId = 1;
        User user = new User();

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(workspaceService.findWorkSpacebyID(workspaceId)).thenReturn(null);

        // Act
        Workspace result = userService.assignWorkspaceByEmail(email, workspaceId);

        // Assert
        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void assignWorkspaceByEmail_WorkspaceAlreadyAssigned() {
        // Arrange
        String email = "test@example.com";
        int workspaceId = 1;

        User user = new User();
        user.setEmail(email);

        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(workspaceId);
        user.getWorkSpaces().add(workspace);

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(workspaceService.findWorkSpacebyID(workspaceId)).thenReturn(workspace);

        // Act
        Workspace result = userService.assignWorkspaceByEmail(email, workspaceId);

        // Assert
        assertNull(result);
        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    public void testResetPassword_ValidUser() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setSecurityAnswer("security answer");
        user.setPassword("newPassword");

        User userFromDB = new User();
        userFromDB.setEmail("test@example.com");
        userFromDB.setSecurityAnswer("security answer");
        userFromDB.setPassword("oldPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(userFromDB);

        // Act
        boolean result = userService.resetPassword(user);

        // Assert
        assertTrue(result);
        assertEquals("newPassword", userFromDB.getPassword());
        verify(userRepository).save(userFromDB);

    }

    @Test
    public void testResetPassword_UserNotFound() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setSecurityAnswer("security answer");
        user.setPassword("newPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        // Act
        boolean result = userService.resetPassword(user);

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(user);
    }

    @Test
    public void testResetPassword_IncorrectAnswer() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");
        user.setSecurityAnswer("Wrong answer");
        user.setPassword("newPassword");

        User userFromDB = new User();
        userFromDB.setEmail("test@example.com");
        userFromDB.setSecurityAnswer("security answer");
        userFromDB.setPassword("oldPassword");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(userFromDB);

        // Act
        boolean result = userService.resetPassword(user);

        // Assert
        assertFalse(result);
        verify(userRepository, never()).save(user);
    }





}


