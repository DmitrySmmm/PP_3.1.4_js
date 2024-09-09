package ru.kata.spring.boot_security.demo.repositories;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository {
    User findById(Long id);
    List<User> findAll();
    Optional<User> findByUsername(String username);
    void save(User user);
    void update(User user);
    void deleteById(Long id);
    long count();
}
