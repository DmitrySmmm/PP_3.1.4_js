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

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {

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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> userRoles = roleRepository.findAllById(roles);
        user.setRoles(new HashSet<>(userRoles));
        userRepository.save(user);
    }

    @Transactional
    public void update(User user, List<Long> roles) {
        User existingUser = userRepository.findById(user.getId());

        if (existingUser != null) {
            // Если пользователь изменил пароль (он не совпадает с хешем)
            if (!user.getPassword().equals(existingUser.getPassword())) {
                // Хешируем новый пароль
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            } else {
                // Если пароль не изменен, оставляем текущий хэш
                existingUser.setPassword(existingUser.getPassword());
            }

            // Обновляем остальные данные пользователя
            existingUser.setUsername(user.getUsername());
            existingUser.setPhoneNumber(user.getPhoneNumber());

            // Обновляем роли
            List<Role> updatedRoles = roleRepository.findAllById(roles);
            existingUser.setRoles(new HashSet<>(updatedRoles));

            // Сохраняем изменения в базе данных
            userRepository.update(existingUser);
        }
    }

//    @Transactional
//    public void update(User user, List<Long> roles) {
//        if (user != null) {
//            user.setUsername(user.getUsername());
//            user.setPassword(passwordEncoder.encode(user.getPassword()));
//            user.setPhoneNumber(user.getPhoneNumber());
//            List<Role> updRoles = roleRepository.findAllById(roles);
//            user.setRoles(new HashSet<>(updRoles));
//            userRepository.update(user);
//        }
//    }

    @Transactional
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

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
