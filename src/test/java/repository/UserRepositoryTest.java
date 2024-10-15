package repository;


import homework1.entity.user.User;
import homework1.exceptions.NoSuchUserException;
import homework1.exceptions.UserAlreadyExistsException;
import homework1.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserRepositoryTest {
    private UserRepository userRepository = new UserRepository();

    @Test
    public void createUserTest() {
        try {
            List<User> userList = createUserList();
            for (User user : userList)
                userRepository.createUser(user);

            List<User> newUserList = userRepository.getUserList();
            assertEquals(userList, newUserList);
        } catch(UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void createWrongUserTest() {
        try {
            List<User> userList = createUserList();
            for (User user : userList)
                userRepository.createUser(user);
            assertThrows(UserAlreadyExistsException.class, () -> userRepository.createUser(userList.getFirst()));
        } catch (UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getUserTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            userRepository.createUser(user);
            assertEquals(user, userRepository.getUser("user1"));
        } catch(NoSuchUserException | UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void getWrongUserTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            userRepository.createUser(user);
            assertThrows(NoSuchUserException.class, () -> {
                userRepository.getUser ("user");
            });
        } catch(UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void updateUserTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            userRepository.createUser(user);
            user = new User("user1", "password", "NEWuser@email.ru", true);
            userRepository.updateUser(user);
            assertEquals(user, userRepository.getUser("user1"));
        } catch(NoSuchUserException | UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void deleteUserTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            userRepository.createUser(user);
            userRepository.deleteUser(user);
            assertThrows(NoSuchUserException.class, () -> userRepository.getUser("user1"));
        } catch(NoSuchUserException | UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void findByEmailTest() {
        try {
            User user = new User("user1", "password", "user@email.ru", false);
            userRepository.createUser(user);
            assertEquals(user, userRepository.findByEmail("user@email.ru"));
        } catch(NoSuchUserException | UserAlreadyExistsException e) {
            System.out.println(e.getMessage());
        }
    }

    // ===================================================================================================================
    // = Implementation
    // ===================================================================================================================

    private List<User> createUserList(){
        List<User> userList = new ArrayList<>();
        userList.add(new User("user1", "password", "user@email.ru", false));
        userList.add(new User("user2", "password2", "user2@email.ru", false));
        userList.add(new User("user3", "password3", "user3@email.ru", false));
        return userList;
    }
}
