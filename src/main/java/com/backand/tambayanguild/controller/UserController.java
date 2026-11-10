package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.entity.UserEntity;
import com.backand.tambayanguild.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

//REST endpoints setters and getters for UserEntity entity
@RestController
@RequestMapping("/api/users")

public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service; 
    }

    @GetMapping 
    public List<UserEntity> getAll() { 
        return service.findAll(); 
    }

    @GetMapping("/{id}") 
    public UserEntity getOne(@PathVariable Long id) { 
        return service.findById(id); 
    }

    @PostMapping 
    @ResponseStatus(HttpStatus.CREATED) 
    public UserEntity create(@RequestBody UserEntity UserEntity) { 
        return service.create(UserEntity); 
    }

    @PutMapping("/{id}") 
    public UserEntity update(@PathVariable Long id, @RequestBody UserEntity UserEntity) { 
        return service.update(id, UserEntity); 
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id) { 
        service.delete(id); 
    }
}
