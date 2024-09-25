package com0.trellobackend.controller;

import com0.trellobackend.dto.BoardDTO;
import com0.trellobackend.dto.WorkspaceDTO;
import com0.trellobackend.model.Board;
import com0.trellobackend.model.Workspace;
import com0.trellobackend.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/boards")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BoardController {

    @Autowired
    BoardService boardService;

    @PostMapping("/create/{workspaceId}")
    public BoardDTO createBoard(@RequestBody Board board, @PathVariable int workspaceId){
        board = boardService.createBoard(board, workspaceId);
        BoardDTO boardDTO = new BoardDTO(board);
        return boardDTO;
    }

    /**
     * @return all boards information
     */
    @GetMapping("/fetch")
    public List<Board> fetchAllBoards(){
        return boardService.fetchAllBoards();
    }

    @GetMapping("/getBoards/{workspaceId}")
    public List<BoardDTO> getBoards(@PathVariable("workspaceId") int workspaceId){
        List<Board> list = boardService.getBoards(workspaceId);
        if(list == null){
            return new LinkedList<>();
        }
        List<BoardDTO> boardDTOS = new LinkedList();
        for (Board board:  list) {
            boardDTOS.add(new BoardDTO(board));
        }
        return boardDTOS;
    }

    @GetMapping("/delete/{boardId}")
    public BoardDTO deleteBoard(@PathVariable int boardId){
        Board board = boardService.deleteBoard(boardId);
        BoardDTO boardDTO = new BoardDTO(board);
        return boardDTO;
    }


    @PutMapping("/edit/{id}")
    public BoardDTO editBoard(@PathVariable int id, @RequestParam String name){
        Board board = boardService.editBoard(id, name);
        if(board == null) return null;
        return new BoardDTO(board);
    }
}
