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
    private final com.backand.tambayanguild.service.CurrentUserService current;

    public UserService(UserRepository users,com.backand.tambayanguild.service.CurrentUserService current) { 
        this.users = users;this.current=current;
    }
    
    public List<UserEntity> findAll() { 
        return java.util.List.of(user(current.requireUserId()));
    }

    public UserEntity findById(Long id) { 
        current.requireUser(id);return users.findById(id).orElseThrow(() -> new IllegalArgumentException("UserEntity not found: " + id)); 
    }

    private UserEntity user(Long id){return users.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));}

    public UserEntity updateProfile(Long id,Long actorId,String displayName,String avatarUrl){if(!id.equals(actorId))throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You may only update your own profile");UserEntity user=findById(id);if(displayName!=null&&!displayName.isBlank()){if(users.existsByUsernameAndIdNot(displayName.trim(),id))throw new ResponseStatusException(HttpStatus.CONFLICT,"Username is already taken");user.setUsername(displayName.trim());}if(avatarUrl!=null){if(avatarUrl.length()>500)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Avatar URL is too long");user.setAvatarUrl(avatarUrl);}return users.save(user);}

    public java.util.Map<String,Object> profileView(UserEntity user){var view=new java.util.LinkedHashMap<String,Object>();view.put("id",user.getId());view.put("username",user.getUsername());view.put("email",user.getEmail());view.put("createdAt",user.getCreatedAt());view.put("xp",user.getXp());view.put("coins",user.getCoins());view.put("avatarUrl",user.getAvatarUrl());return view;}
    
    public void delete(Long id) { throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Account deletion is not available through this endpoint"); }
}
