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
    @Autowired com.backand.tambayanguild.service.ProgressService progress;
    @Autowired com.backand.tambayanguild.repository.DeckRepository decks;
    @Autowired com.backand.tambayanguild.service.DeckService deckService;
    @Autowired com.backand.tambayanguild.service.CurrentUserService current;

    public FlashcardService() {
    }

    // CREATE: add a new flashcard 
    public FlashcardEntity postFlashcard(FlashcardEntity flashcard) {
        Long userId=current.requireUserId();
        // Set id to null so the database makes a new id by itself
        flashcard.setId(null);

        if (flashcard.getDeck() == null) {
            throw new RuntimeException("Deck is required!");
        }
        if(flashcard.getDeck().getId()==null||flashcard.getQuestion()==null||flashcard.getQuestion().isBlank()||flashcard.getAnswer()==null||flashcard.getAnswer().isBlank())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Deck, question, and answer are required");
        var deck = decks.findById(flashcard.getDeck().getId()).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND, "Deck not found"));
        deckService.requireDeckEditor(deck.getId(),userId);
        flashcard.setDeck(deck);
        FlashcardEntity saved = frepo.save(flashcard);
        return saved;
    }

    // READ: get flashcards
    public List<FlashcardEntity> getAllFlashcards() {
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Use the deck flashcard endpoint");
    }

    // Get only the flashcards of one deck
    public List<FlashcardEntity> getFlashcardsByDeck(Long deckId) {
        return frepo.findByDeck_Id(deckId);
    }

    public void requireMember(Long deckId,Long userId){deckService.requireMember(deckId,userId);}
    public void requireEditorForDeck(Long deckId,Long userId){deckService.requireDeckEditor(deckId,userId);}
    public void requireEditor(Long cardId,Long userId){var card=frepo.findById(cardId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Flashcard not found"));deckService.requireDeckEditor(card.getDeck().getId(),userId);}

    @org.springframework.transaction.annotation.Transactional
    public java.util.Map<String,Object> reviewDeck(Long deckId,Long userId){current.requireUser(userId);userId=current.requireUserId();var deck=decks.findById(deckId).orElseThrow(()->new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Deck not found"));progress.requireMember(deck.getGuild().getId(),userId);var cards=frepo.findByDeck_Id(deckId);if(cards.isEmpty())throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST,"Deck has no flashcards");progress.recordStudyActivity(userId,5,2,"CARDS");var view=new java.util.LinkedHashMap<String,Object>();view.put("deckId",deckId);view.put("reviewedCount",cards.size());view.put("xpEarned",5);view.put("flashcards",cards.stream().map(c->{var m=new java.util.LinkedHashMap<String,Object>();m.put("id",c.getId());m.put("question",c.getQuestion());m.put("answer",c.getAnswer());return m;}).toList());return view;}

    //UPDATE: change the question and answer
    public FlashcardEntity putFlashcardDetails(Long id, FlashcardEntity newFlashcardDetails) {
        try {
            // Find the old flashcard
            FlashcardEntity flashcard = frepo.findById(id).get();
            deckService.requireDeckEditor(flashcard.getDeck().getId(),current.requireUserId());

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
