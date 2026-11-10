package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.GroupQuizEntity;
import com.backand.tambayanguild.service.GroupQuizService;

@RestController
@RequestMapping("/api/group-quizzes")
public class GroupQuizController {
    @Autowired
    GroupQuizService gqserv;

    public GroupQuizController() {
    }

    // Create
    @PostMapping("/postNewGroupQuiz")
    public GroupQuizEntity postGroupQuiz(@RequestBody GroupQuizEntity quiz) {
        return gqserv.postGroupQuiz(quiz);
    }

    // Read
    @GetMapping("/getAllGroupQuizzes")
    public List<GroupQuizEntity> getAllGroupQuizzes() {
        return gqserv.getAllGroupQuizzes();
    }

    @GetMapping("/getGroupQuizzesByGuild/{guildId}")
    public List<GroupQuizEntity> getGroupQuizzesByGuild(@PathVariable Long guildId) {
        return gqserv.getGroupQuizzesByGuild(guildId);
    }

    // Update
    @PutMapping("/putGroupQuizDetails")
    public GroupQuizEntity putGroupQuizDetails(@RequestParam Long id, @RequestBody GroupQuizEntity newQuizDetails) {
        return gqserv.putGroupQuizDetails(id, newQuizDetails);
    }

    // Delete
    @DeleteMapping("/deleteGroupQuiz/{id}")
    public String deleteGroupQuiz(@PathVariable Long id) {
        return gqserv.deleteGroupQuiz(id);
    }
}