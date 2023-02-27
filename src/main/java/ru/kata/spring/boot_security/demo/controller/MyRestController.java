package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRestController {
    private final UserService userService;
    public MyRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> showAllUsers() {
        List<User> users = userService.getAllUsers();
        return (users != null && !users.isEmpty())
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    @PostMapping("/admin")
    public ResponseEntity<User> newUser(@RequestBody User user) {
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        userService.removeUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public User getUserById(@PathVariable Integer id) {

        return userService.getUserById(id);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByEmail(Principal principal) {
        System.out.println(principal.getName());
        return new ResponseEntity<>(userService.findByEmail(principal.getName()), HttpStatus.OK);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(userService.getAllRoles(), HttpStatus.OK);
    }

}
