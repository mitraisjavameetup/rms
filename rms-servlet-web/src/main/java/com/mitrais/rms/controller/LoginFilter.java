package com.mitrais.rms.controller;

import javax.servlet.*;

import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String loginURI = request.getContextPath() + "/login";

        boolean loggedIn = this.isUserAuth(request);
        boolean loginRequest = request.getRequestURI().equals(loginURI);

        if (loggedIn || loginRequest) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(loginURI);
        }
    }

    @Override
    public void destroy() {
    }

    protected boolean isUserAuth(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return false;
        }

        for (int i = 0; i < request.getCookies().length; i++) {
            if (request.getCookies()[i].getName().equals("userId")) {
                return true;
            }
        }

        return false;
    }
}
