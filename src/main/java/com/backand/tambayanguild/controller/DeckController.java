package com.backand.tambayanguild.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.backand.tambayanguild.entity.DeckEntity;
import com.backand.tambayanguild.service.DeckService;

@RestController
@RequestMapping("/api/decks")
public class DeckController {
    @Autowired
    DeckService dserv;

    public DeckController() {
    }

    // Create
    @PostMapping("/postNewDeck")
    public DeckEntity postDeck(@RequestBody DeckEntity deck) {
        return dserv.postDeck(deck);
    }

    // Read
    @GetMapping("/getAllDecks")
    public List<DeckEntity> getAllDecks() {
        return dserv.getAllDecks();
    }

    @GetMapping("/getDecksByGuild/{guildId}")
    public List<DeckEntity> getDecksByGuild(@PathVariable Long guildId) {
        return dserv.getDecksByGuild(guildId);
    }

    // Update
    @PutMapping("/putDeckDetails")
    public DeckEntity putDeckDetails(@RequestParam Long id, @RequestBody DeckEntity newDeckDetails) {
        return dserv.putDeckDetails(id, newDeckDetails);
    }

    // Delete
    @DeleteMapping("/deleteDeck/{id}")
    public String deleteDeck(@PathVariable Long id) {
        return dserv.deleteDeck(id);
    }
}