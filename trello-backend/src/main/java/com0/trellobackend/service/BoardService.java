package com0.trellobackend.service;

import com0.trellobackend.model.Board;
import com0.trellobackend.model.Task;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.repository.BoardRepository;
import com0.trellobackend.repository.TaskRepository;
import com0.trellobackend.repository.WorkspaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    WorkspaceRepository workspaceRepository;

    @Autowired
    TaskRepository taskRepository;

    public Board createBoard(Board board, int workspaceId) {
        Optional<Workspace> workspace = workspaceRepository.findById(workspaceId);
        board.setWorkspace(workspace.get());
        return boardRepository.save(board);
    }

    public List<Board> fetchAllBoards() {
        return boardRepository.findAll();
    }

    public List<Board> getBoards(int workspaceId) {
        List<Board> boards = boardRepository.findByWorkspaceWorkspaceId(workspaceId);
        if(boards.isEmpty()){
            return null;
        }
        return boards;
    }

    public Board deleteBoard(int boardId) {
        Board board;
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if(boardOptional.isPresent()){
            board = boardOptional.get();
            Workspace workspace = board.getWorkspace();
            workspace.getBoards().remove(board);

//            List<Task> taskList= board.getTaskList();
//            for (Task task: taskList) {
//                // delete all the tasks in board
//                taskRepository.delete(task);
//            }

            // 删除Board实体
            boardRepository.deleteById(board.getId());
            return board;
        }
        return null;
    }

    public Board editBoard(int id, String newName) {
        Optional<Board> board = boardRepository.findById(id);
        if(!board.isPresent()){
            return null;
        }
        Board savedBoard = board.get();
        savedBoard.setName(newName);
        return boardRepository.save(savedBoard);
    }


}
