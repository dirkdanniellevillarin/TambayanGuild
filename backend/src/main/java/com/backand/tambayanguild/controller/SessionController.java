package com.backand.tambayanguild.controller;

import com.backand.tambayanguild.service.CurrentUserService;
import com.backand.tambayanguild.service.ProgressService;
import com.backand.tambayanguild.service.UserService;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController @RequestMapping("/api/session") public class SessionController {
 private final CurrentUserService current;private final ProgressService progress;private final UserService users;
 public SessionController(CurrentUserService current,ProgressService progress,UserService users){this.current=current;this.progress=progress;this.users=users;}
 @GetMapping public Object currentUser(){return users.profileView(progress.user(current.requireUserId()));}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){return progress.dashboard(current.requireUserId());}
 @GetMapping("/badges") public Object badges(){return progress.badges(current.requireUserId());}
 @GetMapping("/quests") public Object quests(){return progress.questProgress(current.requireUserId());}
 @PutMapping("/profile") public Object updateProfile(@RequestBody UserController.ProfileUpdate request){Long id=current.requireUserId();return users.profileView(users.updateProfile(id,id,request.displayName(),request.avatarUrl()));}
}
