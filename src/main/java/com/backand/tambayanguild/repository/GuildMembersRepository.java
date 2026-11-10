package com.backand.tambayanguild.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backand.tambayanguild.entity.GuildMembersEntity;

@Repository
public interface GuildMembersRepository extends JpaRepository<GuildMembersEntity, Long> {
    List<GuildMembersEntity> findByGuild_Id(Long guildId);
    List<GuildMembersEntity> findByUser_Id(Long userId);
    boolean existsByGuild_IdAndUser_Id(Long guildId, Long userId);
}