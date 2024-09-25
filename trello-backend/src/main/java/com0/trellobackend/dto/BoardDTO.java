package com0.trellobackend.dto;

import com0.trellobackend.model.Board;
import com0.trellobackend.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardDTO {

    private int id;
    private String name;

    public BoardDTO(Board board) {
        if(board ==null){
            return;
        }
        this.id = board.getId();
        this.name = board.getName();
        // Exclude workSpaces field
    }
}
