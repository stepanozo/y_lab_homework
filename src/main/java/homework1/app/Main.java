package homework1.app;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import homework1.entity.user.User;
import homework1.exceptions.UserAlreadyExistsException;
import homework1.repository.UserRepository;

public class Main {

    private static UserRepository userRepository = new UserRepository();

    public static void main(String[] args) {
        try {
            userRepository.createUser(new User("admin", "password", "admin@gmail.com", true));
        } catch (UserAlreadyExistsException e){
            System.out.println(e.getMessage());
        }
        while(true){
            waitForMessage();
        }
    }

    /**
     * Метод, направляющий пользователя в нужные методы в зависимости от его состояния
     */
    public static void waitForMessage(){
        switch (AppStateManager.getUserState()){
            case INITIAL_STATE:
                AppStateManager.initialState();
                break;
            case LOGIN_STATE:
                AppStateManager.loginState();
                break;
            case REGISTER_STATE:
                AppStateManager.registerState();
                break;
            case MAIN_MENU_STATE:
                AppStateManager.mainMenuState();
                break;
            case UPDATE_ACCOUNT_STATE:
                AppStateManager.updateAccountState();
                break;
            case DELETE_ACCOUNT_STATE:
                AppStateManager.deleteAccountState();
                break;
            case CREATE_HABIT_STATE:
                AppStateManager.createHabitState();
                break;
            case CHOOSING_HABIT_FILTER_STATE:
                AppStateManager.choosingHabitFilterState();
                break;
            case WATCHING_HABIT_LIST_STATE:
                AppStateManager.watchingHabitListState();
                break;
            case WATCHING_HABIT_STATE:
                AppStateManager.watchingHabitState();
                break;
            case UPDATE_HABIT_STATE:
                AppStateManager.updateHabitState();
                break;
            case UPDATE_HABIT_NAME_STATE:
                AppStateManager.updateHabitNameState();
                break;
            case DELETE_HABIT_STATE:
                AppStateManager.deleteHabitState();
                break;
            case CHOOSE_HABIT_STATISTICS_STATE:
                AppStateManager.chooseHabitStatisticsState();
                break;
            case WATCHING_HABIT_RECORDS_STATE:
                AppStateManager.watchingHabitRecordsState();
                break;
            case WATCHING_HABIT_RECORD_STATE:
                AppStateManager.watchingHabitRecordState();
                break;
            case DELETE_HABIT_RECORD_STATE:
                AppStateManager.deleteHabitRecordState();
                break;
            case CHOOSING_STATISTICS_STATE:
                AppStateManager.choosingStatisticState();
                break;
            case WATCHING_USERS_STATE:
                AppStateManager.watchingUsersState();
                break;
            case WATCHING_USER_STATE:
                AppStateManager.watchingUserState();
                break;
            case DELETE_USER_STATE:
                AppStateManager.deleteUserState();
                break;
            case BLOCK_USER_STATE:
                AppStateManager.blockUserState();
                break;
        }
    }
}