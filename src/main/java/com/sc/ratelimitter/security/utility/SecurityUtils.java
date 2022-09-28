package com.sc.ratelimitter.security.utility;

import com.sc.ratelimitter.model.User;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class SecurityUtils {

    public org.springframework.security.core.userdetails.User convert(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getPassword(), List.of());
    }

}