package com.backand.tambayanguild.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backand.tambayanguild.entity.DeckEntity;

@Repository
public interface DeckRepository extends JpaRepository<DeckEntity, Long> {
    List<DeckEntity> findByGuild_Id(Long guildId);
}