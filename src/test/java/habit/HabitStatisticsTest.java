package habit;

import homework1.entity.habit.Habit;
import homework1.entity.habit.Periodicity;
import homework1.entity.habitRecord.HabitRecord;
import homework1.repository.HabitRecordRepository;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalDate;

public class HabitStatisticsTest {

    HabitRecordRepository habitRecordRepository = new HabitRecordRepository();

    @Test
    public void dailyStreakTest(){
        Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
        LocalDate currDate = LocalDate.of(2024, 10, 14);
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(1)));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(2)));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(3)));
        Assert.assertEquals(4, habit.getStreakByDate(currDate));
    }

    @Test
    public void weeklyStreakTest(){
        Habit habit = new Habit("Habit1", "Description1", Periodicity.WEEKLY);
        LocalDate currDate = LocalDate.of(2024, 10, 14);
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(1)));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(2)));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(3)));
        Assert.assertEquals(2, habit.getStreakByDate(currDate));
    }

    @Test
    public void numberByDayTest(){
        Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
        LocalDate currDate = LocalDate.of(2024, 10, 14);
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        Assert.assertEquals(3, habit.getNumberByDay(currDate));
    }

    @Test
    public void numberByWeekTest(){
        Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
        LocalDate currDate = LocalDate.of(2024, 10, 14);
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate));
        habitRecordRepository.createHabitRecord(habit, new HabitRecord(currDate.minusDays(1)));
        Assert.assertEquals(3, habit.getNumberByWeek(currDate));
    }
}
