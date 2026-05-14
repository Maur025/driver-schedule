package com.kernotec.driverscheduleauth.config.bucket;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class ThrottlingFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException
    {
        String clientIdentifier = getClientIdentifier(request);
        Bucket bucket = rateLimitService.resolveBucket(clientIdentifier);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            log.error(
                "code: {}, message: Too many requests, Rate limit exceeded for client: {}",
                HttpStatus.TOO_MANY_REQUESTS.value(), clientIdentifier
            );
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setHeader("Retry-After", "60");
            response.getWriter()
                .write("""
                    {
                    "error": "Too many requests - Rate limit exceeded",
                    "message": "You have exceeded the number of allowed requests. Please try again later."
                    }
                    """);
        }
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
