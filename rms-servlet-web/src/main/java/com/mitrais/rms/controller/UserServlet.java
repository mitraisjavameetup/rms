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
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/users/*")
public class UserServlet extends AbstractController {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        try {
            switch (action) {
                case "/new":
                    showNewForm(request, response);
                    break;
                case "/insert":
                    insertUser(request, response);
                    break;
                case "/delete":
                    deleteUser(request, response);
                    break;
                case "/edit":
                    showEditForm(request, response);
                    break;
                case "/update":
                    updateUser(request, response);
                    break;
                default:
                    listUsers(request, response);
                    break;
            }
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServletException {
        UserDao userDao = UserDaoImpl.getInstance();
        List<User> users = userDao.findAll();
        request.setAttribute("users", users);

        RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW_PREFIX + "/users/list" + VIEW_SUFFIX);
        dispatcher.forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW_PREFIX + "/users/form" + VIEW_SUFFIX);
        dispatcher.forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
        long id = Long.parseLong(request.getParameter("id"));

        Optional<User> user = UserDaoImpl.getInstance().find(id);

        RequestDispatcher dispatcher = request.getRequestDispatcher(VIEW_PREFIX + "/users/form" + VIEW_SUFFIX);
        request.setAttribute("user", user);
        dispatcher.forward(request, response);

    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        UserDaoImpl.getInstance().save(user);
        response.sendRedirect("list");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = new User(id, username, password);
        UserDaoImpl.getInstance().update(user);

        response.sendRedirect("list");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        long id = Long.parseLong(request.getParameter("id"));
        User user = new User(id);
        UserDaoImpl.getInstance().delete(user);
        response.sendRedirect("list");

    }
}
