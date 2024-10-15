package homework1.repository;

import homework1.entity.user.User;
import homework1.exceptions.NoSuchUserException;
import homework1.exceptions.UserAlreadyExistsException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации CRUD-запросов для пользователя
 */
public class UserRepository {

    @Getter
    private List<User> userList = new ArrayList<>();
    /**
     * Метод, позволяющий занести в базу нового пользователя
     * @param newUser новый пользователь, которого собираемся занести в базу
     * @exception UserAlreadyExistsException посылается, если пользователь с аналогичным именем или email уже существует
     */
    public void createUser(User newUser) throws UserAlreadyExistsException {
        for(User user : userList) {
            if (user.getEmail().equals(newUser.getEmail())) {
                throw new UserAlreadyExistsException("Пользователь с email " + newUser.getEmail() + " уже существует! ", newUser.getEmail());
            } else if (user.getName().equals(newUser.getName())) {
                throw new UserAlreadyExistsException("Пользователь с именем " + newUser.getName() + " уже существует! ", newUser.getName());
            }
        }
        userList.add(newUser);
    }

    /**
     * Метод, позволяющий обновить данные о пользователе
     * @param user пользователь, подлежащий обновлению
     * @exception NoSuchUserException посылается, если пользователь не был найден в базе
     */
    public void updateUser(User user) throws NoSuchUserException{
        if(userList.contains(user))
                userList.set(userList.indexOf(user), user);
        else throw new NoSuchUserException(user.getName(), "Не удалось найти пользователя с именем " + user.getName());
    }

    /**
     * Метод, позволяющий удалить пользователя
     * @param user - пользователь, подлежащий удалению
     * @exception NoSuchUserException посылается, если пользователь не был найден в базе
     */
    public void deleteUser(User user) throws NoSuchUserException{
        if(userList.contains(user))
            userList.remove(user);
        else throw new NoSuchUserException(user.getName(), "Не удалось найти пользователя с именем " + user.getName());
    }

    /**
     * Метод, позволяющий получить пользователя по имени
     * @param name имя, по которому метод ищет пользователя
     * @exception NoSuchUserException посылается, если пользователь не был найден в базе
     * @return возвращает найденного пользователя
     */
    public User getUser(String name) throws NoSuchUserException{
        for(User user : userList)
            if (user.getName().equals(name))
                return user;
        throw new NoSuchUserException(name, "Не удалось найти пользователя с именем " + name);
    }

    /**
     * Метод, позволяющий получить пользователя по имени
     * @param email адрес электронной почты, по которому метод ищет пользователя
     * @exception NoSuchUserException посылается, если пользователь не был найден в базе
     * @return возвращает найденного пользователя
     */
    public User findByEmail(String email) throws NoSuchUserException{
        for(User user : userList)
            if (user.getEmail().equals(email))
                return user;
        throw new NoSuchUserException(email, "Не удалось найти пользователя с email " + email);
    }
}
