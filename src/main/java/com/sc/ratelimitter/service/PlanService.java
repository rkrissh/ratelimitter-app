package com.sc.ratelimitter.service;

import com.sc.ratelimitter.dto.PlanResponseDto;
import com.sc.ratelimitter.dto.UserPlanUpdationRequestDto;
import com.sc.ratelimitter.model.UserPlan;
import com.sc.ratelimitter.repository.PlanRepository;
import com.sc.ratelimitter.repository.UserPlanRepository;
import com.sc.ratelimitter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PlanService {
    @Autowired
    private  PlanRepository planRepository;
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  UserPlanRepository userPlanRepository;
    @Autowired
    private  RateLimitingService rateLimitingService;

    public ResponseEntity<List<PlanResponseDto>> allPlansInSystemRetreivalHandler() {
        return ResponseEntity
                .ok(planRepository
                        .findAll().parallelStream().map(plan -> PlanResponseDto.builder().id(plan.getId())
                                .name(plan.getName()).limitPerHour(plan.getLimitPerHour()).build())
                        .collect(Collectors.toList()));
    }

    @Transactional
    public ResponseEntity<?> updatePlan(final UUID loggedInUser,
            final UserPlanUpdationRequestDto userPlanUpdationRequestDto) {
        final var user = userRepository.findById(loggedInUser).get();
        final var plan = planRepository.findById(userPlanUpdationRequestDto.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid plan-id provided"));

        if (userPlanRepository.existsByUserAndPlan(user, plan))
            return ResponseEntity.ok().build();

        userPlanRepository.invalidatePreviousPlans(loggedInUser);

        final var userPlan = new UserPlan();
        userPlan.setUser(user);
        userPlan.setPlan(plan);
        userPlan.setIsActive(true);
        userPlanRepository.save(userPlan);

        rateLimitingService.deleteIfExists(user.getId());
        return ResponseEntity.ok().build();
    }

}
