package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.QuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface QuestRepository extends JpaRepository<QuestEntity,Long> {
 List<QuestEntity> findByQuestDate(LocalDate date);
 Optional<QuestEntity> findByQuestDateAndCode(LocalDate date,String code);
}
