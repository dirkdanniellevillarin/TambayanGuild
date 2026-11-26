package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.UserQuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface UserQuestRepository extends JpaRepository<UserQuestEntity,Long> {
 Optional<UserQuestEntity> findByUser_IdAndQuest_Id(Long userId,Long questId);
 List<UserQuestEntity> findByUser_IdAndQuest_QuestDate(Long userId,LocalDate date);
}
