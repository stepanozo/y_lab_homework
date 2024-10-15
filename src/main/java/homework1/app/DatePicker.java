package homework1.app;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DatePicker {

    /**
     * Метод, заставляющий пользователя выбирать дату
     * @return возвращает дату, введённую пользователем
     */
    public static LocalDate PickDate(){
        Scanner in = new Scanner(System.in);
        String response = in.nextLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate date;
        while(true) {
            try{
                date = LocalDate.parse(response, formatter);
                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Некорректная дата, попробуйте ещё раз.");
            }
            response = in.nextLine();
        }
    }

    /**
     * Метод, заставляющий пользователя выбирать дату, которая должна совпадать с указанным днём недели
     * @param dayOfWeek - день недели, в который пользователь должен попасть при вводе даты
     * @return возвращает дату, введённую пользователем
     */
    public static LocalDate PickDate(DayOfWeek dayOfWeek){
        LocalDate date = PickDate();
        while(date.getDayOfWeek()!= dayOfWeek){
            System.out.println("Введите день недели под номером " + dayOfWeek);
            date = PickDate();
        }
        return date;
    }


    /**
     * Метод, заставляющий пользователя выбирать дату, которая должна совпадать с указанным числом месяца
     * @param dayOfMonth - число, в которое пользователь должен попасть при вводе даты
     * @return возвращает дату, введённую пользователем
     */
    public static LocalDate PickDate(int dayOfMonth){
        LocalDate date = PickDate();
        while(date.getDayOfMonth() != 1){
            System.out.println("Введите день месяца под номером " + dayOfMonth);
            date = PickDate();
        }
        return date;
    }
}
