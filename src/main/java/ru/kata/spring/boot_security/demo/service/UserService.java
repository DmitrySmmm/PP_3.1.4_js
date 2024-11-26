package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    List<User> showAll();

    void save(User user);

    void update(User user);

    void delete(long id);

    User findById(Long id);

    Optional<User> findByUsername(String username);

    long count();
}
