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
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;
    @Autowired com.backand.tambayanguild.service.ProgressService progress;

    public GroupQuizController() {
    }

    // Create
    @PostMapping("/postNewGroupQuiz")
    public Object postGroupQuiz(@RequestBody GroupQuizEntity quiz) {
        current.requireUser(quiz.getCreatedBy()==null?null:quiz.getCreatedBy().getId());quiz.setCreatedBy(new com.backand.tambayanguild.entity.UserEntity());quiz.getCreatedBy().setId(current.requireUserId());
        return gqserv.quizView(gqserv.postGroupQuiz(quiz),false);
    }

    @GetMapping("/{id}/session")
    public Object getSession(@PathVariable Long id, @RequestParam(required=false) Long userId) {
        userId=current.requireUserId();
        current.requireUser(userId);
        return gqserv.session(id, userId);
    }

    @GetMapping("/{id}/results") public Object getResults(@PathVariable Long id,@RequestParam(required=false) Long userId){userId=current.requireUserId();return gqserv.results(id,userId);}

    @PostMapping("/{id}/start")
    public Object start(@PathVariable Long id, @RequestParam(required=false) Long userId) { userId=current.requireUserId();return gqserv.start(id,userId); }

    @PostMapping("/{id}/submit")
    public Object submit(@PathVariable Long id, @RequestParam(required=false) Long userId, @RequestBody QuizSubmission submission) {
        userId=current.requireUserId();return gqserv.submit(id,userId,submission.answers());
    }

    public record QuizSubmission(java.util.Map<Long,String> answers) {}

    // Read
    @GetMapping("/getAllGroupQuizzes")
    public List<Object> getAllGroupQuizzes() {
        Long userId=current.requireUserId();return gqserv.getAllGroupQuizzes().stream().filter(q->{try{progress.requireMember(q.getGuild().getId(),userId);return true;}catch(org.springframework.web.server.ResponseStatusException ex){return false;}}).map(q->(Object)gqserv.quizView(q,false)).toList();
    }

    @GetMapping("/getGroupQuizzesByGuild/{guildId}")
    public List<Object> getGroupQuizzesByGuild(@PathVariable Long guildId) {
        current.requireUser(null);progress.requireMember(guildId,current.requireUserId());return gqserv.getGroupQuizzesByGuild(guildId).stream().map(q->(Object)gqserv.quizView(q,false)).toList();
    }

    // Update
    @PutMapping("/putGroupQuizDetails")
    public Object putGroupQuizDetails(@RequestParam Long id,@RequestParam(required=false) Long actorId, @RequestBody GroupQuizEntity newQuizDetails) {
        current.requireUser(actorId);
        return gqserv.quizView(gqserv.putGroupQuizDetails(id, newQuizDetails),false);
    }

    // Delete
    @DeleteMapping("/deleteGroupQuiz/{id}")
    public String deleteGroupQuiz(@PathVariable Long id,@RequestParam(required=false) Long actorId) {
        current.requireUser(actorId);
        return gqserv.deleteGroupQuiz(id);
    }
}
