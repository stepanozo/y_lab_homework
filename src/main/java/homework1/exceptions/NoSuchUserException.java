package homework1.exceptions;

import lombok.Getter;

@Getter
public class NoSuchUserException extends Exception {
    String userName;
    public NoSuchUserException(String message, String userName) {
        super(message);
        this.userName = userName;
    }
}
