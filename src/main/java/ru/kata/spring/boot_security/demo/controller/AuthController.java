package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;


    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String index(Model model, Authentication authentication) {
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        return "index";
    }


    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return "redirect:/user";
        }
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user) {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        user.setRoles(new HashSet<>(roleService.findAllById(list)));
        userService.save(user);
        return "redirect:/login";
    }
}
