package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();
    User getUserById (Integer id);
    User findByEmail (String email);
    void saveUser (User user);
    void removeUserById(Integer id);
    void updateUser (User updateUse);
    public UserDetails loadUserByUsername(String email);
    public List<Role> getAllRoles();


}
