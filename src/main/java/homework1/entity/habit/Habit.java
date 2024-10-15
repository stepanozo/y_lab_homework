package homework1.entity.habit;

import homework1.entity.habitRecord.HabitRecord;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Habit {
    private static long currentId = 0;
    @Getter
    private long id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private Periodicity periodicity;
    private final LocalDate creationDate;
    @Setter
    private boolean isActive;
    private Map<Long, HabitRecord> habitRecords = new HashMap<>();

    /**
     * Конструктор привычки
     * @param name имя привычки
     * @param description описание привычки
     * @param periodicity периодичность привычки (ежедневная, еженедельная)
     */
    public Habit(String name, String description, Periodicity periodicity) {
        currentId++;
        this.id = currentId;
        this.name = name;
        this.description = description;
        this.periodicity = periodicity;
        this.creationDate = LocalDate.now();
        this.isActive = true;
    }

    /**
     * Метод, позволяющий узнать, сколько раз привычка была выполнена в выбранный день
     * @param date день, за который метод считает кол-во исполнения привычки
     * @return количество выполнений привычки за день
     */
    public long getNumberByDay(LocalDate date){
        return habitRecords.entrySet().stream()
                .filter(en -> en.getValue().getDate().equals(date))
                .count();
    }

    /**
     * Метод, позволяющий узнать, сколько раз привычка была выполнена в выбранную неделю
     * @param date первый день недели
     * @return количество выполнений привычки за неделю
     */
    public int getNumberByWeek(LocalDate date){
        LocalDate finalDate = date.plusDays(7);
        int count = 0;
        if(date.getDayOfWeek() == DayOfWeek.MONDAY)
            count = (int)habitRecords.entrySet().stream().filter(en -> en.getValue().getDate().isEqual(date)).count();

        while(true){
            LocalDate finalCurrDate = finalDate;
            if(habitRecords.entrySet().stream().anyMatch(en -> en.getValue().getDate().equals(finalCurrDate)))
                count++;
            finalDate = finalDate.minusDays(1);
            if(finalDate.equals(date))
                break;
        }
        return count;
    }

    /**
     * Метод, позволяющий узнать, сколько раз привычка была выполнена за месяц
     * @param date первый день месяца
     * @return количество выполнений привычки за месяц
     */
    public int getNumberByMonth(LocalDate date){
        LocalDate finalDate = date.plusDays(1);
        while (finalDate.getDayOfMonth()!=1)
            finalDate = finalDate.plusDays(1);

        int count = 0;
        while(!finalDate.equals(date) ) {
            LocalDate finalCurrDate = finalDate;
            if(habitRecords.entrySet().stream().anyMatch(en -> en.getValue().getDate().equals(finalCurrDate)))
                count++;
            finalDate = finalDate.minusDays(1);
        }
        return count;
    }

    /**
     * Метод, позволяющий узнать, сколько раз привычка была выполнена в выбранный день в процентах
     * @param date день, за который метод считает кол-во исполнения привычки
     * @return количество выполнений привычки за день в процентах
     */
    public int getSuccessfullPercentageByDay(LocalDate date) {
        if(habitRecords.entrySet().stream().anyMatch(en -> en.getValue().getDate().equals(date)))
            return 100;
        return 0;
    }

    /**
     * Метод, позволяющий узнать, сколько раз привычка была выполнена в выбранную неделю в процентах
     * @param date первый день недели
     * @return количество выполнений привычки за неделю в процентах
     */
    public double getSuccessfullPercentageByWeek(LocalDate date) {
        LocalDate finalDate = date.plusDays(7);

        if(periodicity == Periodicity.WEEKLY) {
            if(habitRecords.entrySet().stream()
                    .anyMatch(en -> en.getValue().getDate().isBefore(finalDate)
                            && en.getValue().getDate().isAfter(finalDate.minusDays(8)) ))
                    return 100;
            return 0;
        } else
            return (double)getNumberByWeek(date)/7 * 100;
    }

    /**
     * Метод, позволяющий узнать, сколько раз привычка была выполнена за месяц в процентах
     * @param date первый день месяца
     * @return количество выполнений привычки за месяц в процентах
     */
    public double getSuccessfullPercentageByMonth(LocalDate date) {
        LocalDate finalDate = date;
        while (finalDate.getDayOfMonth()!=1)
            finalDate = date.plusDays(1);

        int count = 0;
        if(periodicity == Periodicity.WEEKLY) {

            for(int i =0; i < 4; i++) {
                LocalDate date1 = date;
                if (habitRecords.entrySet().stream()
                        .anyMatch(en -> en.getValue().getDate().isBefore(date1.plusDays(7))
                                && en.getValue().getDate().isAfter(date1.minusDays(1))))
                    count++;
                date = date.plusDays(7);
            }

            for(Map.Entry<Long, HabitRecord> entry: habitRecords.entrySet()){
                LocalDate finalCurrDate = date;
                if(habitRecords.entrySet().stream().anyMatch(en -> en.getValue().getDate().equals(finalCurrDate))) {
                    count++;
                    break;
                }
                date = date.minusDays(1);
                if(finalDate.equals(date))
                    break;
            }
            return (double)count/4 * 100;

        } else {
            count = getNumberByMonth(date);
            return (double)count/30 * 100;
        }
    }

    /**
     * Метод, позволяющий посчитать текущую серию
     * @return количество выполнений подряд
     */
    public int getStreakByDate(LocalDate currDate){
        int streak = 0;
        if(periodicity == Periodicity.DAILY)
        {

            for(Map.Entry<Long, HabitRecord> entry: habitRecords.entrySet()){
                LocalDate finalCurrDate = currDate;
                if(habitRecords.entrySet().stream().anyMatch(en -> en.getValue().getDate().equals(finalCurrDate)))
                        streak++;
                    else break;
                currDate = currDate.minusDays(1);
            }
        } else {

            LocalDate finalCurrDate = currDate;
            if(currDate.getDayOfWeek() == DayOfWeek.MONDAY && habitRecords.entrySet().stream()
                    .anyMatch(en -> en.getValue().getDate().isEqual(finalCurrDate)))
                streak++;
            while(currDate.getDayOfWeek() != DayOfWeek.MONDAY) {
                currDate = currDate.plusDays(1);
            }
            while(true){
                LocalDate finalCurrDate1 = currDate;
                if(habitRecords.entrySet().stream()
                        .noneMatch(en -> en.getValue().getDate().isBefore(finalCurrDate1)
                                &&en.getValue().getDate().isAfter(finalCurrDate1.minusDays(8)) ))
                    break;
                streak++;
                currDate = currDate.minusDays(7);
            }
        }
        return streak;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Habit habit = (Habit) o;
        return name.equals(habit.name) &&
            description.equals(habit.description) &&
            periodicity.equals(habit.periodicity) &&
            creationDate.equals(habit.creationDate) &&
            isActive == habit.isActive;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
