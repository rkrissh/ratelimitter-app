package com.sc.ratelimitter.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class UserLoginRequestDto {

    private final String emailId;
    private final String password;

}
