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

    public DeckService() {
    }

    // Create
    public DeckEntity postDeck(DeckEntity deck) {
        deck.setId(null);
        if (deck.getGuild() == null || deck.getCreatedBy() == null) {
            throw new RuntimeException("Guild and creator are required!");
        }
        return drepo.save(deck);
    }

    // Read
    public List<DeckEntity> getAllDecks() {
        return drepo.findAll();
    }

    public List<DeckEntity> getDecksByGuild(Long guildId) {
        return drepo.findByGuild_Id(guildId);
    }

    // Update (title, description, subject)
    public DeckEntity putDeckDetails(Long id, DeckEntity newDeckDetails) {
        try {
            DeckEntity deck = drepo.findById(id).get();
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
        String msg = "";
        if (drepo.findById(id).isPresent()) {
            drepo.deleteById(id);
            msg = "Deck " + id + " is successfully deleted!";
        } else {
            msg = id + " does not exist!";
        }
        return msg;
    }
}