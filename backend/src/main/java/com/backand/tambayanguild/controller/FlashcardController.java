package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.FlashcardEntity;
import com.backand.tambayanguild.service.FlashcardService;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;
    @Autowired
    FlashcardService fserv;

    public FlashcardController() {
    }

    // Create
    @PostMapping("/postNewFlashcard")
    public FlashcardEntity postFlashcard(@RequestBody FlashcardEntity flashcard) {
        current.requireUser(null);
        if(flashcard.getDeck()==null||flashcard.getDeck().getId()==null)throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Deck is required");
        fserv.requireEditorForDeck(flashcard.getDeck().getId(),current.requireUserId());
        return fserv.postFlashcard(flashcard);
    }

    @PostMapping("/decks/{deckId}/review") public Object review(@PathVariable Long deckId,@RequestParam Long userId){current.requireUser(userId);return fserv.reviewDeck(deckId,current.requireUserId());}

    // Read
    @GetMapping("/getAllFlashcards")
    public List<FlashcardEntity> getAllFlashcards() {
        current.requireUser(null);throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Use the deck flashcard endpoint");
    }

    @GetMapping("/getFlashcardsByDeck/{deckId}")
    public List<FlashcardEntity> getFlashcardsByDeck(@PathVariable Long deckId) {
        current.requireUser(null);fserv.requireMember(deckId,current.requireUserId());
        return fserv.getFlashcardsByDeck(deckId).stream().map(c->{var view=new FlashcardEntity();view.setId(c.getId());view.setQuestion(c.getQuestion());view.setAnswer(c.getAnswer());view.setDeck(null);return view;}).toList();
    }

    // Update
    @PutMapping("/putFlashcardDetails")
    public FlashcardEntity putFlashcardDetails(@RequestParam Long id,@RequestParam(required=false) Long actorId, @RequestBody FlashcardEntity newFlashcardDetails) {
        current.requireUser(actorId);
        current.requireUser(null);fserv.requireEditor(id,current.requireUserId());
        return fserv.putFlashcardDetails(id, newFlashcardDetails);
    }

    // Delete
    @DeleteMapping("/deleteFlashcard/{id}")
    public String deleteFlashcard(@PathVariable Long id,@RequestParam(required=false) Long actorId) {
        current.requireUser(actorId);
        current.requireUser(null);fserv.requireEditor(id,current.requireUserId());
        return fserv.deleteFlashcard(id);
    }
}
