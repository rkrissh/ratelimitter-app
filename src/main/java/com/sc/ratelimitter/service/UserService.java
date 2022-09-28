package com.sc.ratelimitter.service;

import com.sc.ratelimitter.dto.UserAccountCreationRequestDto;
import com.sc.ratelimitter.dto.UserLoginRequestDto;
import com.sc.ratelimitter.dto.UserLoginSuccessDto;
import com.sc.ratelimitter.util.jwt.JwtUtils;
import com.sc.ratelimitter.model.User;
import com.sc.ratelimitter.model.UserPlan;
import com.sc.ratelimitter.repository.PlanRepository;
import com.sc.ratelimitter.repository.UserPlanRepository;
import com.sc.ratelimitter.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;
    @Autowired
    private  PlanRepository planRepository;
    @Autowired
    private  UserPlanRepository userPlanRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<?> signUp(UserAccountCreationRequestDto userAccountCreationRequestDto) {

        if (userRepository.existsByEmailId(userAccountCreationRequestDto.getEmailId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "user account already eixsts with provided email-id");

        final var user = new User();
        user.setEmailId(userAccountCreationRequestDto.getEmailId());
        user.setPassword(passwordEncoder.encode(userAccountCreationRequestDto.getPassword()));
        final var savedUser = userRepository.save(user);

        final var plan = planRepository.findById(userAccountCreationRequestDto.getPlanId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid plan-id provided"));

        final var userPlan = new UserPlan();
        userPlan.setIsActive(true);
        userPlan.setUser(savedUser);
        userPlan.setPlan(plan);
        userPlanRepository.save(userPlan);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<UserLoginSuccessDto> login(UserLoginRequestDto userLoginRequestDto) {
        final User user = userRepository.findByEmailId(userLoginRequestDto.getEmailId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login credentials"));

        if (!passwordEncoder.matches(userLoginRequestDto.getPassword(), user.getPassword()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid login credentials");

        return ResponseEntity.ok(UserLoginSuccessDto.builder().jwt(jwtUtils.generateToken(user)).build());
    }

}
