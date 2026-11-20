package com.backand.tambayanguild.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backand.tambayanguild.entity.FlashcardEntity;

@Repository
public interface FlashcardRepository extends JpaRepository<FlashcardEntity, Long> {
    List<FlashcardEntity> findByDeck_Id(Long deckId);
}