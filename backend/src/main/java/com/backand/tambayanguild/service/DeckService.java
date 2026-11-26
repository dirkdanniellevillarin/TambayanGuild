package com.backand.tambayanguild.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backand.tambayanguild.entity.DeckEntity;
import com.backand.tambayanguild.repository.DeckRepository;

@Service
public class DeckService {
    @Autowired
    DeckRepository drepo;
    @Autowired com.backand.tambayanguild.service.ProgressService progress;
    @Autowired com.backand.tambayanguild.repository.GuildRepository guilds;
    @Autowired com.backand.tambayanguild.repository.FlashcardRepository flashcards;
    @Autowired com.backand.tambayanguild.repository.GuildMembersRepository memberships;
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;
    @Autowired com.backand.tambayanguild.repository.GroupQuizRepository quizzes;

    public DeckService() {
    }

    // Create
    @org.springframework.transaction.annotation.Transactional
    public DeckEntity postDeck(DeckEntity deck) {
        deck.setId(null);
        if (deck.getGuild() == null || deck.getGuild().getId()==null || deck.getCreatedBy() == null || deck.getCreatedBy().getId()==null) {
            throw new RuntimeException("Guild and creator are required!");
        }
        current.requireUser(deck.getCreatedBy().getId());Long actorId=current.requireUserId();progress.requireMember(deck.getGuild().getId(),actorId);
        deck.setGuild(guilds.findById(deck.getGuild().getId()).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Guild not found")));
        deck.setCreatedBy(progress.user(actorId));
        DeckEntity saved = drepo.save(deck);
        return saved;
    }

    // Read
    public List<DeckEntity> getAllDecks() {
        return drepo.findAll();
    }

    public List<DeckEntity> getDecksByGuild(Long guildId) {
        return drepo.findByGuild_Id(guildId);
    }

    public void requireMember(Long deckId,Long userId){DeckEntity deck=drepo.findById(deckId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Deck not found"));progress.requireMember(deck.getGuild().getId(),userId);}
    public java.util.List<DeckEntity> getDecksForMember(Long guildId,Long userId){progress.requireMember(guildId,userId);return drepo.findByGuild_Id(guildId);}
    public void requireDeckEditor(Long deckId,Long userId){current.requireUser(userId);userId=current.requireUserId();DeckEntity deck=drepo.findById(deckId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Deck not found"));var member=progress.requireMember(deck.getGuild().getId(),userId);if(!"LEADER".equalsIgnoreCase(member.getRole())&& !deck.getCreatedBy().getId().equals(userId))throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN,"Only the deck creator or guild leader can edit this deck");}

    public java.util.Map<String,Object> deckView(DeckEntity deck){var m=new java.util.LinkedHashMap<String,Object>();m.put("id",deck.getId());m.put("guildId",deck.getGuild().getId());m.put("createdById",deck.getCreatedBy().getId());m.put("title",deck.getTitle());m.put("description",deck.getDescription());m.put("subject",deck.getSubject());m.put("createdAt",deck.getCreatedAt());return m;}

    public List<DeckEntity> getAllDecksForUser(Long userId){return memberships.findByUser_Id(userId).stream().flatMap(member->drepo.findByGuild_Id(member.getGuild().getId()).stream()).distinct().toList();}

    // Update (title, description, subject)
    public DeckEntity putDeckDetails(Long id, DeckEntity newDeckDetails) {
        try {
            Long actorId=current.requireUserId();requireDeckEditor(id,actorId);
            DeckEntity deck = drepo.findById(id).get();
            if(newDeckDetails.getTitle()==null||newDeckDetails.getTitle().isBlank())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Title is required");
            deck.setTitle(newDeckDetails.getTitle());
            deck.setDescription(newDeckDetails.getDescription());
            deck.setSubject(newDeckDetails.getSubject());
            return drepo.save(deck);
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Deck " + id + " does not exist!");
        }
    }

    // Delete
    public String deleteDeck(Long id) {
        Long actorId=current.requireUserId();requireDeckEditor(id,actorId);
        String msg = "";
        if (drepo.findById(id).isPresent()) {
            var deck=drepo.findById(id).orElseThrow();
            if(!quizzes.findByGuild_Id(deck.getGuild().getId()).stream().filter(q->q.getDeck()!=null&&q.getDeck().getId().equals(id)).toList().isEmpty())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.CONFLICT,"Deck is used by a group quiz");
            flashcards.deleteAll(flashcards.findByDeck_Id(id));
            drepo.deleteById(id);
            msg = "Deck " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }
        return msg;
    }
}
