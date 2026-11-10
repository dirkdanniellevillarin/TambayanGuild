package com.backand.tambayanguild.repository;

import com.backand.tambayanguild.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//For CRUD operations on UserEntity entity
public interface UserRepository extends JpaRepository<UserEntity, Long> {}
