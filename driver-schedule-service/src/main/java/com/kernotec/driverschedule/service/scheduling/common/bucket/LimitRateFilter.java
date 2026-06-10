package com.kernotec.driverschedule.service.scheduling.common.bucket;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class LimitRateFilter extends OncePerRequestFilter {

    private final BucketService bucketService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
        throws ServletException, IOException
    {
        String clientIdentifier = getClientIdentifier(request);
        Bucket bucket = bucketService.getBucket(clientIdentifier);

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            sendErrorResponse(response, clientIdentifier);
        }
    }

    private String getClientIdentifier(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private void sendErrorResponse(HttpServletResponse response, String clientIdentifier)
        throws IOException
    {
        int httpStatusValue = HttpStatus.TOO_MANY_REQUESTS.value();

        log.error(
            "[CODE]: {} - [MESSAGE]: Too many requests, rate limit exceeded for client: {} ",
            httpStatusValue, clientIdentifier
        );

        response.setStatus(httpStatusValue);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.RETRY_AFTER, "60");
        objectMapper.writeValue(
            response.getWriter(), BucketErrorResponse.builder()
                .code(httpStatusValue)
                .error("Too many requests")
                .message(
                    "You have exceeded the number of allowed requests. Please try again later.")
                .build()
        );
    }
}
