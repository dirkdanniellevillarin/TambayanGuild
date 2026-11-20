package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.FlashcardEntity;
import com.backand.tambayanguild.service.FlashcardService;

@RestController
@RequestMapping("/api/flashcards")
public class FlashcardController {
    @Autowired
    FlashcardService fserv;

    public FlashcardController() {
    }

    // Create
    @PostMapping("/postNewFlashcard")
    public FlashcardEntity postFlashcard(@RequestBody FlashcardEntity flashcard) {
        return fserv.postFlashcard(flashcard);
    }

    // Read
    @GetMapping("/getAllFlashcards")
    public List<FlashcardEntity> getAllFlashcards() {
        return fserv.getAllFlashcards();
    }

    @GetMapping("/getFlashcardsByDeck/{deckId}")
    public List<FlashcardEntity> getFlashcardsByDeck(@PathVariable Long deckId) {
        return fserv.getFlashcardsByDeck(deckId);
    }

    // Update
    @PutMapping("/putFlashcardDetails")
    public FlashcardEntity putFlashcardDetails(@RequestParam Long id, @RequestBody FlashcardEntity newFlashcardDetails) {
        return fserv.putFlashcardDetails(id, newFlashcardDetails);
    }

    // Delete
    @DeleteMapping("/deleteFlashcard/{id}")
    public String deleteFlashcard(@PathVariable Long id) {
        return fserv.deleteFlashcard(id);
    }
}