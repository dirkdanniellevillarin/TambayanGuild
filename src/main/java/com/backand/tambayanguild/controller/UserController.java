package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.entity.User;
import com.backand.tambayanguild.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//REST endpoints setters and getters for user entity
@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service; 
    }

    @GetMapping 
    public List<User> getAll() { 
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public User getOne(@PathVariable Long id) { 
        return service.findById(id); 
    }

    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED) 
    public User create(@RequestBody User user) { 
        return service.create(user); 
    }

    @PutMapping("/{id}") 
    public User update(@PathVariable Long id, @RequestBody User user) { 
        return service.update(id, user); 
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id) { 
        service.delete(id); 
    }
}
