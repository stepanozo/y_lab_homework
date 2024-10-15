package homework1.repository;

import homework1.entity.habit.Habit;
import homework1.entity.user.User;
import homework1.exceptions.HabitAlreadyExistsException;
import homework1.exceptions.NoSuchHabitException;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Класс для реализации CRUD-запросов для привычек
 */
public class HabitRepository {

    /**
     * Метод, вносящий новую привычку в базу для выбранного пользователя
     * @param user пользователь, которому необходимо дать новую привычку
     * @param newHabit привычка, присваиваемая пользователю
     * @throws HabitAlreadyExistsException бросается в случае, если пользователь уже имеет данную привычку
     */
    public void createHabit(User user, Habit newHabit) throws HabitAlreadyExistsException {
        if (user.getHabitSet().contains(newHabit))
                throw new HabitAlreadyExistsException("Привычка \"" + newHabit.getName() + "\" уже существует! ", newHabit.getName());
        user.getHabitSet().add(newHabit);
    }

    /**
     * Метод, позволяющий обновить информацию о привычке
     * @param user пользователь, чью привычку необходимо обновить
     * @param habit обновляемая привычка
     * @throws NoSuchHabitException пробрасывается в случае, если привычка не найдена
     */
    public void updateHabit(User user, Habit habit) throws NoSuchHabitException {
        if(user.getHabitSet().contains(habit))
            user.getHabitSet().remove(habit);
        else throw new NoSuchHabitException(habit.getName(), "Не удалось найти привычку с именем " + habit.getName());
    }

    /**
     * Метод, позволяющий удалить выбранную привычку выбранного пользователя
     * @param user пользователь, чья привычка подлежит удалению
     * @param habit привычка, подлежащая удалению
     * @throws NoSuchHabitException пробрасывается в случае, если привычка не найдена
     */
    public void deleteHabit(User user, Habit habit) throws NoSuchHabitException {
        if(user.getHabitSet().contains(habit))
            user.getHabitSet().remove(habit);
        else throw new NoSuchHabitException(habit.getName(), "Не удалось найти привычку с именем " + habit.getName());
    }

    /**
     * Метод, позволяющий получить множество привычек пользователя, удовлетворяющих выбранному условию
     * @param user пользователь, чьи привычки необходимо получить
     * @param condition условие, по которому фильтруются привычки
     * @return Set<Habit> - множество привычек, удовлетворяющих условию
     */
    public List<Habit> getHabitsWithCondition(User user, Predicate<Habit> condition) {
        return user.getHabitSet()
            .stream()
            .filter(condition)
            .collect(Collectors.toList());
    }

    /**
     * Метод, позволяющий получить все привычки пользователя
     * @param user пользователь, чьи привычки нужно получить
     * @return Set<Habit> - список привычек
     */
    public List<Habit> getHabits(User user) {
        return getHabitsWithCondition(user, habit -> true);
    }

    /**
     * Метод, позволяющий найти привычку выбранного пользователя по имени этой привычки
     * @param user пользователь, привычку которого ищет метод
     * @param name имя искомой привычки
     * @return Habit
     * @throws NoSuchHabitException в случае, если привычка не найдена
     */
    public Habit findByUserAndName(User user, String name) throws NoSuchHabitException {
        return user.getHabitSet().stream().filter(habit -> habit.getName().equals(name))
                .findFirst().orElseThrow(() -> new NoSuchHabitException(name, "Не удалось найти привычку с именем " + name));
    }

    /**
     * Метод, проверяющий существование привычки
     */
    public boolean existsByName(User user, String name) {
        return getHabits(user).stream().anyMatch(habit -> habit.getName().equals(name));
    }
}
