package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserServiceImpl userServiceImpl;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserServiceImpl userServiceImpl, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userServiceImpl = userServiceImpl;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        return "index";
    }

    @GetMapping("/user")
    public String showUser(Model model, Authentication authentication) {
        User user = userServiceImpl.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user, @RequestParam List<Long> roleIds) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userServiceImpl.save(user, roleIds);
        return "redirect:/login";
    }
}