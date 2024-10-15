package homework1.entity.habitRecord;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Класс, обозначающий одну запись об исполнении привычки
 */
@Getter
public class HabitRecord {
    private static Long currentId = 0L;
    private Long id;
    @Setter
    private LocalDate date;

    /**
     * Конструктор записи об исполнении привычки
     * @param date дата выполнения привычки
     */
    public HabitRecord(LocalDate date) {
        id = currentId++;
        this.date = date;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        HabitRecord habitRecord = (HabitRecord) o;
        return Objects.equals(habitRecord.getId(), id) && habitRecord.getDate().equals(date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
