package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;


    public UserController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        return "index";
    }

    @GetMapping("/user")
    public String showUser(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/user";
        }
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        userService.save(user, list);
        return "redirect:/login";
    }
}