package com.christian.rossi.progetto_tiw_2023.Servlets.Filters;

import com.christian.rossi.progetto_tiw_2023.Constants.Errors;
import com.christian.rossi.progetto_tiw_2023.Constants.URLs;
import com.christian.rossi.progetto_tiw_2023.Utils.PathBuilder;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = {URLs.DO_ADD_PRODUCT, URLs.DO_CLOSE, URLs.DO_CREATE_AUCTION, URLs.DO_GET_IMAGE, URLs.DO_OFFER, URLs.GET_BUY_PAGE, URLs.GET_DETAILS_PAGE, URLs.GET_HOME_PAGE, URLs.GET_OFFERS_PAGE, URLs.GET_SELL_PAGE, URLs.DO_LOGOUT})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpSession session = httpServletRequest.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            httpServletResponse.sendRedirect(new PathBuilder(URLs.GET_ERROR_PAGE).addParam("error", Errors.AUTH_ERROR).addParam("redirect", URLs.GET_LOGIN_PAGE).toString());
            return;
        }
        chain.doFilter(request, response);
    }
}