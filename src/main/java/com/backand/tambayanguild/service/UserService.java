package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.UserEntity;
import com.backand.tambayanguild.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

//Implements CRUD operations for UserEntity entity
@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) { 
        this.users = users; 
    }
    
    public List<UserEntity> findAll() { 
        return users.findAll(); 
    }

    public UserEntity findById(Long id) { 
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("UserEntity not found: " + id)); 
    }

    public UserEntity create(UserEntity UserEntity) { 
        UserEntity.setId(null); 
        if (users.existsByUsername(UserEntity.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        return users.save(UserEntity); 
    }

    public UserEntity update(Long id, UserEntity input) {
        UserEntity UserEntity = findById(id);
        if (users.existsByUsernameAndIdNot(input.getUsername(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already taken");
        }
        UserEntity.setUsername(input.getUsername()); 
        UserEntity.setEmail(input.getEmail()); 
        UserEntity.setPassword(input.getPassword());
        return users.save(UserEntity);
    }
    
    public void delete(Long id) { users.delete(findById(id)); }
}
