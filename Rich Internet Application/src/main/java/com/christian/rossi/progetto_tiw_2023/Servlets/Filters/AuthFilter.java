package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Constants.Constants;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hrequest = (HttpServletRequest) request;
        HttpServletResponse hresponse = (HttpServletResponse) response;
        HttpSession session = hrequest.getSession();
        if (session.isNew() || session.getAttribute("USER") == null) {
            hresponse.sendRedirect(new PathBuilder(Constants.AUTHENTICATION_PAGE).toString());
            return;
        }
        //filter chain pattern
        chain.doFilter(request, response);
    }
}