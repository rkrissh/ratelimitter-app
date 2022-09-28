package com.sc.ratelimitter.repository;


import com.sc.ratelimitter.model.Plan;
import com.sc.ratelimitter.model.User;
import com.sc.ratelimitter.model.UserPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserPlanRepository extends JpaRepository<UserPlan, Integer> {

    Boolean existsByUserAndPlan(final User user, final Plan plan);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE user_plan_mappings SET is_active = false WHERE user_id = ?1")
    void invalidatePreviousPlans(final UUID user);

    Optional<UserPlan> findByUserIdAndIsActive(final UUID userId, final Boolean isActives);

}
