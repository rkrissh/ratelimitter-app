package com.sc.ratelimitter.repository;


import com.sc.ratelimitter.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Boolean existsByEmailId(final String emailId);

    Optional<User> findByEmailId(final String emailId);

}
