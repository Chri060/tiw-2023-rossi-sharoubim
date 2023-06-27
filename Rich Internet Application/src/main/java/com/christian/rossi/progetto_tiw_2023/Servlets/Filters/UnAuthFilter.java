package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "UnAuthFilter", urlPatterns = {Constants.AUTHENTICATION_PAGE})
public class UnAuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();
        if (session.getAttribute("userID") != null) {
            httpServletResponse.sendRedirect(new PathBuilder(Constants.HOME_PAGE).toString());
            return;
        }
        chain.doFilter(request, response);
    }
}