package homework1.repository;

import homework1.entity.habit.Habit;
import homework1.entity.habitRecord.HabitRecord;
import homework1.exceptions.NoSuchHabitRecordException;

import java.util.Map;

/**
 * Класс для реализации CRUD-запросов для записей о выполнении привычек
 */
public class HabitRecordRepository {

    /**
     * Метод, позволяющий внести в базу новую запись о выбранной привычке
     * @param habit привычка, запись о которой необходимо внести в базу
     * @param habitRecord новая запись о привычке
     */
    public void createHabitRecord(Habit habit, HabitRecord habitRecord){
        habit.getHabitRecords().put(habitRecord.getId(), habitRecord);
    }

    /**
     * Метод, позволяющий получить запись о привычке, используя ID этой записи
     * @param habit привычка, запись о которой метод пытается получить
     * @param id ID записи привычки, которую надо получить
     * @return запись о выбранной привычке по выбранному ID
     * @throws NoSuchHabitRecordException бросается в случае, если не была найдена запись с выбранным id
     */
    public HabitRecord getHabitRecordById(Habit habit, Long id) throws NoSuchHabitRecordException {
        if(!habit.getHabitRecords().containsKey(id)){
            throw new NoSuchHabitRecordException("Не удалось найти запись");
        }
        return habit.getHabitRecords().get(id);
    }

    /**
     * Метод, позволяющий получить все записи о выбранной привычке
     * @param habit привычка, чьи записи метод возвращает
     * @return Map<Long, HabitRecord> - записи о привычках и id этих записей в качестве ключей
     */
    public Map<Long, HabitRecord> getHabitRecordsByHabit(Habit habit) {
        return habit.getHabitRecords();
    }

    /**
     * Метод, позволяющий удалить запись о привычке, используя её ID
     * @param habit привычка, чья запись подлежит удалению
     * @param id id записи привычки
     * @exception NoSuchHabitRecordException пробрасывается, если не была найдена запись о привычке
     */
    public void deleteById(Habit habit, Long id) throws NoSuchHabitRecordException {
        if(!habit.getHabitRecords().containsKey(id)){
            throw new NoSuchHabitRecordException("Не удалось найти запись");
        }
        habit.getHabitRecords().remove(id);
    }

    /**
     * Метод, позволяющий обновить запись о привычке
     * @param habit привычка, о которой нужно обновить запись
     * @param habitRecord обновлённая запись
     * @throws NoSuchHabitRecordException пробрасывается, если запись не была найдена
     */
    public void updateHabitRecord(Habit habit, HabitRecord habitRecord) throws NoSuchHabitRecordException {
        if(!habit.getHabitRecords().containsKey(habitRecord.getId())){
            throw new NoSuchHabitRecordException("Не удалось найти запись");
        }
        habit.getHabitRecords().put(habitRecord.getId(), habitRecord);
    }
}
