package com.backand.tambayanguild.repository;

import com.backand.tambayanguild.entity.Guild;
import org.springframework.data.jpa.repository.JpaRepository;

//For CRUD operations on Guild entity
public interface GuildRepository extends JpaRepository<Guild, Long> {}
