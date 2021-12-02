package com.mycompany.authservice.repository;

import com.mycompany.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUserUUID(UUID userUUID);
    List<User> findAllByIsEnabledIsFalse();
    void deleteByUserUUID(UUID userUUID);
}