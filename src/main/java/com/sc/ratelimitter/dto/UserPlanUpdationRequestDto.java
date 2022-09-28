package com.sc.ratelimitter.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class UserPlanUpdationRequestDto {

    private final UUID planId;

}
