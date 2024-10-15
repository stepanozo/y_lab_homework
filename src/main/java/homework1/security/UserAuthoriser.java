package homework1.security;

import homework1.entity.user.User;
import homework1.exceptions.NoSuchUserException;
import homework1.repository.UserRepository;

/**
 * Класс для авторизации пользователя
 */
public class UserAuthoriser {

    private UserRepository userRepository;

    /**
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return true, если авторизация успешна и false при неудаче
     */
    public boolean authorise(String login, String password) {
        try {
            User user = userRepository.getUser(login);
            return user.getPassword().equals(password) && !user.isBlocked();
        } catch (NoSuchUserException e){
            return false;
        }
    }
}
