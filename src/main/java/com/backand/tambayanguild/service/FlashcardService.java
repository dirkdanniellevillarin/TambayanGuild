package com.backand.tambayanguild.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.backand.tambayanguild.entity.FlashcardEntity;
import com.backand.tambayanguild.repository.FlashcardRepository;

@Service
public class FlashcardService {

    @Autowired
    FlashcardRepository frepo;

    public FlashcardService() {
    }

    // CREATE: add a new flashcard 
    public FlashcardEntity postFlashcard(FlashcardEntity flashcard) {
        // Set id to null so the database makes a new id by itself
        flashcard.setId(null);

        if (flashcard.getDeck() == null) {
            throw new RuntimeException("Deck is required!");
        }
        return frepo.save(flashcard);
    }

    // READ: get flashcards
    public List<FlashcardEntity> getAllFlashcards() {
        return frepo.findAll();
    }

    // Get only the flashcards of one deck
    public List<FlashcardEntity> getFlashcardsByDeck(Long deckId) {
        return frepo.findByDeck_Id(deckId);
    }

    //UPDATE: change the question and answer
    public FlashcardEntity putFlashcardDetails(Long id, FlashcardEntity newFlashcardDetails) {
        try {
            // Find the old flashcard
            FlashcardEntity flashcard = frepo.findById(id).get();

            flashcard.setQuestion(newFlashcardDetails.getQuestion());
            flashcard.setAnswer(newFlashcardDetails.getAnswer());

            return frepo.save(flashcard);
        } catch (NoSuchElementException ex) {
            throw new RuntimeException("Flashcard " + id + " does not exist!");
        }
    }

    //DELETE: remove a flashcard 
    public String deleteFlashcard(Long id) {
        String msg = "";

        // Check first if the flashcard exists
        if (frepo.findById(id).isPresent()) {
            frepo.deleteById(id);
            msg = "Flashcard " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }

        return msg;
    }
}