package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import java.util.List;

@Repository
public interface RoleRepository {
    Role findById(Long id);
    List<Role> findAll();
    List<Role> findAllById(List<Long> ids);
    void save(Role role);
    void update(Role role);
    void deleteById(Long id);
    long count();
}
