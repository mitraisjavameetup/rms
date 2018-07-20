package com.mitrais.rms.controller;

import com.mitrais.rms.dao.UserDao;
import com.mitrais.rms.dao.impl.UserDaoImpl;
import com.mitrais.rms.model.User;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.Cookie;

@WebServlet(
        urlPatterns = {"/login", "/logout"}
)
public class LoginServlet extends AbstractController {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/rms-servlet-web/logout".equalsIgnoreCase(req.getRequestURI())) {
            for (Cookie cookie : req.getCookies()) {
                cookie.setValue("");
                cookie.setMaxAge(0);
                resp.addCookie(cookie);
            }

            String loginURI = req.getContextPath() + "/login";
            resp.sendRedirect(loginURI);
        } else {
            String path = getTemplatePath(req.getServletPath());
            String error = req.getParameter("error");
            if (error != "" && error != null) {
                req.setAttribute("error", "Invalid username or password");
            }

            RequestDispatcher requestDispatcher = req.getRequestDispatcher(path);
            requestDispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equalsIgnoreCase(password)) {
            Cookie id = new Cookie("userId", user.getId().toString());
            id.setMaxAge(60 * 60 * 24);
            resp.addCookie(id);
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            resp.sendRedirect(req.getContextPath() + "/login?error=true");
        }
    }

    private User getUserByUsername(String username) {
        UserDao userDao = UserDaoImpl.getInstance();
        Optional<User> user = userDao.findByUserName(username);
        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }
}
