package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleService = roleService;
    }
    @Override
    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseGet(User::new);
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = findByEmail(email);
        if (user ==null) {
            throw new UsernameNotFoundException(String.format(" '%s' not found!", email));
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                mapPolesToAuthority(user.getRoles()));
    }
    private Collection <? extends GrantedAuthority> mapPolesToAuthority (Collection <Role> roles) {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getName())).collect(Collectors.toList());
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {
        Optional<User> userById = userRepository.findById(id);
        if (userById.isPresent()) {
            return userById.get();
        } else {
            throw new UsernameNotFoundException(String.format("User with %s not found", id));
        }
    }


    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeUserById(Integer id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUser(User updateUser) {
        User user = userRepository.getById(updateUser.getId());
        if(!user.getPassword().equals(updateUser.getPassword())) {
            updateUser.setPassword(bCryptPasswordEncoder.encode(updateUser.getPassword()));
        }
        userRepository.save(updateUser);
    }
    @Override
    public List<Role> getAllRoles() {
        return roleService.getRoles();
    }

}
