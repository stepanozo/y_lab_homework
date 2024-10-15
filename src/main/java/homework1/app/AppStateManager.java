package homework1.app;

import homework1.entity.habit.Habit;
import homework1.entity.habit.Periodicity;
import homework1.entity.habitRecord.HabitRecord;
import homework1.entity.user.User;
import homework1.exceptions.*;
import homework1.repository.HabitRecordRepository;
import homework1.repository.HabitRepository;
import homework1.repository.UserRepository;
import homework1.security.UserAuthoriser;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;

import static homework1.app.UserState.*;


/**
 * Класс, отвечающий за различные состояния пользователя
 */
public class AppStateManager {
    private static Scanner in = new Scanner(System.in);
    private static User currentUser;
    @Getter
    private static UserState userState = INITIAL_STATE;
    private static List<Habit> shownHabits = new ArrayList<>();
    private static List<HabitRecord> shownHabitRecords = new ArrayList<>();
    private static List<User> shownUsers = new ArrayList<>();
    private static Habit shownHabit;
    private static User shownUser;
    private static HabitRecord shownHabitRecord;
    private static Predicate<Habit> habitFilter = habit -> true;

    private static HabitRepository habitRepository = new HabitRepository();
    private static HabitRecordRepository habitRecordRepository = new HabitRecordRepository();
    private static UserRepository userRepository = new UserRepository();
    private static UserAuthoriser userAuthoriser = new UserAuthoriser();


    /**
     * Стартовое состояние
     */
    public static void initialState(){
        System.out.println("Приветствуем в приложении для отслеживания привычек! Выберите опцию: \n1. Авторизация \n2. Регистрация \n");
        String response = in.nextLine();
        switch (response){
            case "1":
                userState = LOGIN_STATE;
                break;
            case "2":
                userState = REGISTER_STATE;
                break;
        }
    }

    /**
     * Состояние входа в систему
     */
    public static void loginState(){
        try {
            System.out.println("Введите логин и пароль (на двух разных строках)");
            String login = in.nextLine();
            String password = in.nextLine();

            if (!userAuthoriser.authorise(login, password)) {
                System.out.println("Не удалось авторизоваться!");
                suggestToGoBack(INITIAL_STATE);
            } else {
                System.out.println("Успешная авторизация!");
                userState = MAIN_MENU_STATE;
                currentUser = userRepository.getUser(login);
            }
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
            userState = LOGIN_STATE;
        }
    }

    /**
     * Состояние регистрации в систему
     */
    public static void registerState(){
        try {
            System.out.println("Введите логин и пароль (на двух разных строках)");
            String login = in.nextLine();
            String password = in.nextLine();
            System.out.println("Теперь введите почту");
            String email = in.nextLine();
            userRepository.createUser(new User(login, password, email, false));
            System.out.println("Успешная регистрация!");
            userState = LOGIN_STATE;
        } catch (UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
            suggestToGoBack(INITIAL_STATE);
        }
    }

    /**
     * Главное меню приложения
     */
    public static void mainMenuState(){
        if(currentUser.isAdmin())
            System.out.println("\n1. Изменить аккаунт\n2. Удалить аккаунт\n3. Добавить привычку\n4. Просмотр привычек\n5. Просмотр пользователей\n6. Выход");
        else
            System.out.println("\n1. Изменить аккаунт\n2. Удалить аккаунт\n3. Добавить привычку\n4. Просмотр привычек\n5. Выход");
        String response = in.nextLine();
        switch (response){
            case "1":
                userState = UPDATE_ACCOUNT_STATE;
                break;
            case "2":
                userState = DELETE_ACCOUNT_STATE;
                break;
            case "3":
                userState = CREATE_HABIT_STATE;
                break;
            case "4":
                userState = CHOOSING_HABIT_FILTER_STATE;
                break;
            case "5":
                if(!currentUser.isAdmin())
                    userState = LOGIN_STATE;
                else userState = WATCHING_USERS_STATE;
                break;
            case "6":
                if(currentUser.isAdmin())
                    userState = LOGIN_STATE;
                break;
        }
    }

    /**
     * Просмотр списка пользователей
     */
    public static void watchingUsersState(){
        System.out.println("Список пользователей: ");
        shownUsers = userRepository.getUserList();
        for(int i = 0; i< shownUsers.size(); i++) {
            System.out.print(i + 1);
            System.out.print(". " + shownUsers.get(i).getName() + "\n");
        }

        System.out.println("Выберите пользователя, либо вариант \"0\", чтобы выйти в меню. ");

        String response = in.nextLine();
        if (response.equals("0"))
            userState = MAIN_MENU_STATE;
        else {
            shownUser = shownUsers.get(Integer.parseInt(response) - 1);
            userState = WATCHING_USER_STATE;
        }
    }

    /**
     * Просмотр аккаунта пользователя
     */
    public static void watchingUserState(){
        System.out.println(shownUser.getName());
        System.out.println(shownUser.getEmail());
        System.out.println(shownUser.isBlocked() ? "Заблокирован ": "Активен");
        System.out.println((!shownUser.isBlocked() ? "\n1. Блокировать" : "\n1. Разблокировать") + "\n2. Удалить\n3. Назад");
        String response = in.nextLine();
        switch (response){
            case "1":
                userState = BLOCK_USER_STATE;
                break;
            case "2":
                userState = DELETE_USER_STATE;
                break;
            case "3":
                userState = MAIN_MENU_STATE;
                break;
        }
    }

    /**
     * Удаление пользователя
     */
    public static void deleteUserState(){
        if(currentUser.getName().equals(shownUser.getName())){
            System.out.println("Нельзя указывать на себя");
            userState = WATCHING_USERS_STATE;
        }
        System.out.println("Вы уверены, что хотите удалить пользователя " + shownUser.getName() + "?\n1. Да, удалить\n2. Нет, не стоит");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    userRepository.deleteUser(shownUser);
                    System.out.println("Пользователь успешно удалён");
                    userState = WATCHING_USERS_STATE;
                } catch (NoSuchUserException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                userState = WATCHING_USERS_STATE;
                break;
        }
    }

    /**
     * Блокирование пользователя
     */
    public static void blockUserState(){
        if(currentUser.getName().equals(shownUser.getName())){
            System.out.println("Нельзя указывать на себя");
            userState = WATCHING_USERS_STATE;
        }
        System.out.println("Вы уверены, что хотите " + (shownUser.isBlocked() ? "раз" : "за") + "блокировать пользователя " + shownUser.getName() + "?\n1. Да, заблокировать\n2. Нет, не стоит");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    shownUser.setBlocked(!shownUser.isBlocked());
                    userRepository.updateUser(shownUser);
                    System.out.println("Пользователь успешно " + (shownUser.isBlocked() ? "раз" : "за") + "блокирован");
                    userState = WATCHING_USERS_STATE;
                } catch (NoSuchUserException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                userState = WATCHING_USERS_STATE;
                break;
        }
    }

    /**
    * Обновление аккаунта пользователя
     * */
    public static void updateAccountState(){
        System.out.println("Что вы хотите обновить?\n1. Имя\n2. Пароль\n3. Email \n4. Назад");
        String response = in.nextLine();
        switch (response){
            case "1":
                suggestUpdateUserName();
                break;
            case "2":
                suggestUpdatePassword();
                break;
            case "3":
                suggestUpdateEmail();
                break;
            case "4":
                userState = MAIN_MENU_STATE;
                break;
        }
    }

    /**
     * Удаление аккаунта пользователя
     */
    public static void deleteAccountState(){
        System.out.println("Вы уверены, что хотите удалить аккаунт?\n1. Да, удалить\n2. Нет, не стоит");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    userRepository.deleteUser(currentUser);
                    System.out.println("Пользователь успешно удалён");
                    userState = LOGIN_STATE;
                } catch (NoSuchUserException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                userState = MAIN_MENU_STATE;
                break;
        }
    }

    /**
     * Создание новой привычки
     */
    public static void createHabitState(){
        System.out.println("Введите название привычки");
        String habitName = in.nextLine();
        System.out.println("Введите описание привычки");
        String habitDescription = in.nextLine();
        System.out.println("Выберите периодичность:\n1. Ежедневная\n2. Еженедельная\n3. Отмена");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    habitRepository.createHabit(currentUser, new Habit(habitName, habitDescription, Periodicity.DAILY));
                    System.out.println("Привычка успешно создана");
                } catch (HabitAlreadyExistsException e){
                    System.out.println(e.getMessage());
                }
                userState = MAIN_MENU_STATE;
                break;
            case "2":
                try {
                    habitRepository.createHabit(currentUser, new Habit(habitName, habitDescription, Periodicity.WEEKLY));
                    System.out.println("Привычка успешно создана");
                } catch (HabitAlreadyExistsException e){
                    System.out.println(e.getMessage());
                }
                userState = MAIN_MENU_STATE;
                break;
            case "3":
                userState = MAIN_MENU_STATE;
                break;
        }
    }

    /**
     * Выбор фильтра для привычек
     */
    public static void choosingHabitFilterState(){
        System.out.println("Хотите отфильтровать привычки?\n1. Да\n2. Нет\n3. Назад");
        String response = in.nextLine();
        boolean cancelFilterChooseflg = false;
        switch (response){
            case "1":
                System.out.println("По какому критерию хотите отфильтровать привычки?\n1. По дате создания\n2. По статусу\n3. Отмена");
                response = in.nextLine();
                switch (response){
                    case "1":
                        System.out.println("Введите самую раннюю дату интервала в формате dd.mm.yyyy");
                        LocalDate beginDate = DatePicker.PickDate();
                        System.out.println("Введите самую позднюю дату интервала в формате dd.mm.yyyy");
                        LocalDate endDate = DatePicker.PickDate();
                        habitFilter = (habit -> {
                            return (habit.getCreationDate().isAfter(beginDate) || habit.getCreationDate().equals(beginDate))
                                    && habit.getCreationDate().isBefore(endDate);
                        });
                        break;
                    case "2":
                        System.out.println("1. Только активные\n2. Только неактивные\n3. Отмена");
                        response = in.nextLine();
                        switch (response){
                            case "1":
                                habitFilter = (Habit::isActive);
                                break;
                            case "2":
                                habitFilter = (habit -> {return !habit.isActive();});
                                break;
                            case "3":
                                cancelFilterChooseflg = true;
                                break;
                        }
                        break;
                    case "3":
                        cancelFilterChooseflg = true;
                        break;
                }
                if(cancelFilterChooseflg)
                    break;
                //Тут специально не пишем break, идём дальше, чтобы полученный предикат сохранился и перешёл в следующий пункт switch.
            case "2":
                userState = WATCHING_HABIT_LIST_STATE;
                break;
            case "3":
                userState = MAIN_MENU_STATE;
                break;
        }
    }

    /**
     * Просмотр привычек пользователя
     */
    public static void watchingHabitListState(){
        System.out.println("Список привычек: ");
        shownHabits = habitRepository.getHabitsWithCondition(currentUser, habitFilter).stream().toList();
        for(int i = 0; i< shownHabits.size(); i++) {
            System.out.print(i + 1);
            System.out.print(". " + shownHabits.get(i).getName() + "\n");
        }

        System.out.println("Выберите привычку, либо вариант \"0\", чтобы выйти в меню. ");

        String response = in.nextLine();
        if (response.equals("0"))
            userState = MAIN_MENU_STATE;
        else {
            shownHabit = shownHabits.get(Integer.parseInt(response) - 1);
            userState = WATCHING_HABIT_STATE;
        }
    }

    /**
     * Просмотр привычки пользователя
     */
    public static void watchingHabitState(){
        System.out.println(shownHabit.getName());
        System.out.println(shownHabit.getDescription());
        System.out.println(shownHabit.getPeriodicity() == Periodicity.WEEKLY ? "Еженедельная" : "Ежедневная");

        System.out.println("\n1. Редактировать\n2. Удалить\n3. Деактивировать.\n4. Отметить выполненной\n5. Смотреть статистику \n6. Назад");
        String response = in.nextLine();
        switch (response){
            case "1":
                userState = UPDATE_HABIT_STATE;
                break;
            case "2":
                userState = DELETE_HABIT_STATE;
                break;
            case "3":
                try {
                    shownHabit.setActive(false);
                    habitRepository.updateHabit(currentUser, shownHabit);
                    System.out.println("Привычка успешно деактивирована");
                } catch (NoSuchHabitException e) {
                    System.out.println(e.getMessage());
                }
                userState = WATCHING_HABIT_STATE;
                break;
            case "4":
                HabitRecord habitRecord = new HabitRecord(LocalDate.now());
                habitRecordRepository.createHabitRecord(shownHabit, habitRecord);
                userState = WATCHING_HABIT_STATE;
                break;
            case "5":
                userState = CHOOSE_HABIT_STATISTICS_STATE;
                break;
            case "6":
                userState = WATCHING_HABIT_LIST_STATE;
                break;
        }
    }

    /**
     * Выбор статистикии о привычке
     */
    public static void chooseHabitStatisticsState(){
        System.out.println("1. История записей\n2. Статистика за период\n3. Текущая серия\n4. Отчёт о прогрессе \n5. Назад");
        String response = in.nextLine();
        switch (response){
            case "1":
                userState = WATCHING_HABIT_RECORDS_STATE;
                break;
            case "2":
                userState = CHOOSING_STATISTICS_STATE;
                break;
            case "3":
                System.out.println("Текущая серия выполнения " + shownHabit.getStreakByDate(LocalDate.now()));
                break;
            case "4":
                System.out.println("Что ты хочешь тут увидеть? Что?! Тебе и так дали 3 пункта со статистикой разной степени хитровыдуманности, какой ещё отчёт?!\n" +
                        "Таких банить надо, поэтому мы тебя выбрасываем из аккаунта на самую первую менюшку.");
                userState = LOGIN_STATE;
                break;
            case "5":
                userState = WATCHING_HABIT_STATE;
                break;
        }
    }

    /**
     * Просмотр записи о привычке
     */
    public static void watchingHabitRecordsState(){
        System.out.println("Список записей: ");
        shownHabitRecords = habitRecordRepository.getHabitRecordsByHabit(shownHabit).values().stream().toList();
        for(int i = 0; i< shownHabitRecords.size(); i++) {
            System.out.print(i + 1);
            System.out.print(". " + shownHabitRecords.get(i).getDate() + "\n");
        }
        System.out.println("Выберите запись, либо вариант \"0\", чтобы выйти в меню. ");
        String response = in.nextLine();
        if (response.equals("0"))
            userState = WATCHING_HABIT_STATE;
        else {
            shownHabitRecord = shownHabitRecords.get(Integer.parseInt(response) - 1);
            userState = WATCHING_HABIT_RECORD_STATE;
        }
    }

    /**
     * Просмотр записи о привычке
     */
    public static void watchingHabitRecordState(){
        System.out.println(shownHabitRecord.getDate());

        System.out.println("\n1. Редактировать\n2. Удалить\n3. Назад");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    System.out.println("Введите новую дату: ");
                    LocalDate date = DatePicker.PickDate();
                    shownHabitRecord.setDate(date);
                    habitRecordRepository.updateHabitRecord(shownHabit, shownHabitRecord);
                    System.out.println("Запись успешно изменена: ");
                } catch (NoSuchHabitRecordException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                userState = DELETE_HABIT_RECORD_STATE;
                break;
            case "3":
                userState = WATCHING_HABIT_STATE;
                break;
        }
    }

    /**
     * Выбор промежутка для статистики по привычке
     */
    public static void choosingStatisticState() {
        System.out.println("1. За день\n2. За неделю\n3. За месяц\n4. Назад");
        String response = in.nextLine();
        LocalDate date;
        switch (response) {
            case "1":
                System.out.println("Введите день в формате \"dd.mm.YYYY\"");
                date = DatePicker.PickDate();
                System.out.println("В течение дня: " + shownHabit.getNumberByDay(date) +
                        "\nВ процентах: " + shownHabit.getSuccessfullPercentageByDay(date));
                break;
            case "2":
                System.out.println("Введите первый день недели в формате \"dd.mm.YYYY\"");
                date = DatePicker.PickDate(DayOfWeek.MONDAY);
                System.out.println("В течение недели: " + shownHabit.getNumberByWeek(date) +
                        "\nВ процентах: " + shownHabit.getSuccessfullPercentageByWeek(date));
                break;
            case "3":
                System.out.println("Введите первый день месяца в формате \"dd.mm.YYYY\"");
                LocalDate localDate = DatePicker.PickDate(1);
                System.out.println("В течение месяца: " + shownHabit.getNumberByMonth(localDate) +
                        "\nВ процентах: " + shownHabit.getSuccessfullPercentageByWeek(localDate));
                break;
            case "4":
                userState = CHOOSING_STATISTICS_STATE;
                break;
        }
    }

    /**
     * Изменение привычки
     */
    public static void updateHabitState(){
        System.out.println("Что вы хотите обновить?\n1. Имя привычки\n2. Описание\n3. Частота \n4. Назад");
        String response = in.nextLine();
        switch (response){
            case "1":
                updateHabitNameState();
                break;
            case "2":
                suggestUpdateHabitDescription();
                break;
            case "3":
                suggestUpdateHabitPeriodicity();
                break;
        }
    }

    /**
     * Удаление записи о привычке
     */
    public static void deleteHabitRecordState(){
        System.out.println("Вы уверены, что хотите удалить запись?\n1. Да, удалить\n2. Нет, не стоит");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    habitRecordRepository.deleteById(shownHabit, shownHabitRecord.getId());
                    System.out.println("Запись успешно удалена");
                    shownHabitRecord = null;
                    userState = WATCHING_HABIT_RECORDS_STATE;
                } catch (NoSuchHabitRecordException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                userState = WATCHING_HABIT_RECORD_STATE;
                break;
        }
    }

    /**
     * Удаление привычки
     */
    public static void deleteHabitState(){
        System.out.println("Вы уверены, что хотите удалить привычку?\n1. Да, удалить\n2. Нет, не стоит");
        String response = in.nextLine();
        switch (response){
            case "1":
                try {
                    habitRepository.deleteHabit(currentUser, shownHabit);
                    System.out.println("Привычка успешно удалена");
                    shownHabit = null;
                    userState = WATCHING_HABIT_LIST_STATE;
                } catch (NoSuchHabitException e) {
                    System.out.println(e.getMessage());
                }
                break;
            case "2":
                userState = WATCHING_HABIT_LIST_STATE;
                break;
        }
    }

    /**
     * Изменение имени привычки
     */
    public static void updateHabitNameState(){
        try {
            System.out.println("Введите имя привычки");
            String name = in.nextLine();
            if(habitRepository.existsByName(currentUser, name)) {
                System.out.println("Привычка с таким именем уже есть.");
                }
            else {
                habitRepository.deleteHabit(currentUser, shownHabit);
                shownHabit.setName(name);
                habitRepository.createHabit(currentUser, shownHabit);
            }
            userState = WATCHING_HABIT_STATE;
        } catch (NoSuchHabitException | HabitAlreadyExistsException e) {
            System.out.println(e.getMessage());
            userState = WATCHING_HABIT_STATE;
        }
    }

    /**
     * Ввод описания привычки
     */
    private static void suggestUpdateHabitDescription(){
        try {
            System.out.println("Введите описание привычки");
            String description = in.nextLine();
            shownHabit.setDescription(description);
            habitRepository.updateHabit(currentUser, shownHabit);
            userState = WATCHING_HABIT_STATE;
        } catch (NoSuchHabitException e) {
            System.out.println(e.getMessage());
            userState = WATCHING_HABIT_STATE;
        }
    }

    /**
     * Выбор периодичности привычки
     */
    private static void suggestUpdateHabitPeriodicity(){
        try {
            System.out.println("Выберите периодичность привычки.\n1. Ежедневная\n2. Еженедельная \n3. Назад");
            String response = in.nextLine();
            if (response.equals("1") || response.equals("2")) {
                shownHabit.setPeriodicity(response.equals("1") ? Periodicity.DAILY : Periodicity.WEEKLY);
                habitRepository.updateHabit(currentUser, shownHabit);
            } else if (response.equals("3")) {
                userState = UPDATE_HABIT_STATE;
            }
            userState = WATCHING_HABIT_STATE;
        } catch (NoSuchHabitException e){
            System.out.println(e.getMessage());
            userState = WATCHING_HABIT_STATE;
        }
    }

    /**
     * Ввод имени пользователя
     */
    private static void suggestUpdateUserName(){
        try {
            System.out.println("Введите имя");
            String login = in.nextLine();
            System.out.println("Подтвердите паролем");
            String password = in.nextLine();
            if (Objects.equals(currentUser.getPassword(), password)) {
                userRepository.deleteUser(currentUser);
                currentUser.setName(login);
                userRepository.createUser(currentUser);
                System.out.println("Логин успешно обновлен.");
                userState = MAIN_MENU_STATE;
            } else {
                System.out.println("Пароль неверный");
                userState = UPDATE_ACCOUNT_STATE;
            }
        } catch (NoSuchUserException | UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
            userState = UPDATE_ACCOUNT_STATE;
        }
    }

    /**
     * Ввод нового пароля пользователя
     */
    private static void suggestUpdatePassword(){
        try {
            System.out.println("Введите пароль");
            String newPassword = in.nextLine();
            System.out.println("Подтвердите старым паролем");
            String password = in.nextLine();
            if (Objects.equals(currentUser.getPassword(), password)) {
                currentUser.setPassword(newPassword);
                userRepository.updateUser(currentUser);
                System.out.println("Пароль успешно обновлен.");
                userState = MAIN_MENU_STATE;
            } else {
                System.out.println("Пароль неверный");
                userState = UPDATE_ACCOUNT_STATE;
            }
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
            userState = UPDATE_ACCOUNT_STATE;
        }
    }


    /**
     * Ввод нового email пользователя
     */
    private static void suggestUpdateEmail(){
        try {
            System.out.println("Введите email");
            String email = in.nextLine();
            System.out.println("Подтвердите паролем");
            String password = in.nextLine();
            if (Objects.equals(currentUser.getPassword(), password)) {
                currentUser.setEmail(email);
                userRepository.updateUser(currentUser);
                System.out.println("Email успешно обновлен.");
                userState = MAIN_MENU_STATE;
            } else {
                System.out.println("Пароль неверный");
                userState = UPDATE_ACCOUNT_STATE;
            }
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
            userState = UPDATE_ACCOUNT_STATE;
        }
    }

    /**
     * Предложение пользователю откатиться на предыдущий шаг
     * @param backState состояние, в которое пользователю предлагается откатиться
     */
    private static void suggestToGoBack(UserState backState){
        System.out.println("\n 1. Продолжить \n 2. Назад\n");
        String response = in.nextLine();
        if(response.equals("2")){
            userState = backState;
        }
    }
}
