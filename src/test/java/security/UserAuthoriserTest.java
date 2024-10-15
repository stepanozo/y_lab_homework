package security;


import homework1.entity.user.User;
import homework1.exceptions.NoSuchUserException;
import homework1.repository.UserRepository;
import homework1.security.UserAuthoriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class UserAuthoriserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    protected UserAuthoriser userAuthoriser;

    @Test
    public void noSuchUserTest() {
        try {
            Mockito.when(userRepository.getUser("user")).thenThrow(new NoSuchUserException("Нет такого пользователя user", "user"));
            boolean success = userAuthoriser.authorise("user", "password");
            assertFalse(success);
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void correctAuthorisationTest(){
        try {
            Mockito.when(userRepository.getUser("user")).thenReturn(new User("user", "password", "user@email.ru", false));
            boolean success = userAuthoriser.authorise("user", "password");
            assertTrue(success);
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void incorrectPasswordTest(){
        try {
            Mockito.when(userRepository.getUser("user")).thenReturn(new User("user", "password", "user@email.ru", false));
            boolean success = userAuthoriser.authorise("user", "WRONGpassword");
            assertFalse(success);
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void userBlockedTest(){
        try {
            User user = new User("user", "password", "user@email.ru", false);
            user.setBlocked(true);
            Mockito.when(userRepository.getUser("user")).thenReturn(user);
            boolean success = userAuthoriser.authorise("user", "password");
            assertFalse(success);
        } catch (NoSuchUserException e) {
            System.out.println(e.getMessage());
        }
    }
}
