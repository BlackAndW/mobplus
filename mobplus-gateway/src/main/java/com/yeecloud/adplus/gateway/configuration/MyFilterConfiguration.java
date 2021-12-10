package com.yeecloud.adplus.gateway.configuration;

import com.yeecloud.adplus.gateway.util.Result;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author: Leonard
 * @create: 2021/12/9
 */
@Component
//@WebFilter(filterName = "MyFilterConfiguration", urlPatterns = "/*")
public class MyFilterConfiguration implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String apiVersion = request.getHeader("Api-Version");
        if (apiVersion != null && Double.valueOf(apiVersion) > 1.0){
            ServletRequest requestWrapper = new BodyWrapper(request);
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
