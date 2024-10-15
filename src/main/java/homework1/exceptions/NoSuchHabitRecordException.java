package homework1.exceptions;

import lombok.Getter;

@Getter
public class NoSuchHabitRecordException extends Exception {
    public NoSuchHabitRecordException(String message) {
        super(message);
    }
}
