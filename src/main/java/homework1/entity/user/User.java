package homework1.entity.user;

import homework1.entity.habit.Habit;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class User {
    @Setter
    private String name;
    @Setter
    private String email;
    @Setter
    private String password;
    @Setter
    private boolean isAdmin;
    @Setter
    private boolean blocked = false;
    private Set<Habit> habitSet = new HashSet<>();

    /**
     * Конструктор пользователя
     * @param name имя пользователя
     * @param password пароль пользователя
     * @param email адрес электронной почты пользователя
     * @param isAdmin является пользователь админом или нет
     */
    public User(String name, String password, String email, boolean isAdmin) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return name.equals(user.name) &&
            email.equals(user.email) &&
            password.equals(user.password) &&
            isAdmin == user.isAdmin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
