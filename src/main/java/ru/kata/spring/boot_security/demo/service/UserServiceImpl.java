package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> showAll() {
        return userRepository.findAll();
    }

    @Transactional
    public void save(User user, List<Long> roles) {
        User newUser = user;
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> userRoles = roleRepository.findAllById(roles);
        newUser.setRoles(new HashSet<>(userRoles));
        userRepository.save(newUser);
    }

    @Transactional
    public void update(User user, Long id, List<Long> roles) {
        User updUser = userRepository.getById(id);
        updUser.setUsername(user.getUsername());
        updUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updUser.setPhoneNumber(user.getPhoneNumber());
        List<Role> updRoles = roleRepository.findAllById(roles);
        updUser.setRoles(new HashSet<>(updRoles));
        userRepository.save(updUser);
    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.getById(id);
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));
        return user;
    }
}
