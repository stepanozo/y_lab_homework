package homework1.exceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends Exception {
    String userName;
    public UserAlreadyExistsException(String message, String userName) {
        super(message);
        this.userName = userName;
    }
}
