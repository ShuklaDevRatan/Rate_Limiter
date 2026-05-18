package com.drs.Rate_Limiter.Config;

import com.drs.Rate_Limiter.DTO.ErrorResponseDTO;
import com.drs.Rate_Limiter.Service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        boolean result = rateLimiterService.getCount(request.getRemoteAddr());

        if (!result) {

            response.setStatus(429);
            response.setContentType("application/json");

            ErrorResponseDTO error = new ErrorResponseDTO(
                    429,
                    "Too Many Requests",
                    "Rate limit exceeded",
                    System.currentTimeMillis()
            );

            String jsonResponse = """
                    {
                       "status": %d,
                       "error": "%s",
                       "message": "%s",
                       "timestamp": %d
                    }
                    """.formatted(
                    error.getStatus(),
                    error.getError(),
                    error.getMessage(),
                    error.getTimestamp()
            );

            response.getWriter().write(jsonResponse);

            return false;
        }

        return true;
    }
}