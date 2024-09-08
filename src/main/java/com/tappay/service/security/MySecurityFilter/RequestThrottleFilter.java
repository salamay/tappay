package com.tappay.service.security.MySecurityFilter;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kong.unirest.HttpStatus;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.concurrent.TimeUnit;
import jakarta.servlet.*;

@Component
public class RequestThrottleFilter implements jakarta.servlet.Filter {

    private int MAX_REQUESTS_PER_SECOND = 50; //or whatever you want it to be

    private LoadingCache<String, Integer> requestCountsPerIpAddress;

    public RequestThrottleFilter(){
        super();
        requestCountsPerIpAddress = Caffeine.newBuilder().
                expireAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(jakarta.servlet.ServletRequest servletRequest, jakarta.servlet.ServletResponse servletResponse, jakarta.servlet.FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String clientIpAddress = getClientIP((HttpServletRequest) servletRequest);
        if(isMaximumRequestsPerSecondExceeded(clientIpAddress)){
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS);
            httpServletResponse.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isMaximumRequestsPerSecondExceeded(String clientIpAddress){
        Integer requests = 0;
        requests = requestCountsPerIpAddress.get(clientIpAddress);
        if(requests != null){
            if(requests > MAX_REQUESTS_PER_SECOND) {
                requestCountsPerIpAddress.asMap().remove(clientIpAddress);
                requestCountsPerIpAddress.put(clientIpAddress, requests);
                return true;
            }

        } else {
            requests = 0;
        }
        requests++;
        requestCountsPerIpAddress.put(clientIpAddress, requests);
        return false;
    }

    public String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0]; // voor als ie achter een proxy zit
    }

    @Override
    public void destroy() {

    }
}
