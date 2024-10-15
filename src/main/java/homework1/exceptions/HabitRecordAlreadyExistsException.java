package homework1.exceptions;

import lombok.Getter;

import java.util.Date;

@Getter
public class HabitRecordAlreadyExistsException extends RuntimeException {
    Date date;
    String habitName;

    public HabitRecordAlreadyExistsException(String message, Date date, String habitName) {
        super(message);
        this.date = date;
        this.habitName = habitName;
    }
}
