package com.backand.tambayanguild.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backand.tambayanguild.entity.GroupQuizEntity;

@Repository
public interface GroupQuizRepository extends JpaRepository<GroupQuizEntity, Long> {
    List<GroupQuizEntity> findByGuild_Id(Long guildId);
}