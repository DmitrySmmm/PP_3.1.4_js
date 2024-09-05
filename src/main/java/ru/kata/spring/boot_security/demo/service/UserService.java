package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> showAll();

    void save(User user, List<Long> roles);

    void update(User user, List<Long> roles);

    void delete(long id);

    User findById(Long id);

    Optional<User> findByUsername(String username);

    long count();
}
