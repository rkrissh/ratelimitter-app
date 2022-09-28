package com.sc.ratelimitter.security.filter;


import com.sc.ratelimitter.repository.UserRepository;
import com.sc.ratelimitter.service.RateLimitingService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@AllArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final RateLimitingService rateLimitingService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            final var loggedInUser = userRepository.findByEmailId(authentication.getName()).get();
            final Bucket tokenBucket = rateLimitingService.resolveBucket(loggedInUser.getId());
            final ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

            if (!probe.isConsumed()) {
                response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(),
                        "Request limit linked to your current plan has been exhausted");
            }
        }
        filterChain.doFilter(request, response);
    }

}
