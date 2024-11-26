package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> showAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void save(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = roleService.findById(1L);
            user.setRoles(Collections.singleton(defaultRole));
        } else {
            // Загружаем роли по их ID
            Set<Role> roles = user.getRoles().stream()
                    .map(role -> roleService.findById(role.getId()))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(User user) {
        // Загружаем пользователя из базы данных
        User userFromDB = userRepository.findById(user.getId());

        if (userFromDB == null) {
            throw new EntityNotFoundException("User not found with ID: " + user.getId());
        }

        // Обновляем только измененные поля
        userFromDB.setPhoneNumber(user.getPhoneNumber());
        userFromDB.setUsername(user.getUsername());

        // Если есть роли, обновляем их
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            Set<Role> roles = user.getRoles().stream()
                    .map(role -> roleService.findById(role.getId())) // Загружаем роли из базы
                    .collect(Collectors.toSet());
            userFromDB.setRoles(roles); // Устанавливаем новые роли
        }

        // Если передан новый пароль
        if (user.getPassword() != null && !user.getPassword().isEmpty() &&
                !user.getPassword().equals(userRepository.findById(user.getId()).getPassword())) {
            userFromDB.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        userRepository.update(userFromDB);
    }

    @Override
    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public long count() {
        return userRepository.count();
    }


    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User '%s' not found", username));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), mapRolesToAuthority(user.getRoles()));
    }

    public Collection<? extends GrantedAuthority> mapRolesToAuthority(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }
}
