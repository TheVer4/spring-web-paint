package com.example.springwebpaint.service;

import com.example.springwebpaint.domain.Role;
import com.example.springwebpaint.domain.User;
import com.example.springwebpaint.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class RegistrationService {

    @Autowired
    private UserRepo userRepo;

    public String registerUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if(userFromDb != null) {
            model.put("message", "User already exists");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepo.save(user);

        return "redirect:/main";

    }
}
