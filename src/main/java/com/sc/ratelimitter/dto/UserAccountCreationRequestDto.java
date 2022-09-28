package com.sc.ratelimitter.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Data
@Builder
@Jacksonized
public class UserAccountCreationRequestDto {

    private final String emailId;
    private final String password;
    private final UUID planId;

}
