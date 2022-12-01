package org.launchcode.javawebdevtechjobsauthentication;

import org.launchcode.javawebdevtechjobsauthentication.controllers.AuthenticationController;
import org.launchcode.javawebdevtechjobsauthentication.models.User;
import org.launchcode.javawebdevtechjobsauthentication.models.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter implements HandlerInterceptor {
    @Autowired
     UserRepository userRepository;
    @Autowired
     AuthenticationController authenticationController;

    private static final List<String> allowedList = Arrays.asList("/login", "/register", "/logout", "/css");


    private static boolean isAllowedListed(String path) {
        for (String pathRoot : allowedList) {
            if (path.startsWith(pathRoot)) {
                System.out.println("is allowed");
                return true;
            }
        }
        System.out.println("is not allowed");
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        System.out.println("preHandle");
        // Don't require sign-in for allowedListed pages
        if (isAllowedListed(request.getRequestURI())) {
            // returning true indicates that the request may proceed
            return true;
        }

        HttpSession session = request.getSession();
        User user = authenticationController.getUserFromSession(session);

        // The user is logged in
        if (user != null) {
            return true;
        }

        // The user is NOT logged in
        response.sendRedirect("login");
        return false;
    }

}
