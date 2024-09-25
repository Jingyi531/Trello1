package com0.trellobackend.dto;

import com0.trellobackend.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private int userID;
    private String email;
    private String password;
    private Integer securityQuestion;
    private String securityAnswer;

    public UserDTO() {
    }

    public UserDTO(User user) {
        if(user ==null){
            return;
        }
        this.userID = user.getUserID();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.securityQuestion = user.getSecurityQuestion();
        this.securityAnswer = user.getSecurityAnswer();
        // Exclude workSpaces field
    }

}
