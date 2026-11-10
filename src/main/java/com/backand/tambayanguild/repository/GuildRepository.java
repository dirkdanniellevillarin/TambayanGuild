package com.backand.tambayanguild.repository;

import com.backand.tambayanguild.entity.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//For CRUD operations on GuildEntity entity
public interface GuildRepository extends JpaRepository<GuildEntity, Long> {}
