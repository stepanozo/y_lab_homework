package homework1.exceptions;

import lombok.Getter;

@Getter
public class NoSuchHabitException extends Exception {
    String habitName;
    public NoSuchHabitException(String message, String habitName) {
        super(message);
        this.habitName = habitName;
    }
}
