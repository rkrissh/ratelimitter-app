package com.sc.ratelimitter.controller;

import com.sc.ratelimitter.dto.PlanResponseDto;
import com.sc.ratelimitter.dto.UserPlanUpdationRequestDto;
import com.sc.ratelimitter.util.jwt.JwtUtils;
import com.sc.ratelimitter.service.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class PlanController {

    @Autowired
    private PlanService planService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping(value = "/plans", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Returns all plan details in the system")
    public ResponseEntity<List<PlanResponseDto>> allPlansInSystemRetreivalHandler() {
        return planService.allPlansInSystemRetreivalHandler();
    }

    @PutMapping(value = "/plan")
    @ResponseStatus(value = HttpStatus.OK)
    @Operation(summary = "Updates logged in users plan")
    public ResponseEntity<?> userPlanUpdationHandler(@Parameter(hidden = true) @RequestHeader(name = "Authorization", required = true) final String header, @RequestBody(required = true) final UserPlanUpdationRequestDto userPlanUpdationRequestDto) {
        return planService.updatePlan(jwtUtils.extractUserId(header), userPlanUpdationRequestDto);
    }


}
