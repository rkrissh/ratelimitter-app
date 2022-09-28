package com.sc.ratelimitter.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class UserLoginSuccessDto {

    private final String jwt;

}
