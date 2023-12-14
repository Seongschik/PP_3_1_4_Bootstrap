package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.Repository.RoleRepository;
import ru.kata.spring.boot_security.demo.Repository.UserRepository;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private UserRepository userRepository;
    private UserService userService;
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserRepository userRepository, UserService userService, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }
    @GetMapping
    public String getAllUsers(Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        User user = userRepository.findById(1L).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + 1L));
        model.addAttribute("user", user);
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("allRoles", allRoles);
        StringBuilder roleIds = new StringBuilder();
        for (Role role : allRoles) {
            roleIds.append(role.getId()).append(",");
        }
        model.addAttribute("roleIds", roleIds.toString());

        return "all-users";
    }

    @GetMapping("/admin/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "redirect:/saveUser";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@ModelAttribute("user") User user, Model model) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("user", user);
            model.addAttribute("allRoles", roleRepository.findAll());
            return "redirect:/admin";
        }
        userRepository.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            model.addAttribute("error", "Username already exists");
            model.addAttribute("user", user);
            model.addAttribute("allRoles", roleRepository.findAll());
            return "redirect:/admin";
        }
        System.out.println("Saving user: " + user);
        userRepository.save(user);
        System.out.println("Saved user: " + user);
        return "redirect:/admin";
    }

//    @GetMapping("/editUser")
//    public ResponseEntity<?> updateUser(@PathVariable Long id, Model model) {
//        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
////        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        model.addAttribute("user", user);
//        return ResponseEntity.ok().build();
//    }

    @PostMapping("/editUser")
    public ResponseEntity<?> saveUpdatedUser(@RequestBody User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Обновление пароля только в том случае, если он предоставлен
        if (user.getNewPassword() != null && !user.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getNewPassword()));
        }

        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/deleteUser={id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/admin/getRoleByName/{name}")
    public Long getRoleIdByName(@PathVariable String name) {
        Role role = roleRepository.findByName(name);
        if (role != null) {
            return role.getId();
        } else {
            return null;
        }
    }

    @GetMapping("/allRoles")
    public String getAllRoles(Model model) {
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("allRoles", allRoles);
        return "allRoles";
    }

}