package com.backand.tambayanguild.service;

import com.backand.tambayanguild.entity.User;
import com.backand.tambayanguild.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

//Implements CRUD operations for User entity
@Service
public class UserService {

    private final UserRepository users;

    public UserService(UserRepository users) { 
        this.users = users; 
    }
    
    public List<User> findAll() { 
        return users.findAll(); 
    }

    public User findById(Long id) { 
        return users.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found: " + id)); 
    }

    public User create(User user) { 
        user.setId(null); return users.save(user); 
    }

    public User update(Long id, User input) {
        User user = findById(id);
        user.setUsername(input.getUsername()); user.setEmail(input.getEmail()); user.setPassword(input.getPassword());
        return users.save(user);
    }
    
    public void delete(Long id) { users.delete(findById(id)); }
}
