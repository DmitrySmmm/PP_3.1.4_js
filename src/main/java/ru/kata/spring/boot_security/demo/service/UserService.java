package ru.kata.spring.boot_security.demo.service;

import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public interface UserService {
    public List<User> showAll();

    public void save(User user, List<Long> roles);

    public void update(User user, Long id, List<Long> roles);

    public void delete(long id);

    public User findById(Long id);

    public Optional<User> findByUsername(String username);

    public long count();
}
