package com0.trellobackend.service;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import com0.trellobackend.model.Board;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.BoardRepository;
import com0.trellobackend.repository.WorkspaceRepository;
import com0.trellobackend.service.BoardService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class BoardServiceTest {
    @Mock
    private BoardRepository boardRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Spy
    @InjectMocks
    private BoardService boardService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBoard_ValidInput() {
        // Arrange
        Board board = new Board();
        board.setId(1);
        board.setName("Test Board");

        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(1);

        when(workspaceRepository.findById(1)).thenReturn(Optional.of(workspace));
        when(boardRepository.save(board)).thenReturn(board);

        // Act
        Board result = boardService.createBoard(board, 1);

        // Assert
        assertEquals(board, result);
        assertEquals(workspace, result.getWorkspace());
        verify(boardRepository).save(board);
    }

    @Test
    public void testFetchAllBoards_() {
        int boardIdCount = 0;
        // Arrange
        List<Board> boards = new ArrayList<>();
        Board board1 = new Board();
        board1.setId(boardIdCount++);
        Board board2 = new Board();
        board1.setId(boardIdCount++);
        boards.add(board1);
        boards.add(board2);

        when(boardRepository.findAll()).thenReturn(boards);

        // Act
        List<Board> result = boardService.fetchAllBoards();

        // Assert
        assertEquals(boards, result);
        verify(boardRepository).findAll();
    }

    @Test
    public void testGetBoards_ExistingWorkspaceId() {
        // Arrange
        int workspaceId = 1;
        int boardIdCount = 0;
        List<Board> boards = new ArrayList<>();
        Board board1 = new Board();
        board1.setId(boardIdCount++);
        Board board2 = new Board();
        board1.setId(boardIdCount++);
        boards.add(board1);
        boards.add(board2);

        when(boardRepository.findByWorkspaceWorkspaceId(workspaceId)).thenReturn(boards);

        // Act
        List<Board> result = boardService.getBoards(workspaceId);

        // Assert
        assertEquals(boards, result);
        verify(boardRepository).findByWorkspaceWorkspaceId(workspaceId);
    }

    @Test
    public void testGetBoards_NonExistingWorkspaceId() {
        // Arrange
        int workspaceId = 1;

        // Return empty workspace list
        when(boardRepository.findByWorkspaceWorkspaceId(workspaceId)).thenReturn(new ArrayList<>());

        // Act
        List<Board> result = boardService.getBoards(workspaceId);

        // Assert
        Assertions.assertNull(result);
        verify(boardRepository).findByWorkspaceWorkspaceId(workspaceId);
    }

    @Test
    public void testDeleteBoard_ExistingBoardId() {
        // Arrange
        int boardId = 1;
        Board board = new Board();
        board.setId(boardId);

        Workspace workspace = new Workspace();
        workspace.setWorkspace_id(1);
        workspace.getBoards().add(board);
        board.setWorkspace(workspace);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        //Act
        Board result = boardService.deleteBoard(boardId);

        // Assert
        assertEquals(board, result);
        assertFalse(workspace.getBoards().contains(board));
        verify(boardRepository).deleteById(boardId);
    }

    @Test
    public void testDeleteBoard_NonExistingBoardId() {
        // Arrange
        int boardId = 1;

        when(boardRepository.findById(boardId)).thenReturn(Optional.empty());

        // Act
        Board result = boardService.deleteBoard(boardId);

        // Assert
        Assertions.assertNull(result);
        verify(boardRepository, Mockito.never()).deleteById(boardId);
    }

    @Test
    public void testEditBoard_ExistingBoard() {
        // Arrange
        int boardId = 1;
        String newName = "New Name";
        Board board = new Board();
        board.setId(boardId);
        board.setName(newName);


        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardRepository.save(board)).thenReturn(board);

        // Act
        Board result = boardService.editBoard(boardId, newName);

        // Assert
        assertEquals(newName, result.getName());
    }


    @Test
    public void testEditBoard_NonExistingBoar() {
        // Arrange
        int boardId = 1;
        String newName = "New Name";

        when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(null));

        // Act
        Board result = boardService.editBoard(boardId, newName);

        // Assert
        assertNull(result);
    }

}