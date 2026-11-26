package com.backand.tambayanguild.repository;
import com.backand.tambayanguild.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity,Long> {
 List<ChatMessageEntity> findTop100ByGuild_IdOrderBySentAtDesc(Long guildId);
}
