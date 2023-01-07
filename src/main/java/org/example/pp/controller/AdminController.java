package org.example.pp.controller;

import org.example.pp.model.Role;
import org.example.pp.model.User;
import org.example.pp.service.RoleServiceImp;
import org.example.pp.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleServiceImp roleService;
    public AdminController(UserService userService, RoleServiceImp roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String showAllUsers(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("email", user.getEmail());
        return "main";
    }

    @RequestMapping("delete")
    public String deleteUser(@RequestParam long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }

    @RequestMapping("new")
    public String getNewUserForm(Model model) {
        model.addAttribute("roles",  roleService.getAllRoles());
        return "newUser";
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public String saveUser(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam Integer age,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam Map<String, String> form) {


        Set<String> roles = roleService.getAllRoles().stream().map(Role::getName).collect(Collectors.toSet());
        Set<Role> userRoles = new HashSet<>();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                userRoles.add(roleService.getRole(key));
            }
        }

        userService.saveUser(new User(firstName, lastName, age, email, password, userRoles));
        return "redirect:/admin";
    }

    @RequestMapping("edit/{user}")
    public String getUserEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user",  user);
        model.addAttribute("roles",  roleService.getAllRoles());
        return "editUser";
    }

    @RequestMapping(value = "edit/{user}", method = RequestMethod.POST)
    public String updateUser(@RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam Integer age,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam Map<String, String> form,
                             @RequestParam Long id) {

        User user = userService.findUserById(id).get();

        user.getRoles().clear();
        Set<String> roles = roleService.getAllRoles().stream().map(Role::getName).collect(Collectors.toSet());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(roleService.getRole(key));
            }
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        user.setPassword(password);

        userService.saveUser(user);

        return "redirect:/admin";
    }
}
