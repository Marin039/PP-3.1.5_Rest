package ru.kata.spring.boot_security.demo.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.Set;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

@Component
public class Admin {

    private final RoleService roleService;
    private final UserService usersService;

    @Autowired
    public Admin(RoleService roleService, UserService usersService) {
        this.roleService = roleService;
        this.usersService = usersService;
    }

    @PostConstruct
    public void initialization() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);
        User admin = new User("admin", "admin", 30L, "admin","admin@mail.ru");
        admin.setRoles(Set.of(roleAdmin));
        usersService.saveUser(admin);

    }
}