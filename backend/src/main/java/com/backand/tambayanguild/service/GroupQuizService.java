package com.backand.tambayanguild.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backand.tambayanguild.entity.GroupQuizEntity;
import com.backand.tambayanguild.repository.GroupQuizRepository;

@Service
public class GroupQuizService {
    @Autowired
    GroupQuizRepository gqrepo;

    public GroupQuizService() {
    }

    // Create
    public GroupQuizEntity postGroupQuiz(GroupQuizEntity quiz) {
        quiz.setId(null);
        if (quiz.getGuild() == null || quiz.getCreatedBy() == null) {
            throw new RuntimeException("Guild and creator are required!");
        }
        if (quiz.getStatus() == null) {
            quiz.setStatus("WAITING");
        }
        return gqrepo.save(quiz);
    }

    // Read
    public List<GroupQuizEntity> getAllGroupQuizzes() {
        return gqrepo.findAll();
    }

    public List<GroupQuizEntity> getGroupQuizzesByGuild(Long guildId) {
        return gqrepo.findByGuild_Id(guildId);
    }

    // Update (title, status, time limit)
    public GroupQuizEntity putGroupQuizDetails(Long id, GroupQuizEntity newQuizDetails) {
        try {
            GroupQuizEntity quiz = gqrepo.findById(id).get();
            quiz.setTitle(newQuizDetails.getTitle());
            quiz.setStatus(newQuizDetails.getStatus());
            quiz.setTimeLimitSeconds(newQuizDetails.getTimeLimitSeconds());
            return gqrepo.save(quiz);
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Group quiz " + id + " does not exist!");
        }
    }

    // Delete
    public String deleteGroupQuiz(Long id) {
        String msg = "";
        if (gqrepo.findById(id).isPresent()) {
            gqrepo.deleteById(id);
            msg = "Group quiz " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }
        return msg;
    }
}