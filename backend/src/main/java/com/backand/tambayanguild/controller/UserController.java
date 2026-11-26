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
    private final com.backand.tambayanguild.service.CurrentUserService current;

    private final UserService service;

    public UserController(UserService service,com.backand.tambayanguild.service.CurrentUserService current) {
        this.service = service;this.current=current;
    }

    @GetMapping 
    public List<Object> getAll() { 
        current.requireUserId();
        return service.findAll().stream().map(u -> (Object)service.profileView(u)).toList(); 
    }

    @GetMapping("/{id}") 
    public Object getOne(@PathVariable Long id) { 
        current.requireUser(id);
        return service.profileView(service.findById(id)); 
    }

    @PutMapping("/{id}/profile")
    public Object updateProfile(@PathVariable Long id,@RequestParam(required=false) Long actorId,@RequestBody ProfileUpdate request){current.requireUser(actorId);return service.profileView(service.updateProfile(id,current.requireUserId(),request.displayName(),request.avatarUrl()));}
    public record ProfileUpdate(String displayName,String avatarUrl){}

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) 
    public void delete(@PathVariable Long id) { 
        current.requireUser(id);service.delete(id); 
    }
}
