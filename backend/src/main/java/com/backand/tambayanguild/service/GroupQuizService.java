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
    @Autowired com.backand.tambayanguild.service.ProgressService progress;
    @Autowired com.backand.tambayanguild.repository.UserRepository users;
    @Autowired com.backand.tambayanguild.repository.GuildMembersRepository memberships;
    @Autowired com.backand.tambayanguild.repository.FlashcardRepository flashcards;
    @Autowired com.backand.tambayanguild.repository.GuildRepository guilds;
    @Autowired com.backand.tambayanguild.repository.DeckRepository decks;
    @Autowired com.backand.tambayanguild.repository.QuizResultRepository results;
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;

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
        if(!"WAITING".equals(quiz.getStatus()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"New quizzes must start in WAITING status");
        if(quiz.getTimeLimitSeconds()==null||quiz.getTimeLimitSeconds()<10||quiz.getTimeLimitSeconds()>3600)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Time limit must be between 10 and 3600 seconds");
        var guild = guilds.findById(quiz.getGuild().getId()).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Guild not found"));
        var creator = users.findById(quiz.getCreatedBy().getId()).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"User not found"));
        progress.requireMember(guild.getId(),creator.getId());
        quiz.setGuild(guild); quiz.setCreatedBy(creator);
        if(quiz.getDeck()==null)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"A flashcard deck is required for a quiz");
        var deck=decks.findById(quiz.getDeck().getId()).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Deck not found"));if(!deck.getGuild().getId().equals(guild.getId()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Quiz deck must belong to its guild");if(flashcards.findByDeck_Id(deck.getId()).isEmpty())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Quiz deck needs flashcards");quiz.setDeck(deck);
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
            Long actorId=current.requireUserId();
            GroupQuizEntity quiz = gqrepo.findById(id).get();
            if(!quiz.getCreatedBy().getId().equals(actorId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Only quiz host can edit it");
            if(!"WAITING".equals(quiz.getStatus()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"Only waiting quizzes can be edited");
            quiz.setTitle(newQuizDetails.getTitle());
            if(newQuizDetails.getScheduledAt()!=null)quiz.setScheduledAt(newQuizDetails.getScheduledAt());
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
            Long actorId=current.requireUserId();if(!gqrepo.findById(id).orElseThrow().getCreatedBy().getId().equals(actorId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Only quiz host can delete it");
            results.deleteAll(results.findByGroupQuiz_IdOrderByScoreDesc(id));
            gqrepo.deleteById(id);
            msg = "Group quiz " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }
        return msg;
    }

    @org.springframework.transaction.annotation.Transactional
    public java.util.Map<String,Object> start(Long quizId,Long userId){
        GroupQuizEntity quiz=gqrepo.findById(quizId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Quiz not found"));
        progress.requireMember(quiz.getGuild().getId(),userId);
        if(!quiz.getCreatedBy().getId().equals(userId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Only the host can start this quiz");
        if(!"WAITING".equals(quiz.getStatus()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"Quiz is not waiting");
        if(quiz.getTimeLimitSeconds()==null||quiz.getTimeLimitSeconds()<10||quiz.getTimeLimitSeconds()>3600)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Time limit must be between 10 and 3600 seconds");
        quiz.setStatus("ACTIVE");quiz.setStartedAt(java.time.LocalDateTime.now());quiz.setEndsAt(quiz.getStartedAt().plusSeconds(quiz.getTimeLimitSeconds()));gqrepo.save(quiz);return quizView(quiz,false);
    }

    @org.springframework.transaction.annotation.Transactional
    public java.util.Map<String,Object> submit(Long quizId,Long userId,java.util.Map<Long,String> answers){
        GroupQuizEntity quiz=gqrepo.findById(quizId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Quiz not found"));
        progress.requireMember(quiz.getGuild().getId(),userId);
        if(!"ACTIVE".equals(quiz.getStatus()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"Quiz is not active");
        if(quiz.getDeck()==null)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Quiz needs a deck of questions");
        var cards=flashcards.findByDeck_Id(quiz.getDeck().getId());
        if(cards.isEmpty())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Quiz deck is empty");
        if(answers==null||answers.size()!=cards.size()||cards.stream().anyMatch(c->!answers.containsKey(c.getId())))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Submit one answer for every quiz question");
        int totalQuestions=cards.size();int score=(int)cards.stream().filter(c->c.getAnswer().trim().equalsIgnoreCase(answers.get(c.getId()).trim())).count();
        if(quiz.getEndsAt()!=null&&java.time.LocalDateTime.now().isAfter(quiz.getEndsAt()))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.GONE,"Quiz time has expired");
        if(results.findByGroupQuiz_IdAndUser_Id(quizId,userId).isPresent())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"A result was already submitted");
        var user=progress.user(userId);results.save(new com.backand.tambayanguild.entity.QuizResultEntity(quiz,user,score,totalQuestions));
        int xp=10+(int)Math.round(40.0*score/totalQuestions);progress.recordStudyActivity(userId,xp,Math.max(1,score*2),"QUIZ");
        long participants=results.findByGroupQuiz_IdOrderByScoreDesc(quizId).size();
        if(participants>=memberships.countByGuild_Id(quiz.getGuild().getId())){quiz.setStatus("FINISHED");gqrepo.save(quiz);}
        java.util.Map<String,Object> response=new java.util.LinkedHashMap<>();response.put("quizId",quizId);response.put("score",score);response.put("totalQuestions",totalQuestions);response.put("xpEarned",xp);response.put("status",quiz.getStatus());response.put("correctAnswers",cards.stream().map(c->{java.util.Map<String,Object> item=new java.util.LinkedHashMap<>();item.put("flashcardId",c.getId());item.put("answer",c.getAnswer());return item;}).toList());return response;
    }

    public java.util.Map<String,Object> session(Long quizId,Long userId){GroupQuizEntity quiz=gqrepo.findById(quizId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Quiz not found"));progress.requireMember(quiz.getGuild().getId(),userId);if("ACTIVE".equals(quiz.getStatus())&&quiz.getEndsAt()!=null&&java.time.LocalDateTime.now().isAfter(quiz.getEndsAt())){quiz.setStatus("FINISHED");gqrepo.save(quiz);}return quizView(quiz,"FINISHED".equals(quiz.getStatus()));}
    public java.util.Map<String,Object> results(Long quizId,Long userId){GroupQuizEntity quiz=gqrepo.findById(quizId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Quiz not found"));progress.requireMember(quiz.getGuild().getId(),userId);java.util.Map<String,Object> out=new java.util.LinkedHashMap<>();out.put("quiz",quizView(quiz,false));out.put("results",results.findByGroupQuiz_IdOrderByScoreDesc(quizId).stream().map(r->{java.util.Map<String,Object> row=new java.util.LinkedHashMap<>();row.put("userId",r.getUser().getId());row.put("username",r.getUser().getUsername());row.put("score",r.getScore());row.put("totalQuestions",r.getTotalQuestions());row.put("completedAt",r.getCompletedAt());return row;}).toList());return out;}

    public java.util.Map<String,Object> quizView(GroupQuizEntity quiz,boolean includeAnswers){java.util.Map<String,Object> view=new java.util.LinkedHashMap<>();view.put("id",quiz.getId());view.put("guildId",quiz.getGuild().getId());view.put("title",quiz.getTitle());view.put("status",quiz.getStatus());view.put("timeLimitSeconds",quiz.getTimeLimitSeconds());view.put("scheduledAt",quiz.getScheduledAt());view.put("startedAt",quiz.getStartedAt());view.put("endsAt",quiz.getEndsAt());view.put("hostId",quiz.getCreatedBy().getId());if(quiz.getDeck()!=null){view.put("deckId",quiz.getDeck().getId());view.put("questions",flashcards.findByDeck_Id(quiz.getDeck().getId()).stream().map(c->{java.util.Map<String,Object> card=new java.util.LinkedHashMap<>();card.put("id",c.getId());card.put("question",c.getQuestion());if(includeAnswers)card.put("answer",c.getAnswer());return card;}).toList());}return view;}
}
