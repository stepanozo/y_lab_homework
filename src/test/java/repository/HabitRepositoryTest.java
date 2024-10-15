package repository;


import homework1.entity.habit.Habit;
import homework1.entity.habit.Periodicity;
import homework1.entity.user.User;
import homework1.exceptions.HabitAlreadyExistsException;
import homework1.exceptions.NoSuchHabitException;
import homework1.repository.HabitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class HabitRepositoryTest {
    private HabitRepository habitRepository = new HabitRepository();

    @Test
    public void createAndGetHabitTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            List<Habit> habits = createHabitList();
            for (Habit habit : habits)
                habitRepository.createHabit(user, habit);
            List<Habit> newHabitList = new ArrayList<>();
            newHabitList.add(habitRepository.findByUserAndName(user, "habit1"));
            newHabitList.add(habitRepository.findByUserAndName(user, "habit2"));
            newHabitList.add(habitRepository.findByUserAndName(user, "habit3"));
            assertEquals(habits, newHabitList);
            assertEquals(habits, habitRepository.getHabits(user));
        } catch(NoSuchHabitException | HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void createAndGetHabitsWithConditionTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            List<Habit> habits = createHabitList();
            for (Habit habit : habits)
                habitRepository.createHabit(user, habit);
            List<Habit> newHabitList = new ArrayList<>();

            assertEquals(habits.stream().filter(habit -> habit.getPeriodicity().equals(Periodicity.WEEKLY)).toList(),
                    habitRepository.getHabitsWithCondition(user, habit -> habit.getPeriodicity().equals(Periodicity.WEEKLY)));
        } catch(HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void createWrongHabitTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            List<Habit> habits = createHabitList();
            for (Habit habit : habits)
                habitRepository.createHabit(user, habit);
            assertThrows(HabitAlreadyExistsException.class, () -> habitRepository.createHabit(user, habits.getFirst()));
        } catch (HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getWrongHabitTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            habitRepository.createHabit(user, habit);
            assertThrows(NoSuchHabitException.class, () -> {
                habitRepository.findByUserAndName(user, "Habit4");
            });
        } catch(HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void updateHabitTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            habitRepository.createHabit(user, habit);
            habit = new Habit("Habit1", "Description1", Periodicity.WEEKLY);
            habitRepository.updateHabit(user, habit);
            assertEquals(habit, habitRepository.findByUserAndName(user, "habit1"));
        } catch(NoSuchHabitException | HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteHabitTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            habitRepository.createHabit(user, habit);
            habitRepository.deleteHabit(user, habit);
            assertThrows(NoSuchHabitException.class, () -> habitRepository.findByUserAndName(user, "habit1"));
        } catch(NoSuchHabitException | HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<Habit> createHabitList(){
        List<Habit> habitList = new ArrayList<>();
        habitList.add(new Habit("Habit1", "Description1", Periodicity.DAILY));
        habitList.add(new Habit("Habit2", "Description2", Periodicity.WEEKLY));
        habitList.add(new Habit("Habit3", "Description3", Periodicity.WEEKLY));
        return habitList;
    }
}
