package homework1.exceptions;

import lombok.Getter;

@Getter
public class HabitAlreadyExistsException extends Exception {
    String habitName;
    public HabitAlreadyExistsException(String message, String habitName) {
        super(message);
        this.habitName = habitName;
    }
}
