package com.backand.tambayanguild.repository;

import com.backand.tambayanguild.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//For CRUD operations on UserEntity entity
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmailIgnoreCase(String email);
    java.util.Optional<UserEntity> findByEmailIgnoreCase(String email);
    boolean existsByUsernameAndIdNot(String username, Long id);
    java.util.List<UserEntity> findAllByOrderByXpDesc();
}
