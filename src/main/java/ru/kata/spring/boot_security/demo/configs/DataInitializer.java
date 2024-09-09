package ru.kata.spring.boot_security.demo.configs;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    public DataInitializer(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {
        try {
            if (roleService.count() == 0) {
                System.out.println("Creating roles...");
                roleService.save(new Role("ROLE_USER"));
                roleService.save(new Role("ROLE_ADMIN"));
            }

            if (userService.count() == 0) {
                System.out.println("Creating admin user...");
                Set<Role> roles = new HashSet<>(roleService.findAll());
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword("admin");
                admin.setPhoneNumber(9991112233L);
                admin.setRoles(roles);
                userService.save(admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
