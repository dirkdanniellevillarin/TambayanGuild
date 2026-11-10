package com.backand.tambayanguild.repository;

import com.backand.tambayanguild.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

//For CRUD operations on User entity
public interface UserRepository extends JpaRepository<User, Long> {}
