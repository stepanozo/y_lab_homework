package repository;


import homework1.entity.habit.Habit;
import homework1.entity.habit.Periodicity;
import homework1.entity.habitRecord.HabitRecord;
import homework1.exceptions.*;
import homework1.repository.HabitRecordRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

@RunWith(MockitoJUnitRunner.class)
public class HabitRepositoryRecordTest {

    @InjectMocks
    private HabitRecordRepository habitRecordRepository;

    @Test
    public void createAndGetHabitRecordTest() {
        try {
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            List<HabitRecord> habitRecords = createHabitRecordList();
            for (HabitRecord habitRecord : habitRecords)
                habitRecordRepository.createHabitRecord(habit, habitRecord);
            List<HabitRecord> newHabitRecordList = new ArrayList<>();
            newHabitRecordList.add(habitRecordRepository.getHabitRecordById(habit, 1L));
            newHabitRecordList.add(habitRecordRepository.getHabitRecordById(habit, 2L));
            newHabitRecordList.add(habitRecordRepository.getHabitRecordById(habit, 3L));
            assertEquals(habitRecords, newHabitRecordList);
            assertEquals(habitRecords, habitRecordRepository.getHabitRecordsByHabit(habit).entrySet().stream().toList());
        } catch(NoSuchHabitRecordException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getWrongHabitRecordsTest() {
        try {
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            HabitRecord habitRecord = new HabitRecord(LocalDate.of(2024, 10, 14));
            habitRecordRepository.createHabitRecord(habit, habitRecord);
            assertThrows(NoSuchHabitRecordException.class, () -> {
                habitRecordRepository.getHabitRecordById(habit, 5L);
            });
        } catch(HabitRecordAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void updateHabitTest() {
        try {
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            HabitRecord habitRecord = new HabitRecord(LocalDate.of(2024, 10, 14));
            habitRecordRepository.createHabitRecord(habit, habitRecord);
            habitRecord = habitRecordRepository.getHabitRecordById(habit, 1L);
            habitRecord.setDate(LocalDate.of(2024, 5, 10));
            habitRecordRepository.updateHabitRecord(habit, habitRecord);
            assertEquals(habitRecord, habitRecordRepository.getHabitRecordById(habit, 1L));
        } catch(NoSuchHabitRecordException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteHabitTest() {
        try {
            Habit habit = new Habit("Habit1", "Description1", Periodicity.DAILY);
            HabitRecord habitRecord = new HabitRecord(LocalDate.of(2024, 10, 14));
            habitRecordRepository.createHabitRecord(habit, habitRecord);
            habitRecordRepository.deleteById(habit, 1L);
            assertThrows(NoSuchHabitRecordException.class, () -> habitRecordRepository.getHabitRecordById(habit, 1L));
        } catch(NoSuchHabitRecordException e) {
            System.out.println(e.getMessage());
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<HabitRecord> createHabitRecordList(){
        List<HabitRecord> habitRecordList = new ArrayList<>();
        habitRecordList.add(new HabitRecord(LocalDate.of(2024, 10, 14)));
        habitRecordList.add(new HabitRecord(LocalDate.of(2024, 10, 13)));
        habitRecordList.add(new HabitRecord(LocalDate.of(2024, 10, 12)));
        return habitRecordList;
    }
}
